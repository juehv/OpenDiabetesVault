/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.data;

import com.csvreader.CsvReader;
import de.jhit.openmediavault.app.container.GlucoseEntry;
import de.jhit.openmediavault.app.container.RawDataEntry;
import de.jhit.openmediavault.app.preferences.Constants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public class LibreTxtImporter {

    public static List<RawDataEntry> parseData(String filepath)
            throws FileNotFoundException {
        List<RawDataEntry> libreEntrys = new ArrayList<>();
        // read file
        CsvReader creader = new CsvReader(filepath, '\t', Charset.forName("UTF-8"));

        try {
            // validate header
            // TODO implement a header-erkenner
            for (int i = 0; i < 2; i++) {
                creader.readHeaders();
                //TODO compute meta data
            }
            //TODO check header data
//            if (!CsvValidator.validateCarelinkHeader(creader)) {
//                Logger.getLogger(CarelinkCsvImporter.class.getName()).
//                        log(Level.SEVERE,
//                                "Stop parser because of unvalid header:\n"
//                                + Arrays.toString(Constants.CARELINK_CSV_HEADER[0])
//                                + "\n{0}", creader.getRawRecord());
//                return null;
//            }

            // read entries
            while (creader.readRecord()) {
                // Todo cathegorize entry
                RawDataEntry entry = parseEntry(creader);
                if (entry != null) {
                    libreEntrys.add(entry);
                    Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
                            Level.INFO, "Got Entry: {0}", entry.toString());
                } else {
//                    Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
//                            Level.FINE, "Drop Entry: {0}", creader.getRawRecord());
                }

            }

        } catch (IOException | ParseException ex) {
            Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
                    Level.SEVERE, "Error while parsing Careling CSV", ex);
        } finally {
            creader.close();
        }
        return libreEntrys;
    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private static RawDataEntry parseEntry(CsvReader reader)
            throws IOException, ParseException {

        String[] validHeader = Constants.LIBRE_CSV_HEADER[0];
        int type = Integer.parseInt(reader.get(validHeader[1]));

        if (type == Constants.LIBRE_TYPE_INTEGER[0]) { // ScanGlucose
            RuntimeDataVault.getInstance().putGlucouseValue(
                    new GlucoseEntry(GlucoseEntry.TYPE.CGM_ALERT,
                            createTimestamp(reader.get(validHeader[0])),
                            Double.parseDouble(reader.get(validHeader[3]))));
        } else if (type == Constants.LIBRE_TYPE_INTEGER[1]) { // HistoricGlucose
            RuntimeDataVault.getInstance().putGlucouseValue(
                    new GlucoseEntry(GlucoseEntry.TYPE.CGM,
                            createTimestamp(reader.get(validHeader[0])),
                            Double.parseDouble(reader.get(validHeader[2]))));
        } else if (type == Constants.LIBRE_TYPE_INTEGER[2]) { // BloodGlucose
            RuntimeDataVault.getInstance().putGlucouseValue(
                    new GlucoseEntry(GlucoseEntry.TYPE.BG,
                            createTimestamp(reader.get(validHeader[0])),
                            Double.parseDouble(reader.get(validHeader[4]))));
        } else {
            Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
                    Level.SEVERE, "Error while type checking!");
            return null;
        }

        // ########### OLD
        RawDataEntry entry = new RawDataEntry();

        entry.timestamp = createTimestamp(reader.get(validHeader[0]));

        if (type == Constants.LIBRE_TYPE_INTEGER[0]) {
            entry.type = Constants.LIBRE_TYPE[0];
            entry.amount = Double.parseDouble(reader.get(validHeader[3]));
        } else if (type == Constants.LIBRE_TYPE_INTEGER[1]) {
            entry.type = Constants.LIBRE_TYPE[1];
            entry.amount = Double.parseDouble(reader.get(validHeader[2]));
        } else if (type == Constants.LIBRE_TYPE_INTEGER[2]) {
            entry.type = Constants.LIBRE_TYPE[2];
            entry.amount = Double.parseDouble(reader.get(validHeader[4]));
        } else {
            Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
                    Level.SEVERE, "Error while type checking!");
            return null;
        }

        return entry;

    }

    private static Date createTimestamp(String dateTime) throws ParseException {
        String format = "yyyy.MM.dd HH:mm";

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(dateTime);
    }
}
