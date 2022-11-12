package com.shurman.homevisits.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DMonth {
    private final int year;
    private final int month;
    private final List<DDay> days;

    DMonth(int year, int month, List<DDay> days) {
        this.year = year;
        this.month = month;
        this.days = days;
    }

    DMonth(int year, int month) {
        this(year, month, IntStream.range(1, 32).mapToObj(d -> new DDay(year, month, d))
                                                .collect(Collectors.toCollection(ArrayList::new)) );
    }

    public DDay date(int dayOfMonth) { return days.get(dayOfMonth - 1); }

    public boolean corresponds(int year, int month) { return year == this.year && month == this.month; }

    List<DDay> getDays() { return days; }
}
