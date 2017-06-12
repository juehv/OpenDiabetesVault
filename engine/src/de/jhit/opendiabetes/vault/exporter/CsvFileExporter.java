/*
 * Copyright (C) 2017 juehv
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
package de.jhit.opendiabetes.vault.exporter;

import com.csvreader.CsvWriter;
import de.jhit.opendiabetes.vault.container.csv.CsvEntry;
import de.jhit.opendiabetes.vault.container.csv.VaultCsvEntry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juehv
 */
public abstract class CsvFileExporter {

    public final static int RESULT_OK = 0;
    public final static int RESULT_ERROR = -1;
    public final static int RESULT_NO_DATA = -2;
    public final static int RESULT_FILE_ACCESS_ERROR = -3;

    protected static final Logger LOG = Logger.getLogger(VaultCsvExporter.class.getName());

    protected final ExporterOptions options;
    protected final String filePath;
    protected FileOutputStream fileOutpuStream;

    protected CsvFileExporter(ExporterOptions options, String filePath) {
        this.options = options;
        this.filePath = filePath;
    }

    protected abstract List<CsvEntry> prepareData();

    public int exportDataToFile() {
        // check file stuff        
        File checkFile = new File(filePath);
        if (checkFile.exists()
                && (!checkFile.isFile() || !checkFile.canWrite())) {
            return RESULT_FILE_ACCESS_ERROR;
        }
        try {
            fileOutpuStream = new FileOutputStream(checkFile);
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error accessing file for output stream", ex);
            return RESULT_FILE_ACCESS_ERROR;
        }

        // create csv data
        List<CsvEntry> csvEntrys = prepareData();
        if (csvEntrys == null || csvEntrys.isEmpty()) {
            return RESULT_NO_DATA;
        }

        // write to file
        try {
            writeToFile(csvEntrys);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error writing odv csv file: {0}" + filePath, ex);
            return RESULT_ERROR;
        }
        return RESULT_OK;
    }

    protected void writeToFile(List<CsvEntry> csvEntries) throws IOException {
        CsvWriter cwriter = new CsvWriter(fileOutpuStream, VaultCsvEntry.CSV_DELIMITER,
                Charset.forName("UTF-8"));

        cwriter.writeRecord(csvEntries.get(0).getCsvHeaderRecord());
        for (CsvEntry item : csvEntries) {
            cwriter.writeRecord(item.toCsvRecord());
        }
        cwriter.flush();
        cwriter.close();
        fileOutpuStream.close();
    }

}
