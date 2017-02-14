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
public abstract class VaultInterpreter {

    protected FileImporter importer;
    protected InterpreterOptions options;
    protected VaultDao db;

    public VaultInterpreter(FileImporter importer, InterpreterOptions options, VaultDao db) {
        this.importer = importer;
        this.options = options;
        this.db = db;
    }

    public void importAndInterpretFromFile(String filePath) {
        // parse file
        List<VaultEntry> result = importer.importFile(filePath);

        // interpret stuff
        result = interpret(result);

        // update DB
        for (VaultEntry item : result) {
            db.putEntry(item);
        }
    }

    public void importAndInterpretFromFileList(String[] filePath) {
        for (String item : filePath) {
            importAndInterpretFromFile(item);
        }
    }

    protected abstract List<VaultEntry> interpret(List<VaultEntry> result);
}
