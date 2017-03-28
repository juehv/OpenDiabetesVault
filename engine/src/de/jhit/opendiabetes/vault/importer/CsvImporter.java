/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.container.RawEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author mswin
 */
public abstract class CsvImporter extends FileImporter {

    protected final CsvValidator validator;
    protected char delimiter;

    public CsvImporter(CsvValidator validator, char delimiter) {
        this.validator = validator;
        this.delimiter = delimiter;
    }

    @Override
    public boolean importFile(String filePath) {
        preprocessingIfNeeded(filePath);

        importedData = new ArrayList<>();
        importedRawData = new ArrayList<>();
        List<String[]> metaEntrys = new ArrayList<>();

        CsvReader creader = null;
        try {
            // open file
            creader = new CsvReader(filePath, delimiter, Charset.forName("UTF-8"));

            //validate header
            do {
                if (!creader.readHeaders()) {
                    // no more lines --> no valid header
                    LOG.log(Level.WARNING, "No valid header found in File:{0}", filePath);
                    return false;
                }
                metaEntrys.add(creader.getHeaders());
            } while (!validator.validateHeader(creader.getHeaders()));
            metaEntrys.remove(metaEntrys.size() - 1); //remove valid header

            // read entries
            while (creader.readRecord()) {
                List<VaultEntry> entryList = parseEntry(creader);
                boolean interpreted = false;
                if (entryList != null && !entryList.isEmpty()) {
                    for (VaultEntry item : entryList) {
                        item.setRawId(importedRawData.size()); // add array position as raw id
                        importedData.add(item);
                        LOG.log(Level.FINE, "Got Entry: {0}", entryList.toString());
                    }
                    interpreted = true;
                }
                importedRawData.add(new RawEntry(creader.getRawRecord(), interpreted));
                LOG.log(Level.FINER, "Put Raw: {0}", creader.getRawRecord());
            }

        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Error while parsing CSV: "
                    + filePath, ex);
        } finally {
            if (creader != null) {
                creader.close();
            }
        }
        return true;
    }

    protected abstract List<VaultEntry> parseEntry(CsvReader creader) throws Exception;

    protected abstract void preprocessingIfNeeded(String filePath);

}
