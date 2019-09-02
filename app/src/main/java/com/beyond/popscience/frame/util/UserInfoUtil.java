package com.beyond.popscience.frame.util;

import android.text.TextUtils;

import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.google.gson.Gson;

/**
 * Created by linjinfa on 2017/6/13.
 * email 331710168@qq.com
 */
public final class UserInfoUtil {

    /**
     *
     */
    private Gson gson = new Gson();

    private UserInfoUtil() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static UserInfoUtil getInstance() {
        return UserInfoUtil.Singleton.INSTANCE.getInstance();
    }

    /**
     * 是否已经登录
     *
     * @return
     */
    public boolean isHasLogin() {
        return !TextUtils.isEmpty(getUserInfo().getToken());
    }

    /**
     * 获取在线的用户信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        String userStr = (String) SPUtils.get(BeyondApplication.getInstance(), "user_info", "{}");
        UserInfo info = null;
        try {
            info = gson.fromJson(userStr, UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (info == null) {
            info = new UserInfo();
        }
        return info;
    }

    /**
     * 保存用户信息
     *
     * @param userInfo
     */
    public void saveUserInfo(UserInfo userInfo) {
        SPUtils.put(BeyondApplication.getInstance(), "user_info", userInfo);
    }


    /**
     * 删除用户信息
     */
    public void removeUserInfo() {
        SPUtils.remove(BeyondApplication.getInstance(), "user_info");
    }

    /**
     * 单例
     */
    private enum Singleton {
        INSTANCE;

        private UserInfoUtil singleton;

        Singleton() {
            singleton = new UserInfoUtil();
        }

        public UserInfoUtil getInstance() {
            return singleton;
        }
    }

}
