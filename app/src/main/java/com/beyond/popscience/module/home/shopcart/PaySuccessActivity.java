package com.beyond.popscience.module.home.shopcart;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.ZczxActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付成功界面
 */
public class PaySuccessActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.leftTxtView)
    TextView leftTxtView;
    @BindView(R.id.rightImgView)
    ImageView rightImgView;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.topReLay)
    RelativeLayout topReLay;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_ledian)
    TextView tvLedian;
    @BindView(R.id.tv_backhome)
    TextView tvBackhome;
    @BindView(R.id.tv_backorder)
    TextView tvBackorder;
    @BindView(R.id.iv11)
    ImageView iv11;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        initUI();
    }

    @Override
    public void initUI() {
        super.initUI();
        // TODO: 2018/3/8  这里边可以写显示数据的东西
        tvBackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.ib_back)
    public void onViewClicked() {
        finish();
    }
}
