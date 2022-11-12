package com.shurman.homevisits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shurman.homevisits.data.DEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonthListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private List<DEntry> list;

    public MonthListAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = new ArrayList<>();
    }

    void reset(List<DEntry> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() { return list.size(); }
    @Override
    public Object getItem(int position) { return list.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_month_list, parent, false);
        DEntry entry = list.get(position);
        ((TextView)convertView.findViewById(R.id.tv_price_salary))
                .setText(String.format(Locale.getDefault(), "%d (%d)", entry.price, entry.salary));
        ((TextView)convertView.findViewById(R.id.tv_count)).setText(String.valueOf(entry.count));
        ((TextView)convertView.findViewById(R.id.tv_sum)).setText(String.valueOf(entry.salary * entry.count));
        return convertView;
    }
}
