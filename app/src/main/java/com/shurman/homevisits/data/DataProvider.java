package com.shurman.homevisits.data;

import android.content.Context;
import android.util.Log;

import com.shurman.homevisits.database.DaoVisits;
import com.shurman.homevisits.database.DataBaseHolder;
import com.shurman.homevisits.database.DataLoad;
import com.shurman.homevisits.database.TablePricelist;
import com.shurman.homevisits.database.TableVisits;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 *      public class providing the access to the database with user data
 *
 */
public class DataProvider {

    public static void loadMonth(Context context, int year, int month, DataLoad.Month cb) {
        new Thread(() -> {
                //  1)  Read database
            List<TableVisits> list = daoVisits(context).loadMonth(year * 100 + month);
            //list.stream().forEach(vis -> l("\t"+vis._yyyymm+":"+vis._date+"="+vis._price_income+"-"+vis._count));
                //  2)  If not present in database -- new empty month
            cb.monthParsed(list.size() > 0
                            ? CompositionUtilities.composeMonth(list, year, month)
                            : new DMonth(year, month) );
        }).start();
    }

    public static void saveDay(Context context, DDay day) {
        int yyyymm = day.getYear() * 100 + day.getMonth();
        int date = day.getDay();
        new Thread(() -> daoVisits(context).saveVisits(
                    day.entries().stream().filter(e -> e.count > 0).map(e -> {
                        TableVisits tv = new TableVisits();
                        tv._yyyymm = yyyymm;
                        tv._date = date;
                        tv._price_income = (e.price << 16) | e.salary;
                        tv._count = e.count;
                        return tv;
                    }).collect(Collectors.toCollection(ArrayList::new))
            )).start();
        day.altered = false;        //  TODO    review approach [synchronization / db callbacks]
                                //  third state with a synchronized change
        /*
         * for preventing state miscondition when data is being saving state switches to 'saving':
         * - if saving drops state to 'unaltered' an awaiting function will set 'altered' successfully;
         * - if 'altered' posted while saving process, callback understands that alteration performed
         *      after saving has initialized and does not drop 'altered' state
         */
    }

    public static void savePriceList(Context context, List<DEntry> priceList) {
        new Thread(() -> {
            DaoVisits dao = daoVisits(context);
            dao.dropPrices();
            dao.savePriceList(priceList.stream().map(de -> {
                TablePricelist tpl = new TablePricelist();
                tpl._price_salary = de.getCombinedPriceSalary();
                tpl._in_use = true;
                return tpl;
            }).collect(Collectors.toList()));
        }).start();
    }

    private static DaoVisits daoVisits(Context context) {
        return DataBaseHolder.get(context).daoVisits();
    }

    private static void l(String text) { Log.d("LOG_TAG::", text); }
}
