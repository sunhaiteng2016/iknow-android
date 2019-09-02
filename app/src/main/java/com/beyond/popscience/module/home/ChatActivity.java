package com.beyond.popscience.module.home;


import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.module.home.fragment.MyChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class ChatActivity extends BaseActivity {

    private String toChatUsername;
    private EaseChatFragment chatFragment;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_chat;
    }

    @Override
    public void initUI() {
        super.initUI();
        toChatUsername = getIntent().getExtras().getString("userId");
        //use EaseChatFratFragment
        chatFragment = new MyChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }


}
