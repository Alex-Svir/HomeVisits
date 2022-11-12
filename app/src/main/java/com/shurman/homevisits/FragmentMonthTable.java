package com.shurman.homevisits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shurman.homevisits.data.DMonth;

public class FragmentMonthTable extends Fragment {
    private MainViewModel viewModel;
    private TextView tvMonth;
    private MonthsTable mTable;
    private DMonth dMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_month_table, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        tvMonth = root.findViewById(R.id.tv_month);
        root.findViewById(R.id.bt_previous).setOnClickListener(v -> viewModel.prevMonth());
        root.findViewById(R.id.bt_next).setOnClickListener(v -> viewModel.nextMonth());
        mTable = new MonthsTable(getContext(), root);

        viewModel.monthData().observe(getViewLifecycleOwner(), month -> {
            dMonth = month;
            fillOut();
        });
        viewModel.currMonth();

        return root;
    }

    private void fillOut() {

    }
}
