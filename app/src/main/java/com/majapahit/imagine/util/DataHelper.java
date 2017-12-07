package com.majapahit.imagine.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rafy on 24/11/2017.
 */

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "imagine.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "" +
                "CREATE TABLE setting(" +
                "name TEXT UNIQUE, " +
                "value TEXT NULL" +
                ");";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);

        sql = "INSERT INTO setting(name, value) VALUES('id', '-');";
        db.execSQL(sql);

        sql = "INSERT INTO setting(name, value) VALUES('name', '-');";
        db.execSQL(sql);

        sql = "INSERT INTO setting(name, value) VALUES('email', '-');";
        db.execSQL(sql);

        sql = "INSERT INTO setting(name, value) VALUES('about', '-');";
        db.execSQL(sql);

        sql = "INSERT INTO setting(name, value) VALUES('location', '-');";
        db.execSQL(sql);

        sql = "INSERT INTO setting(name, value) VALUES('dir', '-');";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}
