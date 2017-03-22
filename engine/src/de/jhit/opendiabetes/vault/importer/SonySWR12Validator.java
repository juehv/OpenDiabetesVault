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
public class SonySWR12Validator extends CsvValidator {

    public static final String HEADER_TIMESTAMP = "event_timestamp";
    public static final String HEADER_TYPE = "activity_type";
    public static final String HEADER_VALUE = "activity_data";
    public static final String HEADER_START_TIME = "start_time";
    public static final String HEADER_END_TIME = "end_time";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String[] CARELINK_HEADER_DE = {
        HEADER_START_TIME, HEADER_END_TIME,
        HEADER_TIMESTAMP, HEADER_TYPE,
        HEADER_VALUE
    };

    public static enum TYPE {
        SLEEP_LIGHT(0), SLEEP_DEEP(0),
        HEART_RATE_VARIABILITY(9), HEART_RATE(8),
        WALK(0), RUN(0), CYCLE(0); // TODO update ints

        final int typeInt;

        TYPE(int typeInt) {
            this.typeInt = typeInt;
        }

        static TYPE fromInt(int typeInt) {
            for (TYPE item : TYPE.values()) {
                if (item.typeInt == typeInt) {
                    return item;
                }
            }
            return null;
        }
    }

    @Override
    public boolean validateHeader(String[] header) {

        boolean result = true;
        Set<String> headerSet = new TreeSet<>(Arrays.asList(header));

        // Check header
        for (String item : CARELINK_HEADER_DE) {
            result &= headerSet.contains(item);
        }
        if (result == true) {
            languageSelection = Language.UNIVERSAL;
        }

        return result;
    }

    public TYPE getSmartbandType(CsvReader creader) throws IOException {
        int typeInt = Integer.parseInt(creader.get(HEADER_TYPE).trim());
        return TYPE.fromInt(typeInt);
    }

    public Date getTimestamp(CsvReader creader) throws IOException, ParseException {
        String timeString = creader.get(HEADER_TIMESTAMP).trim();
        return TimestampUtils.createCleanTimestamp(timeString, TIME_FORMAT);
    }

    public int getValue(CsvReader creader) throws IOException {
        return Integer.parseInt(creader.get(HEADER_VALUE));
    }

    long getStartTime(CsvReader creader) throws IOException {
        return Long.parseLong(creader.get(HEADER_START_TIME));
    }

    long getEndTime(CsvReader creader) throws IOException {
        return Long.parseLong(creader.get(HEADER_END_TIME));
    }
}
