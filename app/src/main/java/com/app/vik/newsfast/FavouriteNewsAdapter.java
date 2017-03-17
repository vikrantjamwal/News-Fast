package com.app.vik.newsfast;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.vik.newsfast.data.NewsContract.NewsEntry;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class FavouriteNewsAdapter extends RecyclerView.Adapter<FavouriteNewsAdapter.FavouriteViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FavouriteNewsAdapter(Context context){
        mContext = context;
    }

    @Override
    public FavouriteNewsAdapter.FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_item, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteNewsAdapter.FavouriteViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String imgUrl = mCursor.getString(mCursor.getColumnIndex(NewsEntry.COLUMN_NEWS_IMAGE_URL));
        String title = mCursor.getString(mCursor.getColumnIndex(NewsEntry.COLUMN_NEWS_TITLE));

        Glide.with(mContext).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.mNewsImage);
        holder.mNewsTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;

        notifyDataSetChanged();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mNewsImage;
        TextView mNewsTextView;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            mNewsImage = (ImageView) itemView.findViewById(R.id.news_img);
            mNewsTextView = (TextView) itemView.findViewById(R.id.news_heading_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            Intent intent = new Intent(mContext, NewsDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("EXTRA_TITLE", mCursor.getString(mCursor.getColumnIndex(NewsEntry.COLUMN_NEWS_TITLE)));
            extras.putString("EXTRA_DESCRIPTION", mCursor.getString(mCursor.getColumnIndex(NewsEntry.COLUMN_NEWS_DESCRIPTION)));
            extras.putString("EXTRA_URL", mCursor.getString(mCursor.getColumnIndex(NewsEntry.COLUMN_NEWS_URL)));
            extras.putString("EXTRA_IMG_URL", mCursor.getString(mCursor.getColumnIndex(NewsEntry.COLUMN_NEWS_IMAGE_URL)));
            intent.putExtras(extras);
            mContext.startActivity(intent);
        }
    }
}
