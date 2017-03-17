package com.app.vik.newsfast.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app.vik.newsfast.data.NewsContract.NewsEntry;

public class NewsProvider extends ContentProvider {

    private static final String LOG_TAG = NewsProvider.class.getSimpleName();
    private NewsDbHelper mDbHelper;

    private static final int NEWS = 1;
    private static final int NEWS_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS, NEWS);

        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS + "/#", NEWS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new NewsDbHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                cursor = database.query(NewsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NEWS_ID:
                selection = NewsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(NewsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                SQLiteDatabase database = mDbHelper.getWritableDatabase();

                long id = database.insert(NewsEntry.TABLE_NAME, null, values);

                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                // Delete all rows that match the selection and selection args
                return deleteNews(uri, selection, selectionArgs);
            case NEWS_ID:
                // Delete a single row given by the ID in the URI
                selection = NewsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return deleteNews(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    private int deleteNews(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted = database.delete(NewsEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
