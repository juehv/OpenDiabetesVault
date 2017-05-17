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
import deprecated_code.RawDataEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import de.jhit.opendiabetes.vault.data.VaultDao;
import deprecated_code.Constants;
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
        try{
        Date timestamp = TimestampUtils.createCleanTimestamp(
                dateString + " " + reader.get(validHeader[0]).replaceAll(":", ""),
                TimestampUtils.TIME_FORMAT_GOOGLE_DE);

        VaultDao.getInstance().putEntry(
                new VaultEntry(VaultEntryType.EXERCISE_GoogleBicycle,
                        timestamp, (double) timeOfAction));

        } catch (Exception ex){}
        return null;

    }
}
