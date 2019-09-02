/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.beyond.library.sharesdk;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.beyond.library.sharesdk.links.Link;
import com.beyond.library.sharesdk.shareInfo.BaseShareFields;
import com.beyond.library.sharesdk.theme.PlatformListPage;
import com.beyond.library.util.InvokeUtil;
import com.mob.tools.utils.UIHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import static com.mob.tools.utils.R.getStringRes;

/**
 * 快捷分享的入口
 * <p/>
 * 通过不同的setter设置参数，然后调用{@link #show(Context)}方法启动快捷分享
 */
public class OnekeyShare implements PlatformActionListener, Callback {
    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;

    private HashMap<String, BaseShareFields> mixFields;
    private ArrayList<CustomerLogo> customers;
    private boolean silent;
    private PlatformActionListener callback;
    private ShareContentCustomizeCallback customizeCallback;
    private boolean disableSSO;
    private HashMap<String, String> hiddenPlatforms;
    private View bgView;
    private SpannableString shareTip;
    private boolean shareProfitMode;

    private Context context;
    private PlatformListFakeActivity.OnShareButtonClickListener onShareButtonClickListener;
    private PlatformListFakeActivity.IndirectListener indirectListener;
    protected PlatformListFakeActivity.OnResultListener onResultListener;

    public OnekeyShare(Context context) {
        mixFields = new HashMap<>();
        this.context = context;
        customers = new ArrayList<CustomerLogo>();
        callback = this;
        hiddenPlatforms = new HashMap<String, String>();
        silent = true;
        ShareSDK.initSDK(context);
        ShareSDK.registerPlatform(Link.class);
    }

    public void setShareParams(HashMap<String, BaseShareFields> mixFields) {
        this.mixFields = mixFields;
    }

    public void show(Context context) {

        // 打开分享菜单的统计
        ShareSDK.logDemoEvent(1, null);

        // 显示方式是由platform和silent两个字段控制的
        // 如果platform设置了，则无须显示九宫格，否则都会显示；
        // 如果silent为true，表示不进入编辑页面，否则会进入。
        // 本类只判断platform，因为九宫格显示以后，事件交给PlatformGridView控制
        // 当platform和silent都为true，则直接进入分享；
        // 当platform设置了，但是silent为false，则判断是否是“使用客户端分享”的平台，
        // 若为“使用客户端分享”的平台，则直接分享，否则进入编辑页面
        if (mixFields.containsKey("platform")) {
            String name = String.valueOf(mixFields.get("platform"));
            Platform platform = ShareSDK.getPlatform(name);

            if (silent
                    || ShareCore.isUseClientToShare(name)
                    || platform instanceof CustomPlatform
                    ) {
                HashMap<Platform, HashMap<String, Object>> shareData = new HashMap<>();
                shareData.put(ShareSDK.getPlatform(name), mixFields.get(name).getShareParamsMap());
                share(shareData);
                return;
            }
        }


        PlatformListFakeActivity platformListFakeActivity = new PlatformListPage();
        platformListFakeActivity.setMixFields(mixFields);
        platformListFakeActivity.setSilent(silent);
        platformListFakeActivity.setCustomerLogos(customers);
        platformListFakeActivity.setBackgroundView(bgView);
        platformListFakeActivity.setShareTip(shareTip);
        platformListFakeActivity.setShareProfitMode(shareProfitMode);
        platformListFakeActivity.setHiddenPlatforms(hiddenPlatforms);
        platformListFakeActivity.setOnShareButtonClickListener(onShareButtonClickListener);
        platformListFakeActivity.setIndirectListener(indirectListener);
        platformListFakeActivity.setThemeShareCallback(new ThemeShareCallback() {
            @Override
            public void doShare(HashMap<Platform, HashMap<String, Object>> shareData) {
                share(shareData);
            }
        });
        platformListFakeActivity.show(context, null);
    }

    /**
     * 是否直接分享
     */
    public void setSilent(boolean silent) {
        this.silent = silent;
    }


    /**
     * 设置自定义的外部回调
     */
    public void setCallback(PlatformActionListener callback) {
        this.callback = callback;
    }

    /**
     * 返回操作回调
     */
    public PlatformActionListener getCallback() {
        return callback;
    }

    /**
     * 设置用于分享过程中，根据不同平台自定义分享内容的回调
     */
    public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
        customizeCallback = callback;
    }

    /**
     * 返回自定义分享内容的回调
     */
    public ShareContentCustomizeCallback getShareContentCustomizeCallback() {
        return customizeCallback;
    }

    /**
     * 设置自己图标和点击事件，可以重复调用添加多次
     */
    public void setCustomerLogo(CustomerLogo cl) {
        customers.add(cl);
    }


    /**
     * 设置一个总开关，用于在分享前若需要授权，则禁用sso功能
     */
    public void disableSSOWhenAuthorize() {
        disableSSO = true;
    }


    /**
     * 添加一个隐藏的platform
     */
    public void addHiddenPlatform(String platform) {
        hiddenPlatforms.put(platform, platform);
    }

    public void setShareProfitMode(boolean shareProfitMode) {
        this.shareProfitMode = shareProfitMode;
    }

    public void setEditPageBackground(View bgView) {
        this.bgView = bgView;
    }

    public void setShareTip(SpannableString shareTip) {
        this.shareTip = shareTip;
    }

    public void setOnShareButtonClickListener(PlatformListFakeActivity.OnShareButtonClickListener onShareButtonClickListener) {
        this.onShareButtonClickListener = onShareButtonClickListener;
    }

    public void setIndirectListener(PlatformListFakeActivity.IndirectListener listener) {
        this.indirectListener = listener;
    }

    /**
     * 循环执行分享
     */
    public void share(HashMap<Platform, HashMap<String, Object>> shareData) {
        boolean started = false;
        for (Entry<Platform, HashMap<String, Object>> ent : shareData.entrySet()) {
            Platform plat = ent.getKey();
            plat.SSOSetting(disableSSO);

            //反射设置 context  @linjinfa
            InvokeUtil.setValue(plat, "setContext", context, Context.class);

            String name = plat.getName();

            boolean isSina = SinaWeibo.NAME.equals(name);
            if (isSina && !plat.isClientValid()) {
                Message msg = new Message();
                msg.what = MSG_TOAST;
                msg.obj = "新浪微博 版本过低或者没有安装，需要升级或安装新浪微博才能使用！";
                UIHandler.sendMessage(msg, this);
                continue;
            }

            HashMap<String, Object> data = ent.getValue();
            int shareType = Platform.SHARE_TEXT;
            String imagePath = String.valueOf(data.get("imagePath"));
            if (imagePath != null && (new File(imagePath)).exists()) {
                shareType = Platform.SHARE_IMAGE;
                if (imagePath.endsWith(".gif")) {
                    shareType = Platform.SHARE_EMOJI;
                } else if (data.containsKey("url") && data.get("url") != null && !TextUtils.isEmpty(data.get("url").toString())) {
                    shareType = Platform.SHARE_WEBPAGE;
                    if (data.containsKey("musicUrl") && data.get("musicUrl") != null && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
                        shareType = Platform.SHARE_MUSIC;
                    }
                }
            } else {
                Bitmap viewToShare = (Bitmap) data.get("viewToShare");
                if (viewToShare != null && !viewToShare.isRecycled()) {
                    shareType = Platform.SHARE_IMAGE;
                    if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
                        shareType = Platform.SHARE_WEBPAGE;
                        if (data.containsKey("musicUrl") && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
                            shareType = Platform.SHARE_MUSIC;
                        }
                    }
                } else {
                    Object imageUrl = data.get("imageUrl");
                    if (imageUrl != null && !TextUtils.isEmpty(String.valueOf(imageUrl))) {
                        shareType = Platform.SHARE_IMAGE;
//                        if (String.valueOf(imageUrl).endsWith(".gif") && data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
//                            shareType = Platform.SHARE_WEBPAGE;
//                        } else
                        if (String.valueOf(imageUrl).endsWith(".gif")) {
                            shareType = Platform.SHARE_EMOJI;
                        } else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
                            shareType = Platform.SHARE_WEBPAGE;
                            if (data.containsKey("musicUrl") && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
                                shareType = Platform.SHARE_MUSIC;
                            }
                        }
                    } else {
                        Object imageData = data.get("imageData");
                        if (imageData != null) {
                            shareType = Platform.SHARE_IMAGE;
                        }
                    }
                }
            }
            data.put("shareType", shareType);

            if (!started) {
                started = true;
//				if (this == callback) {
//					int resId = getStringRes(context, "sharing");
//					if (resId > 0) {
//						showNotification(context.getString(resId));
//					}
//				}
            }
            plat.setPlatformActionListener(callback);
            ShareCore shareCore = new ShareCore();
            shareCore.setShareContentCustomizeCallback(customizeCallback);
            shareCore.share(plat, data);
        }
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();

        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);

        // 分享失败的统计
        ShareSDK.logDemoEvent(4, platform);
    }

    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        Platform platform = null;
        switch (msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1: {
                        if(msg.obj instanceof Platform){
                            platform = (Platform) msg.obj;
                        }
                        if(onResultListener!=null){
                            onResultListener.onSuccess(platform);
                        }

                        // 成功
//						int resId = getStringRes(context, "share_completed");
//                        Platform platform = (Platform) msg.obj;
//						if (resId > 0) {
//                            if(SinaWeibo.NAME.equals(platform.getName())){
//                                showNotification(context.getString(resId));
//                            }
//						}
                    }
                    break;
                    case 2: {
                        if(onResultListener!=null){
                            onResultListener.onCancel(platform);
                        }
                        // 失败
                        String expName = msg.obj.getClass().getSimpleName();
                        if ("WechatClientNotExistException".equals(expName)
                                || "WechatTimelineNotSupportedException".equals(expName)
                                || "WechatFavoriteNotSupportedException".equals(expName)) {
                            int resId = getStringRes(context, "wechat_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("GooglePlusClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "google_plus_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("QQClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "qq_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("YixinClientNotExistException".equals(expName)
                                || "YixinTimelineNotSupportedException".equals(expName)) {
                            int resId = getStringRes(context, "yixin_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("KakaoTalkClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "kakaotalk_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("KakaoStoryClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "kakaostory_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else if ("WhatsAppClientNotExistException".equals(expName)) {
                            int resId = getStringRes(context, "whatsapp_client_inavailable");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        } else {
                            int resId = getStringRes(context, "share_failed");
                            if (resId > 0) {
                                showNotification(context.getString(resId));
                            }
                        }
                    }
                    break;
                    case 3: {
                        if(msg.obj instanceof Platform){
                            platform = (Platform) msg.obj;
                        }
                        if(onResultListener!=null){
                            onResultListener.onCancel(platform);
                        }
                        // 取消
//                        int resId = getStringRes(context, "share_canceled");
//                        if (resId > 0) {
//                            showNotification(context.getString(resId));
//                        }
                    }
                    break;
                }
            }
            break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
            break;
        }
        return false;
    }

    // 在状态栏提示分享操作
    private void showNotification(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public PlatformListFakeActivity.OnResultListener getOnResultListener() {
        return onResultListener;
    }

    public void setOnResultListener(PlatformListFakeActivity.OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }
}
