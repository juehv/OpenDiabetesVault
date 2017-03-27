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
import java.util.logging.Logger;

/**
 *
 * @author juehv
 */
public class SonySWR12Importer extends CsvImporter {
    
    public SonySWR12Importer() {
        super(new SonySWR12Validator(), ',');
    }
    
    @Override
    protected void preprocessingIfNeeded(String filePath) {
        // not needed
    }
    
    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        List<VaultEntry> retVal = new ArrayList<>();
        SonySWR12Validator parseValidator = (SonySWR12Validator) validator;
        
        SonySWR12Validator.TYPE type = parseValidator.getSmartbandType(creader);
        if (type == null) {
            return null;
        }
        
        Date timestamp = parseValidator.getTimestamp(creader);
        if (timestamp == null) {
            return null;
        }
        
        int rawValue = parseValidator.getValue(creader);
        long startTime = parseValidator.getStartTime(creader);
        long endTime = parseValidator.getEndTime(creader);
        double timeSpan = (endTime - startTime) / 60000;
        VaultEntry tmpEntry = null;
        
        switch (type) {
            case SLEEP_LIGHT:
                tmpEntry = new VaultEntry(
                        VaultEntryType.SLEEP_LIGHT,
                        timestamp,
                        timeSpan);
                break;
            case SLEEP_DEEP:
                tmpEntry = new VaultEntry(
                        VaultEntryType.SLEEP_DEEP,
                        timestamp,
                        timeSpan);
                break;
            case HEART_RATE:
                tmpEntry = new VaultEntry(
                        VaultEntryType.HEART_RATE,
                        timestamp,
                        rawValue);
                break;
            case HEART_RATE_VARIABILITY:
                int value1 = (int) ((rawValue >>> 8) & 255);
                int value2 = (int) (255 & rawValue);
                tmpEntry = new VaultEntry(
                        VaultEntryType.HEART_RATE_VARIABILITY,
                        timestamp,
                        value1);
                tmpEntry.setValue2(value2);
                break;
            case RUN:
                tmpEntry = new VaultEntry(
                        VaultEntryType.EXERCISE_TrackerRun,
                        timestamp,
                        timeSpan);
                break;
            case WALK:
                tmpEntry = new VaultEntry(
                        VaultEntryType.EXERCISE_TrackerWalk,
                        timestamp,
                        timeSpan);
                break;
            default:
                Logger.getLogger(this.getClass().getName()).fine("AssertionError");
                throw new AssertionError();
        }
        
        if (tmpEntry != null) {
            retVal.add(tmpEntry);
        }
        return retVal;
    }
    
}
