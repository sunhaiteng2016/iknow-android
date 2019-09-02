package com.beyond.popscience.frame.thirdsdk.gtpush;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.beyond.library.util.L;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.thirdsdk.PushHandlerManager;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/*******************************************************************************************
 * author:keming.tian  on 2017/4/24
 * description：继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调(为解决小概率发生的Android广播丢失问题),
 * 如果注册了了该服务, 则务必要在 AndroidManifest中声明, 否则无法受消息<br>
 * onReceiveMessageData 处理理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理理回执 <br>
 *******************************************************************************************/
public class GeTuiIntentService extends GTIntentService {


    public GeTuiIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        L.e("GeTuiIntentService个推 ronReceiveServicePid：" + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        if (msg == null) {
            return;
        }
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();//透传消息

        String pkg = msg.getPkgName();
        String cid = msg.getClientId();
        BeyondApplication.VivoCid=cid;
        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        L.d("GeTuiIntentService个推 call sendFeedbackMessage = " + (result ? "success" : "failed"));
        L.d("GeTuiIntentService个推 onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            L.e("GeTuiIntentService个推 onReceiveMessageData payload = null");
        } else {
            String data = new String(Base64.decode(payload,Base64.DEFAULT));
            L.d("GeTuiIntentService个推 onReceiveMessageData payload = " + data);
            sendMessage(context, data, 0);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        L.e("GeTuiIntentService个推 onReceiveClientId -> " + "clientid = " + clientid);
        ThirdSDKManager.getInstance().setGetuiDeviceid(clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        L.e("GeTuiIntentService个推 onReceiveOnlineState：" + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        L.e("GeTuiIntentService个推 onReceiveCommandResult：" + cmdMessage);
    }

    private void sendMessage(Context context, String data, int what) {
        L.v("GeTuiIntentService个推 接收到的新消息==========>  " + data + "   what: " + what);
        if (TextUtils.isEmpty(data))
            return;
        PushHandlerManager.getInstance().registerNewMessage(context, data);
    }
}
