/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.exporter;

import java.util.Date;

/**
 *
 * @author Jens
 */
public class ExporterOptions {
    
    public final boolean isImportPeriodRestricted;
    public final Date exportPeriodFrom;
    public final Date exportPeriodTo;

    public ExporterOptions(boolean isImportPeriodRestricted, Date exportPeriodFrom, Date exportPeriodTo) {
        this.isImportPeriodRestricted = isImportPeriodRestricted;
        this.exportPeriodFrom = exportPeriodFrom!= null ? exportPeriodFrom : new Date();
        this.exportPeriodTo = exportPeriodTo!= null ? exportPeriodTo : new Date();
    }
}
