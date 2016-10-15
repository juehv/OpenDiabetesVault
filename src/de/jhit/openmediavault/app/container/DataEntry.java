/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.container;

import de.jhit.openmediavault.app.data.DataHelper;
import de.jhit.openmediavault.app.preferences.Constants;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author juehv
 */
public class DataEntry {

    public String type = "";
    public Date timestamp = new Date();
    public double amount = -1.0;
    public String linkId = "";

    @Override
    public String toString() {
        return "DataEntry{" + "type=" + type + ", timestamp=" + timestamp + '}';
    }

    public String toGuiListEntry() {
        SimpleDateFormat dformat = new SimpleDateFormat(Constants.DATE_TIME_OUTPUT_FORMAT);
        if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])) {
            // Prime
            return dformat.format(timestamp) + " --> " + type;
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[2])) {
            // exercise marker
            return dformat.format(timestamp) + " --> " + type + " --> " + amount;
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])) {
            // BG
            return dformat.format(timestamp) + " --> " + type + " --> " + amount;
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])) {
            // Bolus Wizard (interested in KEs)
            return dformat.format(timestamp) + " --> " + type + " --> " + amount + "BE";
        }
        if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])) {
            // Bolus given
            return dformat.format(timestamp) + " --> " + type + " --> " + amount + "I.E.";
        }
        return toString();
    }

    public boolean isEquivalent(DataEntry item) {
        if (item == null) {
            return false;
        }
        if (this == item) {
            return true;
        }

        if ((type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[1]))
                && ((item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])))) {
            // primes
            if (DataHelper.minutesDiff(this.timestamp, item.timestamp) > 15) {
                return false;
            }
        } else if ((type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[4]))
                && ((item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])))) {
            //bg
            if (DataHelper.minutesDiff(this.timestamp, item.timestamp) > 5) {
                return false;
            }
            if (this.amount - item.amount > 0.0001) {
                return false;
            }
        } else if ((type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[6]))
                && ((item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])))) {
            // bolus 
            if (DataHelper.minutesDiff(this.timestamp, item.timestamp) > 1) {
                return false;
            }
        } else {
            return false;
        }
        return true;
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
