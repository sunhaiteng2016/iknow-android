package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyond.popscience.frame.base.CustomPagerAdapter;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.widget.IRecycling;

import java.util.List;

/**
 * 商品详情 轮播
 * Created by yao.cui on 2017/6/29.
 */

public class GoodsDetailSlideAdapter extends CustomPagerAdapter implements IRecycling {
    private String[] mCarousels;
    private boolean mIsLoop = false;

    public GoodsDetailSlideAdapter(Activity context, boolean isLoop, String[] urls) {
        super(context);
        this.mIsLoop = isLoop;
        this.mCarousels = urls;
    }

    public GoodsDetailSlideAdapter(Fragment context, boolean isLoop, String[] urls) {
        super(context);
        this.mIsLoop = isLoop;
        this.mCarousels = urls;
    }

    public void setmCarousels(String[] mCarousels) {
        this.mCarousels = mCarousels;
    }

    @Override
    public int getCount() {
        return mIsLoop ? Integer.MAX_VALUE : mCarousels.length;
    }

    @Override
    public int getRealCount() {
        return mCarousels.length;
    }

    @Override
    public int getRealPosition(int position) {
        return position % mCarousels.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String carousel = mCarousels[getRealPosition(position)];
        ImageView imgView = new ImageView(context);
        imgView.setTag(carousel);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgView.setLayoutParams(params);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaderUtil.displayImage(context,carousel,imgView,getDisplayImageOptions());
        container.addView(imgView);

        return imgView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
