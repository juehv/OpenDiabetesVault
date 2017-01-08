/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.data;

import de.jhit.openmediavault.app.container.BolusEntry;
import de.jhit.openmediavault.app.container.ExerciseEntry;
import de.jhit.openmediavault.app.container.GlucoseEntry;
import de.jhit.openmediavault.app.container.MealEntry;
import de.jhit.openmediavault.app.container.RawDataEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Holds data while application runtime for processing
 *
 * @author mswin
 */
public class RuntimeDataVault {

    private static final Logger LOG = Logger.getLogger(RuntimeDataVault.class.getName());
    private static RuntimeDataVault INSTANCE = null;

    private final List<GlucoseEntry> glucoseValues = new ArrayList<>();
    private final List<BolusEntry> bolusValues = new ArrayList<>();
    private final List<MealEntry> mealValues = new ArrayList<>();
    private final List<ExerciseEntry> exerciseValues = new ArrayList<>();

    private RuntimeDataVault() {
    }

    public static RuntimeDataVault getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RuntimeDataVault();
        }
        return INSTANCE;
    }

    public void putGlucouseValue(GlucoseEntry value) {
        if (value == null) {
            LOG.warning("Got null GlucoseEntry.");
            return;
        }
        glucoseValues.add(value);
    }

    public void putBolusValue(BolusEntry value) {
        if (value == null) {
            LOG.warning("Got null BolusEntry.");
            return;
        }
        bolusValues.add(value);
    }

    public void putMealEntry(MealEntry value) {
        if (value == null) {
            LOG.warning("Got null MealEntry.");
            return;
        }
        mealValues.add(value);
    }

    public void putExerciseEntry(ExerciseEntry value) {
        if (value == null) {
            LOG.warning("Got null ExerciseEntry.");
            return;
        }
        exerciseValues.add(value);
    }

}
