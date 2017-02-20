/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.exporter;

import com.csvreader.CsvWriter;
import de.jhit.opendiabetes.vault.container.VaultCsvEntry;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class VaultCsvExporter {

    public final static int RESULT_OK = 0;
    public final static int RESULT_ERROR = -1;
    public final static int RESULT_NO_DATA = -2;
    public final static int RESULT_FILE_ACCESS_ERROR = -3;

    private static final Logger LOG = Logger.getLogger(VaultCsvExporter.class.getName());

    private final ExporterOptions options;
    private final VaultDao db;
    private final String filePath;

    public VaultCsvExporter(ExporterOptions options, VaultDao db, String filePath) {
        this.options = options;
        this.db = db;
        this.filePath = filePath;
    }

    public int exportDataToFile() {
        List<VaultEntry> entrys;
        List<VaultCsvEntry> csvEntrys;

        // check file stuff        
        File checkFile = new File(filePath);
        if (checkFile.exists()
                && (!checkFile.isFile() || !checkFile.canWrite())) {
            return RESULT_FILE_ACCESS_ERROR;
        }

        // query entrys
        if (options.isImportPeriodRestricted) {
            entrys = db.queryVaultEntrysBetween(options.exportPeriodFrom,
                    options.exportPeriodTo);
        } else {
            entrys = db.queryAllVaultEntrys();
        }

        if (entrys == null || entrys.isEmpty()) {
            return RESULT_NO_DATA;
        }

        // create csv data
        csvEntrys = prepareData(entrys);

        // write to file
        try {
            writeToFile(csvEntrys);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error writing odv csv file: {0}" + filePath, ex);
            return RESULT_ERROR;
        }
        return RESULT_OK;
    }

    private void writeToFile(List<VaultCsvEntry> csvEntries) throws IOException {
        CsvWriter cwriter = new CsvWriter(filePath, VaultCsvEntry.CSV_DELIMITER,
                Charset.forName("UTF-8"));

        cwriter.writeRecord(VaultCsvEntry.getCsvHeaderRecord());
        for (VaultCsvEntry item : csvEntries) {
            cwriter.writeRecord(item.toCsvRecord());
        }
        cwriter.flush();
        cwriter.close();
    }

    private List<VaultCsvEntry> prepareData(List<VaultEntry> tmpValues) {
        List<VaultCsvEntry> returnValues = new ArrayList<>();

        // list is ordered by timestamp from database (or should be ordered otherwise)
        Date fromTimestamp = tmpValues.get(0).getTimestamp();
        Date toTimestamp = tmpValues.get(tmpValues.size() - 1).getTimestamp();

        if (!tmpValues.isEmpty()) {
            int i = 0;
            while (!fromTimestamp.after(toTimestamp)) {

                VaultCsvEntry tmpCsvEntry = new VaultCsvEntry();
                tmpCsvEntry.setTimestamp(fromTimestamp);

                VaultEntry tmpEntry;
                while (fromTimestamp.equals((tmpEntry = tmpValues.get(i)).getTimestamp())) {
                    if (i < tmpValues.size() - 1) {
                        i++;
                    } else {
                        i--;
                        break;
                    }

                    switch (tmpEntry.getType()) {
                        case GLUCOSE_CGM_ALERT:
                            tmpCsvEntry.setCgmAlertValue(tmpEntry.getValue());
                            break;
                        case GLUCOSE_CGM:
                            // TODO y does this happen
                            // --> when more than one cgm value per minute is available
                            // but cgm ticks are every 15 minutes 
                            if (tmpCsvEntry.getCgmValue()
                                    == VaultCsvEntry.UNINITIALIZED_DOUBLE) {
                                tmpCsvEntry.setCgmValue(tmpEntry.getValue());
                            } else {
                                LOG.log(Level.WARNING, "Drop {0}, hold: {1}",
                                        new Object[]{tmpEntry.toString(),
                                            tmpCsvEntry.toString()});
                            }
                            break;
                        case GLUCOSE_BG:
                            tmpCsvEntry.setBgValue(tmpEntry.getValue());
                            break;
                        case BASAL_Manual:
                        case BASAL_Profile:
                            tmpCsvEntry.setBasalValue(tmpEntry.getValue());
                            tmpCsvEntry.addBasalAnnotation(tmpEntry.getType().toString());
                            break;
                        case BOLUS_Square:
                            tmpCsvEntry.setBolusValue(tmpEntry.getValue());
                            tmpCsvEntry.addBolusAnnotation(
                                    tmpEntry.getType().toString()
                                    + "=" + tmpEntry.getValue2());
                            break;
                        case BOLUS_Normal:
                            tmpCsvEntry.setBolusValue(tmpEntry.getValue());
                            tmpCsvEntry.addBolusAnnotation(
                                    tmpEntry.getType().toString());
                            break;
                        case MEAL_BolusExpert:
                        case MEAL_Manual:
                            tmpCsvEntry.setMealValue(tmpEntry.getValue());
                            break;
                        case EXERCISE_GoogleBicycle:
                        case EXERCISE_GoogleWalk:
                        case EXERCISE_GoogleRun:
                        case EXERCISE_Manual:
                            tmpCsvEntry.setExerciseTimeValue(tmpEntry.getValue());
                            tmpCsvEntry.addExerciseAnnotation(tmpEntry.getType().toString());
                            break;
                        case PUMP_FILL:
                        case PUMP_NO_DELIVERY:
                        case PUMP_REWIND:
                        case PUMP_UNKNOWN_ERROR:
                        case PUMP_SUSPEND:
                        case PUMP_UNSUSPEND:
                            tmpCsvEntry.addPumpAnnotation(tmpEntry.getType().toString());
                            break;
                        case PUMP_PRIME:
                            tmpCsvEntry.addPumpAnnotation(tmpEntry.getType().toString()
                                    + "="
                                    + tmpEntry.getValue());
                            break;
                        case GLUCOSE_CGM_CALIBRATION:
                        case GLUCOSE_CGM_RAW: // TODO implement this two
                            break;
                        default:
                            LOG.severe("ASSERTION ERROR!");
                            throw new AssertionError();
                    }

                }

                if (!tmpCsvEntry.isEmpty()) {
                    returnValues.add(tmpCsvEntry);
                    //LOG.log(Level.INFO, "Export entry: {0}", tmpCsvEntry.toCsvString());
                }
                fromTimestamp = TimestampUtils.addMinutesToTimestamp(fromTimestamp, 1);
            }
        }
        return returnValues;
    }

}
