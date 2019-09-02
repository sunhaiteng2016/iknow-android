package com.beyond.popscience.module.point;

import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;

import butterknife.BindView;

public class InvitePepleActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_point_invite_people;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("邀请收徒");
    }
}
