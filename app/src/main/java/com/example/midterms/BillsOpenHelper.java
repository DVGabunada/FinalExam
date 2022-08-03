package com.example.midterms;

import static com.example.midterms.Bill.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BillsOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myDatabase.db";
    public static final String DATABASE_TABLE = "Bills";
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " +
            DATABASE_TABLE + " (" +
            KEY_ID + " integer primary key autoincrement, " +
            KEY_PREVIOUS_COLUMN + " integer, " +
            KEY_CURRENT_COLUMN + " integer, " +
            KEY_BRAND_COLUMN + " text, " +
            KEY_DIAMETER_COLUMN + " double, " +
            KEY_PACK_COLUMN + " integer," +
            KEY_MONTH_COLUMN + " integer);";

    public BillsOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
