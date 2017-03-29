package com.app.vik.newsfast.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.vik.newsfast.NewsListActivity;
import com.app.vik.newsfast.R;
import com.app.vik.newsfast.pojo.Source;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ListViewHolder> {

    ArrayList<Source> mSources = new ArrayList<>();
    private Context mContext;

    public NewsListAdapter(Context context){
        mContext = context;
    }

    public void setNewsList(ArrayList<Source> sources){
        mSources = sources;

        notifyItemInserted(sources.size() - 1);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        NewsListAdapter.ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.mNewsListTextView.setText(mSources.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNewsListTextView;

        public ListViewHolder(View itemView) {
            super(itemView);
            mNewsListTextView = (TextView) itemView.findViewById(R.id.list_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, NewsListActivity.class);
            intent.putExtra("source_object", mSources.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }

}
