package com.shurman.homevisits.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pricelist")
public class TablePricelist {
    @PrimaryKey
    public int _price_salary;

    public boolean _in_use;
}
