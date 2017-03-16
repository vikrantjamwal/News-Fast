package com.app.vik.newsfast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class NewsArticleAdapter extends RecyclerView.Adapter<NewsArticleAdapter.ArticleViewHolder>{

    private ArrayList<Article> mArticles = new ArrayList<>();
    private Context mContext;

    public NewsArticleAdapter(Context context){
        mContext = context;
    }

    public void setNewsArticles(ArrayList<Article> articles) {
        this.mArticles = articles;

        //update the adapter to reflect the new set of articles
        notifyItemInserted(articles.size() - 1);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_item, parent, false);
        ArticleViewHolder holder = new ArticleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Glide.with(mContext).load(mArticles.get(position).getUrlToImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.mNewsImage);
        holder.mNewsTextView.setText(mArticles.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mNewsImage;
        TextView mNewsTextView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            mNewsImage = (ImageView) itemView.findViewById(R.id.news_img);
            mNewsTextView = (TextView) itemView.findViewById(R.id.news_heading_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, NewsDetailActivity.class);
            intent.putExtra("news_object", mArticles.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}
