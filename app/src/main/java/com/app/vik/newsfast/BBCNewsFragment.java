package com.app.vik.newsfast;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BBCNewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NewsArticleAdapter mNewsArticleAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyTextView;

    private ArrayList<Article> mArticles = new ArrayList<>();

    private static final String ARTICLE_KEY = "articles_key";

    public BBCNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bbcnews, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.bbc_rv);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
        mEmptyTextView = (TextView) rootView.findViewById(R.id.empty_tv);
        mNewsArticleAdapter = new NewsArticleAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Utility.calculateNoOfColumns(getActivity()));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mNewsArticleAdapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        if(savedInstanceState != null && !savedInstanceState.getParcelableArrayList(ARTICLE_KEY).isEmpty()){
            mArticles = savedInstanceState.getParcelableArrayList(ARTICLE_KEY);
            mNewsArticleAdapter.setNewsArticles(mArticles);
            mProgressBar.setVisibility(View.GONE);
        }else {
            if(Utility.isNetworkAvailable(getActivity())) {
                loadArticles();
            }else {
                mProgressBar.setVisibility(View.GONE);
                mEmptyTextView.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    private void loadArticles(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call = apiInterface.getNewsArticles("bbc-news", "e2d41ec29c9344a786104f5b19ed31ef");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.isSuccessful() && response.code() == 200){
                    Result result = response.body();
                    if(result.getStatus().equals("ok")){
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mArticles = result.getArticles();
                        mNewsArticleAdapter.setNewsArticles(mArticles);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARTICLE_KEY, mArticles);
    }
}
