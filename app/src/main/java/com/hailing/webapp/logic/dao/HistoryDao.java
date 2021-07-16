package com.hailing.webapp.logic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hailing.webapp.logic.model.History;
import com.hailing.webapp.util.DataBaseHelper;

import java.util.ArrayList;


public class HistoryDao {

    SQLiteDatabase sqLiteDatabase;
    DataBaseHelper dataBaseHelper;

    public HistoryDao(Context context){
        dataBaseHelper = new DataBaseHelper(context);
    }

    //连接数据库
    public void getDataBaseConn(){
        sqLiteDatabase=dataBaseHelper.getWritableDatabase();
    }

    //关闭数据库
    public void releaseDataBase(){
        sqLiteDatabase.close();
    }

    //查询所有
    public ArrayList<History> query(){
        ArrayList<History> list = new ArrayList<History>();
        History history = null;
        String sql = "select * from history";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        while(cursor.moveToNext()){
            history = new History();
            history.setUrl(cursor.getString(cursor.getColumnIndex(DataBaseHelper.URL)));
            history.setTitle(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TITLE)));
            list.add(history);
        }
        return list;
    }

    public void AddHistory(History history){
        String sql="insert into history(url,title)values(?,?)";
        Object[] bindArgs = {history.getUrl(),history.getTitle()};
        sqLiteDatabase.execSQL(sql,bindArgs);
    }

    //根据url删除
    public void DeleteHistory(String url){
        String sql = "delete from history where url = " + url;
        sqLiteDatabase.execSQL(sql);

    }

}
