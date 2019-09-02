package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.PhoneUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.module.mservice.adapter.GoodsDetailSlideAdapter;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 商品详情
 *
 * Created by yao.cui on 2017/6/24.
 */

public class GoodsDetailActivity extends BaseActivity {
    private static final int TASK_GET_GOODS_DETAIL = 1402;//获取轮播图
    private static final String KEY_PRODUCT_ID = "product_id";

    @BindView(R.id.vp)
    protected AutoViewPager mSlidePager;
    @BindView(R.id.recycleIndicator)
    protected RecyclingCirclePageIndicator mIndicator;

    @BindView(R.id.tvGoodsTitle)
    protected TextView tvGoodsTitle;
    @BindView(R.id.tvMoney)
    protected TextView tvMoney;
    @BindView(R.id.tvPublishTime)
    protected TextView tvTime;
    @BindView(R.id.tvAddress)
    protected TextView tvAddress;
    @BindView(R.id.tvLookMore)
    protected TextView tvLookMore;
    @BindView(R.id.cvHeader)
    protected CircleImageView cvHeader;
    @BindView(R.id.tvName)
    protected TextView tvName;
    @BindView(R.id.ivGoods)
    protected ImageView ivGoods;
    @BindView(R.id.tvDes)
    protected TextView tvDes;
    @BindView(R.id.llShotMsg)
    protected LinearLayout llShotMsg;
    @BindView(R.id.llCall)
    protected LinearLayout llCall;
    @BindView(R.id.tv_title)
    protected TextView tv_title;//activity 标题
    @BindView(R.id.tv_right)
    protected TextView tv_right;

    @BindView(R.id.emptyReLay)
    protected RelativeLayout emptyReLay;

    private GoodsDetail goodsDetail;

    @Request
    private ServiceRestUsage mRestUsage;
    private String mProductId;
    private GoodsDetailSlideAdapter slideAdapter;

    public static void startActivity(Context context, String productId){
        Intent intent = new Intent(context,GoodsDetailActivity.class);
        intent.putExtra(KEY_PRODUCT_ID,productId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void initUI() {
        super.initUI();
        mProductId = getIntent().getStringExtra(KEY_PRODUCT_ID);
        tv_title.setText("商品详情");

        initViewHeight();
    }

    /**
     * 初始化相关View的高度
     */
    private void initViewHeight(){
        int imgHeight = Util.getImageHeight5V3(DensityUtil.getScreenWidth(this));

        mSlidePager.getLayoutParams().height = imgHeight;
        ivGoods.getLayoutParams().height = imgHeight;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGoodsDetail();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSlidePager.getAdapter()!=null){
            mSlidePager.stopAutoScroll();
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_GET_GOODS_DETAIL:

                if (msg.getIsSuccess()){
                    goodsDetail = (GoodsDetail) msg.getObj();
                    if (goodsDetail!= null){
                        emptyReLay.setVisibility(View.GONE);

                        goodsDetail.productId = mProductId;
                        initDetail();
                    }else{
                        emptyReLay.setVisibility(View.VISIBLE);
                    }
                }else{
                    emptyReLay.setVisibility(View.VISIBLE);
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 填充界面数据
     */
    private void initDetail(){
        tvTime.setText(goodsDetail.createTime);
        tvGoodsTitle.setText(goodsDetail.title);
        tvAddress.setText(goodsDetail.address);
        tvDes.setText(goodsDetail.introduce);
        tvName.setText(goodsDetail.realName);
        tvMoney.setText("¥"+goodsDetail.price);
        tv_right.setText("编辑");

        //如果是自己的商品则展示编辑按钮
        if (TextUtils.equals(goodsDetail.userId, UserInfoUtil.getInstance().getUserInfo().getUserId())){
            tv_right.setVisibility(View.VISIBLE);
        }
        ImageLoaderUtil.displayImage(this, goodsDetail.avatar, cvHeader, getAvatarDisplayImageOptions());

        if (TextUtils.isEmpty(goodsDetail.mobile)){
            llCall.setVisibility(View.GONE);
            llShotMsg.setVisibility(View.GONE);
        }

        String[] slide = goodsDetail.getValues();
        //展示轮播 和 商品描述图片
        if (slide!= null && slide.length>0){
            if(slideAdapter == null){
                slideAdapter = new GoodsDetailSlideAdapter(this, true, slide);
                mSlidePager.setAdapter(slideAdapter);

                mSlidePager.setInterval(2000);
                mSlidePager.setCurrentItem(slideAdapter.getRealCount()* 1000, false);

                mIndicator.setViewPager(mSlidePager);
                mIndicator.setCentered(true);
            }else{
                slideAdapter.setmCarousels(slide);
                slideAdapter.notifyDataSetChanged();
            }
            mSlidePager.startAutoScroll();
            // 设置轮播图的比例
            mSlidePager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mSlidePager.getViewTreeObserver().removeOnPreDrawListener(this);
                    ViewGroup.LayoutParams layoutParams = mSlidePager.getLayoutParams();
                    layoutParams.height = (mSlidePager.getWidth() * 3) /4;
                    mSlidePager.setLayoutParams(layoutParams);
                    return true;
                }
            });

            ImageLoaderUtil.displayImage(this, Util.getUrlProportion5V3(slide[slide.length-1]), ivGoods, getDisplayImageOptions());
        }
    }

    @OnClick(R.id.llShotMsg)
    void toShortMsg(View view){
        //打开短信
        if (goodsDetail!= null){
            PhoneUtil.sendSms(this,goodsDetail.mobile,"");
        }

    }

    @OnClick(R.id.llCall)
    void toPhoneCall(View view){
        //打电话
        if (goodsDetail!= null){
            PhoneUtil.callPhoneDial(this,goodsDetail.mobile);
        }

    }
    @OnClick(R.id.tvLookMore)
    void toMore(View view){
        if (goodsDetail!= null){
            PublishListActivity.startActivity(this,goodsDetail.realName,goodsDetail.userId);
        }
    }

    @OnClick(R.id.tv_right)
    void toEditGoods(View view){
        PublishGoodsActivity.startActivityEdit(this,goodsDetail);
    }

    @OnClick(R.id.emptyReLay)
    public void reloadClick(View view){
        showProgressDialog();
        getGoodsDetail();
    }

    /**
     * 获取商品详情
     */
    private void getGoodsDetail(){
        mRestUsage.getGoodsDetail(TASK_GET_GOODS_DETAIL,mProductId);
    }

}
