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

    public static final String CARELINK_CSV_FILE_LAST_PATH = "carelinkLastPath";
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
            = {"Rewind", "Prime", "FillCanula", // TODO correct fill canula
                "BGCapturedOnPump", "BGReceived",
                "BolusWizardBolusEstimate", "BolusNormal"};
    public static final String CARELINK_RAW_VALUE_BGAMOUNT = "AMOUNT";
    public static final String CARELINK_RAW_VALUE_BG_LINK_ID = "PARADIGM_LINK_ID";
    public static final String DATE_TIME_OUTPUT_FORMAT = "dd.MM.yy - HH:mm";

    public static final String HYPO_BORDER_KEY = "hypoBorder";
    public static final double HYPO_BORDER_DEFAULT = 71;
    public static final String HYPER_BORDER_KEY = "hypersBorder";
    public static final double HYPER_BORDER_DEFAULT = 240;

    // could be interesting
//    private static final Map<String, String> CARELINK_TYPE;
//    static {
//        Map<String, String> aMap = new HashMap<>();
//        aMap.put("BG_RECEIVE", "one");
//        aMap.put("BG_RECEIVE", "two");
//        CARELINK_TYPE = Collections.unmodifiableMap(aMap);
//    }
}
