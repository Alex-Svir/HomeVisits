package com.shurman.homevisits.table;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;
import com.shurman.homevisits.data.CompositionUtilities;
import com.shurman.homevisits.data.DMonth;

public class MonthsTable {
    private final RecyclerView rvTable;
    private final  Adapter adapter;
    private final TableLayoutManager lManager;


    public MonthsTable(View root) {
        rvTable = root.findViewById(R.id.rv_table);
        rvTable.setAdapter(adapter = new Adapter());
        rvTable.setLayoutManager(lManager = new TableLayoutManager());
        //rvTable.addItemDecoration(new FixedBorders());
        //rvTable.addItemDecoration(new FrameDrawer());
    }

    public void fillTable(DMonth month) {
        TableDataCarrier tableData = CompositionUtilities.composeTableData(month);
        lManager.reset(tableData.columns, tableData.rows, 2, 1, 0, 2);
        adapter.reset(tableData);
    }

    private static void l(String text) { Log.d("LOG_TAG::", text); }
}