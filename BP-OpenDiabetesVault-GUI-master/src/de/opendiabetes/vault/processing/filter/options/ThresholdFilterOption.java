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

import de.opendiabetes.vault.processing.filter.ThresholdFilter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tiweGH
 */
public class ThresholdFilterOption extends FilterOption {

    double minThreshold = 0;
    double maxThreshold = 0;
    private int mode = 0;

    public static final int OVER = ThresholdFilter.OVER;
    public static final int UNDER = ThresholdFilter.UNDER;
    public static final int BANDPASS = ThresholdFilter.BANDPASS;

    public ThresholdFilterOption(double minThreshold, double maxThreshold, int mode) {
        super(new LinkedHashMap<>(), null);
        super.setDropDownEntries(this.getDropDownEntries());

        super.getParameterNameAndType().put("Mode", Map.class);
        super.getParameterNameAndType().put("MinThreshold", int.class);
        super.getParameterNameAndType().put("MaxThreshold", int.class);

        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
        this.mode = mode;

    }

    public ThresholdFilterOption(double thresholdValue, int mode) {
        super(new HashMap<>(), null);
        super.setDropDownEntries(this.getDropDownEntries());

        super.getParameterNameAndType().put("MinThreshold", int.class);
        super.getParameterNameAndType().put("MaxThreshold", int.class);
        super.getParameterNameAndType().put("Mode", Map.class);

        this.mode = mode;

        if (this.mode == OVER) {
            this.minThreshold = thresholdValue;
        } else if (this.mode == UNDER) {
            this.maxThreshold = thresholdValue;
        }
    }

    public double getMinThresholdValue() {
        return minThreshold;
    }

    public double getMaxThresholdValue() {
        return maxThreshold;
    }

    public int getMode() {
        return mode;
    }

    @Override
    public Map<String, String> getDropDownEntries() {
        Map<String, String> result = new HashMap<>();

        result.put("OVER", "" + OVER);
        result.put("UNDER", "" + UNDER);
        result.put("BANDPASS", "" + BANDPASS);

        return result;
    }
}
