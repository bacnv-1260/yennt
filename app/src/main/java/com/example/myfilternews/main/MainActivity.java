package com.example.myfilternews.main;

import android.app.SearchManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.example.myfilternews.R;
import com.example.myfilternews.favorite.FavoriteFragment;
import com.example.myfilternews.model.Articles;
import com.example.myfilternews.model.News;
import com.example.myfilternews.save.SaveFragment;
import com.example.myfilternews.service.NewsAPI;
import com.example.myfilternews.service.RetrofitClientInstance;
import com.example.myfilternews.sreen.news.NewsFragment;
import com.example.myfilternews.sreen.news.NewsFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NewsFragment.HandleItemClick {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    private static final String API_KEY = "7257ad280bbe43a5a69295e4a85ea66a";
    private NewsFragmentAdapter mAdapter;
    private List<News> listSearch;
    private static final int PERMISSION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setupPermission();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.item_search);
    }

    private void setupPermission() {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                List<Fragment> fragments = new ArrayList<>();
                fragments.add(new NewsFragment());
                fragments.add(new SaveFragment());
                fragments.add(new FavoriteFragment());
                final SimplePagerAdapter simplePagerAdapter =
                    new SimplePagerAdapter(getSupportFragmentManager(), fragments);
                viewPager.setAdapter(simplePagerAdapter);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                    }

                    @Override
                    public void onPageSelected(int i) {
                        if (i == 1){
                            SaveFragment fragment = (SaveFragment) simplePagerAdapter.getItem(i);
                            fragment.updateListSave();
                        }else if (i == 2){
                            FavoriteFragment favoriteFragment =
                                (FavoriteFragment) simplePagerAdapter.getItem(i);
                            favoriteFragment.updateFavoriteFragment();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {
                    }
                });
                tabLayout.setupWithViewPager(viewPager);
            } else {
                requestPermission();
            }
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store " +
                "images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_search, menu);
        searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                NewsAPI service = RetrofitClientInstance.newInstance().create(NewsAPI.class);
                Call<Articles> getNewsListSearch = service.getNewsListSearch(s, API_KEY);
                getNewsListSearch.enqueue(new Callback<Articles>() {
                    @Override
                    public void onResponse(Call<Articles> call, Response<Articles> response) {
                        listSearch = response.body().getArticles();
                        if(listSearch == null) return;
                        mAdapter = new NewsFragmentAdapter(listSearch, getApplicationContext());
                        RecyclerView recyclerView = findViewById(R.id.recycler_view_news);
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onFailure(Call<Articles> call, Throwable t) {
                        Log.d("yen", "onFailure: " + t.getMessage());
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void initView(){
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
    }

    @Override
    public void gotoWebView(News news) {

    }
}
