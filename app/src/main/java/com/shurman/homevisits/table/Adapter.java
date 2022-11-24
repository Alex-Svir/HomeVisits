package com.shurman.homevisits.table;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;
import com.shurman.homevisits.data.DEntry;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<CellHolder> {
    private static final int TYPE_CORNER_TOP = 0;
    private static final int TYPE_TOP = 1;
    private static final int TYPE_LEFT = 2;
    private static final int TYPE_MATRIX = 3;
    private static final int TYPE_CORNER_BOTTOM = 4;
    private static final int TYPE_BOTTOM = 5;

    private int columns;
    private int rows;
    private int count;
    private ArrayList<Integer> mHeaders;
    private ArrayList<DEntry> mMatrix;
    private ArrayList<Integer> mSummary;
    private int sumOffset;

    public Adapter() {
        count = 0;
        columns = 0;
        rows = 0;
        mHeaders = new ArrayList<>();
        mMatrix = new ArrayList<>();
        mSummary = new ArrayList<>();
        sumOffset = 0;
    }

    public void reset(TableDataCarrier tableData) {
        mHeaders = tableData.topHeaders;
        mMatrix = tableData.matrix;
        mSummary = tableData.bottomSummary;
        columns = tableData.columns;
        rows = tableData.rows;
        count = columns * rows;
        sumOffset = mHeaders.size() + mMatrix.size();
        //notifyItemRangeChanged(0, count);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == columns) return TYPE_CORNER_TOP;
        if (position < columns * 2) return TYPE_TOP;
        if (position < mHeaders.size() + mMatrix.size()) {
            if (position % columns == 0) return TYPE_LEFT;
            return TYPE_MATRIX;
        }
        if (position % columns == 0) return TYPE_CORNER_BOTTOM;
        return TYPE_BOTTOM;
    }

    @NonNull
    @Override
    public CellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res_id;
        switch (viewType) {
            case TYPE_CORNER_TOP:
                res_id = R.layout.item_table_corner_top;
                break;
            case TYPE_TOP:
                res_id = R.layout.item_table_header;
                break;
            case TYPE_CORNER_BOTTOM:
                res_id = R.layout.item_table_corner_bottom;
                break;
            case TYPE_BOTTOM:
                res_id = R.layout.item_table_footer;
                break;
            case TYPE_LEFT:
                res_id = R.layout.item_table_left;
                break;
            case TYPE_MATRIX:
            default:
                res_id = R.layout.item_table_data;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(res_id, parent, false);
        return new CellHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CellHolder holder, int position) {
        String str;
        switch (getItemViewType(position)) {
            case TYPE_MATRIX:
                DEntry de = mMatrix.get(position - (columns << 1));
                str = null == de ? "-" : String.valueOf(de.count);
                break;
            case TYPE_TOP:
                str = String.valueOf(mHeaders.get(position));
                break;
            case TYPE_LEFT:
                str = String.valueOf(position / columns - 1);
                break;
            case TYPE_BOTTOM:
                str = String.valueOf(mSummary.get(position - sumOffset));
                break;
            case TYPE_CORNER_TOP:
                str = position == 0 ? "$" : "%";
                break;
            case TYPE_CORNER_BOTTOM:
                str = position == sumOffset ? "N" : "Σ";
                break;
            default:
                str = "";
        }
        holder.text(str);
    }

    @Override
    public int getItemCount() { return count; }
}