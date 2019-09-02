package com.beyond.popscience.frame.thirdsdk.gtpush;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.igexin.sdk.GTServiceManager;

/*******************************************************************************************
 * author:keming.tian  on 2017/4/24
 * description：
 *******************************************************************************************/
public class GeTuiService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        GTServiceManager.getInstance().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        int status = GTServiceManager.getInstance().onStartCommand(GeTuiService.this, intent, flags, startId);

        if (status == Service.START_NOT_STICKY) {
            return Service.START_NOT_STICKY;

        } else if (status == Service.START_STICKY) {
            return Service.START_STICKY;

        } else if (status == Service.START_STICKY_COMPATIBILITY) {
            return Service.START_STICKY_COMPATIBILITY;
        } else if (status == Service.START_REDELIVER_INTENT) {
            return Service.START_REDELIVER_INTENT;
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GTServiceManager.getInstance().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }
}
