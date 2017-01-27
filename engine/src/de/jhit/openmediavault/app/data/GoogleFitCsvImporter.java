/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.data;

import com.csvreader.CsvReader;
import de.jhit.openmediavault.app.container.RawDataEntry;
import de.jhit.openmediavault.app.container.VaultEntry;
import de.jhit.openmediavault.app.container.VaultEntryType;
import de.jhit.openmediavault.app.preferences.Constants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public class GoogleFitCsvImporter {

    public static List<RawDataEntry> parseData(String filepath)
            throws FileNotFoundException {
        // read file
        File googleFile = new File(filepath);
        String dateString = googleFile.getName().split("\\.")[0];

        CsvReader creader = new CsvReader(filepath, ',', Charset.forName("UTF-8"));

        try {
            // validate header
            // TODO implement a header-erkenner
            // for (int i = 0; i < 2; i++) {
            creader.readHeaders();
            //TODO compute meta data
            // }
            //TODO check header data

            // read entries
            while (creader.readRecord()) {
                // Todo cathegorize entry
                RawDataEntry entry = parseEntry(creader, dateString);
                if (entry != null) {
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
        return null;
    }

    private static RawDataEntry parseEntry(CsvReader reader, String dateString)
            throws IOException, ParseException {

        String[] validHeader = Constants.GOOGLE_FIT_CSV_HEADER[0];
        long activityTimeThreshold = 300000;// 5m = 300000ms

        // TODO distinguish between types
        // check if entry is interesting
        String maxSpeedString = reader.get(validHeader[2]);
        if (maxSpeedString == null || maxSpeedString.isEmpty()) {
            return null;
        }
        double maxSpeed = Double.parseDouble(maxSpeedString);
        if (maxSpeed < 6.0) { // almost running
            // use higher threshold
            activityTimeThreshold *= 2; // 10m
        }

        // check if it was long enough
        String timeOfActionString = reader.get(validHeader[3]);
        long timeOfAction = 0L;
        if (timeOfActionString != null && !timeOfActionString.isEmpty()) {
            timeOfAction += Long.parseLong(timeOfActionString);
        }
        timeOfActionString = reader.get(validHeader[4]);
        if (timeOfActionString != null && !timeOfActionString.isEmpty()) {
            timeOfAction += Long.parseLong(timeOfActionString);
        }
        timeOfActionString = reader.get(validHeader[5]);
        if (timeOfActionString != null && !timeOfActionString.isEmpty()) {
            timeOfAction += Long.parseLong(timeOfActionString);
        }
        if (timeOfAction < activityTimeThreshold) { // 5m for run or bike, 10m for walk
            return null;
        }
        timeOfAction = Math.round(timeOfAction / 60000);

        // create timestamp
        Date timestamp = TimestampUtils.createCleanTimestamp(
                dateString + " " + reader.get(validHeader[0]).replaceAll(":", ""),
                TimestampUtils.TIME_FORMAT_GOOGLE_DE);

        VaultDao.getInstance().putEntry(
                new VaultEntry(VaultEntryType.EXERCISE_GoogleBicycle,
                        timestamp, (double) timeOfAction));

        return null;

    }
}
