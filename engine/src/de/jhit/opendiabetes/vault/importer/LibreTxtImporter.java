/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import deprecated_code.RawDataEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import de.jhit.opendiabetes.vault.data.VaultDao;
import deprecated_code.Constants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
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

        // TODO add stuff
        // Teststreifen-Blutzucker (mg/dL)
        // Keton (mmol/L)

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
                    Logger.getLogger(MedtronicCsvImporter.class.getName()).log(
                            Level.INFO, "Got Entry: {0}", entry.toString());
                } else {
//                    Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
//                            Level.FINE, "Drop Entry: {0}", creader.getRawRecord());
                }

            }

        } catch (IOException | ParseException ex) {
            Logger.getLogger(MedtronicCsvImporter.class.getName()).log(
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

        if (type == Constants.LIBRE_TYPE_INTEGER[1]) { // HistoricGlucose
            Date timestamp = TimestampUtils.createCleanTimestamp(reader.get(validHeader[0]),
                    TimestampUtils.TIME_FORMAT_LIBRE_DE);
            double value = Double.parseDouble(reader.get(validHeader[2]));

            VaultDao.getInstance().putEntry(
                    new VaultEntry(VaultEntryType.GLUCOSE_CGM,
                            timestamp, value));
        } else if (type == Constants.LIBRE_TYPE_INTEGER[0]) { // ScanGlucose
            Date timestamp = TimestampUtils.createCleanTimestamp(reader.get(validHeader[0]),
                    TimestampUtils.TIME_FORMAT_LIBRE_DE);
            double value = Double.parseDouble(reader.get(validHeader[3]));

            VaultDao.getInstance().putEntry(
                    new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT,
                            timestamp, value));
        } else if (type == Constants.LIBRE_TYPE_INTEGER[2]) { // BloodGlucose
            Date timestamp = TimestampUtils.createCleanTimestamp(reader.get(validHeader[0]),
                    TimestampUtils.TIME_FORMAT_LIBRE_DE);
            double value = Double.parseDouble(reader.get(validHeader[4]));

            VaultDao.getInstance().putEntry(
                    new VaultEntry(VaultEntryType.GLUCOSE_BG,
                            timestamp, value));
        } else {
            Logger.getLogger(MedtronicCsvImporter.class.getName()).log(
                    Level.SEVERE, "Error while type checking!");
            return null;
        }

        return null;

    }

}
