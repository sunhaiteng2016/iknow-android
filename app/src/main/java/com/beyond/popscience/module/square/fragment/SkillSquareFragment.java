package com.beyond.popscience.module.square.fragment;

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
import android.util.Log;
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
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.pojo.AuthResult;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.SkillRequest;
import com.beyond.popscience.frame.pojo.SkillResponse;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.mservice.CheckAuthTool;
import com.beyond.popscience.module.mservice.adapter.ServiceSlideAdapter;
import com.beyond.popscience.module.square.ClassifyActivity;
import com.beyond.popscience.module.square.SkillPublishActivity;
import com.beyond.popscience.module.square.SquareActivity;
import com.beyond.popscience.module.square.adapter.SquareListAdapter;
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
 * Created by danxiang.feng on 2017/10/11.
 */

public class SkillSquareFragment extends BaseFragment {

    private final int TASK_GET_CAROUSEL = 1501;//获取轮播图
    private final int TASK_GET_SKILL_LIST = 1502;//获取技能列表

    private final int EXTRA_SKILL_CLASSIFY_ID = 1001;

    private final static String SQUARE_TYPE = "square_type";
    /**
     *
     */
    private final static String EXTRA_KEY_WORD_KEY = "keyWord";

    private final String sortItem[][] = {
            {"综合", "价格从高到低", "价格从低到高"},
            null,
            null,
            {"所有", "个人", "企业"}
    };

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
    @BindView(R.id.allLayout)
    protected LinearLayout allLayout;
    @BindView(R.id.allTextView)
    protected TextView allTextView;
    @BindView(R.id.allImageView)
    protected ImageView allImageView;
    @BindView(R.id.distanceLayout)
    protected LinearLayout distanceLayout;
    @BindView(R.id.distanceTxtView)
    protected TextView distanceTxtView;
    @BindView(R.id.classifyLayout)
    protected LinearLayout classifyLayout;
    @BindView(R.id.classifyTxtView)
    protected TextView classifyTxtView;
    @BindView(R.id.typeLayout)
    protected LinearLayout typeLayout;
    @BindView(R.id.typeTxtView)
    protected TextView typeTxtView;
    @BindView(R.id.typeImageView)
    protected ImageView typeImageView;
    @BindView(R.id.publishLayout)
    protected RelativeLayout publishLayout;
    @BindView(R.id.bannerReLay)
    protected LinearLayout bannerReLay;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.publishSkillView)
    TextView publishSkillView;
    @BindView(R.id.publishTaskView)
    TextView publishTaskView;
    Unbinder unbinder;

    @Request
    private SquareRestUsage mRestUsage;

    private int type = 1;  // 1: 技能广场   2：任务广场
    /**
     *
     */
    private String keyWord;

    private SquareListAdapter adapter;
    private ServiceSlideAdapter mImgPageAdapter;

    private int mCurrentPage = 1;
    private List<Carousel> mCarousels;  //轮播response
    private SkillRequest skillRequest = new SkillRequest();
    private SkillResponse skillResponse;

    //当前选中的点
    private int currSelectIndex = -1;
    private Runnable delayRunnable;


    public static SkillSquareFragment newInstance(int type, String keyWord) {
        SkillSquareFragment fragment = new SkillSquareFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SQUARE_TYPE, type);
        bundle.putString(EXTRA_KEY_WORD_KEY, keyWord);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_skill_square_layout;
    }

    @Override
    public void initUI() {
        super.initUI();

        if (BeyondApplication.getInstance().getLocation() != null) {
            String lon = String.valueOf(BeyondApplication.getInstance().getLocation().longitude);
            String lat = String.valueOf(BeyondApplication.getInstance().getLocation().latitude);

            skillRequest.setLat(lat);
            skillRequest.setLon(lon);
        }
        skillRequest.setDistance("0");
        skillRequest.setAreaName(WelcomeActivity.seletedAdress);

        type = getArguments().getInt(SQUARE_TYPE, SquareActivity.SKILL_TYPE);
        keyWord = getArguments().getString(EXTRA_KEY_WORD_KEY);
        requestSkillList();
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
            requestCarousel();
        } else {   //搜索
            publishLayout.setVisibility(View.GONE);
            bannerReLay.setVisibility(View.GONE);

            skillRequest.setQuery(keyWord);
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
        mRestUsage.getSkillCarouselTwo(TASK_GET_CAROUSEL, String.valueOf(type));
    }

    /**
     * 获取商品列表
     */
    private void requestSkillList() {
        if (type == SquareActivity.SKILL_TYPE) {
            mRestUsage.getSkillList(TASK_GET_SKILL_LIST, mCurrentPage, skillRequest);
        } else {
            mRestUsage.getTaskList(TASK_GET_SKILL_LIST, mCurrentPage, skillRequest);
        }
    }

    private void initRecycleView() {
        recycleView.setLayoutManager(new GridLayoutManager(this.getActivity(), 1));
        recycleView.addItemDecoration(new GridSpacingItemDecoration(1, DensityUtil.dp2px(getActivity(), 10), false));
        adapter = new SquareListAdapter(this);
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

    }

    /**
     * 初始化 轮播
     */
    private void initSlide() {
        mImgPageAdapter = new ServiceSlideAdapter(getActivity(), true, mCarousels);
        mImgPageAdapter.setmClassId(String.valueOf(type));
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
            case TASK_GET_SKILL_LIST:
                Log.e("=====技能====", "" + msg.getObj());
                if (msg.getIsSuccess()) {
                    skillResponse = (SkillResponse) msg.getObj();
                    Log.e("=====技能====", "" + skillResponse.toString());
                    if (skillResponse != null) {
                        if (mCurrentPage == 1) {
                            adapter.getDataList().clear();
                        }
                        mCurrentPage++;
                        if (type == SquareActivity.SKILL_TYPE) {
                            if (skillResponse.getSkillList() != null && skillResponse.getSkillList().size() > 0) {
                                adapter.getDataList().addAll(skillResponse.getSkillList());
                            }else{
                                Toast.makeText(getActivity(),"到底了！",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (skillResponse.getTaskList() != null && skillResponse.getTaskList().size() > 0) {
                                adapter.getDataList().addAll(skillResponse.getTaskList());
                            }else{
                                Toast.makeText(getActivity(),"到底了！",Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @OnClick(R.id.allLayout)
    void allClick() {   //综合
        skillRequest.setDistance("0");
        distanceTxtView.setTextColor(getResources().getColor(R.color.grey17));
        switchPopup(0, allLayout);
    }

    @OnClick(R.id.distanceLayout)
    void distanceClick() {   //距离
        dismissAllPopupLayout(currSelectIndex);
        allTextView.setText(sortItem[0][0]);
        skillRequest.setAll(null);
        if ("0".equals(skillRequest.getDistance())) {
            skillRequest.setDistance("1");
            distanceTxtView.setTextColor(getResources().getColor(R.color.blue2));
        } else {
            skillRequest.setDistance("0");
            distanceTxtView.setTextColor(getResources().getColor(R.color.grey17));
        }

        refreshLayout.autoRefresh();
    }

    @OnClick(R.id.classifyLayout)
    void classifyClick() {   //类别
        dismissAllPopupLayout(currSelectIndex);
        ClassifyActivity.startActivityForResult(this, ClassifyActivity.TYPE_SELECT_SKILL, skillRequest.getClassify(), EXTRA_SKILL_CLASSIFY_ID);
    }

    @OnClick(R.id.typeLayout)
    void typeClick() {   //筛选
        switchPopup(3, typeLayout);
    }

    @OnClick(R.id.popupBgLayout)
    void popupBgClick() {
        dismissAllPopupLayout(currSelectIndex);
    }

    @OnClick(R.id.publishSkillView)
    void publishSkillView() {
        dismissAllPopupLayout(currSelectIndex);
        Bundle bundle = new Bundle();
        bundle.putInt("type", SquareActivity.SKILL_TYPE);
        CheckAuthTool.startActivity(getContext(), SkillPublishActivity.class, bundle);
    }

    @OnClick(R.id.publishTaskView)
    void publishTaskView() {
        dismissAllPopupLayout(currSelectIndex);
        Bundle bundle = new Bundle();
        bundle.putInt("type", SquareActivity.SKILL_TYPE);
        CheckAuthTool.startActivity(getContext(), SkillPublishActivity.class, bundle);
       /* AuthResult authResult = new AuthResult();
        authResult.setPeople("1");
        authResult.setCompany("1");
        SkillPublishActivity.startActivity(getContext(), SquareActivity.TASK_TYPE, authResult);*/
    }
    private void switchPopup(final int index, final LinearLayout layout) {
        if (popupBgLayout.getVisibility() == View.GONE) {
            initPopupLayout(index);
            showAllPopupLayout(index);
        } else {
            if (index != currSelectIndex) {
                dismissAllPopupLayout(currSelectIndex);
                if (delayRunnable == null) {
                    delayRunnable = new Runnable() {
                        @Override
                        public void run() {
                            initPopupLayout(index);
                            showAllPopupLayout(index);
                            if (delayRunnable != null) {
                                layout.removeCallbacks(delayRunnable);
                                delayRunnable = null;
                            }
                        }
                    };
                }
                layout.postDelayed(delayRunnable, 400);
            } else {
                dismissAllPopupLayout(currSelectIndex);
            }
        }

    }

    private void initPopupLayout(final int index) {
        popupLayout.removeAllViews();

        for (int i = 0; i < sortItem[index].length; i++) {
            LinearLayout layout = new LinearLayout(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(30));
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            if (index == 0) {
                layout.setGravity(Gravity.CENTER_VERTICAL);
                layout.setPadding(dp2px(13), 0, 0, 0);
            } else {
                layout.setGravity(Gravity.CENTER);
            }

            ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dp2px(15), dp2px(15));
            imageView.setLayoutParams(imageParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(dp2px(10), 0, 0, 0);
            textView.setLayoutParams(textParams);
            textView.setText(sortItem[index][i]);
            textView.setTextSize(14);

            boolean isSelect = false;
            if (index == 0) {
                if ((TextUtils.isEmpty(skillRequest.getAll()) && i == 0) ||
                        ("0".equals(skillRequest.getAll()) && i == 2) ||
                        ("1".equals(skillRequest.getAll()) && i == 1)) {
                    isSelect = true;
                }
            } else if (index == 3) {
                if ((TextUtils.isEmpty(skillRequest.getType()) && i == 0) ||
                        (String.valueOf(i).equals(skillRequest.getType()))) {
                    isSelect = true;
                }
            }

            if (isSelect) {
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
                    if (index == 0) {
                        switch (position) {
                            case 1:
                                skillRequest.setAll("1");
                                break;
                            case 2:
                                skillRequest.setAll("0");
                                break;
                            case 0:
                            default:
                                skillRequest.setAll(null);
                                break;
                        }
                        allTextView.setText(sortItem[index][position]);
                    } else if (index == 3) {
                        switch (position) {
                            case 1:
                            case 2:
                                skillRequest.setType(String.valueOf(position));
                                break;
                            case 0:
                            default:
                                skillRequest.setType(null);
                                break;
                        }
                        typeTxtView.setText(sortItem[index][position]);
                    }
                    dismissAllPopupLayout(currSelectIndex);
                }
            });
            popupLayout.addView(layout);
        }
    }


    //显示综合的弹窗
    private void showAllPopupLayout(int index) {
        currSelectIndex = index;
        if (index == 0) {
            allTextView.setTextColor(getResources().getColor(R.color.blue2));
            typeTxtView.setTextColor(getResources().getColor(R.color.grey17));
        } else if (index == 3) {
            typeTxtView.setTextColor(getResources().getColor(R.color.blue2));
            allTextView.setTextColor(getResources().getColor(R.color.grey17));
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(popupLayout, "translationY", -popupLayout.getMeasuredHeight(), 0),
                ObjectAnimator.ofFloat(popupBgLayout, "alpha", 0, 1),
                ObjectAnimator.ofFloat(index == 0 ? allImageView : typeImageView, "rotation", 0, 180));
        animatorSet.setDuration(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                popupLayout.setVisibility(View.VISIBLE);
                popupBgLayout.setVisibility(View.VISIBLE);

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
    private void dismissAllPopupLayout(int index) {
        currSelectIndex = -1;
        allTextView.setTextColor(getResources().getColor(R.color.grey17));
        typeTxtView.setTextColor(getResources().getColor(R.color.grey17));
        if (popupBgLayout.getVisibility() == View.GONE || index == -1) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(popupLayout, "translationY", 0, -popupLayout
                        .getMeasuredHeight()),
                ObjectAnimator.ofFloat(popupBgLayout, "alpha", 1, 0),
                ObjectAnimator.ofFloat(index == 0 ? allImageView : typeImageView, "rotation", -180, 0));
        animatorSet.setDuration(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                popupLayout.setVisibility(View.GONE);
                popupBgLayout.setVisibility(View.GONE);
                refreshLayout.autoRefresh();

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

    public int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EXTRA_SKILL_CLASSIFY_ID:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String classify = data.getStringExtra(ClassifyActivity.EXTRA_CLASSIFY_KEY);
                    if (!TextUtils.isEmpty(classify)) {
                        skillRequest.setClassify(classify);
                        classifyTxtView.setText(classify);
                    } else {
                        skillRequest.setClassify(null);
                        classifyTxtView.setText("类别");
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
