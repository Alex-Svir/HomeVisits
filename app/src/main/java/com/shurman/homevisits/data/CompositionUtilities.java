package com.shurman.homevisits.data;

import android.util.Log;

import com.shurman.homevisits.Presets;
import com.shurman.homevisits.database.TableVisits;
import com.shurman.homevisits.table.TableDataCarrier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CompositionUtilities {     //  TODO    SLABO improve it???
    public static DMonth composeMonth(List<TableVisits> input, int year, int month) {
        Map<Integer, List<DEntry>> map = input.stream().collect(
                Collectors.groupingBy(
                        tv -> tv._date,
                        HashMap::new,
                        Collectors.mapping(
                                tv -> new DEntry(tv._price_income, tv._count),
                                Collectors.toCollection(ArrayList::new)
                        )
                )
        );
        List<DDay> days = new ArrayList<>(31);
        map.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).forEach(en -> {
            int d = en.getKey();
            while (d > days.size() + 1) days.add(new DDay(year, month, days.size() + 1));
            days.add(new DDay(year, month, d, en.getValue()));
        });
        /*
        map.forEach((d, de) -> {
            while (d > days.size() + 1) days.add(new DDay(year, month, days.size() + 1));
            days.add(new DDay(year, month, d, de));
        });*/
        while (days.size() < 31) days.add(new DDay(year, month, days.size() + 1));
        return new DMonth(year, month, days);
    }

    public static List<DEntry> organizeMonth(DMonth month) {
        Map<Integer, Integer> map = month.getDays().stream()
                .flatMap(d -> d.entries().stream())
                .filter(e -> e.count > 0)
                .collect(Collectors.groupingBy(
                        e -> DEntry.combinePriceSalary(e.price, e.salary),
                        HashMap::new,
                        Collectors.summingInt(e -> e.count)
                ));
        List<DEntry> result = new ArrayList<>(map.size());
        map.forEach((ps, c) -> result.add(new DEntry(ps, c)));
        result.sort(/*(e1, e2) -> {
            if (e1.price == e2.price) return e1.salary - e2.salary;
            return e1.price - e2.price;
        }*/dEntryComparator);
        return result;
    }

    public static TableDataCarrier composeTableData(DMonth month) {
    //public static ArrayList<Integer> tableMonthsHeaders(DMonth month) {
        TableDataCarrier tableData = new TableDataCarrier();
        int[] headerPairs = Stream.concat(Presets.presetPriceList().stream(),
                                    month.getDays().stream().flatMap(d -> d.entries().stream()))
                    .mapToInt(e -> DEntry.combinePriceSalary(e.price, e.salary))
                    .sorted().distinct().toArray();
        tableData.columns = headerPairs.length + 1;
        tableData.topHeaders = new ArrayList<>(tableData.columns << 1);
        tableData.topHeaders.add(tableData.columns);
        Arrays.stream(headerPairs).forEach(i -> tableData.topHeaders.add(DEntry.priceFromPair(i)));
        tableData.topHeaders.add(0);
        Arrays.stream(headerPairs).forEach(i -> tableData.topHeaders.add(DEntry.salaryFromPair(i)));
        //return fixedRows;
    //}
    //public static ArrayList<DEntry> tableMonthsMatrix(DMonth month, List<Integer> headers) {
        //int columns = headers.get(0);
        tableData.matrix = new ArrayList<>(tableData.columns * month.length());
        for (int d = 1; d <= month.length(); ++d) {
            tableData.matrix.add(null);
            List<DEntry> entries = month.date(d).entries();
            for (int h1 = 1, h2 = tableData.columns + 1, e = 0; h1 < tableData.columns; ++h1, ++h2) {
                if (e < entries.size()) {
                    DEntry dEntry = entries.get(e);
                    if (tableData.topHeaders.get(h1).equals(dEntry.price) && tableData.topHeaders.get(h2).equals(dEntry.salary)) {
                        tableData.matrix.add(dEntry);
                        e++;
                        continue;
                    }
                }
                tableData.matrix.add(null);
            }
        }

        tableData.bottomSummary = new ArrayList<>(tableData.columns << 1);
        IntStream.range(0, tableData.columns).forEach(i -> tableData.bottomSummary.add(0));
        int col = tableData.columns;
        for (DEntry de : tableData.matrix) {
            if (col == tableData.columns) col = 1;
            else {
                if (de != null) tableData.bottomSummary.set(col, tableData.bottomSummary.get(col) + de.count);
                ++col;
            }
        }

        tableData.bottomSummary.add(0);
        for (int c1 = 1, c2 = tableData.columns + 1; c1 < tableData.columns; ++c1, ++c2) {
            tableData.bottomSummary.add(tableData.topHeaders.get(c2) * tableData.bottomSummary.get(c1));
        }

        tableData.rows = month.length() + 4;
        return tableData;
    }

    public static Comparator<DEntry> dEntryComparator = (e1, e2) -> {
        if (e1.price == e2.price) return e1.salary - e2.salary;
            return e1.price - e2.price;
    };

    private static void l(String text) { Log.d("LOG_TAG::", text); }
}
