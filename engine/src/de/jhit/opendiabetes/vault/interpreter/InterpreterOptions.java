/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import java.util.Date;

/**
 *
 * @author mswin
 */
public abstract class InterpreterOptions {
   
    public final boolean isImportPeriodRestricted;
    public final Date importPeriodFrom;
    public final Date importPeriodTo;

    public InterpreterOptions(boolean isImportPeriodRestricted, 
            Date importPeriodFrom, Date importPeriodTo) {
        this.isImportPeriodRestricted = isImportPeriodRestricted;
        this.importPeriodFrom = importPeriodFrom;
        this.importPeriodTo = importPeriodTo;
    }

    

}
