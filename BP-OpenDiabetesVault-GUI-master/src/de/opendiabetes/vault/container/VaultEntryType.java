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
package de.opendiabetes.vault.container;

/**
 *
 * @author mswin, tiweGH, a.a.aponte
 */
public enum VaultEntryType {

    // Bolus
    BOLUS_NORMAL(VaultEntryTypeGroup.BOLUS, true),
    BOLUS_SQUARE(VaultEntryTypeGroup.BOLUS, true),
    // Basal
    BASAL_PROFILE(VaultEntryTypeGroup.BASAL, false),
    BASAL_MANUAL(VaultEntryTypeGroup.BASAL, false),
    BASAL_INTERPRETER(VaultEntryTypeGroup.BASAL, false),
    // Exercise
    EXERCISE_MANUAL(VaultEntryTypeGroup.EXERCISE, true),
    /**
     * Low demanding exercise.
     */
    EXERCISE_LOW(VaultEntryTypeGroup.EXERCISE, true),
    /**
     * Medium demanding exercise.
     */
    EXERCISE_MID(VaultEntryTypeGroup.EXERCISE, true),
    /**
     * Highly demanding exercise.
     */
    EXERCISE_HIGH(VaultEntryTypeGroup.EXERCISE, true),
    // Glucose
    GLUCOSE_CGM(VaultEntryTypeGroup.GLUCOSE, false),
    GLUCOSE_CGM_RAW(VaultEntryTypeGroup.GLUCOSE, false),
    GLUCOSE_CGM_ALERT(VaultEntryTypeGroup.GLUCOSE, true),
    GLUCOSE_CGM_CALIBRATION(VaultEntryTypeGroup.GLUCOSE, true),
    GLUCOSE_BG(VaultEntryTypeGroup.GLUCOSE, true),
    GLUCOSE_BG_MANUAL(VaultEntryTypeGroup.GLUCOSE, true),
    GLUCOSE_BOLUS_CALCULATION(VaultEntryTypeGroup.GLUCOSE, true),
    GLUCOSE_ELEVATION_30(VaultEntryTypeGroup.GLUCOSE, false),
    // CGM system
    CGM_SENSOR_FINISHED(VaultEntryTypeGroup.CGM_SYSTEM, true),
    CGM_SENSOR_START(VaultEntryTypeGroup.CGM_SYSTEM, true),
    CGM_CONNECTION_ERROR(VaultEntryTypeGroup.CGM_SYSTEM, true),
    CGM_CALIBRATION_ERROR(VaultEntryTypeGroup.CGM_SYSTEM, true),
    CGM_TIME_SYNC(VaultEntryTypeGroup.CGM_SYSTEM, true),
    // Meal
    MEAL_BOLUS_CALCULATOR(VaultEntryTypeGroup.MEAL, true),
    MEAL_MANUAL(VaultEntryTypeGroup.MEAL, true),
    MEAL_DESCRIPTION(VaultEntryTypeGroup.NONE, true),
    // Pump Events
    PUMP_REWIND(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_PRIME(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_FILL(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_FILL_INTERPRETER(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_NO_DELIVERY(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_SUSPEND(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_UNSUSPEND(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_UNTRACKED_ERROR(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_RESERVOIR_EMPTY(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_TIME_SYNC(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_AUTONOMOUS_SUSPEND(VaultEntryTypeGroup.PUMP_SYSTEM, true),
    PUMP_CGM_PREDICTION(VaultEntryTypeGroup.PUMP_SYSTEM, false),
    // Sleep
    SLEEP_LIGHT(VaultEntryTypeGroup.SLEEP, true),
    SLEEP_REM(VaultEntryTypeGroup.SLEEP, true),
    SLEEP_DEEP(VaultEntryTypeGroup.SLEEP, true),
    // Heart
    HEART_RATE(VaultEntryTypeGroup.HEART, false),
    HEART_RATE_VARIABILITY(VaultEntryTypeGroup.HEART, false),
    BLOOD_PRESSURE(VaultEntryTypeGroup.NONE, true),
    STRESS(VaultEntryTypeGroup.HEART, false),
    WEIGHT(VaultEntryTypeGroup.NONE, true),
    KETONES_BLOOD(VaultEntryTypeGroup.NONE, true),
    KETONES_URINE(VaultEntryTypeGroup.NONE, true),
    KETONES_MANUAL(VaultEntryTypeGroup.NONE, true),
    // Location (VaultEntryTypeGroup.Geocoding)
    LOC_TRANSITION(VaultEntryTypeGroup.LOCATION, true),
    LOC_HOME(VaultEntryTypeGroup.LOCATION, true),
    LOC_WORK(VaultEntryTypeGroup.LOCATION, true),
    LOC_FOOD(VaultEntryTypeGroup.LOCATION, true),
    LOC_SPORTS(VaultEntryTypeGroup.LOCATION, true),
    LOC_OTHER(VaultEntryTypeGroup.LOCATION, true),
    // Machine Learning
    ML_CGM_PREDICTION(VaultEntryTypeGroup.MACHINE_LEARNING, false),
    // Date Mining
    DM_INSULIN_SENSITIVITY(VaultEntryTypeGroup.DATA_MINING, false),
    // More unspecific input
    OTHER_ANNOTATION(VaultEntryTypeGroup.NONE, true),
    Tag(VaultEntryTypeGroup.NONE, true);

    //current handling of MAYBE being true
    private final static boolean MAYBE = true;

    private final VaultEntryTypeGroup typeGroup;

    private final boolean isEvent;

    VaultEntryType(VaultEntryTypeGroup group, boolean isEvent) {
        this.isEvent = isEvent;
        this.typeGroup = group;
    }

    /**
     * Returns the Group of the VaultEntryType
     *
     * @return the Group of the VaultEntryType
     */
    public VaultEntryTypeGroup getGroup() {
        return typeGroup;
    }

    public boolean isEvent() {
        return isEvent;
    }

}
