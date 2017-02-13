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
public class SimplePumpInterpreter extends VaultInterpreter {

    public SimplePumpInterpreter(FileImporter importer,
            InterpreterOptions options, VaultDao db) {
        super(importer, options, db);
    }

    @Override
    protected List<VaultEntry> interpret(List<VaultEntry> result) {
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
            return result; //TODO implement
        }
    }

}
