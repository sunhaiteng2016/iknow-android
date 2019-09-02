package com.beyond.popscience.module.social.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.beyond.popscience.frame.base.CustomFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linjinfa on 2017/6/23.
 * email 331710168@qq.com
 */
public class SocialCircleContentFragmentPagerAdapter extends CustomFragmentPagerAdapter {

    /**
     *
     */
    private List<String> titleList = new ArrayList<>();

    public SocialCircleContentFragmentPagerAdapter(FragmentManager fm, List<String> titleList, List<Fragment> fragmentList, ViewPager viewPager) {
        super(fm, fragmentList, viewPager);
        this.titleList = titleList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(titleList!=null && position>=0 && position<titleList.size()){
            return titleList.get(position);
        }
        return super.getPageTitle(position);
    }

}
