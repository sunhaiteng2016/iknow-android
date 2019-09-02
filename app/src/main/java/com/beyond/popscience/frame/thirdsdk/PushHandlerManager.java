package com.beyond.popscience.frame.thirdsdk;

import android.content.Context;

import com.beyond.popscience.frame.pojo.PushMsg;
import com.beyond.popscience.frame.util.NotifactionUtil;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kevin_Tian on 2017/5/22.
 * 推送消息统一管理类
 */

public class PushHandlerManager {


    private Set<String> messageIds = new HashSet<>();
    private List<ThirdSDKManager.IPushCalback> calbacks = new ArrayList<>();

    public static PushHandlerManager getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    public void registerCallBack(ThirdSDKManager.IPushCalback calback) {
        calbacks.add(calback);
    }

    public void unRegisterCallBack(ThirdSDKManager.IPushCalback calback) {
        calbacks.remove(calback);
    }

    private void notifyCallBack() {
        for (ThirdSDKManager.IPushCalback calback : calbacks) {
            calback.newMessage();
        }
    }

    public void clearCallBack() {
        calbacks.clear();
    }


    public void registerNewMessage(Context context, String message) {

        if (!UserInfoUtil.getInstance().isHasLogin()) {
            return;
        }
        PushMsg msg = new GsonBuilder().create().fromJson(message,PushMsg.class);
        NotifactionUtil.notifyMsg(context,msg);
    }


    private enum Singleton {
        INSTANCE;

        private PushHandlerManager manager;

        Singleton() {
            manager = new PushHandlerManager();
        }

        public PushHandlerManager getInstance() {
            return manager;
        }
    }
}
