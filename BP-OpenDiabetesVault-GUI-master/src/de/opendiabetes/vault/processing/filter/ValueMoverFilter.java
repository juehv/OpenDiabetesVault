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

import de.opendiabetes.vault.processing.filter.options.ValueMoverFilterOption;
import de.opendiabetes.vault.processing.filter.options.GapRemoverFilterOption;
import de.opendiabetes.vault.processing.filter.options.NoneTypeFilterOption;
import de.opendiabetes.vault.processing.filter.options.ClusterFilterOption;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.InBetweenFilterOption;
import de.opendiabetes.vault.processing.filter.options.StandardizeFilterOption;
import de.opendiabetes.vault.processing.filter.options.TimeClusterFilterOption;
import de.opendiabetes.vault.util.TimestampUtils;
import de.opendiabetes.vault.util.VaultEntryUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Does nothing.
 *
 * @author juehv
 */
public class ValueMoverFilter extends Filter {

    VaultEntryType vaultEntryType;
    private double value;
    private boolean isAdd;

    public ValueMoverFilter(FilterOption option) {
        super(option);
        if (option instanceof ValueMoverFilterOption) {

            vaultEntryType = ((ValueMoverFilterOption) option).getVaultEntryType();
            value = ((ValueMoverFilterOption) option).getValue();
            isAdd = ((ValueMoverFilterOption) option).getIsAdd();

        } else {
            String msg = "Option has to be an instance of ValueMoverFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.VALUE_MOVER_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return true;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new ValueMoverFilterOption(vaultEntry.getType(), value, isAdd);
        return new ValueMoverFilter(option);
    }

    @Override
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {

        for (VaultEntry vaultEntry : givenResult.filteredData) {
            if(vaultEntry.getType().equals(vaultEntryType))
            if (isAdd) {
                vaultEntry.setValue(vaultEntry.getValue() + value);
            } else {
                vaultEntry.setValue(vaultEntry.getValue() - value);
            }
        }

        return givenResult;
    }

}
