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

import java.util.Map;

/**
 *
 * @author tiweGH
 */
public abstract class FilterOption {

    private Map<String, Class> parameterNameAndType;
    
    private Map<String, String> dropDownEntriesAndValue;

    public FilterOption(Map<String, Class> parameterNameAndClass, Map<String, String> dropDownEntriesAndValue) {
        this.parameterNameAndType = parameterNameAndClass;
        this.dropDownEntriesAndValue = dropDownEntriesAndValue;
    }

    public Map<String, String> getDropDownEntries() {
        return dropDownEntriesAndValue;
    }

    public void setDropDownEntries(Map<String, String> dropDownEntriesAndValue) {
        this.dropDownEntriesAndValue = dropDownEntriesAndValue;
    }
    
    public Map<String, Class> getParameterNameAndType() {
        return parameterNameAndType;
    }

    public void setParameterNameAndType(Map<String, Class> parameterNameAndClass) {
        this.parameterNameAndType = parameterNameAndClass;
    }

}
