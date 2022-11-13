package com.shurman.homevisits.table;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;

public class MonthsTable {
    private RecyclerView rvTable;
    private Adapter adapter;
    //private RecyclerView.LayoutManager layMan;
    private TableLayoutManager lManager;


    public MonthsTable(Context context, View root) {
        rvTable = root.findViewById(R.id.rv_table);
        CellsCoordinator coordinator = new CellsCoordinator();
        rvTable.setAdapter(adapter = new Adapter(coordinator));
        rvTable.setLayoutManager(lManager = new TableLayoutManager(coordinator));
        //rvTable.setLayoutManager(new GridLayoutManager(context, COLUMNS, RecyclerView.VERTICAL, false));
        //rvTable.addItemDecoration(new FixedBorders());
    }
}
