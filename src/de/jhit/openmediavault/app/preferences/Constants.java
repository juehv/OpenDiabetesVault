/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.preferences;

/**
 *
 * @author juehv
 */
public final class Constants {

    private Constants() {
    }

    public static final int CARELINK_CSV_EMPTY_LINES = 11;
    public static int CARELINK_CSV_LANG_SELECTION = 0;
    public static final String[][] CARELINK_CSV_HEADER = {
        {"Datum", "Zeit", "Roh-Typ", "Roh-Werte"}, //GER
        {"Date", "Time", "Roh-Typ", "Roh-Werte"} // ENG
    };
    public static final String[] CARELINK_CSV_DATETIME_FORMAT = {
        "HH:mm:ssdd.MM.yy", "HH:mm:ssdd.MM.yy"
    };
    public static final String[] CARELINK_TYPE
            = {"Prime", "fill",
                "BGCapturedOnPump", "BGReceived",
                "BolusWizardBolusEstimate", "BolusNormal"}; // TODO add fill
    
    public static final String DATE_TIME_OUTPUT_FORMAT = "dd.MM.yy - HH:mm";

    // could be interesting
//    private static final Map<String, String> CARELINK_TYPE;
//    static {
//        Map<String, String> aMap = new HashMap<>();
//        aMap.put("BG_RECEIVE", "one");
//        aMap.put("BG_RECEIVE", "two");
//        CARELINK_TYPE = Collections.unmodifiableMap(aMap);
//    }
}
