package com.beyond.popscience.locationgoods;


import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.fragment.InformationSubmitFragmentThree;
import com.beyond.popscience.locationgoods.fragment.InformationSubmitFragmentTwo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopXXActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shop_xx;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("商家信息");
        getSupportFragmentManager().beginTransaction().add(R.id.fl, new InformationSubmitFragmentThree()).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }
}
