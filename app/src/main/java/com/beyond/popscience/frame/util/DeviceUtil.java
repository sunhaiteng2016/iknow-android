package com.beyond.popscience.frame.util;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by yao.cui on 2017/7/6.
 */

public class DeviceUtil {

    /**
     * 获取deviceId
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
