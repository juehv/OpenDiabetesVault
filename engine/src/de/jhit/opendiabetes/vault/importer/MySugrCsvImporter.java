/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import static de.jhit.opendiabetes.vault.importer.FileImporter.LOG;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author juehv
 */
public class MySugrCsvImporter extends CsvImporter {

    public MySugrCsvImporter() {
        this(',');
    }

    public MySugrCsvImporter(char delimiter) {
        super(new MySugrCsvValidator(), delimiter);
    }

    @Override
    protected void preprocessingIfNeeded(String filePath) {
        // test for delimiter
        CsvReader creader = null;
        try {
            // test for , delimiter
            creader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            for (int i = 0; i < 15; i++) { // just scan the first 15 lines for a valid header
                if (creader.readHeaders()) {
                    if (validator.validateHeader(creader.getHeaders())) {
                        // found valid header --> finish
                        delimiter = ',';
                        creader.close();
                        LOG.log(Level.FINE, "Use ',' as delimiter for MySugr CSV: {0}", filePath);
                        return;
                    }
                }
            }
            // if you end up here there was no valid header within the range
            // try the other delimiter in normal operation
            delimiter = ';';
            LOG.log(Level.FINE, "Use ';' as delimiter for MySugr CSV: {0}", filePath);
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Error while parsing MySugr CSV in delimiter check: "
                    + filePath, ex);
        } finally {
            if (creader != null) {
                creader.close();
            }
        }
    }

    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
