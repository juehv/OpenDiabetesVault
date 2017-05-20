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
package de.jhit.opendiabetes.vault.importer.validator;

import com.csvreader.CsvReader;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public abstract class CsvValidator {

    protected static final Logger LOG = Logger.getLogger(CsvValidator.class.getName());

    public static enum Language {
        DE, EN, UNIVERSAL;
    };

    protected Language languageSelection;

    public abstract boolean validateHeader(String[] header);
}
