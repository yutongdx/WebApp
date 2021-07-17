package com.hailing.webapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "webapp";
    private static int DB_VERSION = 1;

    public static final String CREATE_HISTORY = "create table history ("
            + "id integer primary key autoincrement, "
            + "url text, "
            + "title text, "
            + "icon text, "
            + "time text ) ";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_HISTORY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
