package com.example.myfilternews.save;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfilternews.R;
import com.example.myfilternews.dbhelper.DBHelper;
import com.example.myfilternews.dbhelper.NewsImplement;
import com.example.myfilternews.model.News;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SaveFragment extends Fragment {
    private SaveFragmentAdapter saveFragmentAdapter;
    private List<News> listNewSave;
    private File myFile;
    private RecyclerView recyclerView;
    private NewsImplement mDao;
    private SaveFragmentAdapter.ItemSaveClickListener listener;

    public SaveFragment() {
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
        readDataFromFolder();
        recyclerView = view.findViewById(R.id.recycler_view_save);
        mDao = new NewsImplement(DBHelper.getInstance(getContext()));
        listNewSave = mDao.getListNews();
        saveFragmentAdapter = new SaveFragmentAdapter(listNewSave, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(saveFragmentAdapter);
    }

    public void updateListSave(){
        recyclerView = getView().findViewById(R.id.recycler_view_save);
        mDao = new NewsImplement(DBHelper.getInstance(getContext()));
        listNewSave = mDao.getListNews();
        saveFragmentAdapter = new SaveFragmentAdapter(listNewSave, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(saveFragmentAdapter);
    }

    private File[] readDataFromFolder(){
        File path =
            getContext().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            try {
                File file = new File(files[i].getPath());
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

}
