package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.module.mservice.adapter.GoodsRecycleAdapter;
import com.beyond.popscience.module.mservice.adapter.ServiceSlideAdapter;
import com.beyond.popscience.widget.GridSpacingItemDecoration;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;
import com.beyond.popscience.widget.ServiceSortView;

import java.util.List;

import butterknife.BindView;

/**
 * c2c 商品列表
 * Created by yao.cui on 2017/6/25.
 */

public class C2CListActivity extends BaseActivity implements ServiceSortView.ISortListener{
    private final int TASK_GET_CAROUSEL = 1401;//获取轮播图
    private final int TASK_GET_GOODS = 1402;//获取商品

    private static final String KEY_CLASS_ID = "class_id";
    private static final String KEY_TITLE = "title";

    @BindView(R.id.vp)
    protected AutoViewPager mSlidePager;
    @BindView(R.id.recycleIndicator)
    protected RecyclingCirclePageIndicator mIndicator;
    @BindView(R.id.tvTitle)
    protected TextView tvSlideTitle;

    @BindView(R.id.gvGoods)
    protected PullToRefreshRecycleView gvGoods;
    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.sortview)
    protected ServiceSortView sortview;
    @BindView(R.id.ctlHeader)
    protected CollapsingToolbarLayout ctlHeader;


    private ServiceSlideAdapter mImgPageAdapter;
    private List<Carousel> mCarousels;

    private String mClassId;
    private int mCurrentPage=1;
    private boolean isRefresh = true;

    private String byDis="0";//根据距离暂无效果 0：近的在前 1:远的在前
    private String byTime="0";//根据时间0:时间早的在前 1：时间晚的在前
    private String byMoney="0";//根据金额 0:价格低的在前 1：价格高的在前
    private String byAll="0";//根据金额 0:价格低的在前 1：价格高的在前

    private ServiceGoodsList mGoodsList;
    GoodsRecycleAdapter mGoodsAdapter;

    @Request
    private ServiceRestUsage mRestUsage;

    public static void startActivity(Context context,String title,String classId){
        Intent intent = new Intent(context,C2CListActivity.class);
        intent.putExtra(KEY_CLASS_ID,classId);
        intent.putExtra(KEY_TITLE,title);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSlidePager.getAdapter()!=null){
            mSlidePager.startAutoScroll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSlidePager.getAdapter()!=null){
            mSlidePager.stopAutoScroll();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_c2c;
    }

    @Override
    public void initUI() {
        super.initUI();
        mClassId = getIntent().getStringExtra(KEY_CLASS_ID);
        tvTitle.setText(getIntent().getStringExtra(KEY_TITLE));
        initGrid();
        getCarousel();
        getGoods();
        sortview.setSortListener(this);

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
                    }else{
                        ctlHeader.setVisibility(View.GONE);
                    }

                }
                break;
            case TASK_GET_GOODS:
                mGoodsList = (ServiceGoodsList) msg.getObj();
                gvGoods.onRefreshComplete();
                if (mGoodsList!= null){
                    if (isRefresh){
                        mGoodsAdapter.getDataList().clear();
                        mCurrentPage = 2;
                    } else {
                        mCurrentPage ++;
                    }

                    mGoodsAdapter.getDataList().addAll(mGoodsList.getProductList());
                    mGoodsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void initGrid(){

        gvGoods.setMode(PullToRefreshBase.Mode.BOTH);
        //推荐服务
        GridLayoutManager glManager = new GridLayoutManager(this,2);
        gvGoods.getRefreshableView().setLayoutManager(glManager);
        gvGoods.getRefreshableView().addItemDecoration(new GridSpacingItemDecoration(2, DensityUtil.dp2px(this,10),false));
        mGoodsAdapter = new GoodsRecycleAdapter(this);
        gvGoods.getRefreshableView().setAdapter(mGoodsAdapter);

        gvGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mCurrentPage = 1;
                isRefresh = true;
                getGoods();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isRefresh = false;
                if (mCurrentPage <=mGoodsList.getTotalpage()){
                    getGoods();
                } else {
                    gvGoods.onRefreshComplete();
                    gvGoods.onLoadMoreCompleteAndNoData();
                }
            }
        });
    }

    /**
     * 初始化 轮播
     */
    private void initSlide(){

        mImgPageAdapter = new ServiceSlideAdapter(this, true,mCarousels);


        mSlidePager.setAdapter(mImgPageAdapter);
        mSlidePager.setInterval(2000);
        mSlidePager.setCurrentItem(mImgPageAdapter.getRealCount()* 1000, false);
        mSlidePager.startAutoScroll();
        mIndicator.setViewPager(mSlidePager);
        mIndicator.setCentered(true);
        //设置轮播图标题
        mSlidePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (tvSlideTitle!= null){
                    tvSlideTitle.setText(mCarousels.get(mImgPageAdapter.getRealPosition(position)).getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

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

    }
    @Override
    public void sortItemClick(ServiceSortView.Type type) {
        if (type== ServiceSortView.Type.TOGETHER){
            byAll = "0";
            byMoney="";
            byDis="";
            byTime="";
        } else if (type == ServiceSortView.Type.PRICE_DOWN){
            byAll = "";
            byMoney="0";
            byDis="";
            byTime="";
        } else if (type == ServiceSortView.Type.PRICE_UP){
            byAll = "";
            byMoney="1";
            byDis="";
            byTime="";
        } else if (type == ServiceSortView.Type.TIME){
            byAll = "";
            byMoney="";
            byDis="";
            byTime="0";
        } else if(type == ServiceSortView.Type.DISTANCE){
            byAll = "";
            byMoney="";
            byDis="0";
            byTime="";
        }

        gvGoods.setTopRefreshing();
    }

    /**
     * 获取轮播图
     */
    private void getCarousel(){
        mRestUsage.getCarousel(TASK_GET_CAROUSEL,mClassId);
    }

    /**
     * 获取商品列表
     */
    private void getGoods(){
        mRestUsage.getProductByCategoryTwo(TASK_GET_GOODS,mClassId,mCurrentPage,byDis,byMoney,byTime,byAll);
    }

}
