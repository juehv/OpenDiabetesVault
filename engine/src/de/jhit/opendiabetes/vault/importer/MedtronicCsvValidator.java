/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author juehv
 */
public class MedtronicCsvValidator extends CsvValidator {

    public static final String CARELINK_HEADER_DE_DATE = "Datum";
    public static final String CARELINK_HEADER_DE_TIME = "Zeit";
    public static final String CARELINK_HEADER_DE_TIMESTAMP = "Zeitstempel";
    public static final String CARELINK_HEADER_DE_TYPE = "Roh-Typ";
    public static final String CARELINK_HEADER_DE_VALUE = "Roh-Werte";
    public static final String TIME_FORMAT_DE = "dd.MM.yy HH:mm:ss";

    public static final String[] CARELINK_HEADER_DE = {
        CARELINK_HEADER_DE_DATE, CARELINK_HEADER_DE_TIME,
        CARELINK_HEADER_DE_TIMESTAMP, CARELINK_HEADER_DE_TYPE,
        CARELINK_HEADER_DE_VALUE
    };

    public static enum TYPE {
        REWIND("Rewind"), PRIME("Prime"),
        EXERCICE("JournalEntryExerciseMarker"),
        BG_MANUAL("BGCapturedOnPump"), BG_RECEIVED("BGReceived"),
        SENSOR_CAL_BG("SensorCalBG"), SENSOR_CAL_FACTOR("SensorCalFactor"),
        SENSOR_VALUE("GlucoseSensorData"), SENSOR_ALERT("AlarmSensor"),
        BOLUS_WIZARD("BolusWizardBolusEstimate"), BOLUS_NORMAL("BolusNormal"), 
        BOLUS_SQUARE("BolusSquare"),
        BASAL("BasalProfileStart"), BASAL_TMP_PERCENT("ChangeTempBasalPercent"),
        BASAL_TMP_RATE("ChangeTempBasal"),
        PUMP_ALERT("AlarmPump"), PUMP_SUSPEND_CHANGED("ChangeSuspendState");

        final String name;

        TYPE(String name) {
            this.name = name;
        }

        static TYPE fromString(String typeString) {
            if (typeString != null && !typeString.isEmpty()) {
                for (TYPE item : TYPE.values()) {
                    if (item.name.equalsIgnoreCase(typeString)) {
                        return item;
                    }
                }
            }
            return null;
        }
    }

    public MedtronicCsvValidator() {
        // TODO implement auto recognition while header parsing
        languageSelection = Language.DE;
    }

    @Override
    public boolean validateHeader(String[] header) {

        boolean result = true;
        Set<String> headerSet = new TreeSet<>(Arrays.asList(header));

        // Check german header
        for (String item : CARELINK_HEADER_DE) {
            result &= headerSet.contains(item);
        }
        if (result == true) {
            languageSelection = Language.DE;
        }

        //TODO check english headers
        return result;
    }

    public String getRawValues(CsvReader creader) throws IOException {
        switch (languageSelection) {
            case DE:
                return creader.get(CARELINK_HEADER_DE_VALUE);
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                throw new AssertionError();
        }
    }

    public TYPE getCarelinkType(CsvReader creader) throws IOException {
        switch (languageSelection) {
            case DE:
                String typeString = creader.get(CARELINK_HEADER_DE_TYPE).trim();
                return TYPE.fromString(typeString);
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                throw new AssertionError();
        }
    }

    public Date getTimestamp(CsvReader creader) throws IOException, ParseException {
        switch (languageSelection) {
            case DE:
                String timeString = creader.get(CARELINK_HEADER_DE_TIMESTAMP).trim();
                return TimestampUtils.createCleanTimestamp(timeString, TIME_FORMAT_DE);
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                throw new AssertionError();
        }
    }

    public Date getManualTimestamp(CsvReader creader) throws IOException, ParseException {
        switch (languageSelection) {
            case DE:
                String timeString1 = creader.get(CARELINK_HEADER_DE_DATE).trim();
                String timeString2 = creader.get(CARELINK_HEADER_DE_TIME).trim();
                return TimestampUtils.createCleanTimestamp(
                        timeString1 + " " + timeString2, TIME_FORMAT_DE);
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                throw new AssertionError();
        }
    }

}
