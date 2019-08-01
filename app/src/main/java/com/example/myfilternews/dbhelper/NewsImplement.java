package com.example.myfilternews.dbhelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myfilternews.model.News;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

public class NewsImplement implements NewsDao {
    private DBHelper mDbHelper;
    List<News> listNews;
    private static NewsImplement sInstance;

    public NewsImplement(DBHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public boolean addNews(News news) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TITLE, news.getTitle());
        contentValues.put(DBHelper.DESCRIPTION, news.getDescription());
        contentValues.put(DBHelper.IMAGE_URL, news.getUrlToImage());
        contentValues.put(DBHelper.URL, news.getUrl());
        db.insert(DBHelper.TABLE_NAME, null, contentValues);
        return true;
    }

    @Override
    public boolean addNewsFavorite(News news) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TITLE, news.getTitle());
        contentValues.put(DBHelper.DESCRIPTION, news.getDescription());
        contentValues.put(DBHelper.IMAGE_URL, news.getUrlToImage());
        contentValues.put(DBHelper.URL, news.getUrl());
        db.insert(DBHelper.TABLE_NAME_FAVORITE, null, contentValues);
        return true;
    }

    @Override
    public News getNews() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cursor = db.rawQuery(DBHelper.TABLE_NAME, selection(), null);
        }
        News news = getNewsFromCursor(cursor);
        return news;
    }

    @Override
    public List<News> getListNews() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME, null);
        listNews = getListNewsFromCursor(cursor);
        return listNews;
    }

    @Override
    public List<News> getListNewsFavorite() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME_FAVORITE, null);
        listNews = getListNewsFromCursor(cursor);
        return listNews;
    }

    @Override
    public boolean deleteNews(String title) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME, DBHelper.TITLE + " = ? ",
            new String[]{title});
        return true;
    }

    @Override
    public boolean deleteNewsFavorite(String title) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME_FAVORITE, DBHelper.TITLE + " = ? ", new String[]{title});
        return true;
    }

    private News getNewsFromCursor(Cursor cursor) {
        News news = new News();
        news.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.TITLE)));
        news.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.DESCRIPTION)));
        news.setUrlToImage(cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_URL)));
        news.setUrl(cursor.getString(cursor.getColumnIndex(DBHelper.URL)));
        return news;
    }

    private List<News> getListNewsFromCursor(Cursor cursor) {
        listNews = new ArrayList<>();
        if (cursor == null) return listNews;
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            News news = getNewsFromCursor(cursor);
            listNews.add(news);
        }
        cursor.close();
        return listNews;
    }

    private String[] selection() {
        return new String[]{
            DBHelper.TITLE,
            DBHelper.DESCRIPTION,
            DBHelper.IMAGE_URL,
            DBHelper.URL,
        };
    }
}
