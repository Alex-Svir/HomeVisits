package com.shurman.homevisits;

import com.shurman.homevisits.data.DEntry;

import java.util.ArrayList;
import java.util.List;

public class Presets {

    public static List<DEntry> __presetPriceList() {
        List<DEntry> list = new ArrayList<>();
        list.add(new DEntry(180, 63, 0));
        list.add(new DEntry(215, 75, 0));
        list.add(new DEntry(225, 78, 0));
        list.add(new DEntry(270, 94, 0));
        list.add(new DEntry(300, 105, 0));
        list.add(new DEntry(370, 135, 0));
        list.add(new DEntry(450, 180, 0));
        return list;
    }
}
