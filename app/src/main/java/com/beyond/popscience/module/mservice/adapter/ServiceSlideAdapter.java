package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beyond.popscience.frame.base.CustomPagerAdapter;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.building.BuildingDetailActivity;
import com.beyond.popscience.module.building.RentDetailActivity;
import com.beyond.popscience.module.job.JobApplyDetailActivity;
import com.beyond.popscience.module.job.JobProvideDetailActivity;
import com.beyond.popscience.module.mservice.GoodsDetailActivity;
import com.beyond.popscience.module.mservice.GoodsDetailV2Activity;
import com.beyond.popscience.module.mservice.GoodsDetailV2Activity2;
import com.beyond.popscience.module.mservice.GoodsNewDetailActivity;
import com.beyond.popscience.module.mservice.WebViewActivity;
import com.beyond.popscience.widget.IRecycling;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务轮播图
 * Created by yao.cui on 2017/6/29.
 */

public class ServiceSlideAdapter extends CustomPagerAdapter implements IRecycling {

    private List<Carousel> mCarousels = new ArrayList<>();
    private boolean mIsLoop = false;
    /**
     * 1:技能 2:任务 3:出租出售 4求租求购 5商品   6:招聘   7:求职
     */
    private String mClassId;

    public ServiceSlideAdapter(Activity context, boolean isLoop, List<Carousel> urls) {
        super(context);
        this.mIsLoop = isLoop;
        this.mCarousels = urls;
        Log.e("", "");
    }

   /* public ServiceSlideAdapter(Fragment context, boolean isLoop, List<Carousel> urls) {
        super(context);
        this.mIsLoop = isLoop;
        this.mCarousels = urls;
        Log.e("","");
    }*/

    public void setmClassId(String mClassId) {
        this.mClassId = mClassId;
    }

    public void setCarousels(List<Carousel> mCarousels) {
        this.mCarousels = mCarousels;
    }

    @Override
    public int getCount() {
        return mIsLoop ? Integer.MAX_VALUE : mCarousels.size();
    }

    @Override
    public int getRealCount() {
        return mCarousels.size();
    }

    @Override
    public int getRealPosition(int position) {
        return position % mCarousels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Carousel carousel = mCarousels.get(getRealPosition(position));

        ImageView imgView = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgView.setLayoutParams(params);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //ImageLoaderUtil.displayImage(context, carousel.getPic(), imgView, getDisplayImageOptions());
        Glide.with(context).load(carousel.getPic()).into(imgView);
        container.addView(imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Carousel carousel1 = (Carousel) mCarousels.get(getRealPosition(position));
                if ("2".equals(carousel1.type)) { //外链
                    WebViewActivity.startActivity(context, carousel1.link, carousel1.getTitle());
                    return;
                }

                if ("5".equals(mClassId)) {//商品
                    //原来的
//                    ServiceGoodsItem serviceGoodsItem = new ServiceGoodsItem();
//                    serviceGoodsItem.setAppGoodsType(mClassId);
//                    serviceGoodsItem.setProductId(carousel1.productId);
//                    GoodsDetailV2Activity.startActivity(context, serviceGoodsItem);

                    //现在的
                    if (!TextUtils.isEmpty(carousel1.uid)) {
                        GoodsNewDetailActivity.startActivity(context, carousel1.uid);
                    } else {
                        GoodsNewDetailActivity.startActivity(context, carousel1.productId);
                    }
                } else if ("1".equals(mClassId) || "2".equals(mClassId)) {// 1. 技能 2.任务

                    ServiceGoodsItem serviceGoodsItem = new ServiceGoodsItem();
                    serviceGoodsItem.setAppGoodsType(mClassId);
                    serviceGoodsItem.setProductId(carousel1.uid);
//                    GoodsDetailV2Activity.startActivity(context, serviceGoodsItem);
                    GoodsDetailV2Activity2.startActivity(context, serviceGoodsItem);
//                    if (!TextUtils.isEmpty(carousel1.uid)) {
//                        GoodsNewDetailActivity.startActivity(context, carousel1.uid);
//                    }else {
//                        GoodsNewDetailActivity.startActivity(context, carousel1.productId);
//                    }
                } else if ("3".equals(mClassId)) { //出租出售
//                    BuildingDetailActivity.startActivity(context, carousel1.uid);
                    if (!TextUtils.isEmpty(carousel1.uid)) {
                        GoodsNewDetailActivity.startActivity(context, carousel1.uid);
                    } else {
                        GoodsNewDetailActivity.startActivity(context, carousel1.productId);
                    }
                } else if ("4".equals(mClassId)) { //求租求购
//                    RentDetailActivity.startActivity(context, carousel1.uid);
                    if (!TextUtils.isEmpty(carousel1.uid)) {
                        GoodsNewDetailActivity.startActivity(context, carousel1.uid);
                    } else {
                        GoodsNewDetailActivity.startActivity(context, carousel1.productId);
                    }
                } else if ("6".equals(mClassId)) { //招聘
                    JobProvideDetailActivity.startActivity(context, carousel1.uid, "1");

                } else if ("7".equals(mClassId)) { //求职
                    JobApplyDetailActivity.startActivty(context, carousel1.uid, "1");
                } else {
//                    GoodsDetailActivity.startActivity(context, carousel1.getProductId());
                    if (!TextUtils.isEmpty(carousel1.uid)) {
                        GoodsNewDetailActivity.startActivity(context, carousel1.uid);
                    } else {
                        GoodsNewDetailActivity.startActivity(context, carousel1.productId);
                    }
                }
            }
        });
        return imgView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
