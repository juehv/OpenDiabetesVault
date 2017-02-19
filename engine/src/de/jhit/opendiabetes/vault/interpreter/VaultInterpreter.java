/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import de.jhit.opendiabetes.vault.container.RawEntry;
import de.jhit.opendiabetes.vault.importer.FileImporter;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public abstract class VaultInterpreter {

    protected static final Logger LOG = Logger.getLogger(VaultInterpreter.class.getName());

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
        if (!importer.importFile(filePath)) {
            return;
        }
        // parse file
        List<VaultEntry> result = importer.getImportedData();
        if (result.isEmpty()) { // not null since importFile is called
            return;
        }

        // filter unwanted dates
        result = dateFiltering(result);
        // interpret stuff
        result = interpret(result);
        if (result == null) {
            return;
        }

        for (RawEntry item : importer.getImportedRawData()) {// not null since importFile is called
            item.setId(db.putRawEntry(item));
        }

        // update DB
        for (VaultEntry item : result) {
            // update raw id (if there is a corresponding raw entry)
            if (item.getRawId() > 0) {
                RawEntry rawEntry = importer.getImportedRawData()
                        .get((int) item.getRawId());
                item.setRawId(rawEntry.getId());
            }
            // put in db
            db.putEntry(item);
        }
    }

    protected abstract List<VaultEntry> interpret(List<VaultEntry> result);
}
