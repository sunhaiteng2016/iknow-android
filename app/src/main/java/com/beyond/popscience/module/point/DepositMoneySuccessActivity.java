package com.beyond.popscience.module.point;

import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class DepositMoneySuccessActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txcg)
    TextView txcg;
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_deposit_money_success_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        String flag = getIntent().getStringExtra("flag");
        tvTitle.setText(flag);
        txcg.setText(flag);


    }

    @OnClick(R.id.deposit_success_button)
    public void onClick() {
        finish();
    }
}
