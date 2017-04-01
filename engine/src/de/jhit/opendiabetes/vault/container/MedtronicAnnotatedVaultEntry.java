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
public class MedtronicAnnotatedVaultEntry extends VaultEntry {

    private final MedtronicCsvValidator.TYPE rawType;

    public MedtronicAnnotatedVaultEntry(VaultEntry copy,
            MedtronicCsvValidator.TYPE rawType) {
        super(copy);
        this.rawType = rawType;
    }

    public double getDuration() {
        return super.getValue2();
    }

    public MedtronicCsvValidator.TYPE getRawType() {
        return rawType;
    }

}
