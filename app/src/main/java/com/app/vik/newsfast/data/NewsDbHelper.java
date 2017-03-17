package com.app.vik.newsfast.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.vik.newsfast.data.NewsContract.NewsEntry;

public class NewsDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favourite.db";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsEntry.COLUMN_NEWS_TITLE + " TEXT," +
                NewsEntry.COLUMN_NEWS_DESCRIPTION + " TEXT," +
                NewsEntry.COLUMN_NEWS_URL + " TEXT," +
                NewsEntry.COLUMN_NEWS_IMAGE_URL+ " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
