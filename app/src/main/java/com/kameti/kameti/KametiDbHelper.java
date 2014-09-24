package com.kameti.kameti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class KametiDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 12;
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
    private static final String TABLE_auction =
    "CREATE TABLE `auction` (" +
            "`auction_id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`kameti_id` INTEGER," +
            "`auction_date` DATE," +
            "`auction_start_time` TIME," +
            "`auction_end_time` TIME," +
            "`auction_winner` INTEGER," +
            "`auction_runnerup` INTEGER," +
            "`minimum_bid_amount` INTEGER," +
            "`maximum_bid_amount` INTEGER," +
            "`member_profit` INTEGER," +
            "`intrest_rate` FLOAT," +
            "`lucky_member1` INTEGER," +
            "`lucky_member2` INTEGER," +
            "`lucky_member3` INTEGER," +
            "`lucky_member4` INTEGER," +
            "`lucky_member5` INTEGER," +
            "`lucky_member6` INTEGER," +
            "`lucky_member7` INTEGER," +
            "`lucky_member8` INTEGER," +
            "`lucky_member9` INTEGER," +
            "`lucky_member10` INTEGER" +
            ")";

    public KametiDbHelper(Context context, String phoneNumber) {
        super(context, phoneNumber + ".db", null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_kameti);
        db.execSQL(TABLE_members);
        db.execSQL(TABLE_auction);
        //TODO: Clear debug data before release
        db.execSQL("INSERT INTO `members` VALUES(1, 'Aayush', '9971308922', NULL)");
        db.execSQL("INSERT INTO `kameti` VALUES(1, 'Gokuldham', 1, '2014-09-19', 10, 25000, 1.5, '13:30:00', '15:00:00', 100, 5, 200, 2, 50, 1)");
        db.execSQL("INSERT INTO `kameti` VALUES(3, 'Sector25', 1, '2014-09-19', 10, 20000, 1.5, '13:30:00', '15:00:00', 100, 5, 200, 2, 50, 1)");
        db.execSQL("INSERT INTO `auction` VALUES(1, 1, '2014-09-19', '13:30:00', '15:00:00', NULL, NULL, 3375, NULL, NULL, 1.5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)");
        db.execSQL("INSERT INTO `auction` VALUES(2, 1, '2014-10-19', '13:30:00', '15:00:00', NULL, NULL, 3000, NULL, NULL, 1.5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS `kameti`");
        db.execSQL("DROP TABLE IF EXISTS `members`");
        db.execSQL("DROP TABLE IF EXISTS `auction`");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}