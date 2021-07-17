package com.hailing.webapp.logic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ViewStructure;

import com.hailing.webapp.logic.model.History;
import com.hailing.webapp.util.DataBaseHelper;

import java.util.ArrayList;


public class HistoryDao {

    /*通过db=dbHelper.getWritableDatabase()获取数据库
    因此下面的方法都传入了db,db是在MainActivity获取的
    不知道是不是这样做
    1.在activity那里先获取数据库再传入db
    2.在Dao的方法里获取数据库db再执行
     */
    //查询所有
    public ArrayList<History> QueryAll(SQLiteDatabase db){
        ArrayList<History> list = new ArrayList<History>();
        History history = null;
        String sql = "select * from history";
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            history = new History();
            history.setId(cursor.getInt(cursor.getColumnIndex("id")));
            history.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            history.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            history.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            history.setTime(cursor.getString(cursor.getColumnIndex("time")));
            list.add(history);
        }
        return list;
    }

    //根据url和时间找到历史记录的id
    public int Query_id(String url,String time,SQLiteDatabase db){
        int id = 0;
        String sql = "select * from history where url = " + url + " and time = " + time;
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        return id;
        
    }

    //根据id查询历史记录
    public void QueryByid(int id,SQLiteDatabase db){
        String sql = "select * from history where id = " + id;
        db.execSQL(sql);
    }


    //根据url查询历史记录
    public  void Query_url(String url,SQLiteDatabase db){
        String sql = "select * from history where url = " + url;
        db.execSQL(sql);

    }


    //添加历史记录
    public void AddHistory(History history,SQLiteDatabase db){
        String sql="insert into history(id,url,title,icon,time)values(?,?,?,?,?)";
        Object[] bindArgs = {history.getId(),history.getUrl(),history.getTitle(),history.getIcon(),history.getTime()};
        db.execSQL(sql,bindArgs);
    }

    //根据url删除
    public void DeleteHistory_Url(String url,SQLiteDatabase db){
        String sql = "delete from history where url = " + url;
        db.execSQL(sql);
    }


    //清空所有历史记录
    public void DeleteAllHistory(SQLiteDatabase db){
        String sql = "delete from history";
        db.execSQL(sql);
    }

}
