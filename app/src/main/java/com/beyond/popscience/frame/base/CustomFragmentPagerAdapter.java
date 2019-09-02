package com.beyond.popscience.frame.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.library.util.InvokeUtil;

import java.util.List;

/**
 * Created by linjinfa 331710168@qq.com on 2014/12/24.
 */
public class CustomFragmentPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener{

    private final int PAGE_INDEX_OFFSET = 100;
    private List<Fragment> fragmentList;
    private FragmentManager fm;
    private ViewPager viewPager;
    private int currPageIndex = 0;
    private int pageIndexOffset = PAGE_INDEX_OFFSET;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public CustomFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, ViewPager viewPager) {
        this.fragmentList = fragmentList;
        this.fm = fm;
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(this);
    }

    /**
     * 根据
     * @return
     */
    public int getPageIndexByPosition(int position) {
        if(fragmentList.size()==0){
            return 0;
        }
        return position%fragmentList.size();
    }

    /**
     *
     * @return
     */
    public int getPageIndexOffset() {
        return pageIndexOffset;
    }

    @Override
    public int getCount() {
        if(fragmentList.size()<5){
            pageIndexOffset = 0;
            return fragmentList.size();
        }
        pageIndexOffset = PAGE_INDEX_OFFSET;
        return fragmentList.size()*pageIndexOffset;
    }

    /**
     *
     * @param largetPosition
     * @return
     */
    public Fragment getItemFragment(int largetPosition){
        int position = getPageIndexByPosition(largetPosition);
        if(position<0 || position >=fragmentList.size()){
            return null;
        }
        return fragmentList.get(position);
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        position = getPageIndexByPosition(position);
        container.removeView(fragmentList.get(position).getView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = getPageIndexByPosition(position);
        Fragment fragment = fragmentList.get(position);
        if(!fragment.isAdded()){
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
        if(fragment.getView().getParent()==null){
            container.addView(fragment.getView());
        }
        return fragment.getView();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        position = getPageIndexByPosition(position);
        if(onPageChangeListener!=null)
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        fragmentList.get(currPageIndex).onPause();
        position = getPageIndexByPosition(position);
        Fragment nextFragment = fragmentList.get(position);
        if(nextFragment.isAdded()){
            nextFragment.onResume();
            InvokeUtil.getValue(nextFragment, "showByViewPager");
        }
        currPageIndex = position;

        if(onPageChangeListener!=null)
            onPageChangeListener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(onPageChangeListener!=null)
            onPageChangeListener.onPageScrollStateChanged(state);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
