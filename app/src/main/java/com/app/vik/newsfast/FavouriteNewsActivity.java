package com.app.vik.newsfast;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.vik.newsfast.data.NewsContract.NewsEntry;

public class FavouriteNewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 10;

    private RecyclerView mRecyclerView;
    private FavouriteNewsAdapter mAdapter;

    private TextView mEmptyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_news);

        mRecyclerView = (RecyclerView) findViewById(R.id.fav_rv);
        mEmptyText = (TextView) findViewById(R.id.no_data_tv);
        mAdapter = new FavouriteNewsAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, Utility.calculateNoOfColumns(this));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                NewsEntry._ID,
                NewsEntry.COLUMN_NEWS_TITLE,
                NewsEntry.COLUMN_NEWS_DESCRIPTION,
                NewsEntry.COLUMN_NEWS_URL,
                NewsEntry.COLUMN_NEWS_IMAGE_URL
        };
        return new CursorLoader(this, NewsEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() == 0 ){
            mEmptyText.setVisibility(View.VISIBLE);
        }else {
            mEmptyText.setVisibility(View.INVISIBLE);
        }
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
