package com.example.myfilternews.service;

import com.example.myfilternews.model.Articles;
import com.example.myfilternews.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {
    @GET("/v2/top-headlines?sources=google-news")
    Call<Articles> getNewsListTop(@Query("apiKey") String apiKey);

    @GET("/v2/everything?q=bitcoin")
    Call<List<News>> getNewsListEverything(@Query("apiKey") String apiKey);

    @GET("/v2/everything")
    Call<Articles> getNewsListSearch(@Query("q") String keySearch,
                                     @Query("apiKey") String apiKey);
}
