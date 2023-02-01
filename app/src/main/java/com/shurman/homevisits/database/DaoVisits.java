package com.shurman.homevisits.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DaoVisits {
    @Query("SELECT * FROM visits WHERE _yyyymm = :yyyymm")
    List<TableVisits> loadMonth(int yyyymm);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveVisits(List<TableVisits> list);

    @Query("SELECT COUNT(*) FROM visits;")
    long count();

    @Query("SELECT _price_salary FROM pricelist WHERE _in_use = 1 ORDER BY _price_salary;")
    List<Integer> loadPricesInUse();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void savePriceList(List<TablePricelist> list);
    @Query("UPDATE pricelist SET _in_use = 0;")
    void dropPrices();
}
