/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.container;

import java.util.Date;

/**
 *
 * @author mswin
 */
public class BolusEntry {
    
    public static enum TYPE {
        BolusExpertNormal, BolusExpertSquare, BolusExpertDual, 
        ManualNormal, ManualSquare, ManualDual;
    };
    
    private final TYPE type;
    private final Date timestamp;
    private final double value;

    public BolusEntry(TYPE type, Date timestamp, double value) {
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    public TYPE getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }
}
