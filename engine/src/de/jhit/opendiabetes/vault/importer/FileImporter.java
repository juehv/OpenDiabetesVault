/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

import de.jhit.opendiabetes.vault.container.RawEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public abstract class FileImporter {

    protected final static Logger LOG = Logger.getLogger(FileImporter.class.getName());
    protected List<VaultEntry> importedData;
    protected List<RawEntry> importedRawData;

    public FileImporter() {
    }

    public abstract boolean importFile(String filePath);

    public List<VaultEntry> getImportedData() {
        return importedData;
    }

    public List<RawEntry> getImportedRawData() {
        return importedRawData;
    }
    
    
}
