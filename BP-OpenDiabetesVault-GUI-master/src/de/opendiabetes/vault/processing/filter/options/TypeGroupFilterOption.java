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

import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import static de.opendiabetes.vault.processing.filter.options.ThresholdFilterOption.BANDPASS;
import static de.opendiabetes.vault.processing.filter.options.ThresholdFilterOption.OVER;
import static de.opendiabetes.vault.processing.filter.options.ThresholdFilterOption.UNDER;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tiweGH
 */
public class TypeGroupFilterOption extends FilterOption {

    private final VaultEntryTypeGroup vaultEntryTypeGroup;

    /**
     * Filters entries with a type in the matching type-group
     *
     * @param vaultEntryTypeGroup
     */
    public TypeGroupFilterOption(VaultEntryTypeGroup vaultEntryTypeGroup) {
        super(new HashMap<>(), null);
        super.setDropDownEntries(this.getDropDownEntries());

        super.getParameterNameAndType().put("VaultEntryTypeGroup", Map.class);

        this.vaultEntryTypeGroup = vaultEntryTypeGroup;

    }

    public VaultEntryTypeGroup getVaultEntryTypeGroup() {
        return vaultEntryTypeGroup;
    }

    @Override
    public Map<String, String> getDropDownEntries() {
        Map<String, String> result = new HashMap<>();

        for (VaultEntryTypeGroup value : VaultEntryTypeGroup.values()) {
            result.put(value.name(), value.name());
        }

        return result;
    }

}
