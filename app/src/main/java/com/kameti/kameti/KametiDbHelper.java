package com.kameti.kameti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class KametiDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 9;
    private static final String TABLE_kameti =
    "CREATE TABLE `kameti` (" +
            "`kameti_id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`kameti_name` TEXT," +
            "`admin_id` INTEGER," +
            "`kameti_start_date` DATE," +
            "`kameti_members` INTEGER," +
            "`kameti_amount` INTEGER," +
            "`kameti_interest_rate` FLOAT," +
            "`bid_start_time` TIME," +
            "`bid_end_time` TIME," +
            "`bid_amount_minimum` INTEGER," +
            "`bid_timer` INTEGER," +
            "`lucky_draw_amount` INTEGER," +
            "`lucky_members` INTEGER," +
            "`runnerup_percentage` INTEGER," +
            "`kameti_rule` INTEGER" +
            ")";
    private static final String TABLE_members =
    "CREATE TABLE `members` (" +
            "`member_id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`user_name` TEXT," +
            "`mobile_number` TEXT," +
            "`pic` BLOB" +
            ")";

    public KametiDbHelper(Context context, String phoneNumber) {
        super(context, phoneNumber + ".db", null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_kameti);
        db.execSQL(TABLE_members);
        db.execSQL("INSERT INTO `members` VALUES(1, 'Aayush', '9971308922', NULL)");
        db.execSQL("INSERT INTO `kameti` VALUES(1, 'Gokuldham', 1, '2014-09-19', 10, 25000, 1.5, '13:30:00', '15:00:00', 100, 5, 200, 2, 50, 1)");
        db.execSQL("INSERT INTO `kameti` VALUES(3, 'Sector25', 1, '2014-09-19', 10, 20000, 1.5, '13:30:00', '15:00:00', 100, 5, 200, 2, 50, 1)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS `kameti`");
        db.execSQL("DROP TABLE IF EXISTS `members`");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}