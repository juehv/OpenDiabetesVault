/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.container;

import de.jhit.openmediavault.app.preferences.Constants;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author juehv
 */
public class DataEntry {

    public String type;
    public Date timestamp;

    @Override
    public String toString() {
        return "DataEntry{" + "type=" + type + ", timestamp=" + timestamp + '}';
    }

    public String toRefillEntry() {
        SimpleDateFormat dformat = new SimpleDateFormat(Constants.DATE_TIME_OUTPUT_FORMAT);
        return dformat.format(timestamp) + " --> " + type;
    }

}
