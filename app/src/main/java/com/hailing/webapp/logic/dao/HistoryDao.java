package com.hailing.webapp.logic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hailing.webapp.logic.model.History;
import com.hailing.webapp.util.DataBaseHelper;

import java.util.ArrayList;


public class HistoryDao {

    SQLiteDatabase sqLiteDatabase;
    DataBaseHelper dbHelper;

    //查询所有
    public ArrayList<History> query(){
        ArrayList<History> list = new ArrayList<History>();
        History history = null;
        String sql = "select * from history";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        while(cursor.moveToNext()){
            history = new History();
            list.add(history);
        }
        return list;
    }

    public void AddHistory(History history){
        String sql="insert into history(url,title,icon,time)values(?,?,?,?)";
        Object[] bindArgs = {history.getUrl(),history.getTitle(),history.getIcon(),history.getTime()};
        sqLiteDatabase.execSQL(sql,bindArgs);
    }

    //根据url删除
    public void DeleteHistory(String url){
        String sql = "delete from history where url = " + url;
        sqLiteDatabase.execSQL(sql);
    }

    public void DeleteAllHistory(){
        String sql = "delete from history";
        sqLiteDatabase.execSQL(sql);
    }

}
