/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.util;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.util.Comparator;

/**
 *
 * @author Jens
 */
public class SortVaultEntryByDate implements Comparator<VaultEntry> {

    @Override
    public int compare(VaultEntry o1, VaultEntry o2) {
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }

    
}
