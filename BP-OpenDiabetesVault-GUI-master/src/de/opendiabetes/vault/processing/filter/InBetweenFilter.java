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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Does nothing.
 *
 * @author juehv
 */
public class InBetweenFilter extends Filter {

    int minValue;
    int maxValue;
    VaultEntryType vaultEntryType;
    boolean wrongEntry = false;
    boolean normalize = false;

    public InBetweenFilter(FilterOption option) {
        super(option);
        if (option instanceof InBetweenFilterOption) {

            vaultEntryType = ((InBetweenFilterOption) option).getVaultEntryType();
            maxValue = ((InBetweenFilterOption) option).getMaxValue();
            minValue = ((InBetweenFilterOption) option).getMinValue();
            normalize = ((InBetweenFilterOption) option).getNormalize();

        } else {
            String msg = "Option has to be an instance of InBetweenFilter";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.IN_BETWEEN_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = true;

        if (entry.getType().equals(vaultEntryType)) {
            if (entry.getValue() > maxValue || entry.getValue() < minValue) {
                wrongEntry = true;
            }
        }

        return result;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new InBetweenFilterOption(vaultEntry.getType(), minValue, maxValue, normalize);
        return new InBetweenFilter(option);
    }

    @Override
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {
        if (wrongEntry) {
            givenResult = new FilterResult();
        } else if (normalize) {
            for (VaultEntry vaultEntry : givenResult.filteredData) {
                if (vaultEntry.getType().equals(vaultEntryType)) {
                    double newValue = (vaultEntry.getValue() - minValue) / (maxValue - minValue);
                    vaultEntry.setValue(newValue);
                }
            }
        }

        return givenResult;
    }

}
