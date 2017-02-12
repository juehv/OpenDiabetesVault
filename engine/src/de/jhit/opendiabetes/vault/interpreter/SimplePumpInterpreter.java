/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import de.jhit.opendiabetes.vault.importer.FileImporter;
import de.jhit.openmediavault.app.container.VaultEntry;
import de.jhit.openmediavault.app.data.VaultDao;
import java.util.List;

/**
 *
 * @author mswin
 */
public class SimplePumpInterpreter extends VaultInterpreter {

    public SimplePumpInterpreter(FileImporter importer,
            InterpreterOptions options, VaultDao db) {
        super(importer, options, db);
    }

    @Override
    protected List<VaultEntry> interpret(List<VaultEntry> result) {
        return result; //TODO implement
    }

}
