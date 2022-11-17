package com.shurman.homevisits.table;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;
import com.shurman.homevisits.data.CompositionUtilities;
import com.shurman.homevisits.data.DEntry;
import com.shurman.homevisits.data.DMonth;

import java.util.ArrayList;

public class MonthsTable {
    private RecyclerView rvTable;
    private Adapter adapter;
    private TableLayoutManager lManager;


    public MonthsTable(Context context, View root) {
        rvTable = root.findViewById(R.id.rv_table);
        CellsCoordinator coordinator = new CellsCoordinator();
        rvTable.setAdapter(adapter = new Adapter(coordinator));
        rvTable.setLayoutManager(lManager = new TableLayoutManager(coordinator));
        //rvTable.addItemDecoration(new FixedBorders());
    }

    public void fillTable(DMonth month) {
        ArrayList<Integer> headers = CompositionUtilities.tableMonthsHeaders(month);
        ArrayList<DEntry> matrix = CompositionUtilities.tableMonthsMatrix(month, headers);
        l("mon len " + month.length());
        l("hdrs len " + headers.size());
        l("mtrx len " + matrix.size());
        adapter.reset(headers, matrix);
    }

    private static void l(String text) {
        Log.d("LOG_TAG::", text);
    }
}
