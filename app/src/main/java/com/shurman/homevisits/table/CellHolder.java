package com.shurman.homevisits.table;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;

public class CellHolder extends RecyclerView.ViewHolder {
    private final TextView tv;
    public CellHolder(View view) {
        super(view);
        tv = view.findViewById(R.id.text);
    }
    public void text(String text) { tv.setText(text); }
}
