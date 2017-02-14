/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author juehv
 */
public class TimestampUtils {

    public static final String TIME_FORMAT_LIBRE_DE = "yyyy.MM.dd HH:mm";
    public static final String TIME_FORMAT_CARELINK_DE = "dd.MM.yy HH:mm:ss";
    public static final String TIME_FORMAT_GOOGLE_DE = "yyyy-MM-dd HHmmss.SSSZ";        //01:00:00.000+01:00

    public static Date createCleanTimestamp(String dateTime, String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date rawDate = df.parse(dateTime);
        return createCleanTimestamp(rawDate);
    }

    public static Date createCleanTimestamp(Date rawDate) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(rawDate);
        // round to 5 minutes
        //        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        //        int mod = unroundedMinutes % 5;
        //        calendar.add(Calendar.MINUTE, mod < 3 ? -mod : (5 - mod));
        // round to 1 minute
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date addMinutesToTimestamp(Date timestamp, int minutes) {
        return new Date(addMinutesToTimestamp(timestamp.getTime(), minutes));
    }

    public static long addMinutesToTimestamp(long timestamp, int minutes) {
        timestamp += minutes * 60000; // 1 m = 60000 ms
        return timestamp;
    }

    public static Date fromLocalDate(LocalDate inputDate) {
        return fromLocalDate(inputDate, 0);
    }

    public static Date fromLocalDate(LocalDate inputDate, long offestInMilliseconds) {
        Date tmpInputDate = Date.from(Instant.from(inputDate
                .atStartOfDay(ZoneId.systemDefault())));
        if (offestInMilliseconds > 0) {
            tmpInputDate = new Date(tmpInputDate.getTime() + offestInMilliseconds);
        }
        return tmpInputDate;
    }

}
