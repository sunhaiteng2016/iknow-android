package com.beyond.popscience.module.home.shopcart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.fragment.SocialFragment;

public class TestActivity extends BaseActivity {

    @Request
    private SocialRestUsage socialRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_test;
    }

    @Override
    public void initUI() {
        super.initUI();
        String titles = getIntent().getStringExtra("titles");
        String newsId = getIntent().getStringExtra("link");
        String pics = getIntent().getStringExtra("pics");
        socialRestUsage.publishArticleTwo(10086, UserInfoUtil.getInstance().getUserInfo().getVillageId(), titles, pics, newsId);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 10086:
                ToastUtil.showCenter(this,"转发成功");
                finish();
                break;
        }
    }
}
