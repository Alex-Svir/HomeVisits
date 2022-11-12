package com.shurman.homevisits;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;

public class DatePager {
    private final Calendar cPointer;
    private final Calendar cToday;              //      TODO    periodic check and reset
    private Day dToday;
    private Day dPointer;

    public DatePager() {
        cPointer = Calendar.getInstance();
        cToday = Calendar.getInstance();
        cToday.set(Calendar.HOUR_OF_DAY, 0);
        cToday.set(Calendar.MINUTE, 0);
        cToday.set(Calendar.SECOND, 0);
        cToday.set(Calendar.MILLISECOND, 0);

        //today = new Day(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        //pointer = new Day(today.year, today.month, today.date);
    }

    public void nextDay() { cPointer.add(Calendar.DATE, 1); }
    public void prevDay() { cPointer.add(Calendar.DATE, -1); }
    public void nextMonth() { cPointer.add(Calendar.MONTH, 1); }
    public void prevMonth() { cPointer.add(Calendar.MONTH, -1); }
    public int year() { return cPointer.get(Calendar.YEAR); }
    public int month() { return cPointer.get(Calendar.MONTH); }
    public int date() { return cPointer.get(Calendar.DAY_OF_MONTH); }
    public boolean today() { return daysFromToday() == 0; }

    public String dateString() {
        return new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(cPointer.getTime());
    }

    public String monthString() {
        return new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cPointer.getTime());
    }

    public int daysFromToday() {
        long offset = ChronoUnit.DAYS.between(cToday.toInstant(), cPointer.toInstant());
        if (cPointer.before(cToday)) offset--;
        return (int) offset;
    }

    private static class Day {
        int year;
        int month;
        int date;
        Day(int y, int m, int d) {year = y; month = m; date = d;}
    }
}
