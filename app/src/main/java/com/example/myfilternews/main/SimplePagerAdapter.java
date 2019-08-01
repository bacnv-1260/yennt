package com.example.myfilternews.main;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.myfilternews.save.SaveFragment;

import java.util.List;

public class SimplePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public SimplePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
       return this.fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0: fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Tin Tuc";
            case 1:
                return "Đã lưu";
            case 2:
                return "Yêu Thich";
        }
        return null;
    }
}
