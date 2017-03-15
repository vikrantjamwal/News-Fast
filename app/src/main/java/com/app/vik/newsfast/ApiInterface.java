package com.app.vik.newsfast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("articles")
    Call<Result> getNewsArticles(@Query("source") String name, @Query("apiKey") String apiKey);

}
