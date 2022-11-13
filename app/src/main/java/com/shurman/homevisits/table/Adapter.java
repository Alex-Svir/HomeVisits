package com.shurman.homevisits.table;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;

public class Adapter extends RecyclerView.Adapter<CellHolder> {
    private final CellsCoordinator coordinator;

    public Adapter(CellsCoordinator coordinator) {
        this.coordinator = coordinator;
        reset();
    }

    public void reset() {
        //coordinator.setDimensions(CellsCoordinator.);
        notifyItemRangeChanged(0, CellsCoordinator.CAPACITY - 1);
    }

    @Override
    public int getItemViewType(int position) { return position; }

    @NonNull
    @Override
    public CellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new VHData(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_data, parent, false), viewType);
        //return new CellHolder(new MySuperDuperCustomView(parent.getContext()));
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_rv, parent, false);

        return new CellHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CellHolder holder, int position) {
        //((VHData)holder).textView().setText(String.valueOf(position));
        TextView tv = holder.view().findViewById(R.id.text);
        tv.setText(String.valueOf(position));
        int cc = (position * 13) & 0xFF;
        int color = 0xAA000000 | cc | (~cc << 8) | (cc << 15);
        //tv.setBackgroundColor(color);
        holder.view().setBackgroundColor(color);
    }

    @Override
    public int getItemCount() { return CellsCoordinator.CAPACITY; }
}
