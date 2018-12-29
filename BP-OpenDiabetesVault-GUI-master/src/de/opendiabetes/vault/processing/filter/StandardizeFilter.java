/*
 * Copyright (C) 2017 juehv
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

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.InBetweenFilterOption;
import de.opendiabetes.vault.processing.filter.options.StandardizeFilterOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Does nothing.
 *
 * @author juehv
 */
public class StandardizeFilter extends Filter {

    double minValue;
    double maxValue;
    boolean initialized = false;
    boolean betweenZeroAndOne;
    VaultEntryType vaultEntryType;

    public StandardizeFilter(FilterOption option) {
        super(option);
        if (option instanceof StandardizeFilterOption) {

            vaultEntryType = ((StandardizeFilterOption) option).getVaultEntryType();
            betweenZeroAndOne = ((StandardizeFilterOption) option).getBetweenZeroAndOne();

        } else {
            String msg = "Option has to be an instance of StandardizeFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.IN_BETWEEN_FILTER;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new StandardizeFilterOption(vaultEntry.getType(), betweenZeroAndOne);
        return new StandardizeFilter(option);
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = true;

        if (entry.getType().equals(vaultEntryType)) {

            if (!initialized) {
                minValue = entry.getValue();
                maxValue = entry.getValue();
                initialized = true;
            } else if (entry.getValue() < minValue) {
                minValue = entry.getValue();
            } else if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
            }

        }

        return result;
    }

    @Override
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {

        for (VaultEntry vaultEntry : givenResult.filteredData) {
            double newValue = 0;
            if (vaultEntry.getType().equals(vaultEntryType)) {
                if (betweenZeroAndOne) {
                    newValue = (vaultEntry.getValue() - minValue) / (maxValue - minValue);
                } else {
                    newValue = 2 * ((vaultEntry.getValue() - minValue) / (maxValue - minValue)) - 1;
                }
                vaultEntry.setValue(newValue);
            }
        }

        return givenResult;
    }

}
