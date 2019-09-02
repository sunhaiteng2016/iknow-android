package com.beyond.popscience.module.building.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.pojo.BuildingRequest;
import com.beyond.popscience.frame.pojo.BuildingResponse;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.building.PublishBuildingActivity;
import com.beyond.popscience.module.building.PublishRentActivity;
import com.beyond.popscience.module.building.adapter.BuildingListAdapter;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.mservice.CheckAuthTool;
import com.beyond.popscience.module.mservice.PublishGoodsActivity;
import com.beyond.popscience.module.mservice.adapter.ServiceSlideAdapter;
import com.beyond.popscience.module.square.ClassifyActivity;
import com.beyond.popscience.widget.GridSpacingItemDecoration;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by danxiang.feng on 2017/10/13.
 */

public class BuildingFragment extends BaseFragment {


    private final int TASK_GET_CAROUSEL = 1501;//获取轮播图
    private final int TASK_GET_LIST = 1502;//

    private final int EXTRA_CLASSIFY_ID = 1001;

    private final static String TYPE = "type";

    private final static String EXTRA_KEY_WORD_KEY = "keyWord";

    private final String TRADE_BUILDING_ITEM[] = {"所有", "出租", "出售"};
    private final String TRADE_RENT_ITEM[] = {"所有", "求租", "求购"};

    @BindView(R.id.vp)
    protected AutoViewPager mSlidePager;
    @BindView(R.id.recycleIndicator)
    protected RecyclingCirclePageIndicator mIndicator;
    @BindView(R.id.tvTitle)
    protected TextView tvSlideTitle;
    @BindView(R.id.ctlHeader)
    protected CollapsingToolbarLayout ctlHeader;
    @BindView(R.id.recycleView)
    protected RecyclerView recycleView;
    @BindView(R.id.popupBgLayout)
    protected LinearLayout popupBgLayout;
    @BindView(R.id.popupLayout)
    protected LinearLayout popupLayout;

    @BindView(R.id.classifyLayout)
    protected LinearLayout classifyLayout;
    @BindView(R.id.classifyTextView)
    protected TextView classifyTextView;
    @BindView(R.id.classifyImageView)
    protected ImageView classifyImageView;
    @BindView(R.id.priceLayout)
    protected LinearLayout priceLayout;
    @BindView(R.id.priceTxtView)
    protected TextView priceTxtView;
    @BindView(R.id.priceImageView)
    protected ImageView priceImageView;
    @BindView(R.id.areaLayout)
    protected LinearLayout areaLayout;
    @BindView(R.id.areaTxtView)
    protected TextView areaTxtView;
    @BindView(R.id.areaImageView)
    protected ImageView areaImageView;
    @BindView(R.id.tradeLayout)
    protected LinearLayout tradeLayout;
    @BindView(R.id.tradeTxtView)
    protected TextView tradeTxtView;
    @BindView(R.id.tradeImageView)
    protected ImageView tradeImageView;
    @BindView(R.id.publishLayout)
    protected RelativeLayout publishLayout;
    /**
     *
     */
    @BindView(R.id.bannerReLay)
    protected LinearLayout bannerReLay;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.publishRentView)
    TextView publishRentView;
    @BindView(R.id.publishBuildingView)
    TextView publishBuildingView;
    Unbinder unbinder;

    @Request
    private BuildingRestUsage mRestUsage;

    private int type = 1;  // 1: 出租出售   2：求租求售
    private String[] tradeItem = null;
    private String keyWord;

    private BuildingListAdapter adapter;
    private ServiceSlideAdapter mImgPageAdapter;

    private int mCurrentPage = 1;
    private List<Carousel> mCarousels;  //轮播response
    private BuildingRequest request = new BuildingRequest();
    private BuildingResponse response;

    public static BuildingFragment newInstance(int type, String keyWord) {
        BuildingFragment fragment = new BuildingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putString(EXTRA_KEY_WORD_KEY, keyWord);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_building_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        type = getArguments().getInt(TYPE, BuildingActivity.BUILDING);
        keyWord = getArguments().getString(EXTRA_KEY_WORD_KEY);
        request.setAreaName(WelcomeActivity.seletedAdress);
        initRecycleView();
        initSearchView();
    }

    /**
     * 初始化搜索状态view
     */
    private void initSearchView() {
        if (TextUtils.isEmpty(keyWord)) {  //非搜索
            publishLayout.setVisibility(View.VISIBLE);
            bannerReLay.setVisibility(View.VISIBLE);
            if (type == BuildingActivity.BUILDING) {
                requestCarousel();
            } else {
                bannerReLay.setVisibility(View.GONE);
            }
        } else {   //搜索
            publishLayout.setVisibility(View.GONE);
            bannerReLay.setVisibility(View.GONE);
            request.setQuery(keyWord);
        }
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
     * 获取商品列表
     */
    private void requestSkillList() {
        if (type == BuildingActivity.BUILDING) {
            mRestUsage.getBuildingList(TASK_GET_LIST, mCurrentPage, request);
        } else {
            mRestUsage.getRentList(TASK_GET_LIST, mCurrentPage, request);
        }
    }

    private void initRecycleView() {
        recycleView.setLayoutManager(new GridLayoutManager(this.getActivity(), 1));
        recycleView.addItemDecoration(new GridSpacingItemDecoration(1, DensityUtil.dp2px(getActivity(), 10), false));
        adapter = new BuildingListAdapter(this);
        adapter.setType(type);
        recycleView.setAdapter(adapter);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                mCurrentPage++;
                requestSkillList();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                mCurrentPage = 1;
                requestSkillList();
            }
        });
        requestSkillList();
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
            case TASK_GET_LIST:
                if (msg.getIsSuccess()) {
                    response = (BuildingResponse) msg.getObj();
                    if (response != null) {
                        if (mCurrentPage == 1) {
                            adapter.getDataList().clear();
                        }
                        mCurrentPage++;
                        if (type == BuildingActivity.BUILDING) {
                            if (response.getBuildingList() != null && response.getBuildingList().size() > 0) {
                                adapter.getDataList().addAll(response.getBuildingList());
                            } else {
                                Toast.makeText(getActivity(), "到底了！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (response.getRentList() != null && response.getRentList().size() > 0) {
                                adapter.getDataList().addAll(response.getRentList());
                            } else {
                                Toast.makeText(getActivity(), "到底了！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @OnClick(R.id.classifyLayout)
    void classifyLayoutClick() {   //类别
        dismissPopupLayout();
        ClassifyActivity.startActivityForResult(this, ClassifyActivity.TYPE_SELECT_BUILDING, request.getClassify(), EXTRA_CLASSIFY_ID);
    }

    @OnClick(R.id.priceLayout)
    void priceLayoutClick() {   //价格
        dismissPopupLayout();
        switchSortType(1);
    }

    @OnClick(R.id.areaLayout)
    void areaLayoutClick() {   //面积
        dismissPopupLayout();
        switchSortType(2);
    }

    @OnClick(R.id.tradeLayout)
    void tradeLayoutClick() {   //更多
        if (popupBgLayout.getVisibility() == View.GONE) {
            initPopupLayout();
            showPopupLayout();
        } else {
            dismissPopupLayout();
        }
    }

    @OnClick(R.id.popupBgLayout)
    void popupBgClick() {
        dismissPopupLayout();
    }

    @OnClick(R.id.publishBuildingView)
    void publishBuildingView() {
        dismissPopupLayout();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        CheckAuthTool.startActivity(getActivity(), PublishBuildingActivity.class, bundle);
        //PublishBuildingActivity.startActivity(getContext(), null);
    }

    @OnClick(R.id.publishRentView)
    void publishRentView() {
        dismissPopupLayout();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        CheckAuthTool.startActivity(getActivity(), PublishBuildingActivity.class, bundle);
       // PublishRentActivity.startActivity(getContext(), null);
    }

    private void switchSortType(int sortType) {  // 1: 价格排序   2：面积排序
        if (sortType == 1) {  //当前点击价格排序
            //重置面积排序
            if (!TextUtils.isEmpty(request.getSquare())) { //说明之前有值
                areaTxtView.setTextColor(getResources().getColor(R.color.grey17));
                arrowDownAnim(areaImageView);
                request.setSquare(null);
            }
            //价格排序	0：小的在前 1:大的在前
            if (TextUtils.isEmpty(request.getPrice())) {
                priceTxtView.setTextColor(getResources().getColor(R.color.blue2));
                request.setPrice("0");
            } else if ("0".equals(request.getPrice())) {
                arrowUpAnim(priceImageView);
                request.setPrice("1");
            } else {
                priceTxtView.setTextColor(getResources().getColor(R.color.grey17));
                arrowDownAnim(priceImageView);
                request.setPrice(null);
            }
        } else if (sortType == 2) {  //当前点击面积排序
            //重置价格排序
            if (!TextUtils.isEmpty(request.getPrice())) { //说明之前有值
                priceTxtView.setTextColor(getResources().getColor(R.color.grey17));
                arrowDownAnim(priceImageView);
                request.setPrice(null);
            }
            //面积排序	0：小的在前 1:大的在前
            if (TextUtils.isEmpty(request.getSquare())) {
                areaTxtView.setTextColor(getResources().getColor(R.color.blue2));
                request.setSquare("0");
            } else if ("0".equals(request.getSquare())) {
                arrowUpAnim(areaImageView);
                request.setSquare("1");
            } else {
                areaTxtView.setTextColor(getResources().getColor(R.color.grey17));
                arrowDownAnim(areaImageView);
                request.setSquare(null);
            }
        }

        refreshLayout.autoRefresh();
    }


    private void initPopupLayout() {
        popupLayout.removeAllViews();
        if (type == BuildingActivity.BUILDING) {
            tradeItem = TRADE_BUILDING_ITEM;
        } else {
            tradeItem = TRADE_RENT_ITEM;
        }

        for (int i = 0; i < tradeItem.length; i++) {
            LinearLayout layout = new LinearLayout(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(30));
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dp2px(15), dp2px(15));
            imageView.setLayoutParams(imageParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(dp2px(10), 0, 0, 0);
            textView.setLayoutParams(textParams);
            textView.setText(tradeItem[i]);
            textView.setTextSize(14);
            if ((TextUtils.isEmpty(request.getTrade()) && i == 0) || String.valueOf(i).equals(request.getTrade())) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.confirm));
                textView.setTextColor(getResources().getColor(R.color.blue2));
            } else {
                imageView.setImageDrawable(null);
                textView.setTextColor(getResources().getColor(R.color.grey9));
            }
            layout.addView(imageView);
            layout.addView(textView);
            layout.setTag(i);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    if (position > 0) {
                        tradeTxtView.setText(tradeItem[position]);
                        request.setTrade(String.valueOf(position));
                    } else {
                        tradeTxtView.setText("更多");
                        request.setTrade(null);
                    }
                    refreshLayout.autoRefresh();
                    dismissPopupLayout();
                }
            });
            popupLayout.addView(layout);
        }
    }


    //显示综合的弹窗
    private void showPopupLayout() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(popupLayout, "translationY", -popupLayout.getMeasuredHeight(), 0),
                ObjectAnimator.ofFloat(popupBgLayout, "alpha", 0, 1),
                ObjectAnimator.ofFloat(tradeImageView, "rotation", 0, 90));
        animatorSet.setDuration(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                popupLayout.setVisibility(View.VISIBLE);
                popupBgLayout.setVisibility(View.VISIBLE);
                tradeTxtView.setTextColor(getResources().getColor(R.color.blue2));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.start();
    }

    /**
     * 隐藏下拉菜单
     */
    private void dismissPopupLayout() {
        if (popupBgLayout.getVisibility() != View.VISIBLE) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(popupLayout, "translationY", 0, -popupLayout
                        .getMeasuredHeight()),
                ObjectAnimator.ofFloat(popupBgLayout, "alpha", 1, 0),
                ObjectAnimator.ofFloat(tradeImageView, "rotation", 90, 0));
        animatorSet.setDuration(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                popupLayout.setVisibility(View.GONE);
                popupBgLayout.setVisibility(View.GONE);
                tradeTxtView.setTextColor(getResources().getColor(R.color.grey17));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.start();
    }

    private void arrowUpAnim(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotation", 0, 180));
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    private void arrowDownAnim(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotation", -180, 0));
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    public int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
                    refreshLayout.autoRefresh();
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
