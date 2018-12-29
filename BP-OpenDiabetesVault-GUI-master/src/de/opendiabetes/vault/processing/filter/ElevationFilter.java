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
import de.opendiabetes.vault.processing.filter.options.ElevationFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Does nothing.
 *
 * @author juehv
 */
public class ElevationFilter extends Filter {

    VaultEntryType vaultEntryType;
    double minElevationPerMinute;
    int minutesBetweenEntries;
    double elevation;
    List<VaultEntry> positiveVaultEntrys = new ArrayList<>();

    public ElevationFilter(FilterOption option) {

        super(option);
        if (option instanceof ElevationFilterOption) {

            vaultEntryType = ((ElevationFilterOption) option).getVaultEntryType();
            minElevationPerMinute = ((ElevationFilterOption) option).getMinElevation();
            minutesBetweenEntries = ((ElevationFilterOption) option).getMinutesBetweenEntries();

            elevation = minElevationPerMinute * minutesBetweenEntries;

        } else {
            String msg = "Option has to be an instance of ElevationFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.COMPACT_QUERY_FILTER;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new ElevationFilterOption(vaultEntry.getType(), minElevationPerMinute, minutesBetweenEntries);
        return new CompactQueryFilter(option);
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        if (vaultEntryType == entry.getType()) {
            positiveVaultEntrys.add(entry);
        }

        return true;
    }

    @Override
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {
        boolean elevationExist = false;

        if (positiveVaultEntrys != null && positiveVaultEntrys.size() > 0) {
            Calendar cal = Calendar.getInstance();

            for (VaultEntry vaultEntry : positiveVaultEntrys) {
                cal.setTime(vaultEntry.getTimestamp());
                cal.add(Calendar.MINUTE, minutesBetweenEntries);

                for (VaultEntry vaultEntry1 : positiveVaultEntrys) {

                    if (vaultEntry1.getTimestamp().after(cal.getTime())) {

                        double tempElevation = (vaultEntry1.getValue() - vaultEntry.getValue()) / (minutesBetweenEntries - 0);

                        if (elevation <= tempElevation) {
                            elevationExist = true;
                        }
                        break;
                    }
                }
                if (elevationExist) {
                    break;
                }
            }
        }
        if (!elevationExist) {
            givenResult = new FilterResult();
        }

        return givenResult;
    }

}
