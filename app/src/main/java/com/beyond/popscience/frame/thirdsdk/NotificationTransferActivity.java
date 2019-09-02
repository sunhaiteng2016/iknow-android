package com.beyond.popscience.frame.thirdsdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beyond.popscience.MainMessage;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.PushMsg;
import com.beyond.popscience.module.home.AddFriendsActivity;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.PopularMainActivity;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.NewsDetailActivity;

import org.greenrobot.eventbus.EventBus;


public class NotificationTransferActivity extends BaseActivity {

    public static String MESSAGE = "message";
    private PushMsg msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msg = (PushMsg) getIntent().getSerializableExtra(MESSAGE);
        if (msg != null) {
            PopularMainActivity.startActivity(this);//启动应用
            jump();
        }

        finish();

    }

    private void jump() {
        News news = new News();
        news.newsId = msg.getTypeId();
        news.title = msg.getTitle();
        if (msg.isNewsHomePage()) {//首页新闻
            news.appNewsType = News.TYPE_HOME_NEWS;
            NewsDetailActivity.startActivity(this, news);
        } else if (msg.isNewsTown()) {//乡镇新闻
            news.appNewsType = News.TYPE_TOWN_NEWS;
            NewsDetailActivity.startActivity(this, news);
        } else if (msg.isNoticeTown()) {//公告
            AnnouncementActivity.startActivity(this, news);
        } else if (msg.isFriends()) {
            Intent intent = new Intent(this, AddFriendsActivity.class);
            startActivity(intent);
        } else if (msg.getType() == 5) {
            Intent intents = new Intent(this, PopularMainActivity.class);
            intents.putExtra("flag", "5");
            this.startActivity(intents);
            EventBus.getDefault().post(new MainMessage(1));
        } else if (msg.getType() == 6) {
            Intent intents = new Intent(this, PopularMainActivity.class);
            intents.putExtra("flag", "5");
            this.startActivity(intents);
            EventBus.getDefault().post(new MainMessage(2));
        }
    }

    @Override
    protected int getLayoutResID() {
        return View.NO_ID;
    }

}
