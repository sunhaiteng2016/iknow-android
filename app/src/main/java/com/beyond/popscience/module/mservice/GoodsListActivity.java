package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.mservice.adapter.GoodsListAdapter;
import com.beyond.popscience.module.mservice.adapter.ServiceSlideAdapter;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;
import com.beyond.popscience.widget.ServiceSortView;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import okhttp3.Call;

/**
 * c2c 商品列表
 * Created by yao.cui on 2017/6/25.
 */

public class GoodsListActivity extends BaseActivity implements ServiceSortView.ISortListener {

    private final int TASK_GET_CAROUSEL = 1401;//获取轮播图
    private final int TASK_GET_GOODS = 1402;//获取商品

    private static final String KEY_SERVICE_CATEGORY = "serviceCategory";

    @BindView(R.id.vp)
    protected AutoViewPager mSlidePager;
    @BindView(R.id.tv_right)
    protected TextView tv_right;

    @BindView(R.id.recycleIndicator)
    protected RecyclingCirclePageIndicator mIndicator;
    @BindView(R.id.tvTitle)
    protected TextView tvSlideTitle;

    @BindView(R.id.gvGoods)
    protected RecyclerView gvGoods;
    @BindView(R.id.sortview)
    protected ServiceSortView sortview;
    @BindView(R.id.ctlHeader)
    protected CollapsingToolbarLayout ctlHeader;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    private ServiceSlideAdapter mImgPageAdapter;
    private List<Carousel> mCarousels;

    private ServiceCategory serviceCategory;
    private int mCurrentPage = 1;
    private boolean isRefresh = true;

    private String byDis = "0";//根据距离暂无效果 0：近的在前 1:远的在前
    private String byTime = "0";//根据时间0:时间早的在前 1：时间晚的在前
    private String byMoney = "0";//根据金额 0:价格低的在前 1：价格高的在前
    private String byAll = "0";//根据金额 0:价格低的在前 1：价格高的在前

    private ServiceGoodsList mGoodsList;
    private GoodsListAdapter mGoodsAdapter;

    @Request
    private ServiceRestUsage mRestUsage;
    private String mprovince, mcity, mcounty;

    public static void startActivity(Context context, ServiceCategory serviceCategory) {
        Intent intent = new Intent(context, GoodsListActivity.class);
        intent.putExtra(KEY_SERVICE_CATEGORY, serviceCategory);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSlidePager.getAdapter() != null) {
            mSlidePager.startAutoScroll();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       getGoods();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSlidePager.getAdapter() != null) {
            mSlidePager.stopAutoScroll();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_list;
    }

    @Override
    public void initUI() {
        super.initUI();
        serviceCategory = (ServiceCategory) getIntent().getSerializableExtra(KEY_SERVICE_CATEGORY);
        Log.e("sht", serviceCategory.toString());
        if (serviceCategory == null) {
            backNoAnim();
            return;
        }
        initGrid();
        getCarousel();

        tv_right.setText(WelcomeActivity.seletedAdress.split("-")[1]);
        sortview.setSortListener(this);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //功能开放的城市
                JSONObject obj = new JSONObject();

                OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/queryOpenArea", obj.toString(), new CallBackUtil.CallBackString() {

                    @Override
                    public void onFailure(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject onj = new JSONObject(response);
                            JSONArray date = onj.getJSONArray("data");
                            String ss = date.toString();
                            String ssss = ss.replace("child", "counties");
                            String s = ssss.replace("id", "areaId");
                            String newjson = "[{\"areaId\": \"130000\",\"areaName\": \"河北省\",\"cities\":" + s + "}]";
                            ArrayList<Province> data = new ArrayList<>();
                            data.addAll(JSON.parseArray(newjson, Province.class));
                            AddressPicker picker = new AddressPicker(GoodsListActivity.this, data);
                            picker.setShadowVisible(true);
                            picker.setHideProvince(true);
                            picker.setTextSize(16);
                            picker.setPadding(10);
                            picker.setSelectedItem("", "台州市", "仙居县");
                            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {

                                @Override
                                public void onAddressPicked(Province province, City city, County county) {
                                    WelcomeActivity.seletedAdress = city.getAreaName() + "-" + county.getAreaName();
                                    //重新的刷新
                                    getCarousel();
                                    refreshLayout.autoRefresh();
                                    tv_right.setText(county.getAreaName());
                                }
                            });
                            picker.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


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
            case TASK_GET_GOODS:
                mGoodsList = (ServiceGoodsList) msg.getObj();
                if (mGoodsList != null) {
                    if (mCurrentPage == 1) {
                        mGoodsAdapter.getDataList().clear();
                        mCurrentPage = 2;
                    } else {
                        mCurrentPage++;
                    }

                    mGoodsAdapter.getDataList().addAll(mGoodsList.getProductList());
                    mGoodsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void initGrid() {
        //推荐服务
        LinearLayoutManager glManager = new LinearLayoutManager(this);
        gvGoods.setLayoutManager(glManager);
        mGoodsAdapter = new GoodsListAdapter(this);
        mGoodsAdapter.setServiceCategory(serviceCategory);
        gvGoods.setAdapter(mGoodsAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                mCurrentPage = 1;
                getGoods();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                mCurrentPage++;
                getGoods();
            }
        });
        getGoods();
    }

    /**
     * 初始化 轮播
     */
    private void initSlide() {

        mImgPageAdapter = new ServiceSlideAdapter(this, true, mCarousels);
        mImgPageAdapter.setmClassId(serviceCategory.getTabId());
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
    public void sortItemClick(ServiceSortView.Type type) {
        if (type == ServiceSortView.Type.TOGETHER) {
            byAll = "0";
            byMoney = "";
            byDis = "";
            byTime = "";
        } else if (type == ServiceSortView.Type.PRICE_DOWN) {
            byAll = "";
            byMoney = "0";
            byDis = "";
            byTime = "";
        } else if (type == ServiceSortView.Type.PRICE_UP) {
            byAll = "";
            byMoney = "1";
            byDis = "";
            byTime = "";
        } else if (type == ServiceSortView.Type.TIME) {
            byAll = "";
            byMoney = "";
            byDis = "";
            byTime = "0";
        } else if (type == ServiceSortView.Type.TIME_UP) {
            byAll = "";
            byMoney = "";
            byDis = "";
            byTime = "1";
        } else if (type == ServiceSortView.Type.DISTANCE) {
            byAll = "";
            byMoney = "";
            byDis = "0";
            byTime = "";
        } else if (type == ServiceSortView.Type.DISTANCE_UP) {
            byAll = "";
            byMoney = "";
            byDis = "1";
            byTime = "";
        }
        refreshLayout.autoRefresh();
    }

    /**
     * 获取轮播图
     */
    private void getCarousel() {
        mRestUsage.getCarouselTwo(TASK_GET_CAROUSEL, serviceCategory.getTabId());
    }

    /**
     * 获取商品列表
     */
    private void getGoods() {
        mRestUsage.getProductByCategoryTwo(TASK_GET_GOODS, serviceCategory.getTabId(), mCurrentPage, byDis, byMoney, byTime, byAll);
    }
    /**
     * 搜索
     */
    @OnClick(R.id.tvSearch)
    public void searchClick() {
        GlobalSearchActivity.startActivityServiceGoods(this);
    }

    @OnClick(R.id.llPublish)
    public void toPublish(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        CheckAuthTool.startActivity(this, PublishGoodsActivity.class, bundle);
      //  PublishGoodsActivity.startActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
