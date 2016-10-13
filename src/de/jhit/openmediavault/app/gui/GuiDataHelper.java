/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.gui;

import de.jhit.openmediavault.app.Launcher;
import de.jhit.openmediavault.app.container.DataEntry;
import de.jhit.openmediavault.app.preferences.Constants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.Preferences;

/**
 *
 * @author mswin
 */
public class GuiDataHelper {

    private static boolean contains(List<DataEntry> entryList, DataEntry entry) {
        for (DataEntry tmp : entryList) {
            if (tmp.isEquivalent(entry)) {
                return true;
            }
        }
        return false;
    }

    public static String[] createRefillList(List<DataEntry> data) {
        List<String> listEntrys = new ArrayList<>();
        List<DataEntry> primeList = new ArrayList<>();

        for (DataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])) {
                primeList.add(item);
            }
        }

        for (DataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])) {
                if (!contains(primeList, item)) {
                    primeList.add(item);
                }
            }
        }

        for (DataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[2])) {
                if (!contains(primeList, item)) {
                    primeList.add(item);
                }
            }
        }
        primeList.sort(DataEntry.getTimeSortComparator());

        for (DataEntry item : primeList) {
            listEntrys.add(item.toRefillEntry());
        }

        return listEntrys.toArray(new String[]{});
    }

    private static List<DataEntry> createCleanBgList(List<DataEntry> data) {
        List<DataEntry> bgList = new ArrayList<>();

        for (DataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])) {
                bgList.add(item);
            }
        }

        for (DataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])) {
                if (!contains(bgList, item)) {
                    bgList.add(item);
                }
            }
        }
        bgList.sort(DataEntry.getTimeSortComparator());
        return bgList;
    }

    public static String[] createHypoList(List<DataEntry> data) {
        List<String> listEntrys = new ArrayList<>();
        List<DataEntry> bgList = createCleanBgList(data);

        double border = Preferences.userNodeForPackage(Launcher.class).
                getDouble(Constants.HYPO_BORDER_KEY, Constants.HYPO_BORDER_DEFAULT);
        for (DataEntry item : bgList) {
            if (item.amount < border && item.amount > 0) {
                listEntrys.add(item.toHypoEntry());
            }
        }

        return listEntrys.toArray(new String[]{});
    }

    public static String[] createHyperList(List<DataEntry> data) {
        List<String> listEntrys = new ArrayList<>();
        List<DataEntry> bgList = createCleanBgList(data);

        double border = Preferences.userNodeForPackage(Launcher.class).
                getDouble(Constants.HYPER_BORDER_KEY, Constants.HYPER_BORDER_DEFAULT);
        for (DataEntry item : bgList) {
            if (item.amount > border && item.amount > 0) {
                listEntrys.add(item.toHypoEntry());
            }
        }

        return listEntrys.toArray(new String[]{});
    }
}
