package com.shurman.homevisits.table;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CellHolder extends RecyclerView.ViewHolder {
    View v;
    public CellHolder(View view) {
        super(view);
        v = view;
    }
    public View view() {return v;}
}
