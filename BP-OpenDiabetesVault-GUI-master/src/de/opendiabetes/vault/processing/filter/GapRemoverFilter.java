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
public class GapRemoverFilter extends Filter {

    VaultEntryType vaultEntryType;
    private long gapTimeInMinutes;

    public GapRemoverFilter(FilterOption option) {
        super(option);
        if (option instanceof GapRemoverFilterOption) {

            vaultEntryType = ((GapRemoverFilterOption) option).getVaultEntryType();
            gapTimeInMinutes = ((GapRemoverFilterOption) option).getClusterTimeInMinutes();

        } else {
            String msg = "Option has to be an instance of GapRemoverFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.GAP_REMOVER_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return true;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new GapRemoverFilterOption(vaultEntry.getType(), gapTimeInMinutes);
        return new GapRemoverFilter(option);
    }

    @Override
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {

        FilterResult result = new FilterResult(removeGap(givenResult.filteredData), givenResult.timeSeries);

        return result;
    }

    public List<VaultEntry> removeGap(List<VaultEntry> data) {
        List<VaultEntry> result = new ArrayList<>();
        List<VaultEntry> currentSubList = new ArrayList<>();
        List<VaultEntry> tmpEntriesWithSameDateAsCurrentFoundEndDate = new ArrayList<>();
        Date startTime = null;
        Date endDate = null;
        Date currentFoundEndDate = null;
        VaultEntry subListEntry = null;

        if (data != null && vaultEntryType != null && gapTimeInMinutes > 0) {

            int dataSize = data.size();
            //          double tenthCounter = 1.0;
//            int nextTenth = (int) Math.round(((double) dataSize / 10.0));
            int index = 0; //we use an additional index because of performance issues vs "indexOf(...)"

            System.out.println("Removing Gaps");
            for (VaultEntry vaultEntry : data) {

                //this is used so that all entries with the same timestamp as the found gapType-entry are handled, not only the entry itself
                if (currentFoundEndDate != null && vaultEntry.getTimestamp().after(currentFoundEndDate)) {
                    if (TimestampUtils.withinDateTimeSpan(startTime, endDate, currentFoundEndDate)) {
                        result.addAll(currentSubList);
                        currentSubList = new ArrayList<>();
                        //currentSubList.add(vaultEntry);
                    } else {
                        //if the last subList is removed (gap), entries with the same timestamp as the found
                        //gapType-entry shouldn't get lost, since they now belong to the start of the next series
                        tmpEntriesWithSameDateAsCurrentFoundEndDate = new ArrayList<>();
                        for (int subListIndex = currentSubList.size() - 1; subListIndex >= 0; subListIndex--) {
                            subListEntry = currentSubList.get(subListIndex);
                            if (subListEntry.getTimestamp().equals(currentFoundEndDate)) {
                                //to keep the same order as given
                                tmpEntriesWithSameDateAsCurrentFoundEndDate.add(0, subListEntry);
                            } else {
                                break;
                            }
                        }
                        //System.out.println("Removed from " + startTime + " to " + endDate + " with " + (currentSubList.size() - tmpEntriesWithSameDateAsCurrentFoundEndDate.size()));
                        currentSubList = tmpEntriesWithSameDateAsCurrentFoundEndDate;
                    }
                    //currentSubList.add(vaultEntry); //is handled in the other if cases
                    //tempList = new ArrayList<>();
                    startTime = currentFoundEndDate;
                    endDate = TimestampUtils.addMinutesToTimestamp(startTime, gapTimeInMinutes);//new Date(startTime.getTime() + gapTimeInMinutes);
                    currentFoundEndDate = null;
                }

                if (vaultEntry.getType() == vaultEntryType) {
                    if (startTime == null) {
                        startTime = vaultEntry.getTimestamp();
                        endDate = TimestampUtils.addMinutesToTimestamp(startTime, gapTimeInMinutes);//new Date(startTime.getTime() + gapTimeInMinutes);
                    } else {
                        currentFoundEndDate = vaultEntry.getTimestamp();
                    }
                    currentSubList.add(vaultEntry);

                } else if (startTime == null && vaultEntry.getType() != vaultEntryType) {
                    result.add(vaultEntry);
                } else {
                    currentSubList.add(vaultEntry);
                }
                //add last temp List if in time span
                if (index == dataSize - 1 && startTime != null) {
                    if (TimestampUtils.withinDateTimeSpan(startTime, endDate, vaultEntry.getTimestamp())) {
                        result.addAll(currentSubList);
                    }
//                    else {
//                        System.out.println("Removed LAST from " + startTime + " to " + endDate + " with " + currentSubList.size());
//                    }
                }

//                if (index == nextTenth) {
//                    System.out.println("Processed " + (int) (((double) index / (double) dataSize) * 100) + "% at " + (new Date(System.currentTimeMillis())));
//                    //System.out.println("Processed " + index + " of " + dataSize + " at " + (new Date(System.currentTimeMillis())));
//                    tenthCounter++;
//                    nextTenth = (int) Math.round(tenthCounter * ((double) dataSize / 10.0));
//                }
                index++;
            }
            System.out.println("Removed " + (dataSize - result.size()) + " entries");
            System.out.println("");
        } else {
            result = data;
        }
        return result;
    }

}
