package com.shurman.homevisits.database;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TableVisits.class, TablePricelist.class}, version = 2)
public abstract class VisitsDataBase extends RoomDatabase {
    public abstract DaoVisits daoVisits();
}
