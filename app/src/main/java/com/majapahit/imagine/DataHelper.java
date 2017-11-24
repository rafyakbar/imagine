package com.majapahit.imagine;

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
                "id int, " +
                "name text null, " +
                "email null, " +
                "password text null, " +
                "location text null, " +
                "about text null);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}
