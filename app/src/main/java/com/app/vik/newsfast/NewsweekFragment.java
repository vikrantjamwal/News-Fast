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
import android.widget.Toast;

import com.app.vik.newsfast.adapters.NewsArticleAdapter;
import com.app.vik.newsfast.network.ApiClient;
import com.app.vik.newsfast.network.ApiInterface;
import com.app.vik.newsfast.pojo.Article;
import com.app.vik.newsfast.pojo.Result;
import com.app.vik.newsfast.utils.ItemOffsetDecoration;
import com.app.vik.newsfast.utils.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsweekFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NewsArticleAdapter mNewsArticleAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyTextView;

    private ArrayList<Article> mArticles = new ArrayList<>();

    private static final String ARTICLE_KEY = "articles_key";

    public NewsweekFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newsweek, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.nw_rv);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
        mEmptyTextView = (TextView) rootView.findViewById(R.id.empty_tv);
        mNewsArticleAdapter = new NewsArticleAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Utility.calculateNoOfColumns(getActivity()));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mNewsArticleAdapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        if(Utility.isNetworkAvailable(getActivity())){
            if(savedInstanceState != null){
                mArticles = savedInstanceState.getParcelableArrayList(ARTICLE_KEY);
                assert mArticles != null;
                if(!mArticles.isEmpty()) {
                    mNewsArticleAdapter.setNewsArticles(mArticles);
                    mProgressBar.setVisibility(View.GONE);
                }else {
                    loadArticles();
                }
            }else {
                loadArticles();
            }
        }else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void loadArticles(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call = apiInterface.getNewsArticles("newsweek", ApiClient.API_KEY);
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
                if (isAdded()) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARTICLE_KEY, mArticles);
    }

}
