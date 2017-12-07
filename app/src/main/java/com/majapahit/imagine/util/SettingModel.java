package com.majapahit.imagine.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Rafy on 24/11/2017.
 */

public class SettingModel {

    public static boolean checkLoginOrRegister(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM setting WHERE name = 'id'", null);
        cursor.moveToFirst();
        //Log.d("message", cursor.getString(1).toString());
        if (cursor.getString(1).toString().equals("-")) {
//            Log.d("message", "false");
            return false;
        }
        return true;
    }

    public static boolean check(SQLiteDatabase db, String name) {
        Cursor cursor = db.rawQuery("SELECT * FROM setting WHERE name = '" + name + "'", null);
        cursor.moveToFirst();
        //Log.d("message", cursor.getString(1).toString());
        if (cursor.getString(1).toString().equals("-")) {
//            Log.d("message", "false");
            return false;
        }
        return true;
    }
}
