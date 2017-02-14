/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import de.jhit.opendiabetes.vault.importer.FileImporter;
import de.jhit.openmediavault.app.container.VaultEntry;
import de.jhit.openmediavault.app.data.VaultDao;
import java.util.ArrayList;
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

    private List<VaultEntry> dateFiltering(List<VaultEntry> result) {
        if (options.isImportPeriodRestricted) {
            List<VaultEntry> retVal = new ArrayList<>();
            for (VaultEntry item : result) {
                if (item.getTimestamp().after(options.importPeriodFrom)
                        && item.getTimestamp().before(options.importPeriodTo)) {
                    retVal.add(item);
                }
            }
            return retVal;
        } else {
            return result;
        }
    }

    public void importAndInterpretFromFile(String filePath) {
        // parse file
        List<VaultEntry> result = importer.importFile(filePath);
        if (result == null) {
            return;
        }

        // filter unwanted dates
        result = dateFiltering(result);
        // interpret stuff
        result = interpret(result);
        if (result == null) {
            return;
        }

        // update DB
        for (VaultEntry item : result) {
            db.putEntry(item);
        }
    }

    protected abstract List<VaultEntry> interpret(List<VaultEntry> result);
}
