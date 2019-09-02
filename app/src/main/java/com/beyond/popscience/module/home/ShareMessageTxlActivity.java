package com.beyond.popscience.module.home;

import android.os.Bundle;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.module.home.fragment.FriendsListFragmentTwo;
import com.beyond.popscience.module.home.fragment.FriendsListFragmentfour;

public class ShareMessageTxlActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_share_tx_l;
    }

    @Override
    public void initUI() {
        super.initUI();
        String userId = getIntent().getStringExtra("userId");
        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
        FriendsListFragmentfour friendsListFragmentTwo = new FriendsListFragmentfour();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("forward_msg_id", forward_msg_id);
        friendsListFragmentTwo.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, friendsListFragmentTwo).commit();
    }
}
