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
package de.jhit.opendiabetes.vault.container;

import java.util.regex.Pattern;

/**
 *
 * @author juehv
 */
public enum VaultEntryAnnotation {
    GLUCOSE_RISE_LAST,
    GLUCOSE_RISE_20_MIN,
    GLUCOSE_BG_METER_SERIAL;

    private final Pattern valuePattern;
    private String value = "";

    private VaultEntryAnnotation() {
        valuePattern = Pattern.compile(".*" + this.toString() + "(=([\\w\\.]+))?.");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // TODO reimplement with pattern matching
//    /**
//     *
//     * @param annotationString representing VaultEntryAnnotation as string
//     *
//     * @return VaultEntryAnnotation represented by the string or null if no (or
//     * more than one) VaultEntryAnnotation found
//     */
//    public static VaultEntryAnnotation fromString(String annotationString) {
//        VaultEntryAnnotation returnValue = null;
//        for (VaultEntryAnnotation item : VaultEntryAnnotation.values()) {
//            if (annotationString.toUpperCase().contains(item.toString().toUpperCase())) {
//                if (returnValue != null) {
//                    returnValue = item;
//                } else {
//                    // found more than one --> error
//                    return null;
//                }
//            }
//        }
//        return returnValue;
//    }
    public String toStringWithValue() {
        return "";//value.isEmpty() ? this.toString() : this.toString() + "=" + value;
    }

}
