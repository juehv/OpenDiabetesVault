/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.opendiabetes.vault.importer.FileImporter;
import de.jhit.opendiabetes.vault.util.SortVaultEntryByDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jens
 */
public class FitnessTrackerInterpreter extends VaultInterpreter {

    private final FitnessTrackerInterpreterOptions myOptions;

    public FitnessTrackerInterpreter(FileImporter importer,
            FitnessTrackerInterpreterOptions options, VaultDao db) {
        super(importer, options, db);
        myOptions = options;
    }

    @Override
    protected List<VaultEntry> interpret(List<VaultEntry> result) {
        List<VaultEntry> data = result;

        // sort by date
        Collections.sort(data, new SortVaultEntryByDate());

        LOG.finer("Start activity data interpretation");
        data = filterActititys(data);

        LOG.finer("Tracker data interpretation finished");

        return data;
    }

    private List<VaultEntry> filterActititys(List<VaultEntry> data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        List<VaultEntry> retVal = new ArrayList<>();
        VaultEntry lastExerciseItem = null;

        for (VaultEntry item : data) {
            switch (item.getType()) {
                case EXERCISE_TrackerBicycle:
                case EXERCISE_TrackerRun:
                case EXERCISE_TrackerWalk:
                    if (lastExerciseItem == null) {
                        // init item
                        lastExerciseItem = item;
                    } else {
                        if (Math.round(
                                (item.getTimestamp().getTime()
                                - lastExerciseItem.getTimestamp().getTime())
                                / 60000)
                                >= myOptions.activitySliceThreshold) {
                            // within a slice
                            item.setValue(item.getValue() + lastExerciseItem.getValue());
                            lastExerciseItem = item; // <-- update timestamp
                        } else {
                            // new slice
                            if (lastExerciseItem.getValue() > myOptions.activityThreshold) {
                                retVal.add(lastExerciseItem);
                                lastExerciseItem = item;
                            }
                        }
                    }
                    break;
                default:
                    retVal.add(item);
            }
        }

        // add last unsliced item
        if (lastExerciseItem != null
                && lastExerciseItem.getValue() > myOptions.activityThreshold) {
            retVal.add(lastExerciseItem);
        }

        return retVal;
    }

}
