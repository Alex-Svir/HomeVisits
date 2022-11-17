package com.shurman.homevisits.table;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;
import com.shurman.homevisits.data.DEntry;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<CellHolder> {
    private static final int TYPE_CORNER = 0;
    private static final int TYPE_TOP = 1;
    private static final int TYPE_LEFT = 2;
    private static final int TYPE_MATRIX = 3;

    private final CellsCoordinator coordinator;
    private int columns;
    private int count;
    private ArrayList<Integer> mHeaders;
    private ArrayList<DEntry> mMatrix;

    public Adapter(CellsCoordinator coordinator) {
        this.coordinator = coordinator;
        reset(new ArrayList<>(), new ArrayList<>());
    }

    public void reset(ArrayList<Integer> headers, ArrayList<DEntry> matrix) {
        mHeaders = headers;
        mMatrix = matrix;
        count = headers.size() + matrix.size();
        columns = headers.isEmpty() ? 0 : headers.get(0);
        coordinator.setTableDimensions(columns, columns == 0 ? 0 : count / columns);
        //notifyItemRangeChanged(0, count);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == columns) return TYPE_CORNER;
        if (position < columns * 2) return TYPE_TOP;
        if (position % columns == 0) return TYPE_LEFT;
        return TYPE_MATRIX;
    }

    @NonNull
    @Override
    public CellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_data, parent, false);
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
            case TYPE_CORNER:
                str = position == 0 ? "$$$" : "$$";
                break;
            default:
                str = "";
        }
        TextView tv = holder.text();
        tv.setText(str);
        //tv.getBackground().setAlpha(255);
    }

    @Override
    public int getItemCount() { return count; }
}