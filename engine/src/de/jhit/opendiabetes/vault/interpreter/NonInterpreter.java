/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.opendiabetes.vault.importer.FileImporter;
import java.util.List;

/**
 *
 * @author juehv
 */
public class NonInterpreter extends VaultInterpreter {

    public NonInterpreter(FileImporter importer, InterpreterOptions options, VaultDao db) {
        super(importer, options, db);
    }

    @Override
    protected List<VaultEntry> interpret(List<VaultEntry> result) {
        return result;
    }

}
