package com.beyond.popscience.module.job;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.PhoneUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.JobRestUsage;
import com.beyond.popscience.frame.pojo.JobDetail;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.fragment.Constant;
import com.beyond.popscience.module.job.adapter.JobListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danxiang.feng on 2017/10/17.
 */

public class JobApplyDetailActivity extends BaseActivity {

    private final int REQ_JOB_APPLY_DETAIL_ID = 1001;

    private static final String EXTRA_APPLYID = "applyId";
    private static final String EXTRA_APPLYI_WTO = "flag";
    @BindView(R.id.tv_title)
    protected TextView tv_title;//activity 标题
    @BindView(R.id.tv_right)
    protected TextView tv_right;
    @BindView(R.id.jobApplyLayout)
    protected LinearLayout jobApplyLayout;
    @BindView(R.id.titleTxtView)
    protected TextView titleTxtView;
    @BindView(R.id.timeTxtView)
    protected TextView timeTxtView;
    @BindView(R.id.addressTxtView)
    protected TextView addressTxtView;
    @BindView(R.id.priceTxtView)
    protected TextView priceTxtView;
    @BindView(R.id.locationRequireTxtView)
    protected TextView locationRequireTxtView;
    @BindView(R.id.industryTxtView)
    protected TextView industryTxtView;
    @BindView(R.id.positionTxtView)
    protected TextView positionTxtView;
    @BindView(R.id.descriptTxtView)
    protected TextView descriptTxtView;
    @BindView(R.id.llCall)
    protected TextView llCall;
    @BindView(R.id.emptyReLay)
    protected RelativeLayout emptyReLay;

    @Request
    private JobRestUsage jobRestUsage;


    private String applyId;
    private JobDetail jobDetail;
    private String flag;

    public static final void startActivty(Context context, String applyId, String flag) {
        Intent intent = new Intent(context, JobApplyDetailActivity.class);
        intent.putExtra(EXTRA_APPLYID, applyId);
        intent.putExtra(EXTRA_APPLYI_WTO, flag);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_job_apply_detail_layout;
    }

    @Override
    public void initUI() {
        super.initUI();

        applyId = getIntent().getStringExtra(EXTRA_APPLYID);
        flag = getIntent().getStringExtra(EXTRA_APPLYI_WTO);
        if (TextUtils.isEmpty(applyId)) {
            backNoAnim();
            return;
        }
        tv_title.setText("求职详情");
        jobApplyLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestApplyDetail();
    }

    private void requestApplyDetail() {
        showProgressDialog();
        jobRestUsage.getJobApplyDetail(REQ_JOB_APPLY_DETAIL_ID, applyId);
    }

    private void refreshView() {
        TextView qzr = (TextView) findViewById(R.id.qzr);
        qzr.setText(jobDetail.realName);

        TextView addressTxtViewone = (TextView) findViewById(R.id.addressTxtViewone);
        if (JobListAdapter.myage != null) {
            addressTxtViewone.setText(JobListAdapter.myage + "岁");
        } else {
            addressTxtViewone.setText(0 + "岁");
        }
        titleTxtView.setText(jobDetail.title);

        String timeTxt = "";
        if (!TextUtils.isEmpty(jobDetail.createTime)) {
            String time = Util.getBetweenTime(BeyondApplication.getInstance().getCurrSystemTime(), jobDetail.createTime);
            timeTxt += "发布于" + time + "前";
        }
        timeTxtView.setText(timeTxt);
        addressTxtView.setText(jobDetail.area);
        if (jobDetail.lowPrice.equals("面议")|| jobDetail.highPrice .equals("面议")){
            priceTxtView.setText("面议");
        }else{
            priceTxtView.setText(jobDetail.lowPrice + "元 - " + jobDetail.highPrice + "元");
        }

        // priceTxtView.setText(jobDetail.lowPrice + " - " + jobDetail.highPrice);
        locationRequireTxtView.setText(jobDetail.address);
        industryTxtView.setText(jobDetail.industry);
        positionTxtView.setText(jobDetail.position);
        descriptTxtView.setText(jobDetail.introduce);
        //如果是自己的商品则展示编辑按钮
        if (flag == null || (flag.equals(""))) {
            tv_right.setVisibility(View.GONE);
        } else {
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
            case REQ_JOB_APPLY_DETAIL_ID:
                if (msg.getIsSuccess()) {
                    jobDetail = (JobDetail) msg.getObj();
                    if (jobDetail != null) {
                        jobApplyLayout.setVisibility(View.VISIBLE);
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
        if (jobDetail != null && !TextUtils.isEmpty(jobDetail.mobile)) {
            PhoneUtil.callPhoneDial(this, jobDetail.mobile);
        }
    }

    @OnClick(R.id.tv_right)
    void rightEdit() {
        PublishJobApplyActivity.startActivity(this, jobDetail);
    }

    @OnClick(R.id.emptyReLay)
    void emptyReLay() {
        requestApplyDetail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.lxsj)
    public void onViewClicked() {
        Intent intent = new Intent(JobApplyDetailActivity.this, ChatActivity.class);
        intent.putExtra(Constant.EXTRA_USER_ID, jobDetail.userId + "");
        startActivity(intent);
    }
}
