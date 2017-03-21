package com.app.vik.newsfast.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.app.vik.newsfast.R;
import com.app.vik.newsfast.data.NewsContract.NewsEntry;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;
    private Intent intent;

    public WidgetDataProvider(Context context, Intent intent){
        mContext = context;
        this.intent = intent;
    }

    private void initCursor() {
        if (mCursor != null) {
            mCursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();

        String[] projection = {
                NewsEntry._ID,
                NewsEntry.COLUMN_NEWS_TITLE,
                NewsEntry.COLUMN_NEWS_DESCRIPTION,
                NewsEntry.COLUMN_NEWS_URL,
                NewsEntry.COLUMN_NEWS_IMAGE_URL
        };

        mCursor = mContext.getContentResolver().query(NewsEntry.CONTENT_URI,
                projection, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        initCursor();
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
    }

    @Override
    public void onDataSetChanged() {
        initCursor();
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        mCursor.moveToPosition(i);
        Log.e("TAG", mCursor.getString(mCursor.getColumnIndex(NewsEntry.COLUMN_NEWS_TITLE)));
        remoteViews.setTextViewText(R.id.widget_text_view, mCursor.getString(mCursor.getColumnIndex(NewsEntry.COLUMN_NEWS_TITLE)));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
