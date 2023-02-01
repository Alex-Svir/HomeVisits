package com.shurman.homevisits.preferences;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import com.shurman.homevisits.MainViewModel;
import com.shurman.homevisits.R;
import com.shurman.homevisits.data.DEntry;
import com.shurman.homevisits.data.DataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class FragmentPreferences extends PreferenceFragmentCompat implements DialogPriceList.OnSaveWatcher {
    private static final String DIALOG_TAG = "dial_price";

    private MainViewModel mViewModel;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, null);

        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        findPreference(getString(R.string.prefs_pricelist_key)).setOnPreferenceClickListener(
                p -> {
                    new DialogPriceList(mViewModel.getExpansionPriceList().stream()
                            .collect(ArrayList::new, (l, de) -> l.add(new DEntry(de)), ArrayList::addAll))//todo is this a copy?!!
                                .setOnSaveWatcher(this)
                                .show(getChildFragmentManager(), DIALOG_TAG);
                    return true;
                }
        );
    }

    @Override
    public void onSave(ArrayList<DEntry> updatedList) {//todo
        mViewModel.updateExpansionPriceList(updatedList);
        DataProvider.savePriceList(getContext(), updatedList);
    }
}
