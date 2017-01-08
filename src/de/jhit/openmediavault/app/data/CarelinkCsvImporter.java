/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.data;

import com.csvreader.CsvReader;
import de.jhit.openmediavault.app.container.RawDataEntry;
import de.jhit.openmediavault.app.preferences.Constants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    public static List<RawDataEntry> parseData(String filepath)
            throws FileNotFoundException {
        List<RawDataEntry> CarelinkEntry = new ArrayList<>();
        // read file
        CsvReader creader = new CsvReader(filepath, ';', Charset.forName("UTF-8"));

        try {
            // validate header
            for (int i = 0; i < Constants.CARELINK_CSV_EMPTY_LINES; i++) {
                creader.readHeaders();
                //TODO compute meta data
            }
            if (!CsvValidator.validateCarelinkHeader(creader)) {
                Logger.getLogger(CarelinkCsvImporter.class.getName()).
                        log(Level.SEVERE,
                                "Stop parser because of unvalid header:\n"
                                + Arrays.toString(Constants.CARELINK_CSV_HEADER[0])
                                + "\n{0}", creader.getRawRecord());
                return null;
            }

            // read entries
            while (creader.readRecord()) {
                // Todo cathegorize entry
                RawDataEntry entry = parseEntry(creader);
                if (entry != null) {
                    CarelinkEntry.add(entry);
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
        return CarelinkEntry;
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
        RawDataEntry entry = null;
        String[] validHeader = Constants.CARELINK_CSV_HEADER[Constants.CARELINK_CSV_LANG_SELECTION];

        String type = reader.get(validHeader[2]);
        for (int i = 0; i < Constants.CARELINK_TYPE.length; i++) {
            if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[i])) {
                String[] rawValues = reader.get(validHeader[3]).split(",");
                switch (i) {
                    case 0: // Rewind
                        entry = new RawDataEntry();
                        entry.type = Constants.CARELINK_TYPE[i];
                        entry.timestamp
                                = createTimestamp(reader.get(validHeader[0]),
                                        reader.get(validHeader[1]));
                        break;
                    case 1: // Prime
                        entry = new RawDataEntry();
                        entry.type = Constants.CARELINK_TYPE[i];
                        entry.timestamp
                                = createTimestamp(reader.get(validHeader[0]),
                                        reader.get(validHeader[1]));
                        break;
                    case 2: // exercise marker
                        entry = new RawDataEntry();
                        entry.type = Constants.CARELINK_TYPE[i];
                        entry.timestamp
                                = createTimestamp(reader.get(validHeader[0]),
                                        reader.get(validHeader[1]));
                        entry.amount = 0; // for better loocking values in gui
                        break;
                    case 3: // BGCapturedOnPump
                        entry = new RawDataEntry();
                        entry.type = Constants.CARELINK_TYPE[i];
                        entry.timestamp
                                = createTimestamp(reader.get(validHeader[0]),
                                        reader.get(validHeader[1]));
                        for (String value : rawValues) {
                            if (value.contains(Constants.CARELINK_RAW_VALUE_AMOUNT)) {
                                entry.amount = Double.
                                        parseDouble(value.split("=")[1]);
                            }
                        }
                        break;
                    case 4: // BGReceived
                        entry = new RawDataEntry();
                        entry.type = Constants.CARELINK_TYPE[i];
                        entry.timestamp
                                = createTimestamp(reader.get(validHeader[0]),
                                        reader.get(validHeader[1]));
                        for (String value : rawValues) {
                            if (value.contains(Constants.CARELINK_RAW_VALUE_AMOUNT)) {
                                entry.amount = Double.
                                        parseDouble(value.split("=")[1]);
                            } else if (value.contains(
                                    Constants.CARELINK_RAW_VALUE_BG_LINK_ID)) {
                                entry.linkId = "#" + value.split("=")[1];
                            }
                        }
                        break;
                    case 5: // BolusWizardBolusEstimate
                        entry = new RawDataEntry();
                        entry.type = Constants.CARELINK_TYPE[i];
                        entry.timestamp
                                = createTimestamp(reader.get(validHeader[0]),
                                        reader.get(validHeader[1]));
                        for (String value : rawValues) {
                            if (value.contains(Constants.CARELINK_RAW_VALUE_CARB_INPUT)) {
                                entry.amount = Double.
                                        parseDouble(value.split("=")[1]);
                                break;
                            }
                        }
                        break;
                    case 6: // BolusNormal
                        entry = new RawDataEntry();
                        entry.type = Constants.CARELINK_TYPE[i];
                        entry.timestamp
                                = createTimestamp(reader.get(validHeader[0]),
                                        reader.get(validHeader[1]));
                        for (String value : rawValues) {
                            if (value.contains(Constants.CARELINK_RAW_VALUE_AMOUNT)) {
                                entry.amount = Double.
                                        parseDouble(value.split("=")[1]);
                                break;
                            }
                        }
                        break;
                    default:
                        Logger.getLogger(CarelinkCsvImporter.class.getName()).log(
                                Level.SEVERE, "Error while type checking!");
                        break;

                }
            }
        }
        // get time
//        String date = reader.get(Constants.THEADER_DATE);
//        String startT = reader.get(Constants.THEADER_TIME_START);
//        String endT = reader.get(Constants.THEADER_TIME_END);

        return entry;

    }

    private static Date createTimestamp(String date, String time) throws ParseException {
        String format = Constants.CARELINK_CSV_DATETIME_FORMAT[Constants.CARELINK_CSV_LANG_SELECTION];

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(time + date);
    }

}
