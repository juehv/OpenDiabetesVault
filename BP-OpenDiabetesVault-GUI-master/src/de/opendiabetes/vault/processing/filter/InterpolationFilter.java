/*
 * Copyright (C) 2017 Jorg
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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.processing.filter.options.InterpolationFilterOption;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import de.opendiabetes.vault.util.SplineInterpolator;
import de.opendiabetes.vault.util.VaultEntryUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author Daniel This class extends Filter. The filter() Method checks if the
 * vaultentrytype of the vaultEntry equals the given vaultentry.
 */
public class InterpolationFilter extends Filter {

    private VaultEntryType vaultEntryType;
    List<Pair<Long, Double>> pairsForInterpolation = new ArrayList<>();
    int entriesBetweenTimeStamps = 0;
    SplineInterpolator splineInterpolator;

    /**
     * Sets the vaultEntryType which will later be used in the filter mechanism.
     *
     * @param option VaultEntryTypeFilterOption
     */
    public InterpolationFilter(FilterOption option) {
        super(option);
        if (option instanceof InterpolationFilterOption) {
            this.vaultEntryType = ((InterpolationFilterOption) option).getVaultEntryType();
            this.entriesBetweenTimeStamps = ((InterpolationFilterOption) option).getEntriesBetweenTimeStamps();
        } else {
            String msg = "Option has to be an instance of InterpolationFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }

    }

    @Override
    FilterType getType() {
        return FilterType.INTERPOLATION_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {

        double counter = 0;
        if (entry.getType().equals(vaultEntryType)) {
            Pair<Long, Double> pair = new Pair<>(entry.getTimestamp().getTime(), entry.getValue());
            pairsForInterpolation.add(pair);
            counter++;
        }

        return true;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new InterpolationFilter(new InterpolationFilterOption(vaultEntry.getType(), entriesBetweenTimeStamps));
    }

    @Override
    FilterResult tearDownAfterFilter(FilterResult givenResult) {
        splineInterpolator = new SplineInterpolator(pairsForInterpolation);

        List<VaultEntry> vaultEntrys = new ArrayList<>();

        for (int i = 0; i < pairsForInterpolation.size() - 1; i++) {
            long timeStepBetweenEntries = (pairsForInterpolation.get(i + 1).getKey() - pairsForInterpolation.get(i).getKey()) / entriesBetweenTimeStamps;

            for (int j = 0; j < entriesBetweenTimeStamps; j++) {
                vaultEntrys.add(new VaultEntry(vaultEntryType, new Date(Long.parseLong((pairsForInterpolation.get(i).getKey() + (j * timeStepBetweenEntries)) + "")), splineInterpolator.interpolate(pairsForInterpolation.get(i).getKey() + (j * timeStepBetweenEntries))));
            }
        }
        /**
         * for (VaultEntry vaultEntry : givenResult.filteredData) { if
         * (vaultEntry.getType().equals(vaultEntryType)) {
         * vaultEntry.setValue(splineInterpolator.interpolate(vaultEntry.getValue()));
         * } }*
         */

        vaultEntrys.addAll(givenResult.filteredData);

        return new FilterResult(VaultEntryUtils.sort(vaultEntrys), givenResult.timeSeries);
    }
}
