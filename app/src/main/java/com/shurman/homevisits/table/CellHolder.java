package com.shurman.homevisits.table;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.R;

public class CellHolder extends RecyclerView.ViewHolder {
    TextView tv;
    public CellHolder(View view) {
        super(view);
        tv = view.findViewById(R.id.text);
    }
    public TextView text() {return tv;}
}
