package com.beyond.popscience;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.module.home.AddFriendsActivity;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.beyond.popscience.module.social.NoticeDetailActivity;

import org.greenrobot.eventbus.EventBus;

public class HuaWeiActivity extends BaseActivity {

    /**
     * 乡镇新闻
     */
    public static final int TYPE_TOWN_NEWS = 1;
    /**
     * 乡镇公告
     */
    public static final int TYPE_TOWN_ANNOUNCEMENT = 2;

    /**
     * homepage news
     */
    public static final int TYPE_HOME_NEWS = 3;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_hua_wei;
    }

    @Override
    public void initUI() {
        super.initUI();
        try {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String intentUrl = intent.toUri(Intent.URI_INTENT_SCHEME);
            String[] urls = intentUrl.split(";");
            Log.e("sht", "" + intentUrl);
            String typeId = urls[3];
            String type = urls[4];
            String[] mTypes = type.split("=");
            String[] mTypesid = typeId.split("=");

            String typesss = mTypes[1];
            String typesidss = mTypesid[1];

            News news = new News();
            news.newsId = typesidss;
            news.title = "通知详情";

            if (typesss.equals("1")) {
                news.appNewsType = News.TYPE_HOME_NEWS;
                NewsDetailActivity.startActivity(this, news);
            } else if (typesss.equals("2")) {
                news.appNewsType = News.TYPE_TOWN_NEWS;
                NewsDetailActivity.startActivity(this, news);
            } else if (typesss.equals("3")) {
                AnnouncementActivity.startActivity(this, news);
            } else if (typesss.equals("4")) {
                Intent intents = new Intent(this, AddFriendsActivity.class);
                startActivity(intents);
            } else if (typesss.equals("5")) {
                Intent intents = new Intent(this, PopularMainActivity.class);
                intents.putExtra("flag", "5");
                this.startActivity(intents);
                EventBus.getDefault().post(new MainMessage(1));
            } else if (typesss.equals("6")) {
                Intent intents = new Intent(this, PopularMainActivity.class);
                intents.putExtra("flag", "6");
                this.startActivity(intents);
                EventBus.getDefault().post(new MainMessage(2));
            }else {
                Intent intents = new Intent(this, PopularMainActivity.class);
                startActivity(intents);
            }
        } catch (Exception e) {
        }
        setBadgeNum(0, this);
        finish();
    }

    public void setBadgeNum(int num, Context context) {
        try {
            Bundle bunlde = new Bundle();
            bunlde.putString("package", "com.gymj.apk.xj");
            bunlde.putString("class", "com.beyond.popscience.module.home.WelcomeActivity");
            bunlde.putInt("badgenumber", num);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
