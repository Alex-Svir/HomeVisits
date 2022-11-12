package com.shurman.homevisits.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TableVisits.class}, version = 1)
public abstract class VisitsDataBase extends RoomDatabase {
    public abstract DaoVisits daoVisits();
}
