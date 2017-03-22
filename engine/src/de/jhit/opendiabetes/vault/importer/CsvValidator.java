/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public abstract class CsvValidator {

    protected static final Logger LOG = Logger.getLogger(CsvValidator.class.getName());

    public static enum Language {
        DE, EN, UNIVERSAL;
    };

    protected Language languageSelection;

    public abstract boolean validateHeader(String[] header);
}
