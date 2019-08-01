package com.example.myfilternews.sreen.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfilternews.model.News;
import com.example.myfilternews.service.NewsAPI;
import com.example.myfilternews.R;
import com.example.myfilternews.service.RetrofitClientInstance;
import com.example.myfilternews.model.Articles;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment{
    private List<News> mListNews;
    private static final String API_KEY = "7257ad280bbe43a5a69295e4a85ea66a";
    private NewsFragmentAdapter newsFragmentAdapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressDialog progressDialog;

    public NewsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();
        getDataAPI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public List<News> getDataAPI(){
        mListNews = new ArrayList<>();
        NewsAPI service = RetrofitClientInstance.newInstance().create(NewsAPI.class);
        Call<Articles> getListNews = service.getNewsListTop(API_KEY);
        getListNews.enqueue(new Callback<Articles>() {
            @Override
            public void onResponse(Call<Articles> call, Response<Articles> response) {
                mListNews = response.body().getArticles();
                showOnNewsFragment(mListNews);
            }

            @Override
            public void onFailure(Call<Articles> call, Throwable t) {
                Log.d("failure", "onFailure: "+t.getMessage());
            }
        });
        return mListNews;
    }

    private void showOnNewsFragment(List<News> mListNews) {
        newsFragmentAdapter = new NewsFragmentAdapter(mListNews, getActivity());
        recyclerView = getView().findViewById(R.id.recycler_view_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(newsFragmentAdapter);
        progressDialog.dismiss();
    }
}
