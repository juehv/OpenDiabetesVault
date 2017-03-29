/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.container;

/**
 *
 * @author mswin
 */
public enum VaultEntryType {
    // Bolus
// Bolus
    BOLUS_Normal,
    BOLUS_Square,
    // Basal
    BASAL_Profile,
    BASAL_Manual,
    // Exercise
    EXERCISE_Manual,
    EXERCISE_GoogleRun,
    EXERCISE_GoogleWalk,
    EXERCISE_GoogleBicycle,
    EXERCISE_TrackerWalk,
    EXERCISE_TrackerRun,
    EXERCISE_TrackerBicycle,
    // Glucose
    GLUCOSE_CGM,
    GLUCOSE_CGM_RAW,
    GLUCOSE_CGM_ALERT,
    GLUCOSE_CGM_CALIBRATION,
    GLUCOSE_BG,
    // Meal
    MEAL_BolusExpert,
    MEAL_Manual,
    // Pump Events
    PUMP_REWIND,
    PUMP_PRIME,
    PUMP_FILL,
    PUMP_NO_DELIVERY,
    PUMP_SUSPEND,
    PUMP_UNSUSPEND,
    PUMP_UNKNOWN_ERROR,
    // Sleep
    SLEEP_LIGHT,
    SLEEP_REM,
    SLEEP_DEEP,
    // Heart
    HEART_RATE,
    HEART_RATE_VARIABILITY,
    STRESS,
    // Location (Geocoding)
    LOC_TRANSISTION,
    LOC_HOME,
    LOC_WORK,
    LOC_FOOD,
    LOC_SPORTS,
    LOC_OTHER;
}
