package com.hailing.webapp.logic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hailing.webapp.MainActivity;
import com.hailing.webapp.logic.model.BookMark;
import com.hailing.webapp.logic.model.History;
import com.hailing.webapp.util.DataBaseHelper;

import java.util.ArrayList;


public class BookMarkDao {

    //添加书签
    public void addBookmark(SQLiteDatabase db, BookMark bookMark){
        String sql = "insert into bookmark(icon,title,url) values(?,?,?)";
        Object[] bindArgs = {bookMark.getIcon(), bookMark.getTitle(), bookMark.getUrl()};
        db.execSQL(sql, bindArgs);
    }

    //查询所有书签
    public ArrayList<BookMark> queryAll(SQLiteDatabase db){
        ArrayList<BookMark> list = new ArrayList<BookMark>();
        BookMark bookMark = null;
        String sql = "select * from bookmark";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do{
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                bookMark = new BookMark(icon, title, url);
                list.add(bookMark);
            }while (cursor.moveToNext());
        }
        return list;
    }

    //根据url查询某条书签
    public BookMark queryOne(SQLiteDatabase db, String url){
        String sql = "select * from bookmark where url = ?";
        BookMark bookMark = null;
        Cursor cursor = db.rawQuery(sql, new String[]{url});
        if (cursor.moveToFirst()){
            String icon = cursor.getString(cursor.getColumnIndex("icon"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            bookMark = new BookMark(icon, title, url);
        }
        return bookMark;
    }

    //删除所有标签
    public void deleteAllBookmark(SQLiteDatabase db){
        String sql = "delete from bookmark";
        db.execSQL(sql);
    }

    //根据url删除标签
    public void deleteOneBookmark(SQLiteDatabase db, String url){
        String sql = "delete from bookmark where url = ?";
        db.execSQL(sql, new String[]{url});
    }

    //根据url修改书签的title
    public void updateBookmark(SQLiteDatabase db, String url, String newtitle){
        String sql = "update bookmark set title = ? where url = ?";
        db.execSQL(sql, new String[]{newtitle, url});
    }

}
