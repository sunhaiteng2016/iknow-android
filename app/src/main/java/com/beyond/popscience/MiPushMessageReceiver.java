package com.beyond.popscience;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.pojo.PushMsg;
import com.beyond.popscience.module.home.AddFriendsActivity;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.beyond.popscience.module.social.NoticeDetailActivity;
import com.google.gson.Gson;
import com.helper.utils.SharedPreferencesUtils;
import com.hyphenate.push.platform.mi.EMMiMsgReceiver;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 DemoMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 * {@code
 *  <receiver
 *      android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"
 *      android:exported="true">
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.ERROR" />
 *      </intent-filter>
 *  </receiver>
 *  }</pre>
 * 3、DemoMessageReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、DemoMessageReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、DemoMessageReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、DemoMessageReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、DemoMessageReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 *
 * @author mayixiang
 */
public class MiPushMessageReceiver extends EMMiMsgReceiver {

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        String content = message.getContent();
        PushMsg getuiData = new Gson().fromJson(content, PushMsg.class);
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
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {

    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        Log.e("shttt", cmdArg1);
        BeyondApplication.xiaomiCid = cmdArg1;
        SharedPreferencesUtils.setParam(context, "xiaomi", "");
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
    }


    @SuppressLint("SimpleDateFormat")
    private static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }

}
