package com.beyond.popscience.module.personal;


import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.module.home.fragment.SocialFragmentTwo;

public class MyTeamActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_my_team;
    }

    @Override
    public void initUI() {
        super.initUI();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl, new SocialFragmentTwo()).commit();
    }
}
