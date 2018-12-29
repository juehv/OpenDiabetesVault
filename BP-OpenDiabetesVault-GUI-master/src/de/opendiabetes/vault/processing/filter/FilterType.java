/*
 * Copyright (C) 2017 mswin
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

/**
 *
 * @author mswin
 */
public enum FilterType {
    // basic time
    TIME_SPAN,
    TIME_POINT,
    DATE_TIME_POINT,
    DATE_TIME_SPAN,
    CLUSTER,
    // available data
    TYPE,
    GROUP,
    // data absence
    TYPE_ABSENCE,
    // threshould
    THRESHOLD,
    // filter nothing
    NONE,
    MARKER,
    // filter using other filters
    AND,
    OR,
    COMBINATION_FILTER,
    COUNTER,
    LOGIC,
    POSITION,
    QUERY, VAULT_ENTRY_TYPE_FILTER, IN_BETWEEN_FILTER, COMPACT_QUERY_FILTER, GAP_REMOVER_FILTER, CLUSTER_FILTER, FILTER_HIT_TYPE_COUNTER_FILTER, NONE_TYPE_FILTER, VAULT_ENTRY_TYPE_COUNTER_FILTER, INTERPOLATION_FILTER, VALUE_MOVER_FILTER;

}
