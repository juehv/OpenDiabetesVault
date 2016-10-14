/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.data;

import com.csvreader.CsvReader;
import de.jhit.openmediavault.app.preferences.Constants;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author juehv
 */
public class CsvValidator {

    static boolean validateCarelinkHeader(CsvReader creader) throws IOException {

        boolean result = true;
        Set<String> header = new TreeSet<>(Arrays.asList(creader.getHeaders()));

        // Check english headers
        for (int i = 0; i < Constants.CARELINK_CSV_HEADER.length; i++) {
            for (String item : Constants.CARELINK_CSV_HEADER[i]) {
                result &= header.contains(item);
            }
            if (result == true) {
                Constants.CARELINK_CSV_LANG_SELECTION = i;
                break;
            }
        }

        return result;
    }

}
