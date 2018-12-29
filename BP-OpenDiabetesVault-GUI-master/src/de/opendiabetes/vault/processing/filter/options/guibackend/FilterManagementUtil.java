/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.opendiabetes.vault.processing.filter.options.guibackend;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.opendiabetes.vault.processing.DataSlicer;
import de.opendiabetes.vault.processing.VaultEntrySlicer;
import de.opendiabetes.vault.processing.filter.AndFilter;
import de.opendiabetes.vault.processing.filter.ClusterFilter;
import de.opendiabetes.vault.processing.filter.CombinationFilter;
import de.opendiabetes.vault.processing.filter.CompactQueryFilter;
import de.opendiabetes.vault.processing.filter.CounterFilter;
import de.opendiabetes.vault.processing.filter.DateTimePointFilter;
import de.opendiabetes.vault.processing.filter.DateTimeSpanFilter;
import de.opendiabetes.vault.processing.filter.ElevationFilter;
import de.opendiabetes.vault.processing.filter.ElevationPointFilter;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.FilterHitCounterFilter;
import de.opendiabetes.vault.processing.filter.FilterResult;
import de.opendiabetes.vault.processing.filter.GapRemoverFilter;
import de.opendiabetes.vault.processing.filter.InBetweenFilter;
import de.opendiabetes.vault.processing.filter.InterpolationFilter;
import de.opendiabetes.vault.processing.filter.NoneTypeFilter;
import de.opendiabetes.vault.processing.filter.OrFilter;

import de.opendiabetes.vault.processing.filter.StandardizeFilter;
import de.opendiabetes.vault.processing.filter.ThresholdFilter;
import de.opendiabetes.vault.processing.filter.TimePointFilter;
import de.opendiabetes.vault.processing.filter.TimeSpanFilter;
import de.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.opendiabetes.vault.processing.filter.ValueMoverFilter;
import de.opendiabetes.vault.processing.filter.VaultEntryTypeCounterFilter;
import de.opendiabetes.vault.processing.filter.VaultEntryTypeFilter;
import de.opendiabetes.vault.processing.filter.options.AndFilterOption;
import de.opendiabetes.vault.processing.filter.options.ClusterFilterOption;

import de.opendiabetes.vault.processing.filter.options.CompactQueryFilterOption;

import de.opendiabetes.vault.processing.filter.options.DateTimeSpanFilterOption;
import de.opendiabetes.vault.processing.filter.options.ElevationFilterOption;
import de.opendiabetes.vault.processing.filter.options.ElevationPointFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterHitCounterFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.GapRemoverFilterOption;
import de.opendiabetes.vault.processing.filter.options.InBetweenFilterOption;
import de.opendiabetes.vault.processing.filter.options.InterpolationFilterOption;
import de.opendiabetes.vault.processing.filter.options.NoneTypeFilterOption;
import de.opendiabetes.vault.processing.filter.options.OrFilterOption;
import de.opendiabetes.vault.processing.filter.options.QueryFilterOption;
import de.opendiabetes.vault.processing.filter.options.StandardizeFilterOption;
import de.opendiabetes.vault.processing.filter.options.ThresholdFilterOption;
import de.opendiabetes.vault.processing.filter.options.TimePointFilterOption;
import de.opendiabetes.vault.processing.filter.options.TimeSpanFilterOption;
import de.opendiabetes.vault.processing.filter.options.TypeGroupFilterOption;
import de.opendiabetes.vault.processing.filter.options.ValueMoverFilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeCounterFilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Daniel
 */
public class FilterManagementUtil {

    private List<FilterAndOption> filterAndOptions;

    public FilterManagementUtil() {
        filterAndOptions = new ArrayList<>();

        //Combine Filter contains Filter list as parameter
        filterAndOptions.add(new FilterAndOption(new AndFilterOption(null), new AndFilter(new AndFilterOption(null))));
        filterAndOptions.add(new FilterAndOption(new OrFilterOption(null), new OrFilter(new OrFilterOption(null))));

        //normal Filter
        //filterAndOptions.add(new FilterAndOption(new DateTimePointFilterOption(new Date(), 0), new DateTimePointFilter(new DateTimePointFilterOption(new Date(), 0))));
        filterAndOptions.add(new FilterAndOption(new DateTimeSpanFilterOption(new Date(), new Date()), new DateTimeSpanFilter(new DateTimeSpanFilterOption(new Date(), new Date()))));
        filterAndOptions.add(new FilterAndOption(new ThresholdFilterOption(0, 0), new ThresholdFilter(new ThresholdFilterOption(0, 0))));
        filterAndOptions.add(new FilterAndOption(new TimePointFilterOption(LocalTime.now(), 0), new TimePointFilter(new TimePointFilterOption(LocalTime.now(), 0))));
        filterAndOptions.add(new FilterAndOption(new TimeSpanFilterOption(LocalTime.now(), LocalTime.now()), new TimeSpanFilter(new TimeSpanFilterOption(LocalTime.now(), LocalTime.now()))));
        filterAndOptions.add(new FilterAndOption(new TypeGroupFilterOption(null), new TypeGroupFilter(new TypeGroupFilterOption(null))));
        filterAndOptions.add(new FilterAndOption(new VaultEntryTypeFilterOption(null), new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(null))));
        //filterAndOptions.add(new FilterAndOption(new CombinationFilterOption(new ArrayList<VaultEntry>(), null, null), new CombinationFilter(new CombinationFilterOption(new ArrayList<VaultEntry>(), null, null))));
//        filterAndOptions.add(new FilterAndOption(new QueryFilterOption(null, null, 0, 0), new QueryFilter(new QueryFilterOption(null, null, 0, 0))));
        filterAndOptions.add(new FilterAndOption(new ElevationFilterOption(null, 0, 0), new ElevationFilter(new ElevationFilterOption(null, 0, 0))));
        filterAndOptions.add(new FilterAndOption(new ElevationPointFilterOption(null, 0, 0), new ElevationPointFilter(new ElevationPointFilterOption(null, 0, 0))));
        filterAndOptions.add(new FilterAndOption(new CompactQueryFilterOption(null), new CompactQueryFilter(new CompactQueryFilterOption(null))));
        filterAndOptions.add(new FilterAndOption(new InBetweenFilterOption(null, 0, 0, false), new InBetweenFilter(new InBetweenFilterOption(null, 0, 0, false))));
        filterAndOptions.add(new FilterAndOption(new VaultEntryTypeCounterFilterOption(null, 0, 0, false), new VaultEntryTypeCounterFilter(new VaultEntryTypeCounterFilterOption(null, 0, 0, false))));
        filterAndOptions.add(new FilterAndOption(new FilterHitCounterFilterOption(null, 0, 0, false), new FilterHitCounterFilter(new FilterHitCounterFilterOption(null, 0, 0, false))));
        filterAndOptions.add(new FilterAndOption(new StandardizeFilterOption(null, false), new StandardizeFilter(new StandardizeFilterOption(null, false))));
        filterAndOptions.add(new FilterAndOption(new ClusterFilterOption(null), new ClusterFilter(new ClusterFilterOption(null))));
        filterAndOptions.add(new FilterAndOption(new NoneTypeFilterOption(null), new NoneTypeFilter(new NoneTypeFilterOption(null))));
        filterAndOptions.add(new FilterAndOption(new GapRemoverFilterOption(null, 0), new GapRemoverFilter(new GapRemoverFilterOption(null, 0))));
        filterAndOptions.add(new FilterAndOption(new InterpolationFilterOption(null, 0), new InterpolationFilter(new InterpolationFilterOption(null, 0))));
        //filterAndOptions.add(new FilterAndOption(new ValueMoverFilterOption(null, 0, false), new ValueMoverFilter(new ValueMoverFilterOption(null, 0, false))));
    }

    public List<String> getAllFilters() {
        List<String> result = new ArrayList<>();

        for (FilterAndOption filterAndOption : filterAndOptions) {
            result.add(filterAndOption.getName());
        }

        return result;
    }

    public List<String> getAllNotCombineFilters() {
        List<String> result = new ArrayList<>();

        for (FilterAndOption filterAndOption : filterAndOptions) {
            if (!filterAndOption.isCombine()) {
                result.add(filterAndOption.getName());
            }
        }

        return result;
    }

    public List getCombineFilter() {
        List<String> result = new ArrayList<>();

        for (FilterAndOption filterAndOption : filterAndOptions) {
            if (filterAndOption.isCombine()) {
                result.add(filterAndOption.getName());
            }
        }

        return result;
    }

    public Map<String, Class> getParametersFromName(String name) {
        Map<String, Class> result = new HashMap<>();

        for (FilterAndOption filterAndOption : filterAndOptions) {
            if (filterAndOption.getName().equals(name)) {
                result = filterAndOption.getParameterAndType();
            }
        }

        return result;
    }

    public FilterAndOption getFilterAndOptionFromName(String name) {
        FilterAndOption result = null;

        for (FilterAndOption filterAndOption : filterAndOptions) {
            if (filterAndOption.getName().equals(name)) {
                result = filterAndOption;
            }
        }

        return result;
    }

    public List<Filter> combineFilters(List<String> combineFilters, List<List<FilterNode>> allColumnsFilterNodes) {
        List<Filter> result = new ArrayList<>();

        int i = 0;

        for (String combineFilter : combineFilters) {

            List<FilterNode> filterNodes = allColumnsFilterNodes.get(i);
            List<Filter> filtersForCombine = new ArrayList<>();

            if (filterNodes.size() > 0) {
                for (FilterNode filterNode : filterNodes) {
                    if (filterNode.getFilters() != null && filterNode.getFilters().size() > 0) {

                        for (Filter filter : filterNode.getFilters()) {
                            filtersForCombine.add(filter);
                        }

                    } else {
                        Filter tempFilter = getFilterFromFilterNode(filterNode, null);
                        filtersForCombine.add(tempFilter);
                    }
                }

                Filter tempFilter = getFilterFromFilterNode(new FilterNode(combineFilter), filtersForCombine);
                result.add(tempFilter);
            }

            i++;
        }

        return result;
    }

    public FilterResult sliceVaultEntries(List<Filter> filters, List<VaultEntry> vaultEntries) {
        VaultEntrySlicer vaultEntrySlicer = new VaultEntrySlicer();

        vaultEntrySlicer.registerFilter(filters);

        FilterResult result = vaultEntrySlicer.sliceEntries(vaultEntries);

        return result;

    }

    public List<Filter> getFiltersFromFilterNodes(List<FilterNode> filterNodes) {
        List<Filter> result = new ArrayList<>();

        for (FilterNode filterNode : filterNodes) {
            result.add(getFilterFromFilterNode(filterNode, null));
        }

        return result;
    }

    public Filter getFilterFromFilterNode(FilterNode filterNode, List<Filter> filtersForCombine) {
        Filter result = null;
        FilterAndOption filterAndOption = getFilterAndOptionFromName(filterNode.getName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //CombineFilter
            if (filterAndOption.getFilterOptionName().equals(AndFilterOption.class.getSimpleName())) {
                if (filtersForCombine != null && filtersForCombine.size() > 0) {
                    result = new AndFilter(new AndFilterOption(filtersForCombine));
                } else {
                    List<Filter> tmpFilters = getFiltersFromFilterNodes(filterNode.getParameterAndFilterNodesFromName("Filters"));
                    result = new AndFilter(new AndFilterOption(tmpFilters));
                }

            } else if (filterAndOption.getFilterOptionName().equals(OrFilterOption.class.getSimpleName())) {

                if (filtersForCombine != null && filtersForCombine.size() > 0) {
                    result = new OrFilter(new OrFilterOption(filtersForCombine));
                } else {
                    List<Filter> tmpFilters = getFiltersFromFilterNodes(filterNode.getParameterAndFilterNodesFromName("Filters"));
                    result = new OrFilter(new OrFilterOption(tmpFilters));
                }
            } //NonCombineFilter
            else if (filterAndOption.getFilterOptionName().equals(DateTimeSpanFilterOption.class.getSimpleName())) {
                result = new DateTimeSpanFilter(new DateTimeSpanFilterOption(formatter.parse(filterNode.getParameterAndValues().get("StartTime")), formatter.parse(filterNode.getParameterAndValues().get("EndTime"))));
            } else if (filterAndOption.getFilterOptionName().equals(ThresholdFilterOption.class.getSimpleName())) {
                result = new ThresholdFilter(new ThresholdFilterOption(Integer.parseInt(filterNode.getParameterAndValues().get("MinThreshold").trim()), Integer.parseInt(filterNode.getParameterAndValues().get("MaxThreshold").trim()), Integer.parseInt(filterNode.getParameterAndValues().get("Mode").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(TimePointFilterOption.class.getSimpleName())) {
                result = new TimePointFilter(new TimePointFilterOption(LocalTime.parse(filterNode.getParameterAndValues().get("LocalTime").trim()), Integer.parseInt(filterNode.getParameterAndValues().get("MarginInMinutes").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(TimeSpanFilterOption.class.getSimpleName())) {
                result = new TimeSpanFilter(new TimeSpanFilterOption(LocalTime.parse(filterNode.getParameterAndValues().get("StartTime").trim()), LocalTime.parse(filterNode.getParameterAndValues().get("EndTime").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(TypeGroupFilterOption.class.getSimpleName())) {
                result = new TypeGroupFilter(new TypeGroupFilterOption(VaultEntryTypeGroup.valueOf(filterNode.getParameterAndValues().get("VaultEntryTypeGroup"))));
            } else if (filterAndOption.getFilterOptionName().equals(VaultEntryTypeFilterOption.class.getSimpleName())) {
                result = new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType"))));
            } /**
             * Durch Sampeln überflüssig else if
             * (filterAndOption.getFilterOptionName().equals(CombinationFilterOption.class.getSimpleName()))
             * { Filter firstFilter =
             * getFilterFromFilterNode(filterNode.getParameterAndFilterNodesFromName("FirstFilter").get(0),
             * null); Filter secondFilter =
             * getFilterFromFilterNode(filterNode.getParameterAndFilterNodesFromName("SecondFilter").get(0),
             * null); List<VaultEntry> data = filterNode.getData(); result = new
             * CombinationFilter(new CombinationFilterOption(data, firstFilter,
             * secondFilter)); }
             */
            /**
             * Kann durch CompactQueryFilter und CounterFilterabgebildet werden
             * else if
             * (filterAndOption.getFilterOptionName().equals(QueryFilterOption.class.getSimpleName()))
             * { Filter mainFilter =
             * getFilterFromFilterNode(filterNode.getParameterAndFilterNodesFromName("MainFilter").get(0),
             * null); Filter innerFilter =
             * getFilterFromFilterNode(filterNode.getParameterAndFilterNodesFromName("InnerFilter").get(0),
             * null); result = new QueryFilter(new QueryFilterOption(mainFilter,
             * innerFilter,
             * Integer.parseInt(filterNode.getParameterAndValues().get("minSize").trim()),
             * Integer.parseInt(filterNode.getParameterAndValues().get("maxSize").trim())));
             * }
             */
            else if (filterAndOption.getFilterOptionName().equals(InBetweenFilterOption.class.getSimpleName())) {
                result = new InBetweenFilter(new InBetweenFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType")), Integer.parseInt(filterNode.getParameterAndValues().get("MinValue").trim()), Integer.parseInt(filterNode.getParameterAndValues().get("MaxValue").trim()), Boolean.valueOf(filterNode.getParameterAndValues().get("Normieren").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(CompactQueryFilterOption.class.getSimpleName())) {
                List<Filter> filters = getFiltersFromFilterNodes(filterNode.getParameterAndFilterNodesFromName("Filters"));
                result = new CompactQueryFilter(new CompactQueryFilterOption(filters));
            } else if (filterAndOption.getFilterOptionName().equals(ElevationFilterOption.class.getSimpleName())) {
                String doubleString = filterNode.getParameterAndValues().get("MinElevationPerMinute").trim().replace(",", ".");
                result = new ElevationFilter(new ElevationFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType")), Double.parseDouble(doubleString), Integer.parseInt(filterNode.getParameterAndValues().get("MinutesBetweenEntries").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(ElevationPointFilterOption.class.getSimpleName())) {
                String doubleString = filterNode.getParameterAndValues().get("MinElevationPerMinute").trim().replace(",", ".");
                result = new ElevationPointFilter(new ElevationPointFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType")), Double.parseDouble(doubleString), Integer.parseInt(filterNode.getParameterAndValues().get("MinutesBetweenEntries").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(VaultEntryTypeCounterFilterOption.class.getSimpleName())) {
                result = new VaultEntryTypeCounterFilter(new VaultEntryTypeCounterFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType")), Integer.parseInt(filterNode.getParameterAndValues().get("MinHits").trim()), Integer.parseInt(filterNode.getParameterAndValues().get("MaxHits").trim()), Boolean.valueOf(filterNode.getParameterAndValues().get("NoneHits").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(FilterHitCounterFilterOption.class.getSimpleName())) {
                Filter filter = getFilterFromFilterNode(filterNode.getParameterAndFilterNodesFromName("Filter").get(0), null);
                result = new FilterHitCounterFilter(new FilterHitCounterFilterOption(filter, Integer.parseInt(filterNode.getParameterAndValues().get("MinHits").trim()), Integer.parseInt(filterNode.getParameterAndValues().get("MaxHits").trim()), Boolean.valueOf(filterNode.getParameterAndValues().get("NoneHits").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(StandardizeFilterOption.class.getSimpleName())) {
                result = new StandardizeFilter(new StandardizeFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType")), Boolean.valueOf(filterNode.getParameterAndValues().get("BetweenZeroAndOne").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(ClusterFilterOption.class.getSimpleName())) {
                result = new ClusterFilter(new ClusterFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType"))));
            } else if (filterAndOption.getFilterOptionName().equals(NoneTypeFilterOption.class.getSimpleName())) {
                result = new NoneTypeFilter(new NoneTypeFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType"))));
            } else if (filterAndOption.getFilterOptionName().equals(GapRemoverFilterOption.class.getSimpleName())) {
                result = new GapRemoverFilter(new GapRemoverFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType")), Long.parseLong(filterNode.getParameterAndValues().get("GapTimeInMinutes").trim())));
            } else if (filterAndOption.getFilterOptionName().equals(InterpolationFilterOption.class.getSimpleName())) {
                result = new InterpolationFilter(new InterpolationFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType")), Integer.parseInt(filterNode.getParameterAndValues().get("EntriesBetweenTimeStamps").trim())));
            }
            /**
             * else if
             * (filterAndOption.getFilterOptionName().equals(ValueMoverFilterOption.class.getSimpleName()))
             * { result = new ValueMoverFilter(new
             * ValueMoverFilterOption(VaultEntryType.valueOf(filterNode.getParameterAndValues().get("VaultEntryType")),
             * Double.parseDouble(filterNode.getParameterAndValues().get("Value").trim()),
             * Boolean.valueOf(filterNode.getParameterAndValues().get("Add").trim())));
             * }*
             */

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return result;
    }

    public FilterResult getLastDay(List<VaultEntry> importedData) {
        FilterResult result = null;
        Date date = importedData.get(importedData.size() - 1).getTimestamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        DateTimeSpanFilter dateTimeSpanFilter = new DateTimeSpanFilter(new DateTimeSpanFilterOption(calendar.getTime(), date));
        result = dateTimeSpanFilter.filter(importedData);

        return result;
    }

}
