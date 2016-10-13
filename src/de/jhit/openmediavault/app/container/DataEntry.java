/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.container;

import de.jhit.openmediavault.app.preferences.Constants;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author juehv
 */
public class DataEntry {

    public String type = "";
    public Date timestamp = new Date();
    public Double amount = -1.0;
    public String linkId = "";

    @Override
    public String toString() {
        return "DataEntry{" + "type=" + type + ", timestamp=" + timestamp + '}';
    }

    public String toRefillEntry() {
        SimpleDateFormat dformat = new SimpleDateFormat(Constants.DATE_TIME_OUTPUT_FORMAT);
        return dformat.format(timestamp) + " --> " + type;
    }

    public String toHypoEntry() {
        SimpleDateFormat dformat = new SimpleDateFormat(Constants.DATE_TIME_OUTPUT_FORMAT);
        return dformat.format(timestamp) + " --> " + type + " --> " + amount;
    }

    public String toHyperEntry() {
        SimpleDateFormat dformat = new SimpleDateFormat(Constants.DATE_TIME_OUTPUT_FORMAT);
        return dformat.format(timestamp) + " --> " + type + " --> " + amount;
    }

    public boolean isEquivalent(DataEntry item) {
        if (item == null) {
            return false;
        }
        if (this == item) {
            return true;
        }

        if ((type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[2]))
                && ((item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[2])))) {
            // primes
            if (minutesDiff(this.timestamp, item.timestamp) > 15) {
                return false;
            }
        } else if ((type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[4]))
                && ((item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])))) {
            //bg
            if (minutesDiff(this.timestamp, item.timestamp) > 5) {
                return false;
            }
            if (this.amount - item.amount > 0.0001) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public static int minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return Math.abs((int) ((laterDate.getTime() / 60000)
                - (earlierDate.getTime() / 60000)));
    }

    public static Comparator<DataEntry> getTimeSortComparator() {

        Comparator<DataEntry> comp = new Comparator<DataEntry>() {

            @Override
            public int compare(DataEntry a, DataEntry b) {
                return a.timestamp.compareTo(b.timestamp);
            }
        };

        return comp;
    }

}
