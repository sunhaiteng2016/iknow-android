package com.beyond.popscience.module.job;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.PhoneUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.JobRestUsage;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.JobDetail;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.module.building.BuildingDetailActivity;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.fragment.Constant;
import com.beyond.popscience.module.mservice.CheckAuthTool;
import com.beyond.popscience.module.mservice.adapter.ServiceSlideAdapter;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 招聘详情
 * Created by danxiang.feng on 2017/10/17.
 */

public class JobProvideDetailActivity extends BaseActivity {

    private final int REQ_JOB_PROVIDE_DETAIL_ID = 1201;

    private static final String EXTRA_JOBID = "jobId";
    private static final String EXTRA_JOBID_TWO = "flag";
    @BindView(R.id.tv_title)
    protected TextView tv_title;//activity 标题
    @BindView(R.id.tv_right)
    protected TextView tv_right;
    @BindView(R.id.jobProvideLayout)
    protected LinearLayout jobProvideLayout;
    @BindView(R.id.bannerReLay)
    protected RelativeLayout bannerReLay;
    @BindView(R.id.vp)
    protected AutoViewPager mSlidePager;
    @BindView(R.id.recycleIndicator)
    protected RecyclingCirclePageIndicator mIndicator;
    @BindView(R.id.titleTxtView)
    protected TextView titleTxtView;
    @BindView(R.id.timeTxtView)
    protected TextView timeTxtView;
    @BindView(R.id.addressTxtView)
    protected TextView addressTxtView;
    @BindView(R.id.priceTxtView)
    protected TextView priceTxtView;
    @BindView(R.id.companyTxtView)
    protected TextView companyTxtView;
    @BindView(R.id.educationTxtView)
    protected TextView educationTxtView;
    @BindView(R.id.workExperienceTxtView)
    protected TextView workExperienceTxtView;
    @BindView(R.id.requireTxtView)
    protected TextView requireTxtView;
    @BindView(R.id.descriptTxtView)
    protected TextView descriptTxtView;
    @BindView(R.id.llCall)
    protected TextView llCall;
    @BindView(R.id.emptyReLay)
    protected RelativeLayout emptyReLay;

    @Request
    private JobRestUsage jobRestUsage;

    private ServiceSlideAdapter mImgPageAdapter;

    private String jobId;
    private JobDetail jobDetail;
    private String flag;


    public static void startActivity(Context context, String jobId, String flag) {
        Intent intent = new Intent(context, JobProvideDetailActivity.class);
        intent.putExtra(EXTRA_JOBID, jobId);
        intent.putExtra(EXTRA_JOBID_TWO, flag);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_job_provide_detial_layout;
    }

    @Override
    public void initUI() {
        super.initUI();

        jobId = getIntent().getStringExtra(EXTRA_JOBID);
        flag = getIntent().getStringExtra(EXTRA_JOBID_TWO);
       /* if (flag!=null||!(flag.equals(""))){
            tv_right.setVisibility(View.GONE);
        }else{
            tv_right.setVisibility(View.VISIBLE);
        }*/
        if (TextUtils.isEmpty(jobId)) {
            backNoAnim();
            return;
        }
        tv_title.setText("招聘详情");
        jobProvideLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestJobDetail();
    }

    /**
     * 初始化 轮播
     */
    private void initSlide(String pictures) {
        if (TextUtils.isEmpty(pictures)) {
            bannerReLay.setVisibility(View.GONE);
            return;
        }
        List<String> list = Arrays.asList(pictures.split(","));
        if (list == null || list.size() == 0) {
            bannerReLay.setVisibility(View.GONE);
            return;
        }
        bannerReLay.setVisibility(View.VISIBLE);
        List<Carousel> mCarousels = new ArrayList<>();
        Carousel carousel = null;
        for (String str : list) {
            if (!TextUtils.isEmpty(str)) {
                carousel = new Carousel();
                carousel.setPic(str);
                mCarousels.add(carousel);
            }
        }
        mImgPageAdapter = new ServiceSlideAdapter(this, true, mCarousels);
        mImgPageAdapter.setmClassId("6");
        mSlidePager.setAdapter(mImgPageAdapter);
        mSlidePager.setInterval(2000);
        mSlidePager.setCurrentItem(mImgPageAdapter.getRealCount() * 1000, false);
        mSlidePager.startAutoScroll();
        mIndicator.setViewPager(mSlidePager);
        mIndicator.setCentered(true);
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

    private void requestJobDetail() {
        showProgressDialog();
        jobRestUsage.getJobProvideDetail(REQ_JOB_PROVIDE_DETAIL_ID, jobId);
    }

    private void refreshView() {
        titleTxtView.setText(jobDetail.title);
        TextView zp_moblie = (TextView) findViewById(R.id.zp_moblie);
        TextView zp_iphone = (TextView) findViewById(R.id.zp_iphone);
        zp_iphone.setText(jobDetail.mobile);
        zp_moblie.setText(jobDetail.realName);
        timeTxtView.setText(jobDetail.createTime);
        addressTxtView.setText(jobDetail.address);
        if (jobDetail.lowPrice.equals("-1")|| jobDetail.highPrice .equals("-1")){
            priceTxtView.setText("面议");
        }else{
            priceTxtView.setText(jobDetail.lowPrice + "元 - " + jobDetail.highPrice + "元");
        }
       // priceTxtView.setText(jobDetail.lowPrice + "元 - " + jobDetail.highPrice + "元");
        companyTxtView.setText(jobDetail.company);
        educationTxtView.setText(jobDetail.education);
        workExperienceTxtView.setText(jobDetail.workExperience);
        requireTxtView.setText(jobDetail.introduce);
        descriptTxtView.setText(jobDetail.require);

        initSlide(jobDetail.detailPics);

        if (flag == null || flag.equals("")) {
            tv_right.setVisibility(View.GONE);
        } else {
            //如果是自己的商品则展示编辑按钮
            if (TextUtils.equals(jobDetail.userId, UserInfoUtil.getInstance().getUserInfo().getUserId())) {
                tv_right.setText("编辑");
                tv_right.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case REQ_JOB_PROVIDE_DETAIL_ID:
                if (msg.getIsSuccess()) {
                    jobDetail = (JobDetail) msg.getObj();
                    if (jobDetail != null) {
                        jobProvideLayout.setVisibility(View.VISIBLE);
                        emptyReLay.setVisibility(View.GONE);
                        refreshView();
                        return;
                    }
                }
                emptyReLay.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.llCall)
    void llCall() {
        if (jobDetail != null) {
            PhoneUtil.callPhoneDial(this, jobDetail.mobile);
        }else{
            Toast.makeText(this,"暂无联系方式！",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_right)
    void rightEdit() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PublishJobProvideActivity.JOB_DETAIL, jobDetail);
        CheckAuthTool.startActivity(this, PublishJobProvideActivity.class, 2, bundle);
    }

    @OnClick(R.id.emptyReLay)
    void emptyReLay() {
        requestJobDetail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.lxmj)
    public void onViewClicked() {
        if (jobDetail.userId.equals("-1")) {
            ToastUtil.showCenter(this, "该内容为后台发布，请通过电话联系");
        } else {
            Intent intent = new Intent(JobProvideDetailActivity.this, ChatActivity.class);
            intent.putExtra(Constant.EXTRA_USER_ID, jobDetail.userId + "");
            startActivity(intent);
        }

    }
}
