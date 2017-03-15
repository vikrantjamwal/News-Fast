package com.app.vik.newsfast;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        final TextView headingTextView = (TextView) findViewById(R.id.tv_heading);
        TextView descriptionTextView = (TextView) findViewById(R.id.tv_description);

        if (getIntent().hasExtra("news_object")) {
            Article article = getIntent().getParcelableExtra("news_object");

            final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
            Glide.with(this).load(article.getUrlToImage()).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imageView.setImageBitmap(resource);

                    Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            headingTextView.setBackgroundColor(palette.getDominantColor(0x000000));
                        }
                    };

                    if (resource != null && !resource.isRecycled()) {
                        Palette.from(resource).generate(paletteListener);
                    }

                }
            });

            headingTextView.setText(article.getTitle());
            descriptionTextView.setText(article.getDescription());
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
