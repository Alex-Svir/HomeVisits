package com.shurman.homevisits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shurman.homevisits.data.DDay;
import com.shurman.homevisits.data.DEntry;
import com.shurman.homevisits.data.DataProvider;

public class FragmentDay extends Fragment implements DayListAdapter.CountersWatcher {
    private static final int MIN_COUNT = 0;
    private static final int MAX_COUNT = 99;     //  todo set 99 back!

    private MainViewModel viewModel;
    private DDay mDayData;

    private TextView tvDate;
    private TextView tvDaysDiff;
    private ListView lvEntries;
    private DayListAdapter listAdapter;
    private TextView tvTotalVisits;
    private TextView tvTotalIncome;
    private FloatingActionButton fabSave;

    private int offsetFromToday;
    private int mTotalVisits;
    private int mTotalIncome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        tvDate = root.findViewById(R.id.tv_date);
        tvDaysDiff = root.findViewById(R.id.tv_day_relative);
        lvEntries = root.findViewById(R.id.lv_entries);
        lvEntries.setAdapter(listAdapter = new DayListAdapter(getContext(), this));
        tvTotalVisits = root.findViewById(R.id.tv_visits);
        tvTotalIncome = root.findViewById(R.id.tv_salary);
        fabSave = root.findViewById(R.id.fab_save);
        fabSave.setOnClickListener(v -> {
            DataProvider.saveDay(getContext(), mDayData);
            fabSave.setVisibility(View.GONE);               //  TODO    better from callback for insurance
        });

        root.findViewById(R.id.bt_previous).setOnClickListener(v -> viewModel.prevDay());
        root.findViewById(R.id.bt_next).setOnClickListener(v -> viewModel.nextDay());

        viewModel.dayData().observe(getViewLifecycleOwner(), dday -> {
            mDayData = dday;
            fillOut();
        });
        viewModel.editModeData().observe(getViewLifecycleOwner(), em -> {
            if (em) enterEditMode();
            else leaveEnterMode();
        });

        viewModel.currDay();
        return root;
    }

    private void fillOut() {
        mTotalVisits = mDayData.entries().stream().mapToInt(entry -> entry.count).sum();
        mTotalIncome = mDayData.entries().stream().mapToInt(entry -> entry.count * entry.salary).sum();
        offsetFromToday = viewModel.getDatesDifference();
        tvDate.setText(viewModel.getDateString());
        tvDaysDiff.setText(RepresentationUtilities.representDaysOffset(getContext(), viewModel.getDatesDifference()));
        //lvEntries.setAdapter(listAdapter = new DayListAdapter(getContext(), mDayData.entries(), this));
        listAdapter.reset(mDayData.entries());
        tvTotalVisits.setText(String.valueOf(mTotalVisits));
        tvTotalIncome.setText(String.valueOf(mTotalIncome));
        fabSave.setVisibility(mDayData.isAltered() ? View.VISIBLE : View.GONE);
    }

    private void enterEditMode() {
        if (mDayData.expandIfCorresponds(Presets.presetPriceList()))
            listAdapter.setList(mDayData.entries());
        listAdapter.edit(true);
    }

    private void leaveEnterMode() {
        listAdapter.edit(false);
    }

    @Override
    public String counterAdjusted(int position, boolean add) {
        DEntry entry = mDayData.entries().get(position);
        if (entry.count == (add ? MAX_COUNT : MIN_COUNT))
            return String.valueOf(entry.count);

        mDayData.alter();
        if (add) {
                entry.count++;
                mTotalVisits++;
                mTotalIncome += entry.salary;
        } else {
                entry.count--;
                mTotalVisits--;
                mTotalIncome -= entry.salary;
        }
        tvTotalVisits.setText(String.valueOf(mTotalVisits));
        tvTotalIncome.setText(String.valueOf(mTotalIncome));
        fabSave.setVisibility(View.VISIBLE);
        return String.valueOf(entry.count);
    }
}
