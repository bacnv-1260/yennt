package com.example.myfilternews.dbhelper;

import com.example.myfilternews.model.News;

import java.util.List;

public interface NewsDao {
    boolean addNews(News news);
    News getNews();
    List<News> getListNews();
}
