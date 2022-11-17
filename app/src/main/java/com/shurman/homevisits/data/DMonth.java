package com.shurman.homevisits.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DMonth {
    private final int year;
    private final int month;
    private final int length;
    private final List<DDay> days;

    DMonth(int year, int month, List<DDay> days) {
        this.year = year;
        this.month = month;
        this.days = days;
        length = daysInMonths(year, month);
    }

    DMonth(int year, int month) {
        this(year, month, IntStream.range(1, 32).mapToObj(d -> new DDay(year, month, d))
                                                .collect(Collectors.toCollection(ArrayList::new)) );
    }

    public DDay date(int dayOfMonth) { return days.get(dayOfMonth - 1); }

    public boolean corresponds(int year, int month) { return year == this.year && month == this.month; }

    List<DDay> getDays() { return days; }

    public int length() { return length; }

    public static int daysInMonths(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
