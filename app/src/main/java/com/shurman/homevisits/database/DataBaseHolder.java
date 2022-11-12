package com.shurman.homevisits.database;

import android.content.Context;

import androidx.room.Room;

public class DataBaseHolder {
    private static final String DB_NAME = "visits_db";
    private static VisitsDataBase db = null;

    public static VisitsDataBase get(Context context) {
        if (db == null)
            db = Room.databaseBuilder(context, VisitsDataBase.class, DB_NAME).build();
        return db;
    }

    private DataBaseHolder() {}
}
