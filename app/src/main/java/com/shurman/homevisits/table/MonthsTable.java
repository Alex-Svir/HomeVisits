package com.shurman.homevisits.table;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;
import com.shurman.homevisits.data.CompositionUtilities;
import com.shurman.homevisits.data.DEntry;
import com.shurman.homevisits.data.DMonth;

import org.shurman.tablelayoutmanager.SimpleCellBordersDecoration;
import org.shurman.tablelayoutmanager.TableLayoutManager;

import java.util.List;

public class MonthsTable {
    private final RecyclerView rvTable;
    private final  Adapter adapter;
    private final TableLayoutManager lManager;


    public MonthsTable(View root) {
        rvTable = root.findViewById(R.id.rv_table);
        rvTable.setAdapter(adapter = new Adapter());
        rvTable.setLayoutManager(lManager = new TableLayoutManager());
        rvTable.addItemDecoration(new SimpleCellBordersDecoration());
    }

    public void fillTable(DMonth month, List<DEntry> samplePriceList) {
        TableDataCarrier tableData = CompositionUtilities.composeTableData(month, samplePriceList);
        lManager.reset(tableData.columns, tableData.rows, 2, 1, 0, 2);
        adapter.reset(tableData);
    }
}