/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import static de.jhit.opendiabetes.vault.importer.FileImporter.LOG;
import de.jhit.openmediavault.app.container.VaultEntry;
import de.jhit.openmediavault.app.container.VaultEntryType;
import de.jhit.openmediavault.app.preferences.Constants;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jens
 */
public class MedtronicCsvImporter extends CsvImporter {

    private static final Pattern AMOUNT_PATTERN = Pattern.compile(".*AMOUNT=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern ISIG_PATTERN = Pattern.compile(".*ISIG=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern RATE_PATTERN = Pattern.compile(".*RATE=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CARB_INPUT_PATTERN = Pattern.compile(".*CARB_INPUT=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern BG_INPUT_PATTERN = Pattern.compile(".*BG_INPUT=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern DURATION_PATTERN = Pattern.compile(".*DURATION=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern RAW_TYPE_PATTERN = Pattern.compile(".*RAW_TYPE=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern ALARM_TYPE_PATTERN = Pattern.compile(".*ALARM_TYPE=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);

    private static Date createTimestamp(String date, String time) throws ParseException {
        String format = Constants.CARELINK_CSV_DATETIME_FORMAT[Constants.CARELINK_CSV_LANG_SELECTION];

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(time + date);
    }

    public MedtronicCsvImporter() {
        super(new MedtronicCsvValidator(), ',');
    }

    public MedtronicCsvImporter(char delimiter) {
        super(new MedtronicCsvValidator(), delimiter);
    }

    private static VaultEntry extractEntry(Date timestamp, VaultEntryType type,
            String rawValues, Pattern pattern, String[] fullEntry) {
        if (rawValues != null && !rawValues.isEmpty()) {
            Matcher m = pattern.matcher(rawValues);
            if (m.matches()) {
                String matchedString = m.group(1).replace(",", ".");
                try {
                    double value = Double.parseDouble(matchedString);
                    return new VaultEntry(type,
                            timestamp,
                            value);
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "{0} -- Record: {1}",
                            new Object[]{ex.getMessage(), Arrays.toString(fullEntry)});
                }
            }
        }
        return null;
    }
    
    @Override
    protected void preprocessingIfNeeded(String filePath){
        // test for delimiter
         CsvReader creader = null;
        try {
            // test for , delimiter
            creader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            for (int i =0; i< 15; i++){ // just scan the first 15 lines for a valid header
                if (creader.readHeaders()){
                    if (validator.validateHeader(creader.getHeaders())){                    
                        // found valid header --> finish
                        delimiter = ',';
                        creader.close();
                        LOG.log(Level.INFO, "Found ',' as delimiter for Carelink CSV: {0}", filePath);
                        return;
                    }
                }
            }
            // if you end up here there was no valid header within the range
            // try the other delimiter in normal operation
            delimiter = ';';
            LOG.log(Level.INFO, "Found ';' as delimiter for Carelink CSV: {0}", filePath);
                
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Error while parsing Careling CSV in delimiter checkF: "
                    + filePath, ex);
        } finally {
            if (creader != null) {
                creader.close();
            }
        }
    }

    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        List<VaultEntry> retVal = new ArrayList<>();
        MedtronicCsvValidator parseValidator = (MedtronicCsvValidator) validator;

        MedtronicCsvValidator.TYPE type = parseValidator.getCarelinkType(creader);
        if (type == null) {
            return null;
        }
        Date timestamp;
        try {
            timestamp = parseValidator.getTimestamp(creader);
        } catch (ParseException ex) {
            // maybe old format without good timestamp
            // try again with seperated fields
            timestamp = parseValidator.getManualTimestamp(creader);
        }
        if (timestamp == null) {
            return null;
        }
        String rawValues = parseValidator.getRawValues(creader);
        VaultEntry tmpEntry;

        switch (type) {
            case BASAL:
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.BASAL_Profile, rawValues,
                        RATE_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                break;
            case BG_MANUAL:
            case BG_RECEIVED:
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.GLUCOSE_BG, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                break;
            case BOLUS_WIZARD: // TODO store bolus suggestion somewhere   
                // meal information
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.MEAL_BolusExpert, rawValues,
                        CARB_INPUT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                // bg information --> TODO new data line (s.th. like user informed /user view)
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM_ALERT, rawValues,
                        BG_INPUT_PATTERN, creader.getValues());
                if (tmpEntry != null && tmpEntry.getValue() > 0.0) {
                   // retVal.add(tmpEntry); //<-- comment out for better plot
                }
                break;
            case BOLUS: // TODO check other bolus types
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.BOLUS_ManualNormal, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                break;
            case EXERCICE:
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.EXERCISE_Manual, rawValues,
                        DURATION_PATTERN, creader.getValues());

                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                } else {
                    // add marker without duration for old pumps
                    retVal.add(new VaultEntry(VaultEntryType.EXERCISE_Manual,
                            timestamp, VaultEntry.VALUE_UNUSED));
                }
                break;
            case PRIME:
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.PUMP_PRIME, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }

                break;
            case PUMP_ALERT:
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.PUMP_NO_DELIVERY, rawValues,
                        RAW_TYPE_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    if (tmpEntry.getValue() != 4.0) {
                        tmpEntry = new VaultEntry(VaultEntryType.PUMP_UNKNOWN_ERROR,
                                timestamp, tmpEntry.getValue());
                    }
                    retVal.add(tmpEntry);
                }

                break;
            case REWIND:
                retVal.add(new VaultEntry(VaultEntryType.PUMP_REWIND, timestamp,
                        VaultEntry.VALUE_UNUSED));
                break;
            case SENSOR_ALERT:
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM_ALERT, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    // check if it is really a cgm-bg-alert
                    Matcher m = ALARM_TYPE_PATTERN.matcher(rawValues);
                    if (m.matches()) {
                        if (m.group(1).equalsIgnoreCase("102")
                                || m.group(1).equalsIgnoreCase("101")) {
                            retVal.add(tmpEntry);
                        }
                    }
                }

                break;
            case SENSOR_CAL_BG:
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM_CALIBRATION, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }

                break;
            case SENSOR_CAL_FACTOR:
                // not interesting right now --> drop

                break;
            case SENSOR_VALUE:
                // calibrated cgm value
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }

                // measured raw value
                tmpEntry = extractEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM_RAW, rawValues,
                        ISIG_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                break;
            default:
                throw new AssertionError();
        }

        return retVal;
    }

}
