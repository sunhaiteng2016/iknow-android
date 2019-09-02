package com.beyond.popscience;

import android.content.Context;
import android.content.Intent;

import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.pojo.PushMsg;
import com.beyond.popscience.module.home.AddFriendsActivity;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.google.gson.Gson;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class ViVoPushMessageReceiverImpl extends OpenClientPushMessageReceiver {
    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage msg) {
        Map<String, String> content = msg.getParams();
        String sssss = new Gson().toJson(content);
        PushMsg getuiData = new Gson().fromJson(sssss, PushMsg.class);
        News news = new News();
        news.newsId = getuiData.getTypeId();
        news.title = getuiData.getTitle();
        if (getuiData.getType() == 1) {
            news.appNewsType = News.TYPE_HOME_NEWS;
            NewsDetailActivity.startActivity(context, news);
        } else if (getuiData.getType() == 2) {
            news.appNewsType = News.TYPE_TOWN_NEWS;
            NewsDetailActivity.startActivity(context, news);
        } else if (getuiData.getType() == 3) {
            AnnouncementActivity.startActivity(context, news);
        } else if (getuiData.getType() == 4) {
            Intent intents = new Intent(context, AddFriendsActivity.class);
            context.startActivity(intents);
        } else if (getuiData.getType() == 5) {
            Intent intents = new Intent(context, PopularMainActivity.class);
            intents.putExtra("flag", "5");
            context.startActivity(intents);
            EventBus.getDefault().post(new MainMessage(1));
        } else if (getuiData.getType() == 6) {
            Intent intents = new Intent(context, PopularMainActivity.class);
            intents.putExtra("flag", "6");
            context.startActivity(intents);
            EventBus.getDefault().post(new MainMessage(2));
        } else {
            Intent intent = new Intent(context, PopularMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    public void onReceiveRegId(Context context, String regId) {
        BeyondApplication.VivoCid = regId;
    }
}
