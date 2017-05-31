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
    BASAL_INTERPRETER,
    // Exercise
    EXERCISE_Manual,
    EXERCISE_Walk,
    EXERCISE_Bicycle,
    EXERCISE_Run,
    // Glucose
    GLUCOSE_CGM,
    GLUCOSE_CGM_RAW,
    GLUCOSE_CGM_ALERT,
    GLUCOSE_CGM_CALIBRATION,
    GLUCOSE_BG,
    GLUCOSE_BG_MANUAL,
    GLUCOSE_BOLUS_CALCULATION,
    // Meal
    MEAL_BolusExpert,
    MEAL_Manual,
    // Pump Events
    PUMP_REWIND,
    PUMP_PRIME,
    PUMP_FILL,
    PUMP_FILL_INTERPRETER,
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
