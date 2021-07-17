package com.hailing.webapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_HISTORY = "create table history ("
            + "id integer primary key autoincrement, "
            + "url text, "
            + "title text, "
            + "icon text, "
            + "time text ) ";

    public static final String CREATE_BOOKMARK = "create table bookmark ("
            + "id integer primary key autoincrement, "
            + "icon text, "
            + "title text, "
            + "url text ) ";

    private Context mContext;


    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY);
        db.execSQL(CREATE_BOOKMARK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
