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

import de.opendiabetes.vault.processing.filter.options.FilterHitCounterFilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeCounterFilterOption;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.CounterFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class extends filter and checks if the given Filter hits an specific
 * amount, after a result the hitcounter will be reset.
 *
 * @author Daniel
 */
public class FilterHitCounterFilter extends Filter {

    private Filter filter;
    private int maxHits;
    private int minHits;
    private boolean noneHits;
    private int currentHit;

    public FilterHitCounterFilter(FilterOption option) {
        super(option);
        if (option instanceof FilterHitCounterFilterOption) {
            this.filter = ((FilterHitCounterFilterOption) option).getFilter();
            this.maxHits = ((FilterHitCounterFilterOption) option).getMaxHits();
            this.minHits = ((FilterHitCounterFilterOption) option).getMinHits();
            this.noneHits = ((FilterHitCounterFilterOption) option).getNoneHits();

            currentHit = 0;

        } else {
            String msg = "Option has to be an instance of FilterHitCounterFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    FilterType getType() {
        return FilterType.FILTER_HIT_TYPE_COUNTER_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = true;
        if (filter.matchesFilterParameters(entry)) {
            currentHit++;
        }

        return result;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new FilterHitCounterFilter(new FilterHitCounterFilterOption(filter, minHits, maxHits, noneHits));
    }

    @Override
    FilterResult tearDownAfterFilter(FilterResult givenResult) {
        FilterResult result;

        if ((currentHit >= minHits && currentHit <= maxHits) || (noneHits && currentHit == 0)) {
            result = givenResult;
        } else {
            result = new FilterResult();
        }
        currentHit = 0;

        return result;
    }
}
