/*
 * Copyright (C) 2018 Nutzer
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
package de.opendiabetes.vault.processing.filter.options;

import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Nutzer
 */
public class FilterHitCounterFilterOption extends FilterOption {

    private final Filter filter;
    private final int maxHits;
    private final int minHits;
    private final boolean noneHits;

    public FilterHitCounterFilterOption(Filter filter, int minHits, int maxHits, boolean noneHits) {
        super(new LinkedHashMap<>(), null);
        super.getParameterNameAndType().put("Filter", Filter.class);
        super.getParameterNameAndType().put("NoneHits", boolean.class);
        super.getParameterNameAndType().put("MinHits", int.class);
        super.getParameterNameAndType().put("MaxHits", int.class);
        

        this.maxHits = maxHits;
        this.minHits = minHits;
        this.noneHits = noneHits;
        this.filter = filter;
    }

    public Filter getFilter() {
        return filter;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public int getMinHits() {
        return minHits;
    }

    public boolean getNoneHits() {
        return noneHits;
    }

}
