/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

import de.jhit.openmediavault.app.container.VaultEntry;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public abstract class FileImporter {

    protected final static Logger LOG = Logger.getLogger(FileImporter.class.getName());

    public FileImporter() {
    }

    public abstract List<VaultEntry> importFile(String filePath);
}
