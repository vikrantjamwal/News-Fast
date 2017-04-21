package com.app.vik.newsfast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vik.newsfast.adapters.NewsArticleAdapter;
import com.app.vik.newsfast.network.ApiClient;
import com.app.vik.newsfast.network.ApiInterface;
import com.app.vik.newsfast.pojo.Article;
import com.app.vik.newsfast.pojo.Result;
import com.app.vik.newsfast.pojo.Source;
import com.app.vik.newsfast.utils.ItemOffsetDecoration;
import com.app.vik.newsfast.utils.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NewsArticleAdapter mNewsArticleAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyTextView;

    private ArrayList<Article> mArticles = new ArrayList<>();

    private static final String ARTICLE_KEY = "articles_key";

    private Source mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.news_list_rv);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mEmptyTextView = (TextView) findViewById(R.id.empty_tv);
        mNewsArticleAdapter = new NewsArticleAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, Utility.calculateNoOfColumns(this));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mNewsArticleAdapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        if (getIntent().hasExtra("source_object")) {
            mSource = getIntent().getParcelableExtra("source_object");
            setTitle(mSource.getName());
        }

        if (Utility.isNetworkAvailable(this)) {
            if (savedInstanceState != null) {
                mArticles = savedInstanceState.getParcelableArrayList(ARTICLE_KEY);
                assert mArticles != null;
                if (!mArticles.isEmpty()) {
                    mNewsArticleAdapter.setNewsArticles(mArticles);
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    assert mSource != null;
                    loadArticles(mSource.getId());
                }
            } else {
                assert mSource != null;
                loadArticles(mSource.getId());
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void loadArticles(String id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call = apiInterface.getNewsArticles(id, ApiClient.API_KEY);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Result result = response.body();
                    if (result.getStatus().equals("ok")) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mArticles = result.getArticles();
                        mNewsArticleAdapter.setNewsArticles(mArticles);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(NewsListActivity.this, getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARTICLE_KEY, mArticles);
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
