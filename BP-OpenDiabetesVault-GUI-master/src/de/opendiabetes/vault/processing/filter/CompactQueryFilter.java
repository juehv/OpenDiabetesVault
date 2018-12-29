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
import de.opendiabetes.vault.processing.filter.options.CompactQueryFilterOption;
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
public class CompactQueryFilter extends Filter {

    List<Filter> filters;
    boolean[] boolForFilters;

    public CompactQueryFilter(FilterOption option) {
        super(option);
        if (option instanceof CompactQueryFilterOption) {

            filters = ((CompactQueryFilterOption) option).getFilters();

            if(filters != null)
                boolForFilters = new boolean[filters.size()];
            else
                boolForFilters = new boolean[0];

            for (int i = 0; i < boolForFilters.length; i++) {
                boolForFilters[i] = false;
            }

        } else {
            String msg = "Option has to be an instance of CompactQueryFilter";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.COMPACT_QUERY_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = true;

        int counter = 0;
        for (Filter filter : filters) {
            if (filter.matchesFilterParameters(entry)) {
                boolForFilters[counter] = true;
            }
            counter++;
        }

        return result;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new CompactQueryFilterOption(filters);
        return new CompactQueryFilter(option);
    }

    @Override
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {
        boolean allTrue = true;

        for (boolean boolForFilter : boolForFilters) {
            if (boolForFilter == false) {
                allTrue = false;
            }
        }

        if (!allTrue) {
            givenResult = new FilterResult();
        }

        return givenResult;
    }

}
