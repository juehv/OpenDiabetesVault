/*
 * Copyright (C) 2017 Jens Heuschkel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
                boolean parsed = false;
                if (entryList != null && !entryList.isEmpty()) {
                    for (VaultEntry item : entryList) {
                        item.setRawId(importedRawData.size()); // add array position as raw id
                        importedData.add(item);
                        LOG.log(Level.FINE, "Got Entry: {0}", entryList.toString());
                    }
                    parsed = true;
                }
                importedRawData.add(new RawEntry(creader.getRawRecord(), parsed));
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
