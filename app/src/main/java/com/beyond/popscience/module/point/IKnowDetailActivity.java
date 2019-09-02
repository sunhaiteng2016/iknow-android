package com.beyond.popscience.module.point;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IKnowDetailActivity extends BaseActivity {

    @BindView(R.id.detail_layout)
    ImageView detailLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_iknow_detail;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("科普绿币");
    }

    @OnClick(R.id.detail_layout)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.ib_back)
    public void onBackClicked() {
        finish();
    }
}
