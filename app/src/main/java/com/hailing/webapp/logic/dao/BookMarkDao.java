package com.hailing.webapp.logic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hailing.webapp.logic.model.BookMark;

import java.util.ArrayList;


public class BookMarkDao {

    SQLiteDatabase sqLiteDatabase;
    DataBaseHelper dataBaseHelper;

    //创建dao对象时获取数据库的引用
    public BookMarkDao(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    //添加书签
    public void addBookmark(BookMark bookMark){
        String sql = "insert into bookmark(icon,title,url) values(?,?,?)";
        Object[] bindArgs = {bookMark.getIcon(), bookMark.getTitle(), bookMark.getUrl()};
        sqLiteDatabase.execSQL(sql, bindArgs);
    }

    //查询所有书签
    public ArrayList<BookMark> queryAll(){
        ArrayList<BookMark> list = new ArrayList<BookMark>();
        BookMark bookMark;
        String sql = "select * from bookmark";

        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            if (cursor.moveToFirst()){
                do{
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String icon = cursor.getString(cursor.getColumnIndex("icon"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    bookMark = new BookMark(id, icon, title, url);
                    list.add(bookMark);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //根据url查询某条书签
    public BookMark queryByUrl(String url){
        String sql = "select * from bookmark where url = ?";
        BookMark bookMark = null;

        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{url})) {
            if (cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                bookMark = new BookMark(id, icon, title, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookMark;
    }

    //删除所有标签
    public void deleteAllBookmark(){
        String sql = "delete from bookmark";
        sqLiteDatabase.execSQL(sql);
    }

    //根据id删除标签
    public void deleteById(int id){
        String sql = "delete from bookmark where id = ?";
        sqLiteDatabase.execSQL(sql, new String[]{String.valueOf(id)});
    }

    //根据id修改书签
    public void updateBookmark(BookMark bookMark){
        String sql = "update bookmark set title = ?, url = ? where id = ?";
        sqLiteDatabase.execSQL(sql, new String[]{bookMark.getTitle(), bookMark.getUrl(), String.valueOf(bookMark.getId())});
    }

}
