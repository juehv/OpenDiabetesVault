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
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nutzer
 */
public class ClusterFilterOption extends FilterOption {

    VaultEntryType vaultEntryType;

    public ClusterFilterOption(VaultEntryType type) {
        super(new HashMap<>(), null);
        super.setDropDownEntries(this.getDropDownEntries());

        super.getParameterNameAndType().put("VaultEntryType", Map.class);

        this.vaultEntryType = type;
    }

    @Override
    public Map<String, String> getDropDownEntries() {
        Map<String, String> result = new HashMap<>();

        for (VaultEntryType value : VaultEntryType.values()) {
            result.put(value.name(), value.name());
        }

        return result;
    }

    public VaultEntryType getVaultEntryType() {
        return vaultEntryType;
    }

}
