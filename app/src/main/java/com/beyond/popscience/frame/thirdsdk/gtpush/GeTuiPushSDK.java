package com.beyond.popscience.frame.thirdsdk.gtpush;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.beyond.library.util.L;
import com.igexin.sdk.PushManager;

/**
 * 个推
 * Created by linjinfa on 2017/5/10.
 * email 331710168@qq.com
 */
public class GeTuiPushSDK {

    /**
     *
     */
    private final int REQUEST_PERMISSION = 900;
    /**
     *
     */
    private Application application;

    /**
     * 初始化
     * @param activity
     */
    public void init(Activity activity){
        application = activity.getApplication();

        PackageManager pkgManager = activity.getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission = pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION);
        } else {//参数GeTuiService.class可传null  ，null则用sdk里面的pushservice
            PushManager.getInstance().initialize(activity.getApplicationContext(), GeTuiService.class);
        }
        //注册接受消息 服务
        PushManager.getInstance().registerPushIntentService(activity.getApplicationContext(), GeTuiIntentService.class);
    }

    /**
     * 初始化个推SDK（需要授权后再初始化）
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(application, GeTuiService.class);
            } else {
                L.e(this.getClass().getSimpleName(), "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
                PushManager.getInstance().initialize(application, GeTuiService.class);
            }
        }
    }


}
