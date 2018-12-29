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
import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import java.time.LocalTime;
import java.util.HashMap;

/**
 *
 * @author tiweGH
 */
public class TimePointFilterOption extends FilterOption {

    private LocalTime timePoint;
    private final int marginBeforeInMinutes;
    private final int marginAfterInMinutes;

    public TimePointFilterOption(LocalTime timePoint, int marginInMinutes) {
        super(new HashMap<>(), null);
        super.getParameterNameAndType().put("LocalTime", LocalTime.class);
        super.getParameterNameAndType().put("MarginInMinutes", int.class);        
        
        this.timePoint = timePoint;
        this.marginAfterInMinutes = marginInMinutes;
        this.marginBeforeInMinutes = marginInMinutes;
    }

    /**
     * Initialize fields and calculates:
     * <p>
     * startTime: timepoint- marginBeforeInMinutes<p>
     * endTime: timepoint+ marginAfterInMinutes
     *
     * @param timePoint
     * @param marginBeforeInMinutes
     * @param marginAfterInMinutes
     */
    public TimePointFilterOption(LocalTime timePoint, int marginBeforeInMinutes, int marginAfterInMinutes) {
        super(new HashMap<>(), null);
        super.getParameterNameAndType().put("LocalTime", LocalTime.class);
        super.getParameterNameAndType().put("MarginBeforeInMinutes", int.class);
        super.getParameterNameAndType().put("MarginAfterInMinutes", int.class);
        
        
        this.timePoint = timePoint;
        this.marginAfterInMinutes = marginAfterInMinutes;
        this.marginBeforeInMinutes = marginBeforeInMinutes;
    }

    public LocalTime getTimePoint() {
        return timePoint;
    }

    public int getmarginAfterInMinutes() {
        return marginAfterInMinutes;
    }

    public int getMarginBeforeInMinutes() {
        return marginBeforeInMinutes;
    }

}
