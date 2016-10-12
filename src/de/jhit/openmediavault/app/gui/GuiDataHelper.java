/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.gui;

import de.jhit.openmediavault.app.container.DataEntry;
import de.jhit.openmediavault.app.preferences.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mswin
 */
public class GuiDataHelper {
    public static String[] createRefillList(List<DataEntry> data){
        List<String> listEntrys = new ArrayList<>();
        
        for (DataEntry item : data){
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])||
                    item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])){
                listEntrys.add(item.toRefillEntry());
            }
        }
        
        return listEntrys.toArray(new String[]{});
    }
}
