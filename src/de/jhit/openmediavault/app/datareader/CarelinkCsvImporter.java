/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.datareader;

import com.csvreader.CsvReader;
import de.jhit.openmediavault.app.container.DataEntry;
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
 * @author Jens
 */
public class CarelinkCsvImporter {

    /**
     *
     * @param filepath
     * @return
     * @throws FileNotFoundException
     */
    public static List<DataEntry> parseCsvBookEntrys(String filepath)
            throws FileNotFoundException {
        List<DataEntry> bookEntrys = new ArrayList<>();
        // read file
        CsvReader creader = new CsvReader(filepath, ',', Charset.forName("UTF-8"));

        try {
            // validate header
            for (int i = 0; i < Constants.CARELINK_CSV_EMPTY_LINES; i++) {
                creader.readHeaders();
            }
            if (!CsvValidator.validateCarelinkHeader(creader)) {
                Logger.getLogger(CarelinkCsvImporter.class.getName()).
                        log(Level.SEVERE,
                                "Stop parser because of unvalid header:\n"+
                                Arrays.toString(Constants.CARELINK_CSV_HEADER)+
                                "\n{0}", creader.getRawRecord());
                return null;
            }

            // read entries
            while (creader.readRecord()) {
                // Todo cathegorize entry
                    DataEntry entry = new DataEntry();
                    
                    entry = parseEntry(entry, creader);
                    bookEntrys.add(entry);
                    System.out.println(creader.getCurrentRecord() + " " + entry.toString());
                
            }

        } catch (IOException | ParseException ex) {
            Logger.getLogger(CarelinkCsvImporter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            creader.close();
        }
        return bookEntrys;
    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private static DataEntry parseEntry(DataEntry entry, CsvReader reader)
            throws IOException, ParseException {
        // get time
//        String date = reader.get(Constants.THEADER_DATE);
//        String startT = reader.get(Constants.THEADER_TIME_START);
//        String endT = reader.get(Constants.THEADER_TIME_END);

        return entry;

    }

}
