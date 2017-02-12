/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.container;

/**
 *
 * @author mswin
 */
public enum VaultEntryType {
    // Bolus
    BOLUS_BolusExpertNormal,
    BOLUS_BolusExpertSquare,
    BOLUS_BolusExpertDual,
    BOLUS_ManualNormal,
    BOLUS_ManualSquare,
    BOLUS_ManualDual,
    // Basal
    BASAL_Profile,
    BASAL_Manual,
    // Exercise
    EXERCISE_Manual,
    EXERCISE_GoogleRun,
    EXERCISE_GoogleWalk,
    EXERCISE_GoogleBicycle,
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
    PUMP_NO_DELIVERY,
    PUMP_UNKNOWN_ERROR;

    public boolean isBolusType() {
        switch (this) {
            case BOLUS_BolusExpertNormal:
            case BOLUS_BolusExpertSquare:
            case BOLUS_BolusExpertDual:
            case BOLUS_ManualNormal:
            case BOLUS_ManualSquare:
            case BOLUS_ManualDual:
                return true;
            default:
                return false;
        }
    }

    public boolean isExerciseType() {
        switch (this) {
            case EXERCISE_Manual:
            case EXERCISE_GoogleRun:
            case EXERCISE_GoogleWalk:
            case EXERCISE_GoogleBicycle:
                return true;
            default:
                return false;
        }
    }

    public boolean isGlucoseType() {
        switch (this) {
            case GLUCOSE_CGM:
            case GLUCOSE_CGM_ALERT:
            case GLUCOSE_BG:
                return true;
            default:
                return false;
        }
    }

    public boolean isMealType() {
        switch (this) {
            case MEAL_BolusExpert:
            case MEAL_Manual:
                return true;
            default:
                return false;
        }
    }
}
