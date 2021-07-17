package com.hailing.webapp.logic.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    //数据库名称
    private static final String DB_NAME="WebApp.db";
    //数据库版本
    private static final int DB_VERSION=1;

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


    public DataBaseHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
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
