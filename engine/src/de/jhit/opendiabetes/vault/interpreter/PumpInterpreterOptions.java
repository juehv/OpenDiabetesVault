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
public class PumpInterpreterOptions extends InterpreterOptions {
    
    public final boolean FillCanulaAsNewKatheder;
    public final int FillCanulaCooldown;

    public PumpInterpreterOptions(boolean FillCanulaAsNewKatheder, 
            int FillCanulaCooldown, boolean isImportPeriodRestricted, 
            Date importPeriodFrom, Date importPeriodTo) {
        super(isImportPeriodRestricted, importPeriodFrom, importPeriodTo);
        this.FillCanulaAsNewKatheder = FillCanulaAsNewKatheder;
        this.FillCanulaCooldown = FillCanulaCooldown;
    }

    
    
    
}
