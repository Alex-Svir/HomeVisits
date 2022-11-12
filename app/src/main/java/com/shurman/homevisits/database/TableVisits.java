package com.shurman.homevisits.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "visits", primaryKeys = {"_yyyymm", "_date", "_price_income"})
public class TableVisits {

    public int _yyyymm;

    public int _date;

    public int _price_income;

    public int _count;
}
