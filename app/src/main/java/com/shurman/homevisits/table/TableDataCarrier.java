package com.shurman.homevisits.table;

import com.shurman.homevisits.data.DEntry;

import java.util.ArrayList;

public class TableDataCarrier {
    public int columns;
    public int rows;
    public ArrayList<Integer> topHeaders;
    public ArrayList<DEntry> matrix;
    public ArrayList<Integer> bottomSummary;
}
