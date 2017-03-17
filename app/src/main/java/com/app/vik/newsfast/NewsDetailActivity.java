package com.app.vik.newsfast;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


public class NewsDetailActivity extends AppCompatActivity {

    private Article mArticle;

    private TextView mTitle, mDescription;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);

        mTitle = (TextView) findViewById(R.id.tv_heading);
        mDescription = (TextView) findViewById(R.id.tv_description);

        mImageView = (ImageView) findViewById(R.id.backdrop);

        if (getIntent().hasExtra("news_object")) {
            mArticle = getIntent().getParcelableExtra("news_object");
            loadItem(mArticle.getTitle(), mArticle.getDescription(), mArticle.getUrl(), mArticle.getUrlToImage());
        }else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String title = extras.getString("EXTRA_TITLE");
            String description = extras.getString("EXTRA_DESCRIPTION");
            String url = extras.getString("EXTRA_URL");
            String img = extras.getString("EXTRA_IMG_URL");

            loadItem(title, description, url, img);
        }

    }

    private void loadItem(String title, String description, String url, String imgUrl){

        Glide.with(this).load(imgUrl).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mImageView.setImageBitmap(resource);

                Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        mTitle.setBackgroundColor(palette.getDominantColor(0x000000));
                    }
                };

                if (resource != null && !resource.isRecycled()) {
                    Palette.from(resource).generate(paletteListener);
                }

            }
        });

        mTitle.setText(title);
        mDescription.setText(description);
    }

    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String title = mArticle.getTitle();
            String description = mArticle.getDescription();
            String newsUrl = mArticle.getUrl();
            String imgUrl = mArticle.getUrlToImage();

            ContentValues values = new ContentValues();
            values.put(NewsEntry.COLUMN_NEWS_TITLE, title);
            values.put(NewsEntry.COLUMN_NEWS_DESCRIPTION, description);
            values.put(NewsEntry.COLUMN_NEWS_URL, newsUrl);
            values.put(NewsEntry.COLUMN_NEWS_IMAGE_URL, imgUrl);

            Uri rowId = getContentResolver().insert(NewsEntry.CONTENT_URI, values);
            if (ContentUris.parseId(rowId) != -1) {
                Toast.makeText(NewsDetailActivity.this, "News item added to Favourites", Toast.LENGTH_SHORT).show();
//                isPresent = true;
//                primaryKey = ContentUris.parseId(rowId);
//                fab.setImageResource(R.drawable.ic_favorite_white_24dp);
            }
        }
    };

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
