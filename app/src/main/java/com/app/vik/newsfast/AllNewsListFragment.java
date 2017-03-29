package com.app.vik.newsfast;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.vik.newsfast.adapters.NewsListAdapter;
import com.app.vik.newsfast.pojo.Source;
import com.app.vik.newsfast.utils.Utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class AllNewsListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NewsListAdapter mNewsListAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyTextView;

    ArrayList<Source> mSources = new ArrayList<>();

    private static final String LIST_KEY = "list_key";

    public AllNewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_news_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_news_rv);
        mEmptyTextView = (TextView) rootView.findViewById(R.id.empty_tv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
        mNewsListAdapter = new NewsListAdapter(getActivity());
        mRecyclerView.setAdapter(mNewsListAdapter);

        if (Utility.isNetworkAvailable(getActivity())) {
            if (savedInstanceState != null) {
                mSources = savedInstanceState.getParcelableArrayList(LIST_KEY);
                assert mSources != null;
                if(!mSources.isEmpty()) {
                    mNewsListAdapter.setNewsList(mSources);
                    mProgressBar.setVisibility(View.GONE);
                }else {
                    new FetchNewsSources().execute();
                }
            } else {
                new FetchNewsSources().execute();
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_KEY, mSources);
    }

    public class FetchNewsSources extends AsyncTask<Void, Void, ArrayList<Source>> {

        @Override
        protected ArrayList<Source> doInBackground(Void... voids) {

            URL url = null;
            try {
                url = new URL("https://newsapi.org/v1/sources?language=en");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String jsonResponse = Utility.getResponseFromHttpUrl(url);
                return Utility.parseSources(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Source> sources) {
            if (sources != null && !sources.isEmpty()) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mSources = sources;
                mNewsListAdapter.setNewsList(mSources);
            }
        }
    }

}
