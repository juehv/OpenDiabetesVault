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
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author juehv
 */
public class LifelogStressDBImporter extends CsvImporter {

    public LifelogStressDBImporter() {
        super(new NonValidator(), ',');
    }

    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        List<VaultEntry> retVal = new ArrayList<>();

        Date timestamp = new Date(Long.parseLong(creader.get("timestamp")));

        double stressLevel = Double.parseDouble(creader.get("value"));
        VaultEntry tmpEntry = new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, timestamp, stressLevel);
        retVal.add(tmpEntry);

        return retVal;
    }

    @Override
    protected void preprocessingIfNeeded(String filePath) {
        // not needed
    }

}
