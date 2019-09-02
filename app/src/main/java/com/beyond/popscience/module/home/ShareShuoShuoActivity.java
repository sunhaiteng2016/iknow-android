package com.beyond.popscience.module.home;

import android.os.Bundle;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.module.home.fragment.FriendsListFragmentTwo;
import com.beyond.popscience.module.home.fragment.SocialFragmentThree;

public class ShareShuoShuoActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_share_tx_l;
    }

    @Override
    public void initUI() {
        super.initUI();
        String articleid = getIntent().getStringExtra("articleid");
        String acatar = getIntent().getStringExtra("acatar");
        String content =getIntent().getStringExtra("content");

        SocialFragmentThree friendsListFragmentTwo=new SocialFragmentThree();
        Bundle bundle = new Bundle();
        bundle.putString("articleid",articleid);
        bundle.putString("acatar",acatar);
        bundle.putString("content",content);
        friendsListFragmentTwo.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl,friendsListFragmentTwo ).commit();

    }
}
