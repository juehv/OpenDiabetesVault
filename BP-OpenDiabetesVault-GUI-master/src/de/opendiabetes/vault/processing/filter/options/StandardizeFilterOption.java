/*
 * Copyright (C) 2018 tiweGH
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tiweGH
 */
public class StandardizeFilterOption extends FilterOption {

    private final VaultEntryType type;
    private final boolean betweenZeroAndOne;

    /**
     *
     * @param type
     */
    public StandardizeFilterOption(VaultEntryType type, boolean betweenZeroAndOne) {
        super(new HashMap<>(), null);
        super.setDropDownEntries(this.getDropDownEntries());

        super.getParameterNameAndType().put("VaultEntryType", Map.class);
        super.getParameterNameAndType().put("BetweenZeroAndOne", boolean.class);

        this.betweenZeroAndOne = betweenZeroAndOne;
        this.type = type;
    }

    public VaultEntryType getVaultEntryType() {
        return type;
    }

    @Override
    public Map<String, String> getDropDownEntries() {
        Map<String, String> result = new HashMap<>();

        for (VaultEntryType value : VaultEntryType.values()) {
            result.put(value.name(), value.name());
        }

        return result;
    }

    public boolean getBetweenZeroAndOne() {
        return betweenZeroAndOne;
    }

}
