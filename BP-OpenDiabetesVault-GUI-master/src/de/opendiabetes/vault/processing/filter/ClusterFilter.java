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

import de.opendiabetes.vault.processing.filter.options.ClusterFilterOption;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.InBetweenFilterOption;
import de.opendiabetes.vault.processing.filter.options.StandardizeFilterOption;
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
public class ClusterFilter extends Filter {

    VaultEntryType vaultEntryType;
    String vaultEntryClusterString;

    public ClusterFilter(FilterOption option) {
        super(option);
        if (option instanceof ClusterFilterOption) {

            vaultEntryType = ((ClusterFilterOption) option).getVaultEntryType();

            if (vaultEntryType != null) {
                vaultEntryClusterString = vaultEntryType.name().split("_")[0];
            }

        } else {
            String msg = "Option has to be an instance of ClusterFilter";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.CLUSTER_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return true;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new ClusterFilterOption(vaultEntry.getType());
        return new ClusterFilter(option);
    }

    @Override
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {

        List<VaultEntry> listToCluster = new ArrayList<>();

        for (VaultEntry vaultEntry : givenResult.filteredData) {

            if (vaultEntry.getType().equals(vaultEntryType)) {
                listToCluster.add(vaultEntry);
            }
            
            /**
            if (vaultEntry.getType().name().contains(vaultEntryClusterString)) {
                listToCluster.add(vaultEntry);
            }**/
            
            

        }

        double avgValue1 = 0;
        long timeMillis = 0;
        int divider = 1;

        if (listToCluster.size() > 0) {
            divider = listToCluster.size();
        }

        for (VaultEntry vaultEntry : listToCluster) {
            givenResult.filteredData.remove(vaultEntry);
            avgValue1 += vaultEntry.getValue();
            timeMillis += vaultEntry.getTimestamp().getTime();
        }

        VaultEntry vaultEntry = new VaultEntry(vaultEntryType, new Date(timeMillis / divider));
        vaultEntry.setValue(avgValue1 / divider);

        givenResult.filteredData.add(vaultEntry);
        VaultEntryUtils.sort(givenResult.filteredData);

        return givenResult;
    }

}
