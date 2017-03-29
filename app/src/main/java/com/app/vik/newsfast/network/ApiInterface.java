package com.app.vik.newsfast.network;

import com.app.vik.newsfast.pojo.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("articles")
    Call<Result> getNewsArticles(@Query("source") String name, @Query("apiKey") String apiKey);

}
