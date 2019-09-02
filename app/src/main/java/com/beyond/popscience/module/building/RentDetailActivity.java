package com.beyond.popscience.module.building;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.PhoneUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.fragment.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linjinfa on 2017/10/16.
 * email 331710168@qq.com
 */
public class RentDetailActivity extends BaseActivity {

    /**
     *
     */
    private final static String EXTRA_BUILD_ID_KEY = "buildId";

    /**
     *
     */
    private final int REQUEST_DETAIL_TASK_ID = 101;
    @BindView(R.id.priceLinLay)
    LinearLayout priceLinLay;

    /**
     * @param context
     */
    public static void startActivity(Context context, String buildId) {
        Intent intent = new Intent(context, RentDetailActivity.class);
        intent.putExtra(EXTRA_BUILD_ID_KEY, buildId);
        context.startActivity(intent);
    }

    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    @BindView(R.id.tv_right)
    protected TextView tv_right;

    @BindView(R.id.buildTitleTxtView)
    protected TextView buildTitleTxtView;

    @BindView(R.id.classifyNameTxtView)
    protected TextView classifyNameTxtView;

    @BindView(R.id.lowPriceTxtView)
    protected TextView lowPriceTxtView;

    @BindView(R.id.highPriceTxtView)
    protected TextView highPriceTxtView;

    @BindView(R.id.payTxtView)
    protected TextView payTxtView;

    @BindView(R.id.hourseTypeRequireTxtView)
    protected TextView hourseTypeRequireTxtView;

    @BindView(R.id.areaTxtView)
    protected TextView areaTxtView;

    @BindView(R.id.locationRequireTxtView)
    protected TextView locationRequireTxtView;

    @BindView(R.id.descTxtView)
    protected TextView descTxtView;

    @Request
    private BuildingRestUsage buildingRestUsage;
    /**
     *
     */
    private String buildId;
    /**
     *
     */
    private BuildingDetail buildingDetail;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_rent_detail;
    }

    @Override
    public void initUI() {
        super.initUI();

        buildId = getIntent().getStringExtra(EXTRA_BUILD_ID_KEY);
        if (TextUtils.isEmpty(buildId)) {
            backNoAnim();
            return;
        }
        titleTxtView.setText("求租求购");
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDetail();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_DETAIL_TASK_ID:
                if (msg.getIsSuccess()) {
                    buildingDetail = (BuildingDetail) msg.getObj();

                    initDataView();
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     *
     */
    private void initDataView() {
        if (buildingDetail != null) {
            String titleHtml = buildingDetail.title;
            if ("1".equals(buildingDetail.sellType)) {  //	1：中介 2：房东
                titleHtml += "<font color=#4f98d5>(中介)</font>";
            } else if ("2".equals(buildingDetail.sellType)) {    //1：中介 2：客户
                titleHtml += "<font color=#4f98d5>(客户)</font>";
            }

            buildTitleTxtView.setText(Html.fromHtml(titleHtml == null ? "" : titleHtml));
            classifyNameTxtView.setText(buildingDetail.classifyName);
            if (buildingDetail.startPrice.equals("-1.00") || buildingDetail.endPrice.equals("-1.00")) {
                lowPriceTxtView.setText("面议");
            } else {
                String qiuzhu = "1".equals(buildingDetail.trade) ? "求租" : "求购";
                if (qiuzhu.equals("求租")){
                    lowPriceTxtView.setText( buildingDetail.startPrice + "/月");
                    highPriceTxtView.setText( "~"+buildingDetail.endPrice + "/月");
                }else{
                    lowPriceTxtView.setText( buildingDetail.startPrice + "元");
                    highPriceTxtView.setText( "~"+buildingDetail.endPrice + "元");
                }
            }
            payTxtView.setText("1".equals(buildingDetail.trade) ? "求租" : "求购");
            hourseTypeRequireTxtView.setText(buildingDetail.hourseTypeRequire);
            areaTxtView.setText(buildingDetail.startArea + "m²~" + buildingDetail.endArea + "m²");
            locationRequireTxtView.setText(buildingDetail.locationRequire);
            descTxtView.setText(buildingDetail.introduce);

            //如果是自己的商品则展示编辑按钮
            if (TextUtils.equals(buildingDetail.userId, UserInfoUtil.getInstance().getUserInfo().getUserId())) {
                tv_right.setText("编辑");
                tv_right.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 请求详情
     */
    private void requestDetail() {
        showProgressDialog();
        buildingRestUsage.getRentDetail(REQUEST_DETAIL_TASK_ID, buildId);
    }

    @OnClick(R.id.llCall)
    void toPhoneCall(View view) {
        //打电话
        if (buildingDetail != null) {
            PhoneUtil.callPhoneDial(this, buildingDetail.mobile);
        }
    }

    @OnClick(R.id.llShotMsg)
    void toShortMsg(View view) {
        //打开短信
        if (buildingDetail != null) {
            PhoneUtil.sendSms(this, buildingDetail.mobile, "");
        }
    }

    @OnClick(R.id.tv_right)
    void rightEdit() {
        PublishRentActivity.startActivity(this, buildingDetail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.lxmj)
    public void onViewClicked() {
        if (buildingDetail.userId.equals("-1")) {
            ToastUtil.showCenter(this, "该内容为后台发布，请通过电话联系");
        } else {
            Intent intent = new Intent(RentDetailActivity.this, ChatActivity.class);
            intent.putExtra(Constant.EXTRA_USER_ID, buildingDetail.userId + "");
            startActivity(intent);
        }

    }
}
