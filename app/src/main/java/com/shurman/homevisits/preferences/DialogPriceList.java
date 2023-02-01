package com.shurman.homevisits.preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shurman.homevisits.R;
import com.shurman.homevisits.data.DEntry;

import java.util.ArrayList;

public class DialogPriceList extends DialogFragment {
    interface OnSaveWatcher {
        void onSave(ArrayList<DEntry> updatedList);
    }

    private static final String SAVED_LIST_TAG = "ValuesList";

    private OnSaveWatcher mWatcher;

    private ListView mList;
    private ArrayList<DEntry> mValues;

    public DialogPriceList() { super(); }//todo

    public DialogPriceList(@NonNull ArrayList<DEntry> values) {//todo
        super();
        mValues = values;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_pref_pricelist, container, false);

        if (savedInstanceState != null) {
            mValues = savedInstanceState.getIntegerArrayList(SAVED_LIST_TAG)
                    .stream().map(i -> (i == null ? null : new DEntry(i, 0)))
                    //.stream().map(i -> new DEntry(i, 0))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
        assert mValues != null : "mValues should be initialized";

        mList = root.findViewById(R.id.list);
        DialogPricesAdapter adapter = new DialogPricesAdapter(getContext(), mValues);
        mList.setAdapter(adapter);

        root.findViewById(R.id.bt_back).setOnClickListener(v -> dismiss());
        root.findViewById(R.id.bt_ok).setOnClickListener(v -> {//todo
            if (mWatcher != null) {
                if (mValues.get(mValues.size() - 1) == null) {
                    mValues.remove(mValues.size() - 1);
                }
                mWatcher.onSave(mValues);
            }
            dismiss();
        });
        root.findViewById(R.id.bt_add).setOnClickListener(v -> {
            if (adapter.add()) {
                mList.setItemChecked(mList.getAdapter().getCount() - 1, true);
            }
        });

        return root;
    }

    public DialogPriceList setOnSaveWatcher(OnSaveWatcher watcher) {
        mWatcher = watcher;
        return this;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntegerArrayList(SAVED_LIST_TAG, mValues.stream()
                .map(de -> (de == null ? null : de.getCombinedPriceSalary()))
                //.map(DEntry::getCombinedPriceSalary)//todo NPE while adding new item
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll)
        );
        super.onSaveInstanceState(outState);
    }
}
