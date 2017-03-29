package com.app.vik.newsfast;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vik.newsfast.data.NewsContract.NewsEntry;
import com.app.vik.newsfast.pojo.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import static com.app.vik.newsfast.R.id.fab;


public class NewsDetailActivity extends AppCompatActivity {

    private Article mArticle;

    private TextView mTitle, mDescription;
    private ImageView mImageView;
    private FloatingActionButton mFab;

    private ContentValues mValues;

    private boolean isPresent = false;
    private long primaryKey = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        mFab = (FloatingActionButton) findViewById(fab);
        mFab.setOnClickListener(fabListener);

        mTitle = (TextView) findViewById(R.id.tv_heading);
        mDescription = (TextView) findViewById(R.id.tv_description);

        mImageView = (ImageView) findViewById(R.id.backdrop);

        if (getIntent().hasExtra("news_object")) {
            mArticle = getIntent().getParcelableExtra("news_object");
            loadItems(mArticle.getTitle(), mArticle.getDescription(), mArticle.getUrl(), mArticle.getUrlToImage());
        } else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String title = extras.getString("EXTRA_TITLE");
            String description = extras.getString("EXTRA_DESCRIPTION");
            String url = extras.getString("EXTRA_URL");
            String img = extras.getString("EXTRA_IMG_URL");

            loadItems(title, description, url, img);
        }
    }

    private void loadItems(String title, String description, String url, String imgUrl) {

        Glide.with(this).load(imgUrl).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mImageView.setImageBitmap(resource);

                Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        mTitle.setBackgroundColor(palette.getDominantColor(Color.BLACK));
                        mTitle.setTextColor(Color.WHITE);
                    }
                };

                if (resource != null && !resource.isRecycled()) {
                    Palette.from(resource).generate(paletteListener);
                }

            }
        });

        isPresentInDB(url);

        mTitle.setText(title);
        mDescription.setText(description);

        mValues = new ContentValues();
        mValues.put(NewsEntry.COLUMN_NEWS_TITLE, title);
        mValues.put(NewsEntry.COLUMN_NEWS_DESCRIPTION, description);
        mValues.put(NewsEntry.COLUMN_NEWS_URL, url);
        mValues.put(NewsEntry.COLUMN_NEWS_IMAGE_URL, imgUrl);

        mFab.setVisibility(View.VISIBLE);

        if(isPresent){
            mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
        }else {
            mFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int rowDeleted = 0;

            if (isPresent && primaryKey != -1) {
                Uri uri = ContentUris.withAppendedId(NewsEntry.CONTENT_URI, primaryKey);
                rowDeleted = getContentResolver().delete(uri, null, null);
                if (rowDeleted != 0) {
                    Toast.makeText(NewsDetailActivity.this, "News item removed from Favourites", Toast.LENGTH_SHORT).show();
                    isPresent = false;
                    mFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
            } else {
                if (mValues != null) {
                    Uri rowId = getContentResolver().insert(NewsEntry.CONTENT_URI, mValues);
                    if (ContentUris.parseId(rowId) != -1) {
                        Toast.makeText(NewsDetailActivity.this, "News item added to Favourites", Toast.LENGTH_SHORT).show();
                        isPresent = true;
                        primaryKey = ContentUris.parseId(rowId);
                        mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    }
                }
            }
        }
    };

    private void isPresentInDB(String url) {
        String[] projection = {
                NewsEntry._ID,
                NewsEntry.COLUMN_NEWS_URL,
        };
        Cursor cursor = null;
        cursor = getContentResolver().query(NewsEntry.CONTENT_URI, projection, null, null, null);

        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String urlFromDb = cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_URL));
                    if (url.equalsIgnoreCase(urlFromDb)) {
                        primaryKey = cursor.getInt(cursor.getColumnIndex(NewsEntry._ID));
                        isPresent = true;
                        return;
                    }
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
