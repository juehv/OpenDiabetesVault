/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
