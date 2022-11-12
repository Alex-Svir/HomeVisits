package com.shurman.homevisits;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shurman.homevisits.data.DEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DayListAdapter extends BaseAdapter {
    interface CountersWatcher {
        String counterAdjusted(int position, boolean add);
    }

    private final LayoutInflater inflater;
    private final CountersWatcher watcher;
    private List<DEntry> list;                    //  TODO    make it explicit ArrayList
    private boolean editable = false;

    public DayListAdapter(Context context, CountersWatcher watcher) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.watcher = watcher;
        this.list = new ArrayList<>();
    }

    public void edit(boolean edit) {
        editable = edit;
        notifyDataSetChanged();
    }

    public void setList(List<DEntry> list) { this.list = list; }

    public void reset(List<DEntry> list) {
        //editable = false;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }
    @Override
    public Object getItem(int position) { return position >= list.size() ? null : list.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView == null
                ? inflater.inflate(R.layout.item_day_list, parent, false)
                : convertView;
        DEntry entry = list.get(position);
        TextView tv = view.findViewById(R.id.tv_prices);
        tv.setText(String.format(Locale.getDefault(), "%d / %d", entry.price, entry.salary));

        TextView tvCount = view.findViewById(R.id.tv_count);
        tvCount.setText(String.valueOf(entry.count));

        ImageView btLess = view.findViewById(R.id.bt_less);
        ImageView btMore = view.findViewById(R.id.bt_more);
        if (editable) {
            btLess.setOnClickListener(v -> tvCount.setText(watcher.counterAdjusted(position, false)));
            btMore.setOnClickListener(v -> tvCount.setText(watcher.counterAdjusted(position, true)));
        }
        btLess.setVisibility( editable ? View.VISIBLE : View.INVISIBLE);
        btMore.setVisibility(editable ? View.VISIBLE : View.INVISIBLE);
        return view;
    }

    private static void l(String text) {
        Log.d("LOG_TAG::DLAdapter::", text);
    }
}
