package com.example.myfilternews.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfilternews.R;
import com.example.myfilternews.dbhelper.DBHelper;
import com.example.myfilternews.dbhelper.NewsDao;
import com.example.myfilternews.dbhelper.NewsImplement;
import com.example.myfilternews.model.News;
import com.example.myfilternews.save.SaveFragmentAdapter;

import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsDao mDao;
    private List<News> listNewFavorite;
    private FavoriteFragmentAdapter favoriteFragmentAdapter;
    public FavoriteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateFavoriteFragment();
    }

    public void updateFavoriteFragment(){
        recyclerView = getView().findViewById(R.id.recycler_view_save);
        mDao = new NewsImplement(DBHelper.getInstance(getContext()));
        listNewFavorite = mDao.getListNewsFavorite();
        favoriteFragmentAdapter = new FavoriteFragmentAdapter(listNewFavorite, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(favoriteFragmentAdapter);
    }
}
