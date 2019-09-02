package com.beyond.library.sharesdk;

import android.content.Context;
import android.text.TextUtils;

import com.beyond.library.sharesdk.links.Link;
import com.beyond.library.sharesdk.more.ArticleShare;
import com.beyond.library.sharesdk.more.MoreShare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 */
public class ShareSdkConfig {

    /**
     * 默认分享 渠道
     */
    private final static String[] DEFAULT_SHARE_NAMES = {
            Wechat.NAME,                // 微信
            WechatMoments.NAME,       // 朋友圈
            QQ.NAME,                           // QQ
            QZone.NAME,
            MoreShare.NAME,
            ArticleShare.NAME,
            Link.NAME,
//            SinaWeibo.NAME,               // 新浪微博
    };

    /**
     * platformName, AppId, AppKey, AppSecret, RedirectUrl, ShareByAppClient
     */
    private String[][] releaseAppKeys = {
            {WechatMoments.NAME, "wx17ba4e733d511e06", "", "4be02cfac65fa7ab5b638b129adbc530", "", "false"},
            {Wechat.NAME, "wx17ba4e733d511e06", "", "4be02cfac65fa7ab5b638b129adbc530", "", "false"},
            {QZone.NAME, "1105853487", "da1zXYJVBySZHUJ8", "", "", "true"},
            {QQ.NAME, "1105853487", "da1zXYJVBySZHUJ8", "", "", "true"},
            {SinaWeibo.NAME, "", "2318910061", "fde3065dd2e48afc3838cf2d852456f3", "http://www.daikuandawang.com/", "true"},
            {ShortMessage.NAME, "", "", "", "", "false"}
    };

    /**
     * app logo
     */
    private String logoPath;

    private ShareSdkConfig() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static ShareSdkConfig getInstance() {
        return ShareSdkConfig.Singleton.INSTANCE.getInstance();
    }

    /**
     * 注册sdk
     *
     * @param context
     * @param sdkAppKey
     */
    public ShareSdkConfig initSDK(Context context, String sdkAppKey) {
        ShareSDK.initSDK(context, sdkAppKey);
        ShareSDK.registerPlatform(Link.class);
        ShareSDK.registerPlatform(MoreShare.class);
        ShareSDK.registerPlatform(ArticleShare.class);
        return this;
    }

    /**
     * 注册 appkey
     *
     * @param appKeys
     */
    public ShareSdkConfig registerAppKey(String[][] appKeys) {
        for (int i = 0; i < appKeys.length; i++) {
            if (appKeys[i].length < 6) {
                continue;
            }
            String appId = appKeys[i][1];
            String appKey = appKeys[i][2];
            String appSecret = appKeys[i][3];
            String redirectUrl = appKeys[i][4];
            String shareByAppClient = appKeys[i][5];

            HashMap<String, Object> hashMap = new HashMap<>();
            if (!TextUtils.isEmpty(appId))
                hashMap.put("AppId", appId);
            if (!TextUtils.isEmpty(appKey))
                hashMap.put("AppKey", appKey);
            if (!TextUtils.isEmpty(appSecret))
                hashMap.put("AppSecret", appSecret);
            if (!TextUtils.isEmpty(redirectUrl))
                hashMap.put("RedirectUrl", redirectUrl);
            if (!TextUtils.isEmpty(shareByAppClient))
                hashMap.put("ShareByAppClient", shareByAppClient);
            hashMap.put("BypassApproval", "false");
            hashMap.put("Enable", "true");
            ShareSDK.setPlatformDevInfo(appKeys[i][0], hashMap);
        }
        return this;
    }

    /**
     * 获取默认分享渠道
     *
     * @return
     */
    public static String[] getDefaultShareNames() {
        return DEFAULT_SHARE_NAMES;
    }

    /**
     * 获取默认分享渠道
     *
     * @return
     */
    public Platform[] getDefaultPlatforms() {
        List<Platform> platformList = getDefaultPlatformList();
        return platformList.toArray(new Platform[0]);
    }

    /**
     * 获取默认分享渠道
     *
     * @return
     */
    public List<Platform> getDefaultPlatformList() {
        List<Platform> platformList = new ArrayList<>();
        for (int i = 0; i < DEFAULT_SHARE_NAMES.length; i++) {
            platformList.add(ShareSDK.getPlatform(DEFAULT_SHARE_NAMES[i]));
        }
        return platformList;
    }

    /**
     * 获取微信分享渠道
     *
     * @return
     */
    public List<Platform> getwetchatPlatformList() {
        List<Platform> platformList = new ArrayList<>();
      //  platformList.add(ShareSDK.getPlatform(Wechat.NAME));
       // platformList.add(ShareSDK.getPlatform(WechatMoments.NAME));
        return platformList;
    }

    /**
     * 获取APP的logo path
     *
     * @return
     */
    public String getLogoPath() {
        return logoPath;
    }

    /**
     * 设置
     *
     * @param logoPath
     */
    public ShareSdkConfig setLogoPath(String logoPath) {
        this.logoPath = logoPath;
        return this;
    }

    /**
     * 单例
     */
    private enum Singleton {
        INSTANCE;

        private ShareSdkConfig singleton;

        Singleton() {
            singleton = new ShareSdkConfig();
        }

        public ShareSdkConfig getInstance() {
            return singleton;
        }
    }

}
