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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class DateTimePointFilterOption extends FilterOption {

    private Date dateTimePoint;
    private long marginBeforeInMinutes;
    private long marginAfterInMinutes;

    public DateTimePointFilterOption(Date dateTimePoint, int marginInMinutes) {
        super(new HashMap<>(), null);
        super.getParameterNameAndType().put("DateTimePoint", Date.class);
        super.getParameterNameAndType().put("MarginInMinutes", int.class);
        
        this.dateTimePoint = dateTimePoint;
        this.marginBeforeInMinutes = marginInMinutes;
        this.marginAfterInMinutes = marginInMinutes;
    }

    /**
     * Initialize fields and calculates:
     * <p>
     * startTime: date - marginBeforeInMinutes<p>
     * endTime: date + marginAfterInMinutes
     *
     * @param dateTimePoint
     * @param marginBeforeInMinutes
     * @param marginAfterInMinutes
     */
    public DateTimePointFilterOption(Date dateTimePoint, int marginBeforeInMinutes, int marginAfterInMinutes) {
        super(new HashMap<>(), null);
        super.getParameterNameAndType().put("DateTimePoint", Date.class);
        super.getParameterNameAndType().put("MarginBeforeInMinutes", int.class);
        super.getParameterNameAndType().put("MarginAfterInMinutes", int.class);
        
        this.dateTimePoint = dateTimePoint;
        this.marginBeforeInMinutes = marginBeforeInMinutes;
        this.marginAfterInMinutes = marginAfterInMinutes;
    }

    public long getMarginBeforeInMinutes() {
        return marginBeforeInMinutes;
    }

    public long getAfterBeforeInMinutes() {
        return marginAfterInMinutes;
    }

    public Date getDateTimePoint() {
        return dateTimePoint;
    }

}
