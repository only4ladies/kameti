package com.kameti.kameti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class KametiDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Kameti.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE `Kameti` (" +
            "`_id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`name` TEXT" +
            ")";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS `Kameti`";

    public KametiDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}