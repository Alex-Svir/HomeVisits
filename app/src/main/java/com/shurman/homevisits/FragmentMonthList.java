package com.shurman.homevisits;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.shurman.homevisits.data.CompositionUtilities;
import com.shurman.homevisits.data.DEntry;
import com.shurman.homevisits.data.DMonth;

import java.util.List;

public class FragmentMonthList extends Fragment {
    private MainViewModel viewModel;
    private DMonth mMonthData;

    private TextView tvMonth;
    private ListView lvEntries;
    private MonthListAdapter adapter;
    private TextView tvTotalVisits;
    private TextView tvTotalIncome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_month, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        tvMonth = root.findViewById(R.id.tv_month);
        lvEntries = root.findViewById(R.id.lv_entries);
        lvEntries.setAdapter(adapter = new MonthListAdapter(getContext()));
        tvTotalVisits = root.findViewById(R.id.tv_count);
        tvTotalIncome = root.findViewById(R.id.tv_sum);

        float sz = getResources().getDimension(R.dimen.list_month_summary_text_size);
        tvTotalVisits.setTextSize(TypedValue.COMPLEX_UNIT_PX, sz);
        tvTotalIncome.setTextSize(TypedValue.COMPLEX_UNIT_PX, sz);

        View vv = root.findViewById(R.id.in_titles);
        TextView tvPC = vv.findViewById(R.id.tv_price_salary);
        tvPC.setText(R.string.pricelist_hint);
        tvPC.setTextColor(Color.BLACK);
        TextView tvCnt = vv.findViewById(R.id.tv_count);
        tvCnt.setText(R.string.visits_hint);
        tvCnt.setTextColor(Color.BLACK);
        TextView tvInc = vv.findViewById(R.id.tv_sum);
        tvInc.setText(R.string.income_hint);
        tvInc.setTextColor(Color.BLACK);

        root.findViewById(R.id.bt_previous).setOnClickListener(v -> viewModel.prevMonth());
        root.findViewById(R.id.bt_next).setOnClickListener(v -> viewModel.nextMonth());

        viewModel.monthData().observe(getViewLifecycleOwner(), dm -> {
            mMonthData = dm;
            fillOut();

            //CompositionUtilities.foo(dm);
        });
        viewModel.currMonth();

        return root;
    }

    private void fillOut() {
        tvMonth.setText(viewModel.getMonthString());
        List<DEntry> entries = CompositionUtilities.organizeMonth(mMonthData);
        adapter.reset(entries);
        tvTotalVisits.setText(String.valueOf(entries.stream().mapToInt(e -> e.count).sum()));
        tvTotalIncome.setText(String.valueOf(entries.stream().mapToInt(e -> e.count * e.salary).sum()));
    }
}
