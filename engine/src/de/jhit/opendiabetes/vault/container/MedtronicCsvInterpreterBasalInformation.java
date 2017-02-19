/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.container;

import de.jhit.opendiabetes.vault.importer.MedtronicCsvValidator;

/**
 *
 * @author mswin
 */
public class MedtronicCsvInterpreterBasalInformation extends VaultEntry {

    private final double duration;
    private final MedtronicCsvValidator.TYPE rawType;

    public MedtronicCsvInterpreterBasalInformation(VaultEntry copy,
            double duration, MedtronicCsvValidator.TYPE rawType) {
        super(copy);
        this.duration = duration;
        this.rawType = rawType;
    }

    public double getDuration() {
        return duration;
    }

    public MedtronicCsvValidator.TYPE getRawType() {
        return rawType;
    }

}
