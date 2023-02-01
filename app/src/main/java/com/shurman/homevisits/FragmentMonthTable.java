package com.shurman.homevisits;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.shurman.homevisits.data.DMonth;
import com.shurman.homevisits.table.MonthsTable;

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

        if (detectLandscape()) defineAltMenuBehavior(root);

        tvMonth = root.findViewById(R.id.tv_month);
        root.findViewById(R.id.bt_previous).setOnClickListener(v -> viewModel.prevMonth());
        root.findViewById(R.id.bt_next).setOnClickListener(v -> viewModel.nextMonth());
        mTable = new MonthsTable(root);

        viewModel.monthData().observe(getViewLifecycleOwner(), month -> {
            dMonth = month;
            fillOut();
        });
        viewModel.currMonth();

        return root;
    }

    private void fillOut() {
        tvMonth.setText(viewModel.getMonthString());
        mTable.fillTable(dMonth, viewModel.getExpansionPriceList());
    }

    private boolean detectLandscape() {
        switch (getActivity().getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                return true;
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
            default:
                return false;
        }
    }

    private void defineAltMenuBehavior(View root) {
        FragmentActivity fragAct = getActivity();
        if (! (fragAct instanceof AppCompatActivity))
            return;
        AppCompatActivity acAct = ((AppCompatActivity) fragAct);
        ActionBar aBar = acAct.getSupportActionBar();
        if (null == aBar)
            return;
        aBar.hide();
        View altMenuButton = root.findViewById(R.id.bt_menu);
        altMenuButton.setVisibility(View.VISIBLE);
        altMenuButton.setOnClickListener(v -> {
            if (aBar.isShowing()) aBar.hide();
            else aBar.show();
        });
    }

    private static void l(String text) { Log.d("LOG_TAG::", text); }
}
