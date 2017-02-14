/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

import de.jhit.opendiabetes.vault.importer.FileImporter;
import de.jhit.opendiabetes.vault.util.SortVaultEntryByDate;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.data.VaultDao;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mswin
 */
public class SimplePumpInterpreter extends VaultInterpreter {
   private final SimplePumpInterpreterOptions myOptions;

    public SimplePumpInterpreter(FileImporter importer,
            SimplePumpInterpreterOptions options, VaultDao db) {
        super(importer, options, db);
        myOptions = options;
    }

    @Override
    protected List<VaultEntry> interpret(List<VaultEntry> result) {
        List<VaultEntry> data = fillCanulaInterpretation(result);
        
        return data;
    }
    
    private List<VaultEntry> fillCanulaInterpretation(List<VaultEntry> result){
        VaultEntry rewindHandle = null;
        VaultEntry primeHandle = null;
        VaultEntry latesFillCanulaHandle = null;
        int cooldown = 0;
        
        // configure options        
        if (myOptions.FillCanulaAsNewKatheder){
            // ignore cooldown if option is disabled
            cooldown = myOptions.FillCanulaCooldown;
        }
        
        // sort by date
        Collections.sort(result, new SortVaultEntryByDate());
        
        // go through timeline
        for (VaultEntry item : result){
            
            // reset handles if cooldown is over
            if (rewindHandle != null && primeHandle != null &&
                    ((item.getTimestamp().getTime() - rewindHandle.getTimestamp().getTime()) 
                        > cooldown)){
                
                    Date fillDate;
                    if (latesFillCanulaHandle != null){
                        fillDate = latesFillCanulaHandle.getTimestamp();
                    } else {
                        fillDate = primeHandle.getTimestamp();                                
                    } 
                    result.add(new VaultEntry(VaultEntryType.PUMP_FILL, 
                            fillDate, 
                            VaultEntry.VALUE_UNUSED));
                }
            
            // find new pairs
            if (item.getType() == VaultEntryType.PUMP_REWIND){
                // filling line starts with rewind
                rewindHandle = item;
            } else if (item.getType() == VaultEntryType.PUMP_PRIME){
                // is pump rewinded?
                if (rewindHandle != null){
                    // is pump already primed?
                    if (primeHandle != null){
                        // no --> this is the prime
                        primeHandle = item;
                    } else {
                        // yes --> must be fill canula
                        latesFillCanulaHandle = item;
                    }
                } else if (myOptions.FillCanulaAsNewKatheder){
                    // no prime event? --> new katheder (if enabled)
                    result.add(new VaultEntry(VaultEntryType.PUMP_FILL, 
                            item.getTimestamp(), 
                            VaultEntry.VALUE_UNUSED));                    
                }
            }
        }
        
        // sort by date again <-- not neccesary because database will do it
        //Collections.sort(result, new SortVaultEntryByDate());
        return result;
    }

}
