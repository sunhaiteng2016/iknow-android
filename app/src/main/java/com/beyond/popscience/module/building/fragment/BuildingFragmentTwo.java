package com.beyond.popscience.module.building.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;

import android.text.TextUtils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.LinearLayout;

import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;

import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.pojo.BuildingRequest;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.module.building.BuildingActivity;

import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.mservice.adapter.ServiceSlideAdapter;
import com.beyond.popscience.module.square.ClassifyActivity;

import com.beyond.popscience.widget.RecyclingCirclePageIndicator;

import java.util.List;


/**
 * Created by danxiang.feng on 2017/10/13.
 */

public class BuildingFragmentTwo extends BaseFragment {


    private final int TASK_GET_CAROUSEL = 1501;//获取轮播图

    private final int EXTRA_CLASSIFY_ID = 1001;

    private final static String TYPE = "type";
    /**
     *
     */
    private final static String EXTRA_KEY_WORD_KEY = "keyWord";


    protected AutoViewPager mSlidePager;
    protected RecyclingCirclePageIndicator mIndicator;
    protected TextView tvSlideTitle;
    protected CollapsingToolbarLayout ctlHeader;
    protected PullToRefreshRecycleView recycleView;
    protected LinearLayout classifyLayout;
    protected TextView classifyTextView;
    protected TextView priceTxtView;


    @Request
    public BuildingRestUsage mRestUsage;

    private int type = 1;  // 1: 出租出售   2：求租求售

    private ServiceSlideAdapter mImgPageAdapter;

    private List<Carousel> mCarousels;  //轮播response
    private BuildingRequest request = new BuildingRequest();


    public static BuildingFragmentTwo newInstance(int type, String keyWord) {
        BuildingFragmentTwo fragment = new BuildingFragmentTwo();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putString(EXTRA_KEY_WORD_KEY, keyWord);
        fragment.setArguments(bundle);

        return fragment;
    }
    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_building_layouttwo;
    }

    @Override
    public void initUI() {
        super.initUI();
        mSlidePager=(AutoViewPager)getView().findViewById(R.id.vp);
        mIndicator=(RecyclingCirclePageIndicator )getView().findViewById(R.id.recycleIndicator);
        tvSlideTitle=(TextView )getView().findViewById(R.id.tvTitle);
        ctlHeader=(CollapsingToolbarLayout )getView().findViewById(R.id.ctlHeader);
        recycleView=(PullToRefreshRecycleView )getView().findViewById(R.id.recycleView);
        classifyLayout =(LinearLayout)getView().findViewById(R.id.classifyLayout);
        classifyTextView=(TextView )getView().findViewById(R.id.classifyTextView);
        priceTxtView=(TextView )getView().findViewById(R.id.priceTxtView);



        type = getArguments().getInt(TYPE, BuildingActivity.BUILDING);
        requestCarousel();
        request.setAreaName(WelcomeActivity.seletedAdress);
        recycleView.setRefreshing();
    }



    @Override
    public void onResume() {
        super.onResume();
        if (mSlidePager.getAdapter() != null) {
            mSlidePager.startAutoScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSlidePager.getAdapter() != null) {
            mSlidePager.stopAutoScroll();
        }
    }

    /**
     * 获取轮播图
     */
    private void requestCarousel() {
        mRestUsage.getCarouselTwo(TASK_GET_CAROUSEL);
    }


    /**
     * 初始化 轮播
     */
    private void initSlide() {
        mImgPageAdapter = new ServiceSlideAdapter(getActivity(), true, mCarousels);
        mImgPageAdapter.setmClassId(type == BuildingActivity.BUILDING ? "3" : "4");
        mSlidePager.setAdapter(mImgPageAdapter);
        mSlidePager.setInterval(2000);
        mSlidePager.setCurrentItem(mImgPageAdapter.getRealCount() * 1000, false);
        mSlidePager.startAutoScroll();
        mIndicator.setViewPager(mSlidePager);
        mIndicator.setCentered(true);
        //设置轮播图标题
        mSlidePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (tvSlideTitle != null) {
                    tvSlideTitle.setText(mCarousels.get(mImgPageAdapter.getRealPosition(position)).getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 设置轮播图的比例
        mSlidePager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mSlidePager.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewGroup.LayoutParams layoutParams = mSlidePager.getLayoutParams();
                layoutParams.height = (mSlidePager.getWidth() * 3) / 4;
                mSlidePager.setLayoutParams(layoutParams);
                return true;
            }
        });

    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_GET_CAROUSEL:
                if (msg.getIsSuccess() && mCarousels == null) {
                    mCarousels = (List<Carousel>) msg.getObj();
                    if (mCarousels != null && !mCarousels.isEmpty()) {
                        ctlHeader.setVisibility(View.VISIBLE);
                        initSlide();
                    } else {
                        ctlHeader.setVisibility(View.GONE);
                    }
                }
                break;

        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EXTRA_CLASSIFY_ID:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String classify = data.getStringExtra(ClassifyActivity.EXTRA_CLASSIFY_KEY);
                    if (!TextUtils.isEmpty(classify)) {
                        request.setClassify(classify);
                        classifyTextView.setText(classify);
                    } else {
                        request.setClassify(null);
                        classifyTextView.setText("类别");
                    }
                    recycleView.setRefreshing();
                }
                break;
        }
    }
}
