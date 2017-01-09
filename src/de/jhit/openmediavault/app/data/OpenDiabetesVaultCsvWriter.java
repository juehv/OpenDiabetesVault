/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.data;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import de.jhit.openmediavault.app.container.RawDataEntry;
import de.jhit.openmediavault.app.container.VaultEntry;
import de.jhit.openmediavault.app.preferences.Constants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public class OpenDiabetesVaultCsvWriter {
    
    public static void writeData(String filepath, List<VaultEntry> cgmEntries)
            throws FileNotFoundException {
        CsvWriter cwriter = new CsvWriter(filepath, ',', Charset.forName("UTF-8"));

//        try {
//            // validate header
//            for (int i = 0; i < Constants.CARELINK_CSV_EMPTY_LINES; i++) {
//                creader.readHeaders();
//                //TODO compute meta data
//            }
//            if (!CsvValidator.validateCarelinkHeader(creader)) {
//                Logger.getLogger(CarelinkCsvImporter.class.getName()).
//                        log(Level.SEVERE,
//                                "Stop parser because of unvalid header:\n"
//                                + Arrays.toString(Constants.CARELINK_CSV_HEADER[0])
//                                + "\n{0}", creader.getRawRecord());
//                return null;
//            }
//
//            // read entries
//            while (creader.readRecord()) {
//                // Todo cathegorize entry
//                RawDataEntry entry = parseEntry(creader);
//                if (entry != null) {
//                    CarelinkEntry.add(entry);
//                    Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
//                            Level.INFO, "Got Entry: {0}", entry.toString());
//                } else {
////                    Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
////                            Level.FINE, "Drop Entry: {0}", creader.getRawRecord());
//                }
//
//            }
//
//        } catch (IOException | ParseException ex) {
//            Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
//                    Level.SEVERE, "Error while parsing Careling CSV", ex);
//        } finally {
//            creader.close();
//        }
//        return CarelinkEntry;
    }
}
