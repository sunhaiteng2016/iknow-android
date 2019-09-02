package com.beyond.popscience.frame.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.pojo.PushMsg;
import com.beyond.popscience.frame.thirdsdk.NotificationTransferActivity;

/**
 * Created by yao.cui on 2017/7/22.
 */

public class NotifactionUtil {
    /**
     * 默认的notification id
     */
    public static final int DEFAULT_NOTIFICATION_ID = 5000;
    /**
     * notification id
     */
    public static int notificationId = DEFAULT_NOTIFICATION_ID;

    private static final int DEFAULT_SERVICE_REQUEST_CODE = 111;

    public static void notifyMsg(Context context, PushMsg msg){

        Intent startIntent = new Intent(context, NotificationTransferActivity.class);
        startIntent.putExtra(NotificationTransferActivity.MESSAGE,msg);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, DEFAULT_SERVICE_REQUEST_CODE,
                startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(msg.getTitle())
                        .setContentText(msg.getAuchor())
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notifyMgr.notify(getNewNotificationId(), builder.build());
    }

    /**
     * 获取新的notificationId
     *
     * @return
     */
    private static int getNewNotificationId() {
        notificationId++;
        return notificationId;
    }
}
