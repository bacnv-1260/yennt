package com.example.myfilternews.service;

public class ApiUtils {
    public static final String BASE_URL = "https://newsapi.org";
    public static NewsAPI getRetrofitClient(){
        return RetrofitClientInstance.newInstance().create(NewsAPI.class);
    }
}
