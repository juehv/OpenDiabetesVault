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
package de.opendiabetes.vault.processing.preprocessing;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.GapRemoverFilter;
import de.opendiabetes.vault.processing.filter.options.GapRemoverFilterOption;
import de.opendiabetes.vault.processing.preprocessing.options.GapRemoverPreprocessorOption;
import de.opendiabetes.vault.processing.preprocessing.options.PreprocessorOption;
import de.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiweGH
 */
public class GapRemover extends Preprocessor {

    private final long gapTimeInMinutes;
    private final VaultEntryType gapType;

    /**
     * Get's a dataset of VaultEntries and removes gaps between entries of the
     * given types, which are bigger than the given time in minutes
     *
     * @param preprocessorOption specific options for gap search
     */
    public GapRemover(PreprocessorOption preprocessorOption) {
        super(preprocessorOption);
        if (preprocessorOption instanceof GapRemoverPreprocessorOption) {

            this.gapTimeInMinutes = ((GapRemoverPreprocessorOption) preprocessorOption).getGapTimeInMinutes();
            this.gapType = ((GapRemoverPreprocessorOption) preprocessorOption).getGapType();
        } else {
            String msg = "Option has to be an instance of GapRemoverPreprocessorOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    public List<VaultEntry> preprocess(List<VaultEntry> data) {
        GapRemoverFilter gapRemoverFilter = new GapRemoverFilter(new GapRemoverFilterOption(gapType, gapTimeInMinutes));
        return gapRemoverFilter.removeGap(data);
    }

}
