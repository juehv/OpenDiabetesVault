/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import java.util.Date;

/**
 *
 * @author Jens
 */
public class FitnessTrackerInterpreterOptions extends InterpreterOptions {
    
    public final int activityThreshold;
    
    public FitnessTrackerInterpreterOptions(boolean isImportPeriodRestricted, 
            Date importPeriodFrom, Date importPeriodTo,
            int activityThreshold) {
        super(isImportPeriodRestricted, importPeriodFrom, importPeriodTo);
        this.activityThreshold = activityThreshold;
    }
    
}
