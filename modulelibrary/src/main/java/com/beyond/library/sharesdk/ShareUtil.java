package com.beyond.library.sharesdk;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;

import com.beyond.library.R;
import com.beyond.library.sharesdk.more.ArticleShare;
import com.beyond.library.sharesdk.more.MoreShare;
import com.beyond.library.sharesdk.shareInfo.BaseShareFields;
import com.beyond.library.sharesdk.shareInfo.ShareInfoFactory;
import com.beyond.library.util.InvokeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareUtil {

    /*
     ************************************************ Share SDK Action Callback ************************************************
     */
    private static boolean isShow = false;
    public final static int SHARE_RESULT_NORMAL_FINISH = 0;
    public final static int SHARE_RESULT_CANCEL = 1;

    public interface OnShareListener {
        void onResult(int result);
    }

    public static OnShareListener onShareListener = null;

    public static void onResult(boolean normal) {
        if (onShareListener != null) {
            onShareListener.onResult(normal ? SHARE_RESULT_NORMAL_FINISH : SHARE_RESULT_CANCEL);
        }
        onShareListener = null;
        isShow = false;
    }

    /*
     ******************************************************** Share SDK ********************************************************
     */

    /**
     * 分享
     *
     * @param context
     * @param webViewShare
     */
    public static void directShare(Context context, WebViewShare webViewShare) {
        Map<String, WebViewShare> webViewShareMap = new HashMap<>();
        String shareNames[] = ShareSdkConfig.getInstance().getDefaultShareNames();
        for (String shareName : shareNames) {
            webViewShareMap.put(shareName, webViewShare);
        }
        directShare(context, false, null, webViewShareMap, null);
    }

    /**
     * 分享
     *
     * @param context
     * @param webViewShare
     */
    public static void directShare(Context context, WebViewShare webViewShare, PlatformListFakeActivity.OnShareButtonClickListener onShareButtonClickListener) {
        Map<String, WebViewShare> webViewShareMap = new HashMap<>();
        String shareNames[] = ShareSdkConfig.getInstance().getDefaultShareNames();
        for (String shareName : shareNames) {
            webViewShareMap.put(shareName, webViewShare);
        }
        directShare(context, false, null, webViewShareMap, null, onShareButtonClickListener);
    }


    /**
     * 分享
     *
     * @param context
     * @param webViewShare
     */
    public static void directShare(Context context, WebViewShare webViewShare, PlatformListFakeActivity.OnResultListener onResultListener) {
        Map<String, WebViewShare> webViewShareMap = new HashMap<>();
        String shareNames[] = ShareSdkConfig.getInstance().getDefaultShareNames();
        for (String shareName : shareNames) {
            webViewShareMap.put(shareName, webViewShare);
        }
        directShare(context, false, null, webViewShareMap, null, null, onResultListener);
    }

    /**
     * 分享
     *
     * @param context
     * @param webViewShare
     */
    public static void directShareWeiXin(Context context, WebViewShare webViewShare, PlatformListFakeActivity.OnResultListener onResultListener) {
        Map<String, WebViewShare> webViewShareMap = new HashMap<>();
        webViewShareMap.put(Wechat.NAME, webViewShare);
        webViewShareMap.put(WechatMoments.NAME, webViewShare);
        directShareWetChat(context, false, null, webViewShareMap, null, null, onResultListener);
    }

    /**
     * 分享
     *
     * @param context
     * @param webViewShare
     */
    public static void directShare(Context context, String shareNames[], WebViewShare webViewShare) {
        Map<String, WebViewShare> webViewShareMap = new HashMap<>();
        for (String shareName : shareNames) {
            webViewShareMap.put(shareName, webViewShare);
        }
        directShare(context, false, null, webViewShareMap, null);
    }

    /**
     * @param context
     * @param isBypassApproval 是否绕过审核
     * @param platformName
     * @param obj
     * @param listener
     */
    public static void directShare(Context context, boolean isBypassApproval, List<Platform> platformName, Object obj, OnShareListener listener) {
        directShare(context, isBypassApproval, platformName, obj, listener, null);
    }

    /**
     * @param context
     * @param isBypassApproval 是否绕过审核
     * @param platformName
     * @param obj
     * @param listener
     */
    public static void directShare(Context context, boolean isBypassApproval, List<Platform> platformName, Object obj, OnShareListener listener, PlatformListFakeActivity.OnShareButtonClickListener onShareButtonClickListener) {
        directShare(context, isBypassApproval, platformName, obj, listener, null, null);
    }

    /**
     * @param context
     * @param isBypassApproval 是否绕过审核
     * @param platformName
     * @param obj
     * @param listener
     */
    public static void directShare(Context context, boolean isBypassApproval, List<Platform> platformName, Object obj, OnShareListener listener, PlatformListFakeActivity.OnShareButtonClickListener onShareButtonClickListener, PlatformListFakeActivity.OnResultListener onResultListener) {
        if (isShow) {
            return;
        }

        if (platformName == null) {
            platformName = ShareSdkConfig.getInstance().getDefaultPlatformList();
        }
        if (onShareListener == null) {
            onShareListener = listener;
        }
        HashMap<String, BaseShareFields> fields = ShareInfoFactory.create(context, platformName, obj);
        if (fields == null) {
            return;
        }
        if (isBypassApproval) {
            if (platformName != null && platformName.size() != 0) {
                for (Platform platform : platformName) {
                    //绕过审核
                    InvokeUtil.setFieldValue(platform, "c", true);
                }
            }
        }
        if (platformName.size() > 1) {
            OnekeyShare share = null;
            if (context instanceof Activity) {
                share = new OnekeyShare(((Activity) context).getApplicationContext());
            } else {
                share = new OnekeyShare(context);
            }
            share.setShareParams(fields);
            share.setOnShareButtonClickListener(onShareButtonClickListener);
            share.setOnResultListener(onResultListener);
            share.show(context);
            isShow = true;
        } else {
            singleShare(context, platformName, fields);
        }
    }

    /**
     * @param context
     * @param isBypassApproval 是否绕过审核
     * @param platformName
     * @param obj
     * @param listener
     */
    public static void directShareWetChat(Context context, boolean isBypassApproval, List<Platform> platformName, Object obj, OnShareListener listener, PlatformListFakeActivity.OnShareButtonClickListener onShareButtonClickListener, PlatformListFakeActivity.OnResultListener onResultListener) {
        if (isShow) {
            return;
        }
        if (platformName == null) {
            platformName = ShareSdkConfig.getInstance().getwetchatPlatformList();
        }
        if (onShareListener == null) {
            onShareListener = listener;
        }
        HashMap<String, BaseShareFields> fields = ShareInfoFactory.create(context, platformName, obj);
        if (fields == null) {
            return;
        }
        if (isBypassApproval) {
            if (platformName != null && platformName.size() != 0) {
                for (Platform platform : platformName) {
                    //绕过审核
                    InvokeUtil.setFieldValue(platform, "c", true);
                }
            }
        }
        if (platformName.size() > 1) {
            OnekeyShare share = null;
            if (context instanceof Activity) {
                share = new OnekeyShare(((Activity) context).getApplicationContext());
            } else {
                share = new OnekeyShare(context);
            }
            share.setShareParams(fields);
            // QQ.NAME,                           // QQ
            //            QZone.NAME,                  // QQ空间
            //            MoreShare.NAME,
            share.addHiddenPlatform(QQ.NAME);
            share.addHiddenPlatform(QZone.NAME);
            share.addHiddenPlatform(MoreShare.NAME);
            share.setOnShareButtonClickListener(onShareButtonClickListener);
            share.setOnResultListener(onResultListener);
            share.show(context);
            isShow = true;
        } else {
            singleShare(context, platformName, fields);
        }
    }

    /**
     * 无界面，直接分享
     */
    private static void singleShare(Context context, List<Platform> platforms, HashMap<String, BaseShareFields> mixFields) {
        if (platforms == null || platforms.isEmpty() || mixFields == null) {
            return;
        }
        for (Platform item : platforms) {
            String platformName = item.getName();
            HashMap<Platform, HashMap<String, Object>> shareData = new HashMap<>();
            shareData.put(item, mixFields.get(platformName).getShareParamsMap());
            new OnekeyShare(context).share(shareData);
        }
    }

}
