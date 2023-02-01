package com.shurman.homevisits.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DDay {
    private final int year;
    private final int month;
    private final int day;
    private List<DEntry> entries;
    boolean altered = false;

    DDay(int y, int m, int d, List<DEntry> ee) {
        year = y;
        month = m;
        day = d;
        entries = ee;
    }

    DDay(int y, int m, int d) { this(y, m, d, new ArrayList<>()); }

    public void alter() {altered = true;}
    public boolean isAltered() {return altered;}

    public List<DEntry> entries() {return entries;}

    int getYear() { return year; }
    int getMonth() { return month; }
    int getDay() { return day; }

    /******
     * Asserts preset is presorted
     *
     * @param preset must be presorted
     */
    public boolean expandIfCorresponds(List<DEntry> preset) {
        entries.sort(DEntry.comparator);

        if (preset.size() <= entries.size())
            return false;

        List<DEntry> result = new ArrayList<>(preset.size());
        int posPreset = 0;
        int posList = 0;
        while (posPreset < preset.size() && posList < entries.size()) {
            if (preset.get(posPreset).corresponds(entries.get(posList)))
                result.add(entries.get(posList++));
            else
                result.add(preset.get(posPreset));
            posPreset++;
        }
        if (posList < entries.size())
            return false;
        while (posPreset < preset.size())
            result.add(preset.get(posPreset++));
        entries = result;
        return true;
    }

    public boolean expandForce(List<DEntry> preset) {
        entries.sort((e1, e2) -> {
            if (e1.price == e2.price) return e1.salary - e2.salary;
            return e1.price - e2.price;
        });

        if (preset.size() == entries().size()) {
            boolean identical = true;
            for (int i = 0; i < entries().size(); i++) {
                if (!preset.get(i).corresponds(entries().get(i))) {
                    identical = false;
                    break;
                }
            }
            if (identical) return false;
        }

        ArrayList<DEntry> result = new ArrayList<>(preset.size() + entries().size());
        int posEntries = 0;
        int posPreset = 0;
        while (posEntries < entries.size() && posPreset < preset.size()) {
            DEntry fromPreset = preset.get(posPreset);
            DEntry fromEntries = entries.get(posEntries);
            if (fromPreset.price == fromEntries.price) {
                if (fromPreset.salary < fromEntries.salary) {
                    result.add(fromPreset);
                    posPreset++;
                    continue;
                }
                if (fromPreset.salary == fromEntries.salary) { posPreset++; }
                result.add(fromEntries);
                posEntries++;
                continue;
            }
            if (fromPreset.price < fromEntries.price) {
                result.add(fromPreset);
                posPreset++;
                continue;
            }
            result.add(fromEntries);
            posEntries++;
        }
        while (posPreset < preset.size()) { result.add(preset.get(posPreset++)); }
        while (posEntries < entries.size()) { result.add(entries.get(posEntries++)); }
        entries = result;
        return true;
    }

    public List<DEntry> trimZeroes() {
        return entries = entries.stream()
                .filter(e -> e.count > 0).collect(Collectors.toCollection(ArrayList::new));
    }
}
