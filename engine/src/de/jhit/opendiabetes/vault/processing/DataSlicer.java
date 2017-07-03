/*
 * Copyright (C) 2017 Jens Heuschkel
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
package de.jhit.opendiabetes.vault.processing;

import de.jhit.opendiabetes.vault.container.SliceEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juehv
 */
public class DataSlicer {

    private final List<SimpleFilter> registeredFilter = new ArrayList<>();
    private final DataSlicerOptions options;

    public DataSlicer(DataSlicerOptions options) {
        this.options = options;
    }

    /**
     * Slices dataset with respect to registered filters.
     *
     * @param data the data set which should be filtered
     * @return a list of slice entrys matching the registered filters or an
     * empty list if no filter matches
     */
    public List<SliceEntry> sliceData(List<VaultEntry> data) {
        List<SliceEntry> retVal = new ArrayList<>();

        // TODO this implementation is too simple and will not work
        // since every entry contains just one value type ...
        // for cross referencing filter (e.g. high bg value at night)
        // we have to check several entries (e.g. bg value entries + sleep entries)
        //
        for (VaultEntry entry : data) {
            boolean violate = false;
            for (SimpleFilter filter : registeredFilter) {
                if (!filter.matches(entry)) {
                    violate = true;
                    break;
                }
            }
            if (!violate) {
                retVal.add(new SliceEntry(
                        TimestampUtils.addMinutesToTimestamp(
                                entry.getTimestamp(),
                                -1 * options.margin),
                        options.duration));
            }
        }

        return retVal;
    }

    /**
     * Registeres a filter for slicing. Should be called before slicing.
     * Registered filteres are always combined as logical AND.
     *
     * @param filter
     */
    public void registerFilter(SimpleFilter filter) {
        registeredFilter.add(filter);
    }

}
