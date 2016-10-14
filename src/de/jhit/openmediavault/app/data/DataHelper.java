/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.data;

import de.jhit.openmediavault.app.container.DataEntry;
import de.jhit.openmediavault.app.preferences.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mswin
 */
public class DataHelper {

    public static boolean dataEntryListContains(List<DataEntry> entryList, DataEntry entry) {
        for (DataEntry tmp : entryList) {
            if (tmp.isEquivalent(entry)) {
                return true;
            }
        }
        return false;
    }

    public static int minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }
        return Math.abs((int) ((laterDate.getTime() / 60000) - (earlierDate.getTime() / 60000)));
    }

    // ###################################################################
    // List computations
    // ###################################################################
    public static List<DataEntry> createCleanBgList(List<DataEntry> data) {
        List<DataEntry> bgList = new ArrayList<>();

        for (DataEntry item : data) {
            // stronges is rf bg from measureing device
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])) {
                bgList.add(item);
            }
        }

        for (DataEntry item : data) {
            // weaker is user entered values
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])) {
                if (!dataEntryListContains(bgList, item)) {
                    bgList.add(item);
                }
            }
        }
        bgList.sort(DataEntry.getTimeSortComparator());
        return bgList;
    }

    public static List<DataEntry> createHypoList(List<DataEntry> cleanBgList,
            double threshold, int hypoTimeRange) {
        List<DataEntry> listEntrys = new ArrayList<>();
        DataEntry lastItem = null;

        for (DataEntry item : cleanBgList) {
            if (item.amount < threshold && item.amount > 0) {
                // check if the item does belong to the same hypo
                if (lastItem == null
                        || minutesDiff(lastItem.timestamp,
                                item.timestamp) > hypoTimeRange) {
                    lastItem = item;
                    listEntrys.add(item);
                }
            } else if (item.amount > 0) {
                // item that is over the threshold --> disconinues the series
                lastItem = null;
            }
        }

        // second pass to exclude
        return listEntrys;
    }

    public static List<DataEntry> createHyperList(List<DataEntry> cleanBgList,
            double threshold, int hyperTimeRange) {
        List<DataEntry> listEntrys = new ArrayList<>();

        for (DataEntry item : cleanBgList) {
            if (item.amount > threshold && item.amount > 0) {
                listEntrys.add(item);
            }
        }

        return listEntrys;
    }

    public static List<DataEntry> createCleanPrimeList(List<DataEntry> data) {
        List<DataEntry> primeList = new ArrayList<>();

        for (DataEntry item : data) {
            // strongest indicator for prime is rewind
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])) {
                primeList.add(item);
            }
        }

        for (DataEntry item : data) {
            // a little less strong is prime
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])) {
                if (!dataEntryListContains(primeList, item)) {
                    primeList.add(item);
                }
            }
        }

        primeList.sort(DataEntry.getTimeSortComparator());
        return primeList;
    }

    public static List<DataEntry> filterFollowingHypoValues(List<DataEntry> cleanBgList,
            Date startTime, int minuteRange, double threshold) {
        List<DataEntry> listEntrys = new ArrayList<>();

        for (DataEntry item : cleanBgList) {
            if (startTime.before(item.timestamp)
                    && minutesDiff(item.timestamp, startTime) < minuteRange) {
                listEntrys.add(item);
                if (item.amount > threshold) {
                    // completes the series
                    break;
                }
            }
        }

        return listEntrys;
    }

    public static DataEntry filterNextValue(List<DataEntry> cleanBgList,
            Date startTime) {

        for (DataEntry item : cleanBgList) {
            if (startTime.before(item.timestamp)) {
                return item;
            }
        }

        return null;
    }

    // this list is needed to create a combination of ke and bolus. --> later
//    public static List<DataEntry> createCleanBolusList(List<DataEntry> data) {
//        List<DataEntry> bolusList = new ArrayList<>();
//
//        for (DataEntry item : data) {
//            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])) {
//                bolusList.add(item);
//            }
//        }
//
//        bolusList.sort(DataEntry.getTimeSortComparator());
//        return bolusList;
//    }
    public static List<DataEntry> createCleanWizardList(List<DataEntry> data) {
        List<DataEntry> bolusList = new ArrayList<>();

        for (DataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])) {
                bolusList.add(item);
            }
        }

        // fill up boli without wizard (uuhhh bad guys)
        for (DataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])) {
                if (!dataEntryListContains(bolusList, item)) {
                    bolusList.add(item);
                }
            }
        }

        bolusList.sort(DataEntry.getTimeSortComparator());
        return bolusList;
    }

    public static List<DataEntry> filterTimeRange(List<DataEntry> list,
            Date fromRagen, Date toRange) {
        //TODO implement
        return list;
    }

    // ###################################################################
    // GUI Helper
    // ###################################################################
    public static String[] createGuiList(List<DataEntry> cleanBgList) {
        List<String> listEntrys = new ArrayList<>();

        for (DataEntry item : cleanBgList) {
            listEntrys.add(item.toGuiListEntry());
        }

        return listEntrys.toArray(new String[]{});
    }
}
