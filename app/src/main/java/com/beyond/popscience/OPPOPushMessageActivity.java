package com.beyond.popscience;

import android.content.Intent;
import android.os.Bundle;

import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.PushMsg;
import com.beyond.popscience.module.home.AddFriendsActivity;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Set;

public class OPPOPushMessageActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_oppopush_message;
    }

    @Override
    public void initUI() {
        super.initUI();
        try {
            if (getIntent().getExtras() != null) {
                // 取参数值
                Bundle bundle = getIntent().getExtras();
                Set<String> set = bundle.keySet();
                HashMap<String, String> hm = new HashMap<>();
                if (set != null) {
                    for (String key : set) {
                        hm.put(key, bundle.getString(key));
                    }
                }
                String sss = hm.toString();
                PushMsg getuiData = new Gson().fromJson(sss, PushMsg.class);
                News news = new News();
                news.newsId = getuiData.getTypeId();
                news.title = getuiData.getTitle();
                if (getuiData.getType() == 1) {
                    news.appNewsType = News.TYPE_HOME_NEWS;
                    NewsDetailActivity.startActivity(this, news);
                } else if (getuiData.getType() == 2) {
                    news.appNewsType = News.TYPE_TOWN_NEWS;
                    NewsDetailActivity.startActivity(this, news);
                } else if (getuiData.getType() == 3) {
                    AnnouncementActivity.startActivity(this, news);
                } else if (getuiData.getType() == 4) {
                    Intent intents = new Intent(this, AddFriendsActivity.class);
                    this.startActivity(intents);
                } else if (getuiData.getType() == 5) {
                    Intent intents = new Intent(this, PopularMainActivity.class);
                    intents.putExtra("flag", "5");
                    this.startActivity(intents);
                    EventBus.getDefault().post(new MainMessage(1));
                } else if (getuiData.getType() == 6) {
                    Intent intents = new Intent(this, PopularMainActivity.class);
                    intents.putExtra("flag", "6");
                    this.startActivity(intents);
                    EventBus.getDefault().post(new MainMessage(2));
                } else {
                    Intent intent = new Intent(this, PopularMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);
                }
            }
        } catch (Exception e) {

        }
        finish();
    }
}
