package com.hailing.webapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    //数据库名称
    private static final String DB_NAME="webapp";
    //数据库版本
    private static final int DB_VERSION=1;

    //表的字段名
    public static final String URL="url";
    public static final String TITLE="title";
    public static final String ICON="icon";
    public static final String TIME="time";

    public DataBaseHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    //创建一个history表格
    db.execSQL("create table if not exists "+"history"+"("
            + URL + "text"
            + TITLE + "text"
            + ICON + "text"
            + TIME + "text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
