/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

/**
 *
 * @author juehv
 */
public class NonValidator extends CsvValidator {

    @Override
    public boolean validateHeader(String[] header) {
        return true;
    }
    
}
