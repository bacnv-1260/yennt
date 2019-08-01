package com.example.myfilternews.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

     static final String TABLE_NAME = "news";
     static final String TABLE_NAME_FAVORITE = "favorite";
     static final String DATA_BASE_NAME = "mNews";
     static final String ID = "id";
     static final String TITLE = "title";
     static final String DESCRIPTION = "description";
     static final String IMAGE_URL = "image_url";
     static final String URL = "url";

    private static DBHelper sInstance;

    private static final String QUERY_TABLE_DOWNLOAD = "CREATE TABLE " + TABLE_NAME + " ( "
        + TITLE + " TEXT, "
        + DESCRIPTION + " TEXT, "
        + IMAGE_URL + " TEXT, "
        + URL + " TEXT)";

    private static final String CREATE_TABLE_FAVORITE = "CREATE TABLE " + TABLE_NAME_FAVORITE + " ( "
        + TITLE + " TEXT, "
        + DESCRIPTION + " TEXT, "
        + IMAGE_URL + " TEXT, "
        + URL + " TEXT)";


    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context);
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DATA_BASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_TABLE_DOWNLOAD);
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_FAVORITE);
        onCreate(db);
    }
}
