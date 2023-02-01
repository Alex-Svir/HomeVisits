package com.shurman.homevisits.data;

import android.content.Context;
import android.util.Log;

import com.shurman.homevisits.database.DaoVisits;
import com.shurman.homevisits.database.DataBaseHolder;

import java.util.List;

public class AppInitialLoader {
    public interface Callback {
        void onLoad(Result result);
    }

    public static class Result {
        public List<Integer> priceList;
    }

    public static void load(Context context, Callback callback) {
        new Thread(() -> {
            Result result = new Result();
            DaoVisits daoVisits = DataBaseHolder.get(context).daoVisits();
            result.priceList = daoVisits.loadPricesInUse();
            callback.onLoad(result);
        }).start();
    }
}
