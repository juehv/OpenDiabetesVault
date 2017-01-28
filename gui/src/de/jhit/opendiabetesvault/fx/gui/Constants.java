/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetesvault.fx.gui;

import javafx.stage.FileChooser;

/**
 *
 * @author mswin
 */
public final class Constants {
    private Constants() {
    }
    
    // File Filter
    public static final FileChooser.ExtensionFilter CSV_EXTENSION_FILTER = new FileChooser.ExtensionFilter("CSV", "*.csv");
    public static final FileChooser.ExtensionFilter TXT_EXTENSION_FILTER = new FileChooser.ExtensionFilter("TXT", "*.txt");

    // Prefs
    public static final String MEDTRONIC_IMPORT_PATH_KEY = "medtronicImportPath";
    public static final String MEDTRONIC_IMPORT_CHECKBOX_KEY = "medtronicImportCheckbox";
    
    // unused
    public static final String CARELINK_USER_KEY = "carelinkUser";
    public static final String CARELINK_PW_KEY = "carelinkPw";
    public static final String GOOGLE_USER_KEY = "googleUser";
    public static final String GOOGLE_PW_KEY = "googlePw";
//    public static final String BG_UNIT_KEY = "bgUnit";
//    public static final String BG_UNIT_MG = "mg";
//    public static final String BG_UNIT_MMOL = "mmol";
//    public static final String BG_UNIT_DEFAULT = BG_UNIT_MG;
    public static final String HYPO_THRESHOLD_MG_KEY = "hypoMgThreshold";
    public static final String HYPO_THRESHOLD_MMOL_KEY = "hypoMmolThreshold";
    public static final double HYPO_THRESHOLD_MG_DEFAULT = 71;
    public static final double HYPO_THRESHOLD_MMOL_DEFAULT = 3.9;
    public static final String HYPO_FOLLOW_TIME_KEY = "hypoFollowingTime";
    public static final int HYPO_FOLLOW_TIME_DEFAULT = 60;
    public static final String HYPER_FOLLOW_TIME_KEY = "hyperFollowingTime";
    public static final int HYPER_FOLLOW_TIME_DEFAULT = 1440;
    public static final String HYPER_THRESHOLD_MG_KEY = "hyperMgThreshold";
    public static final String HYPER_THRESHOLD_MMOL_KEY = "hyperMmolThreshold";
    public static final double HYPER_THRESHOLD_MG_DEFAULT = 180;
    public static final double HYPER_THRESHOLD_MMOL_DEFAULT = 16.7;
    public static final String HYPO_EXERCISE_HISTORY_TIME_KEY = "hypoExerciseHistoryTime";
    public static final int HYPO_EXERCISE_HISTORY_TIME_DEFAULT = 180;
    public static final String HYPO_MEAL_HISTORY_TIME_KEY = "hypoFoodHistoryTime";
    public static final int HYPO_MEAL_HISTORY_TIME_DEFAULT = 720;
    public static final String HYPER_MEAL_HISTORY_TIME_KEY = "hyperFoodHistoryTime";
    public static final int HYPER_MEAL_HISTORY_TIME_DEFAULT = 720;

    public static final String SLEEP_INDICATION_BED_TIME_KEY = "sleepIndicationStartTime";
    public static final int SLEEP_INDICATION_BED_TIME_DEFAULT = 22;
    public static final String SLEEP_INDICATION_WAKEUP_TIME_KEY = "sleepIndicationStopTime";
    public static final int SLEEP_INDICATION_WAKEUP_TIME_DEFAULT = 6;
    public static final String SLEEP_INDICATION_THRESHOLD_KEY = "sleepIndicationThreshold";
    public static final int SLEEP_INDICATION_THRESHOLD_DEFAULT = 30;

    public static final double MG_TO_MMOL_FACTOR = 0.0555;
    public static final double MMOL_TO_MG_FACTOR = 18.0182;
}
