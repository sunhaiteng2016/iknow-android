package com.beyond.popscience.module.job.fragment;

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
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.JobRestUsage;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.JobRequest;
import com.beyond.popscience.frame.pojo.JobResponse;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.frame.view.SelectCultureDialog;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.job.PublishJobApplyActivity;
import com.beyond.popscience.module.job.PublishJobProvideActivity;
import com.beyond.popscience.module.job.adapter.JobListAdapter;
import com.beyond.popscience.module.mservice.CheckAuthTool;
import com.beyond.popscience.module.mservice.adapter.ServiceSlideAdapter;
import com.beyond.popscience.module.square.ClassifyActivity;
import com.beyond.popscience.module.town.TownCategoryActivity;
import com.beyond.popscience.widget.GridSpacingItemDecoration;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;
import com.beyond.popscience.widget.wheelview.WheelMenuInfo;
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

public class JobFragment extends BaseFragment {


    private final int TASK_GET_CAROUSEL = 1501;//获取轮播图
    private final int TASK_GET_LIST = 1502;//

    private final int CODE_SELECT_ADDRES = 101;  //选择地点
    private final int CODE_SELECT_POSITION = 102; //选择职位


    private final static String TYPE = "type";
    private final static String EXTRA_KEY_WORD_KEY = "keyWord";

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

    @BindView(R.id.addressTextView)
    protected TextView addressTextView;
    @BindView(R.id.addressImageView)
    protected ImageView addressImageView;
    @BindView(R.id.positionTxtView)
    protected TextView positionTxtView;
    @BindView(R.id.positionImageView)
    protected ImageView positionImageView;
    @BindView(R.id.educationTxtView)
    protected TextView educationTxtView;
    @BindView(R.id.educationImageView)
    protected ImageView educationImageView;
    @BindView(R.id.priceTxtView)
    protected TextView priceTxtView;
    @BindView(R.id.priceImageView)
    protected ImageView priceImageView;
    @BindView(R.id.publishLayout)
    protected RelativeLayout publishLayout;
    @BindView(R.id.bannerReLay)
    protected LinearLayout bannerReLay;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.addressLayout)
    LinearLayout addressLayout;
    @BindView(R.id.positionLayout)
    LinearLayout positionLayout;
    @BindView(R.id.educationLayout)
    LinearLayout educationLayout;
    @BindView(R.id.priceLayout)
    LinearLayout priceLayout;
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.publishJobProvideView)
    TextView publishJobProvideView;
    @BindView(R.id.publishJobApplyView)
    TextView publishJobApplyView;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Request
    private JobRestUsage mRestUsage;

    private int type = 1;  // 1: 招聘   2：求职
    /**
     *
     */
    private String keyWord;

    private JobListAdapter adapter;
    private ServiceSlideAdapter mImgPageAdapter;

    private int mCurrentPage = 1;
    private List<Carousel> mCarousels;  //轮播response
    private JobRequest request = new JobRequest();
    private JobResponse response;
    /**
     * 薪资选择
     */
    private SelectCultureDialog selectSalaryDialog;
    /**
     * 文化程度
     */
    private SelectCultureDialog selectEducationDialog;
    /**
     * 当前的地址
     */
    private Address currAddress;

    public static JobFragment newInstance(int type, String keyWord) {
        JobFragment fragment = new JobFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putString(EXTRA_KEY_WORD_KEY, keyWord);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_job_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        request.setAreaName(WelcomeActivity.seletedAdress);
        type = getArguments().getInt(TYPE, JobActivity.JOB_PROVIDE);
        keyWord = getArguments().getString(EXTRA_KEY_WORD_KEY);
        requestSkillList();
        initRecycleView();
        requestCarousel();
    }

    /**
     * 初始化搜索状态view
     */
    private void initSearchView() {
        if (TextUtils.isEmpty(keyWord)) {  //非搜索
            publishLayout.setVisibility(View.VISIBLE);
            bannerReLay.setVisibility(View.GONE);
            if (type == JobActivity.JOB_PROVIDE) {
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
     *
     */
    private void requestSkillList() {
        if (type == JobActivity.JOB_PROVIDE) {
            mRestUsage.getJobProvideList(TASK_GET_LIST, mCurrentPage, request);
        } else {
            mRestUsage.getJobApplyList(TASK_GET_LIST, mCurrentPage, request);
        }
    }

    private void initRecycleView() {
        recycleView.setLayoutManager(new GridLayoutManager(this.getActivity(), 1));
        recycleView.addItemDecoration(new GridSpacingItemDecoration(1, DensityUtil.dp2px(getActivity(), 10), false));
        adapter = new JobListAdapter(this);
        adapter.setType(type);
        recycleView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                mCurrentPage = 1;
                requestSkillList();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                mCurrentPage++;
                requestSkillList();
            }
        });

    }

    /**
     * 初始化 轮播
     */
    private void initSlide() {
        mImgPageAdapter = new ServiceSlideAdapter(getActivity(), true, mCarousels);
        mImgPageAdapter.setmClassId(type == JobActivity.JOB_PROVIDE ? "6" : "7");

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
                    response = (JobResponse) msg.getObj();
                    if (response != null) {
                        if (mCurrentPage == 1) {
                            adapter.getDataList().clear();
                        }
                        mCurrentPage++;
                        if (type == JobActivity.JOB_PROVIDE) {
                            if (response.getJobList() != null && response.getJobList().size() > 0) {
                                adapter.getDataList().addAll(response.getJobList());
                            } else {
                                Toast.makeText(getActivity(), "到底了！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (response.getJobApplyList() != null && response.getJobApplyList().size() > 0) {
                                adapter.getDataList().addAll(response.getJobApplyList());
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

    @OnClick(R.id.addressLayout)
    void addressLayout() {   //地点
        TownCategoryActivity.startActivityForResult(this, currAddress, true, CODE_SELECT_ADDRES);
    }

    @OnClick(R.id.positionLayout)
    void positionLayout() {   //职位
        ClassifyActivity.startActivityForResult(this, ClassifyActivity.TYPE_SELECT_JOB_POSITION, request.getPosition(), CODE_SELECT_POSITION);
    }

    @OnClick(R.id.educationLayout)
    void educationLayout() {   //学历
        if (selectEducationDialog == null) {
            selectEducationDialog = new SelectCultureDialog(getContext(), SelectCultureDialog.TYPE_EDUCATION);
            selectEducationDialog.setCyclic(false);
            selectEducationDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if (wheelMenuInfo != null) {
                        if (!TextUtils.isEmpty(wheelMenuInfo.getCode())) {
                            educationTxtView.setText(wheelMenuInfo.getName());
                        } else {
                            educationTxtView.setText("学历");
                        }
                        request.setEducation(wheelMenuInfo.getCode());
                        refreshLayout.autoRefresh();
                    }
                }
            });
        }
        selectEducationDialog.setSelectedMenu(request.getEducation());
        selectEducationDialog.show();
    }

    @OnClick(R.id.priceLayout)
    void priceLayout() {   //薪资
        if (selectSalaryDialog == null) {
            selectSalaryDialog = new SelectCultureDialog(getContext(), SelectCultureDialog.TYPE_SALARY);
            selectSalaryDialog.setCyclic(false);
            selectSalaryDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if (wheelMenuInfo != null) {
                        if (!TextUtils.isEmpty(wheelMenuInfo.getCode())) {
                            priceTxtView.setText(wheelMenuInfo.getName());
                        } else {
                            priceTxtView.setText("薪资");
                        }
                        request.setSalary(wheelMenuInfo.getCode());
                        refreshLayout.autoRefresh();
                    }
                }
            });
        }
        selectSalaryDialog.setSelectedMenu(request.getSalary());
        selectSalaryDialog.show();
    }

    @OnClick(R.id.publishJobProvideView)
    void publishJobProvideView() {
        CheckAuthTool.startActivity(getContext(), PublishJobProvideActivity.class, 2, null);
    }

    @OnClick(R.id.publishJobApplyView)
    void publishJobApplyView() {
        Bundle bundle = new Bundle();
        bundle.putInt("checkAuthType", 1);
        CheckAuthTool.startActivity(getActivity(), PublishJobProvideActivity.class, bundle);
    }

    public int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_SELECT_ADDRES:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Address address = (Address) data.getSerializableExtra("address");

                    if (address != null) {
                        currAddress = address;
                        addressTextView.setText(address.getName());
                        request.setArea(String.valueOf(address.getId()));
                        mCurrentPage = 1;
                        refreshLayout.autoRefresh();
                    } else {
                        addressTextView.setText("地点");
                        request.setArea(null);
                    }
                }
                break;
            case CODE_SELECT_POSITION:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String position = data.getStringExtra(ClassifyActivity.EXTRA_CLASSIFY_KEY);
                    if (!TextUtils.isEmpty(position)) {
                        positionTxtView.setText(position);
                        request.setPosition(position);
                        mCurrentPage = 1;
                        refreshLayout.autoRefresh();
                    } else {
                        positionTxtView.setText("职位");
                        request.setPosition(null);
                    }
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
