/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import de.jhit.opendiabetes.vault.container.MedtronicCsvInterpreterBasalInformation;
import de.jhit.opendiabetes.vault.importer.FileImporter;
import de.jhit.opendiabetes.vault.util.SortVaultEntryByDate;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.data.VaultDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public class SimplePumpInterpreter extends VaultInterpreter {

    private final SimplePumpInterpreterOptions myOptions;

    public SimplePumpInterpreter(FileImporter importer,
            SimplePumpInterpreterOptions options, VaultDao db) {
        super(importer, options, db);
        myOptions = options;
    }

    @Override
    protected List<VaultEntry> interpret(List<VaultEntry> result) {
        List<VaultEntry> data = result;

        // sort by date
        Collections.sort(data, new SortVaultEntryByDate());

        LOG.finer("Start basal interpretation");
        data = applyTempBasalEvents(data);
        data = considerSuspendAsBasalOff(data);
        LOG.finer("Start fill canula interpretation");
        data = fillCanulaInterpretation(data);

        LOG.finer("Pump data interpretation finished");
        return data;
    }

    private List<VaultEntry> fillCanulaInterpretation(List<VaultEntry> result) {
        if (result == null || result.isEmpty()) {
            return result;
        }
        //TODO prefill the handles with database information (when multiple files are read, this is neede)
        VaultEntry rewindHandle = null;
        VaultEntry primeHandle = null;
        VaultEntry latesFillCanulaHandle = null;
        VaultEntry canulaFillAsPumpFillCandidate = null;
        int cooldown = 0;
        List<VaultEntry> fillEvents = new ArrayList<>();

        // configure options        
        if (myOptions.FillCanulaAsNewKatheder) {
            // ignore cooldown if option is disabled
            cooldown = myOptions.FillCanulaCooldown * 60000;
        }

        // check if handle prefill is needed
        VaultEntry rewindFromDb = db.queryLatestEventBefore(result.get(0).getTimestamp(),
                VaultEntryType.PUMP_REWIND);
        VaultEntry primeFromDb = db.queryLatestEventBefore(result.get(0).getTimestamp(),
                VaultEntryType.PUMP_PRIME);
        if (rewindFromDb != null) {
            if (((primeFromDb != null
                    && rewindFromDb.getTimestamp().after(primeFromDb.getTimestamp()))
                    || primeFromDb == null)
                    && (result.get(0).getTimestamp().getTime()
                    - rewindFromDb.getTimestamp().getTime()) < 10800000L) {
                // rewind without prime has no fill event --> add as handle when its within 3 hours
                rewindHandle = rewindFromDb;

            } else if (primeFromDb != null && (rewindFromDb.getTimestamp().getTime()
                    - result.get(0).getTimestamp().getTime()) < cooldown) {
                // rewind has prime (so it has a fill event) but is within cooldown
                // --> prefill handles and delete their fill event
                // TODO find and kill fill event in db
                rewindHandle = rewindFromDb;
                primeHandle = primeFromDb;
            }
        }

        // go through timeline
        for (VaultEntry item : result) {

            // reset handles if cooldown is over
            if (rewindHandle != null && primeHandle != null
                    && ((item.getTimestamp().getTime() - rewindHandle.getTimestamp().getTime())
                    > cooldown)) {

                Date fillDate;
                if (latesFillCanulaHandle != null) {
                    fillDate = latesFillCanulaHandle.getTimestamp();
                } else {
                    fillDate = primeHandle.getTimestamp();
                }
                fillEvents.add(new VaultEntry(VaultEntryType.PUMP_FILL,
                        fillDate,
                        VaultEntry.VALUE_UNUSED));
                rewindHandle = null;
                primeHandle = null;
                latesFillCanulaHandle = null;
                canulaFillAsPumpFillCandidate = null;
            }

            // reverse cooldown for canula as ne katheder interpretation
            if (myOptions.FillCanulaAsNewKatheder
                    && canulaFillAsPumpFillCandidate != null
                    && ((item.getTimestamp().getTime()
                    - canulaFillAsPumpFillCandidate.getTimestamp().getTime())
                    > cooldown)) {
                // there is no rewind within the range --> new fill event
                fillEvents.add(canulaFillAsPumpFillCandidate);
            }

            // find new pairs
            if (item.getType() == VaultEntryType.PUMP_REWIND) {
                // filling line starts with rewind
                rewindHandle = item;
            } else if (item.getType() == VaultEntryType.PUMP_PRIME) {
                // is pump rewinded?
                if (rewindHandle != null) {
                    // is pump already primed?
                    if (primeHandle == null) {
                        // no --> this is the prime
                        primeHandle = item;
                    } else {
                        // yes --> must be fill canula
                        latesFillCanulaHandle = item;
                    }
                } else if (myOptions.FillCanulaAsNewKatheder) {
                    // no prime event? --> new katheder (if enabled)
                    canulaFillAsPumpFillCandidate = new VaultEntry(VaultEntryType.PUMP_FILL,
                            item.getTimestamp(),
                            VaultEntry.VALUE_UNUSED);
                }
            }
        }

        // process last prime entrys
        if (rewindHandle != null && primeHandle != null) {
            Date fillDate;
            if (latesFillCanulaHandle != null) {
                fillDate = latesFillCanulaHandle.getTimestamp();
            } else {
                fillDate = primeHandle.getTimestamp();
            }
            fillEvents.add(new VaultEntry(VaultEntryType.PUMP_FILL,
                    fillDate,
                    VaultEntry.VALUE_UNUSED));
        }

        //merge
        result.addAll(fillEvents);

        // sort by date again <-- not neccesary because database will do it
        //Collections.sort(result, new SortVaultEntryByDate());
        return result;
    }

    private List<VaultEntry> considerSuspendAsBasalOff(List<VaultEntry> data) {
        // suspends will stop basal rate --> add basal 0 point
        // after suspension, pump has new basal event by itselve
        // while suspension, pump does not create basal profile events :)
        if (data == null || data.isEmpty()) {
            return data;
        }
        List<VaultEntry> basalEvents = new ArrayList<>();
        for (VaultEntry item : data) {
            if (item.getType() == VaultEntryType.PUMP_SUSPEND) {
                basalEvents.add(new VaultEntry(VaultEntryType.BASAL_Manual,
                        item.getTimestamp(), 0.0));
            }
        }
        data.addAll(basalEvents);
        return data;
    }

    private List<VaultEntry> applyTempBasalEvents(List<VaultEntry> data) {
        // if tmp basal ocures, real basal rate must be calculated
        // it is possible, that tmp basal rate events have an effect on db data
        if (data == null || data.isEmpty()) {
            return data;
        }
        List<VaultEntry> basalEvents = new ArrayList<>();
        List<VaultEntry> historicBasalProfileEvents = new ArrayList<>();
        List<VaultEntry> killedBasalEvents = new ArrayList<>();

        for (VaultEntry item : data) {
            if (item.getType() == VaultEntryType.BASAL_Manual
                    && item instanceof MedtronicCsvInterpreterBasalInformation) {
                MedtronicCsvInterpreterBasalInformation basalItem
                        = (MedtronicCsvInterpreterBasalInformation) item;

                // check if we need db data (will just match the first element if at all)
                if (basalItem.getDuration() > (basalItem.getTimestamp().getTime()
                        - data.get(0).getTimestamp().getTime())) {
                    // find affected basal_profil events in db
                    // add them to historic list and kill them from db
                    // they will be pushed corrected to db again with this run
                    //TODO implement
                }

                // get affected historic elements from this dataset
                List<VaultEntry> affectedHistoricElements = new ArrayList<>();
                for (int i = historicBasalProfileEvents.size() - 1; i >= 0; i--) {
                    VaultEntry historicItem = historicBasalProfileEvents.get(i);

                    if (basalItem.getDuration() > (basalItem.getTimestamp().getTime()
                            - historicItem.getTimestamp().getTime())) {
                        // kill event and save value for percentage calculation
                        killedBasalEvents.add(historicItem);
                        affectedHistoricElements.add(historicItem);
                    } else {
                        // jungest now available element is not affected 
                        // --> no other remaining elements are affected
                        // add to affected list for calculation but don't kill it
                        affectedHistoricElements.add(historicItem);
                        break;
                    }
                }
                if (affectedHistoricElements.isEmpty()) {
                    LOG.log(Level.WARNING, "Could not calculate tmp basal, "
                            + "because no profile elements are found\n{0}",
                            basalItem.toString());
                    break;
                }

                // apply changes
                switch (basalItem.getRawType()) {
                    case BASAL_TMP_PERCENT:
                        // calculate new rate
                        Date startTimestamp = new Date((long) (basalItem.getTimestamp().getTime()
                                - basalItem.getDuration()));
                        // first manual item needs special imestamp
                        double currentBasalValue = affectedHistoricElements.get(
                                affectedHistoricElements.size() - 1).getValue();
                        double newBasalValue = currentBasalValue
                                * basalItem.getValue() * 0.01;
                        basalEvents.add(new VaultEntry(
                                VaultEntryType.BASAL_Manual,
                                startTimestamp,
                                newBasalValue));

                        for (int i = 0; i < affectedHistoricElements.size() - 2; i++) {
                            currentBasalValue = affectedHistoricElements.get(i)
                                    .getValue();
                            newBasalValue = currentBasalValue
                                    * basalItem.getValue() * 0.01;
                            basalEvents.add(new VaultEntry(
                                    VaultEntryType.BASAL_Manual,
                                    affectedHistoricElements.get(i).getTimestamp(),
                                    newBasalValue));
                        }

                        // restore rate from jungest profile event afterwords
                        basalEvents.add(new VaultEntry(
                                VaultEntryType.BASAL_Profile,
                                basalItem.getTimestamp(),
                                affectedHistoricElements.get(0).getValue()));
                        break;
                    case BASAL_TMP_RATE:
                        // add new rate
                        basalEvents.add(new VaultEntry(
                                VaultEntryType.BASAL_Manual,
                                new Date((long) (basalItem.getTimestamp().getTime()
                                        - basalItem.getDuration())),
                                basalItem.getValue()));

                        // restore rate from jungest profile event afterwords
                        basalEvents.add(new VaultEntry(
                                VaultEntryType.BASAL_Profile,
                                basalItem.getTimestamp(),
                                affectedHistoricElements.get(0).getValue()));
                        break;
                    default:
                        Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                        throw new AssertionError();
                }

                killedBasalEvents.add(item);
            } else if (item.getType() == VaultEntryType.BASAL_Profile) {
                historicBasalProfileEvents.add(item);
            }
        }

        data.removeAll(killedBasalEvents);
        data.addAll(basalEvents);
        return data;
    }

}
