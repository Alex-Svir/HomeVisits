package com.shurman.homevisits;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shurman.homevisits.data.AppInitialLoader;
import com.shurman.homevisits.data.DDay;
import com.shurman.homevisits.data.DEntry;
import com.shurman.homevisits.data.DMonth;
import com.shurman.homevisits.data.DataProvider;
import com.shurman.homevisits.database.DataLoad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainViewModel extends AndroidViewModel implements DataLoad.Month {
    private final List<DMonth> months;
    private final DatePager datePtr;
    private final MutableLiveData<AppNavigator> navData;
    private MutableLiveData<DDay> dDayData = null;
    private MutableLiveData<DMonth> dMonthData = null;
    private final MutableLiveData<Boolean> inEditMode;
    private ArrayList<DEntry> expansionPriceList;

    public MainViewModel(Application application) {
        super(application);
        months = new ArrayList<>();
        datePtr = new DatePager();
        inEditMode = new MutableLiveData<>(false);
        navData = new MutableLiveData<>(new AppNavigator());
    }

    public LiveData<Boolean> editModeData() { return inEditMode; }
    public void toggleEditMode() { inEditMode.setValue(!inEditMode.getValue()); }

    public LiveData<AppNavigator> navLiveData() {
        return navData;
    }

    public void requestScreen(AppNavigator.Request screen) {
        AppNavigator an = navData.getValue();
        if (an == null) an = new AppNavigator(screen);
        else an.goTo(screen);
        navData.postValue(an);
    }

    public AppNavigator.Screen getCurrentScreen() {
        assert navData.getValue() != null : "In MainViewModel: navData value == null";
        return navData.getValue().screen();
    }

    public LiveData<DDay> dayData() {
        if (dDayData == null) dDayData = new MutableLiveData<>();
        return dDayData;
    }

    public LiveData<DMonth> monthData() {
        if (dMonthData == null) dMonthData = new MutableLiveData<>();
        return dMonthData;
    }

    @Override
    synchronized
    public void monthParsed(DMonth month) {
        months.add(month);
        if (dMonthData != null) dMonthData.postValue(month);
        if (dDayData != null) {
            dDayData.postValue(month.date(datePtr.date()));
            if (datePtr.today()) inEditMode.postValue(true);    //  // TODO: 05.11.2022     editMode    <<< ; force expand
        }
    }
//--------------------------------------------------------------------------------------------------
    private void updateDay() {
        DMonth aMonth = findSelectedMonth();
        if (aMonth == null)
            DataProvider.loadMonth(getApplication(), datePtr.year(), datePtr.month(), this);
            //  TODO    set placeholder or progressbar to DayFragment
        else {
            dDayData.setValue(aMonth.date(datePtr.date()));
            if (datePtr.today()) inEditMode.setValue(true);     //  // TODO: 05.11.2022     editMode    <<< ; force expand
        }
    }

    public void currDay() {
        updateDay();
    }

    public void prevDay() {
        datePtr.prevDay();
        inEditMode.setValue(false);
        updateDay();
    }

    public void nextDay() {
        datePtr.nextDay();
        inEditMode.setValue(false);
        updateDay();
    }
//--------------------------------------------------------------------------------------------------
    private void updateMonth() {
        inEditMode.setValue(false);                             //  // TODO: 05.11.2022     editMode    <<<
        DMonth aMonth = findSelectedMonth();
        if (aMonth == null)
            DataProvider.loadMonth(getApplication(), datePtr.year(), datePtr.month(), this);
        else
            dMonthData.setValue(aMonth);
    }

    public void currMonth() { updateMonth(); }

    public void nextMonth() {
        datePtr.nextMonth();
        updateMonth();
    }

    public void prevMonth() {
        datePtr.prevMonth();
        updateMonth();
    }
//--------------------------------------------------------------------------------------------------
    private DMonth findSelectedMonth() {
        int m = datePtr.month();
        int y = datePtr.year();
        return months.stream().filter(dm -> dm.corresponds(y, m)).findFirst().orElse(null);
    }

    public String getDateString() { return datePtr.dateString(); }
    public String getMonthString() { return datePtr.monthString(); }
    public int getDatesDifference() { return datePtr.daysFromToday(); }

    public void appInitialLoad() {
        AppInitialLoader.load(getApplication(), result -> {
            expansionPriceList = result.priceList.stream()
                    .map(i -> new DEntry(i, 0))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            requestScreen(AppNavigator.Request.DAYS);
        });
    }

    public ArrayList<DEntry> getExpansionPriceList() {
        return expansionPriceList;
    }

    public void updateExpansionPriceList(ArrayList<DEntry> updatedList) {
        expansionPriceList = updatedList;
    }

    private static void l(String text) { Log.d("LOG_TAG::", text); }
}

