/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.opendiabetes.vault.importer.FileImporter;
import de.jhit.opendiabetes.vault.util.SortVaultEntryByDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jens
 */
public class FitnessTrackerInterpreter extends VaultInterpreter {

    private final FitnessTrackerInterpreterOptions myOptions;

    public FitnessTrackerInterpreter(FileImporter importer,
            FitnessTrackerInterpreterOptions options, VaultDao db) {
        super(importer, options, db);
        myOptions = options;
    }

    @Override
    protected List<VaultEntry> interpret(List<VaultEntry> result) {
        List<VaultEntry> data = result;

        // sort by date
        Collections.sort(data, new SortVaultEntryByDate());

        LOG.finer("Start activity data interpretation");
        data = filterActititys(data);

        LOG.finer("Tracker data interpretation finished");

        return data;
    }

    private List<VaultEntry> filterActititys(List<VaultEntry> data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        List<VaultEntry> retVal = new ArrayList<>();
        List<VaultEntry> dbValues = db.queryExerciseBetween(
                data.get(0).getTimestamp(),
                data.get(data.size() - 1).getTimestamp());
        VaultEntry lastExerciseItem = null;
        VaultEntryType typeBestEfford = null;

        for (VaultEntry item : data) {
            switch (item.getType()) {
                case EXERCISE_TrackerBicycle:
                case EXERCISE_TrackerRun:
                case EXERCISE_TrackerWalk:
                    // TODO also use this for google
                    // google activity type > tracker activity type
                    if (lastExerciseItem == null) {
                        // init item
                        lastExerciseItem = item;
                        typeBestEfford = item.getType();
                    } else {
                        if (Math.round(
                                (item.getTimestamp().getTime()
                                - lastExerciseItem.getTimestamp().getTime())
                                / 60000)
                                >= Math.round(myOptions.activitySliceThreshold
                                        + lastExerciseItem.getValue())) {
                            // within a slice
                            lastExerciseItem.setValue(item.getValue() + lastExerciseItem.getValue()); // add time to value
                            if (typeBestEfford != null
                                    && item.getType().ordinal() > typeBestEfford.ordinal()) {
                                typeBestEfford = item.getType();
                            }
                        } else {
                            // new slice
                            // check if found slice already exist from other source
                            for (VaultEntry historyEntry : dbValues) {
                                if (lastExerciseItem != null 
                                        && historyEntry.getTimestamp().after(lastExerciseItem.getTimestamp())
                                        && Math.round(historyEntry.getTimestamp().getTime() / 60000)
                                        <= Math.round(lastExerciseItem.getValue())) { // is within duration
                                    // check if db value is more valuable
                                    if (typeBestEfford != null
                                            && historyEntry.getType().ordinal() > typeBestEfford.ordinal()) {
                                        // --> DB Entry is better
                                        if (historyEntry.getValue() > lastExerciseItem.getValue()) {
                                            // kill item
                                            lastExerciseItem = null;
                                        } else {
                                            // this entry has a longer duration
                                            typeBestEfford = historyEntry.getType();
                                        }
                                    }
                                } else if (lastExerciseItem != null 
                                        && historyEntry.getTimestamp().after(lastExerciseItem.getTimestamp())
                                        && Math.round(historyEntry.getTimestamp().getTime() / 60000)
                                        > Math.round(lastExerciseItem.getValue())){
                                    // we passed the current time point --> stop searching
                                    break;
                                }
                            }
                            // save old slice
                            if (lastExerciseItem != null
                                    && lastExerciseItem.getValue() > myOptions.activityThreshold) {
                                lastExerciseItem.setType(typeBestEfford);
                                retVal.add(lastExerciseItem);
                            }
                            //  setup for new slice search
                            lastExerciseItem = item;
                            typeBestEfford = item.getType();

                        }
                    }
                    break;
                default:
                    retVal.add(item);
            }
        }

        // add last unsliced item
        if (lastExerciseItem != null
                && lastExerciseItem.getValue() > myOptions.activityThreshold) {
            retVal.add(lastExerciseItem);
        }

        return retVal;
    }

}
