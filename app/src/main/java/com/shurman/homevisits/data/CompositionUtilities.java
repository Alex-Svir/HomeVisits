package com.shurman.homevisits.data;

import android.util.Log;

import com.shurman.homevisits.Presets;
import com.shurman.homevisits.database.TableVisits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public static ArrayList<Integer> tableMonthsHeaders(DMonth month) {
        int[] headerPairs = Stream.concat(Presets.presetPriceList().stream(),
                                    month.getDays().stream().flatMap(d -> d.entries().stream()))
                    .mapToInt(e -> DEntry.combinePriceSalary(e.price, e.salary))
                    .sorted().distinct().toArray();
        int columns = headerPairs.length + 1;
        ArrayList<Integer> fixedRows = new ArrayList<>(columns << 1);
        fixedRows.add(columns);
        Arrays.stream(headerPairs).forEach(i -> fixedRows.add(DEntry.priceFromPair(i)));
        fixedRows.add(0);
        Arrays.stream(headerPairs).forEach(i -> fixedRows.add(DEntry.salaryFromPair(i)));
        return fixedRows;
    }

    public static ArrayList<DEntry> tableMonthsMatrix(DMonth month, List<Integer> headers) {
        int columns = headers.get(0);
        ArrayList<DEntry> matrix = new ArrayList<>(columns * month.length());
        for (int d = 1; d <= month.length(); ++d) {
            matrix.add(null);
            final List<DEntry> entries = month.date(d).entries();
            for (int h1 = 1, h2 = columns+1, e = 0; h1 < columns; ++h1, ++h2) {
                if (e < entries.size()) {
                    final DEntry dEntry = entries.get(e);
                    if (headers.get(h1).equals(dEntry.price) && headers.get(h2).equals(dEntry.salary)) {
                        matrix.add(dEntry);
                        e++;
                        continue;
                    }
                }
                matrix.add(null);
            }
        }
        return matrix;
    }

    public static Comparator<DEntry> dEntryComparator = (e1, e2) -> {
        if (e1.price == e2.price) return e1.salary - e2.salary;
            return e1.price - e2.price;
    };

    private static void l(String text) { Log.d("LOG_TAG::", text); }
}
