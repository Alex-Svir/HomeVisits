package com.shurman.homevisits.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DataBaseHolder {
    private static final String DB_NAME = "visits_db";
    private static VisitsDataBase db = null;

    public static VisitsDataBase get(Context context) {
        if (db == null)
            db = Room.databaseBuilder(context, VisitsDataBase.class, DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build();
        return db;
    }

    private DataBaseHolder() {}

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'pricelist' ('_price_salary' INTEGER NOT NULL, "
                    + "'_in_use' INTEGER NOT NULL, PRIMARY KEY('_price_salary'))");
        }
    };
}
