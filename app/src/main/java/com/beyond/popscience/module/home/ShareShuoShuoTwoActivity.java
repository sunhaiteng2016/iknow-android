package com.beyond.popscience.module.home;

import android.os.Bundle;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.module.home.fragment.SocialFragmentfour;

public class ShareShuoShuoTwoActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_share_tx_l;
    }

    @Override
    public void initUI() {
        super.initUI();
        String titles = getIntent().getStringExtra("titles");
        String newsId = getIntent().getStringExtra("link");
        String pics =getIntent().getStringExtra("pics");

        SocialFragmentfour friendsListFragmentTwo=new SocialFragmentfour();
        Bundle bundle = new Bundle();
        bundle.putString("titles",titles);
        bundle.putString("link",newsId);
        bundle.putString("pics",pics);
        friendsListFragmentTwo.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl,friendsListFragmentTwo ).commit();
    }
}
