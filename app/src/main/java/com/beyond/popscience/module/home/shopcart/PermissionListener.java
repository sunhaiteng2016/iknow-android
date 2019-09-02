package com.beyond.popscience.module.home.shopcart;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public interface PermissionListener {

        void onSucceed(int requestCode, @NonNull List<String> grantPermissions);

        void onFailed(int requestCode, @NonNull List<String> deniedPermissions);


}
