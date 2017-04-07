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
public class ExerciseInterpreterOptions extends InterpreterOptions {
    
    public final int activityThreshold;
    public final int activitySliceThreshold;
    
    public ExerciseInterpreterOptions(boolean isImportPeriodRestricted, 
            Date importPeriodFrom, Date importPeriodTo,
            int activityThreshold,
            int activitySliceThreshold) {
        super(isImportPeriodRestricted, importPeriodFrom, importPeriodTo);
        this.activityThreshold = activityThreshold;
        this.activitySliceThreshold = activitySliceThreshold;
    }
    
}
