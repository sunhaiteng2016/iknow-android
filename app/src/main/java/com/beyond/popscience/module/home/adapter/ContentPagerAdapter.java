package com.beyond.popscience.module.home.adapter;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ContentPagerAdapter extends FragmentPagerAdapter {

    public  List<Fragment> list;
    public  List<String> title;
    public ContentPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> title) {
        super(fm);
        this.list=list;
        this.title=title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
