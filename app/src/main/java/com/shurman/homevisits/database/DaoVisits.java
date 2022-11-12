package com.shurman.homevisits.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoVisits {
    @Query("SELECT * FROM visits WHERE _yyyymm = :yyyymm")
    List<TableVisits> loadMonth(int yyyymm);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveVisits(List<TableVisits> list);

    @Query("SELECT COUNT(*) FROM visits;")
    long count();
}
