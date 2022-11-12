package com.shurman.homevisits.data;

import com.shurman.homevisits.database.TableVisits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompositionUtilities {
    public static DMonth composeMonth(List<TableVisits> input, int year, int month) {
        Map<Integer, List<DEntry>> map = input.stream().collect(
                Collectors.groupingBy(
                        tv -> tv._date,
                        HashMap::new,
                        Collectors.mapping(
                                tv -> new DEntry((tv._price_income >>> 16) & 0xFFFF, tv._price_income & 0xFFFF, tv._count),
                                Collectors.toCollection(ArrayList::new)
                        )
                )
        );

        List<DDay> days = new ArrayList<>(31);
        map.forEach((d, de) -> {
            while (d > days.size() + 1) days.add(new DDay(year, month, days.size() + 1));
            days.add(new DDay(year, month, d, de));
        });
        while (days.size() < 31) days.add(new DDay(year, month, days.size() + 1));

        return new DMonth(year, month, days);
    }

    public static List<DEntry> organizeMonth(DMonth month) {
        Map<Integer, Integer> map = month.getDays().stream()
                .flatMap(d -> d.entries().stream())
                .filter(e -> e.count > 0)
                .collect(Collectors.groupingBy(
                        e -> (e.price << 16) | e.salary,
                        HashMap::new,
                        Collectors.summingInt(e -> e.count)
                ));
        List<DEntry> result = new ArrayList<>(map.size());
        map.forEach((ps, c) -> result.add(new DEntry(ps >>> 16, ps & 0xFFFF, c)));
        result.sort((e1, e2) -> {
            if (e1.price == e2.price) return e1.salary - e2.salary;
            return e1.price - e2.price;
        });
        return result;
    }
}
