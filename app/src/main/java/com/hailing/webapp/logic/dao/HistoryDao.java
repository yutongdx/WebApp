package com.hailing.webapp.logic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hailing.webapp.logic.model.History;

import java.util.ArrayList;


public class HistoryDao {


    SQLiteDatabase sqLiteDatabase;
    DataBaseHelper dataBaseHelper;

    //创建dao对象时获取数据库的引用
    public HistoryDao(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    //查询所有
    public ArrayList<History> queryAll() {
        ArrayList<History> list = new ArrayList<History>();
        History history;
        String sql = "select * from history";
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                do{
                    history = new History();
                    history.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    history.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                    history.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    history.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                    history.setTime(cursor.getString(cursor.getColumnIndex("time")));
                    list.add(history);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //根据url和时间找到历史记录的id
    public int queryId(String url, String time) {
        int id = -1;
        String sql = "select * from history where url = '" + url + "' and time = '" + time + "'";
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //添加历史记录
    public void addHistory(History history) {
        String sql = "insert into history(url,title,icon,time)values(?,?,?,?)";
        Object[] bindArgs = {history.getUrl(), history.getTitle(), history.getIcon(), history.getTime()};
        sqLiteDatabase.execSQL(sql, bindArgs);
    }

    //根据id删除
    public void deleteHistoryById(int id) {
        String sql = "delete from history where id = '" + id + "'";
        sqLiteDatabase.execSQL(sql);
    }

    //清空所有历史记录
    public void deleteAllHistory() {
        String sql = "delete from history";
        sqLiteDatabase.execSQL(sql);
    }

}
