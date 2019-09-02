package com.beyond.popscience.frame.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Process;
import android.os.SystemClock;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.sharesdk.ShareSdkConfig;
import com.beyond.library.util.DateUtil;
import com.beyond.library.util.L;
import com.beyond.popscience.BuildConfig;
import com.beyond.popscience.LoginActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.pojo.SystemTime;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.FileUtil;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.entity.Address;
import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.Constant;
import com.helper.HxEaseuiHelper;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNotifyMsgHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMPushConfigs;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.push.EMPushConfig;
import com.hyphenate.push.EMPushHelper;
import com.hyphenate.push.EMPushType;
import com.hyphenate.push.PushListener;
import com.hyphenate.util.EMLog;
import com.igexin.sdk.PushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * Created by linjinfa on 2017/4/17.
 * email 331710168@qq.com
 */
public class BeyondApplication extends MultiDexApplication {
    /**
     * 测试地址
     * <p>
     * http://kpnew.appwzd.cn/kepu  正式
     */
     public final static String BaseUrl = "http://kpnew.appwzd.cn/kepu";
    //public final  static  String BaseUrl="http://19.16.10.201:8080/kepu";
    /**
     *
     */
    private static BeyondApplication instance = null;

    /**
     * 自定义Activity栈
     */
    private List<Activity> activityStackList = new ArrayList<>();

     /**
     * 服务器返回的时间
     */
    private SystemTime systemTime;

    /**
     * 缓存地址
     */
    private List<Address> cacheAddressList = new ArrayList<>();

    /**
     * 服务端返回的地址
     */
    private List<Address> serverAddressList = new ArrayList<>();
    /**
     *
     */
    private List<Address> areaAddressList = new ArrayList<>();
    /**
     *
     */
    private ThirdSDKManager.Location location;
    public static String HuaWeiCid;
    public static String xiaomiCid;
    public static String VivoCid;
    public static String OPPOCid;

    /**
     * platformName, AppId, AppKey, AppSecret, RedirectUrl, ShareByAppClient
     */
    private String[][] releaseAppKeys = {
            {WechatMoments.NAME, "wx73cd00ec8a9d5658", "9f361d7e2b67c2caecb371300d49d87f", "", "", "false"},
            {Wechat.NAME, "wx73cd00ec8a9d5658", "9f361d7e2b67c2caecb371300d49d87f", "", "", "false"},
            {QZone.NAME, "1106035278", "BXMcOaLcU1FtNIwJ", "", "", "true"},
            {QQ.NAME, "1106035278", "BXMcOaLcU1FtNIwJ", "", "", "true"},
            {SinaWeibo.NAME, "", "2318910061", "fde3065dd2e48afc3838cf2d852456f3", "http://www.daikuandawang.com/", "true"},
            {ShortMessage.NAME, "", "", "", "", "false"}
    };
    public static String currtMoblie;
    private EaseUI easeUI;
    private EMPushConfig.Builder builder;
    private EMOptions Emoptions;

    /**
     * @return
     */
    public static BeyondApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initBugLy();
        L.writeDebugLogs(BuildConfig.DEBUG_MODE);
        ImageLoaderUtil.init(this);
        Fresco.initialize(this);
        ShareSdkConfig.getInstance().initSDK(this, "1ec518278e780").registerAppKey(releaseAppKeys).setLogoPath(saveAppLogoForShare());

        ThirdSDKManager.getInstance().init(this, BuildConfig.DEBUG_MODE);
        initX5WebView();
        ZXingLibrary.initDisplayOpinion(this);
        initHuanXin();
        initHuaWei();
        initMiPush();
        initOPPO();
        initViVo();
        EMPushHelper.getInstance().setPushListener(new PushListener() {
            @Override
            public void onError(EMPushType pushType, long errorCode) {
                // TODO: 返回的errorCode仅9xx为环信内部错误，可从EMError中查询，其他错误请根据pushType去相应第三方推送网站查询。
            }
        });

    }

    private void initHuaWei() {
        HMSAgent.init(this);
        HMSAgent.Push.getToken(new GetTokenHandler() {
            @Override
            public void onResult(int i) {

            }
        });
        HMSAgent.Push.enableReceiveNotifyMsg(true, new EnableReceiveNotifyMsgHandler() {
            @Override
            public void onResult(int i) {
                Log.e("sht", "enableReceiveNormalMsg:end code=" + i);
            }
        });

    }

    //小米推送
    private void initMiPush() {
        if (shouldInit()) {
            MiPushClient.registerPush(this, "2882303761517881532", "5391788183532");
        }
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.e("xiaoMi", content, t);
            }

            @Override
            public void log(String content) {
                Log.e("xiaoMi", content);
            }
        };
        Logger.setLogger(this, newLogger);
    }


    private void initOPPO() {
        try {
            //在执行Oppo推送注册之前，需要先判断当前平台是否支持Oppo推送
            if (PushManager.getInstance().isPushTurnedOn(this)) {

                com.coloros.mcssdk.PushManager.getInstance().register(this, "189B2bXbFae8Cw4GoOoco40wW", "08989029cfe041b54e8520a51A609b4c", new PushCallback() {
                    @Override
                    public void onRegister(int i, String s) {
                        OPPOCid = s;
                    }

                    @Override
                    public void onUnRegister(int i) {

                    }

                    @Override
                    public void onGetAliases(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onSetAliases(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onUnsetAliases(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onSetUserAccounts(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onUnsetUserAccounts(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onGetUserAccounts(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onSetTags(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onUnsetTags(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onGetTags(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onGetPushStatus(int i, int i1) {

                    }

                    @Override
                    public void onSetPushTime(int i, String s) {

                    }

                    @Override
                    public void onGetNotificationStatus(int i, int i1) {

                    }
                });//setPushCallback接口也可设置callback
            }
        } catch (Exception e) {

        }

    }

    private void initViVo() {
        PushClient.getInstance(getApplicationContext()).initialize();
        //并且打开推送服务
        PushClient.getInstance(this).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                if (i == 0) {
                    Log.e("NPL", "打开推送服务成功");
                } else {
                    Log.e("NPL", "打开推送服务失败");
                }
            }
        });
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private void initHuanXin() {
        builder = new EMPushConfig.Builder(this);
        Emoptions = new EMOptions();
        builder.enableOppoPush("189B2bXbFae8Cw4GoOoco40wW", "64F35c41B1f70E12F32A086Bf4A14819")
                .enableMiPush("2882303761517881532", "5391788183532")
                .enableHWPush();
        Emoptions.setPushConfig(builder.build());
        EaseUI.getInstance().init(this.getApplicationContext(), Emoptions);
        HxEaseuiHelper.getInstance().init(this.getApplicationContext());
    }

    /**
     * 腾讯X5内核浏览器
     */
    private void initX5WebView() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                if (b) {
                    Log.e("sunht", "腾讯内核浏览器初始化成功");
                }
            }
        };
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * 初始化Bugly
     */
    private void initBugLy() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "f8e88b5162", BuildConfig.DEBUG, strategy);
    }

    /**
     * 获取进程号对应的进程名
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存app logo
     */
    private String saveAppLogoForShare() {
        Bitmap appLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String appLogoPath = FileUtil.saveBitmap(appLogo, new File(FileUtil.getDirectory(VKConstants.CACHE_IMG), "app_logo.jpg").getAbsolutePath());
        appLogo.recycle();
        return appLogoPath;
    }

    /**
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (!activityStackList.contains(activity)) {
            activityStackList.add(activity);
        }
    }

    /**
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activityStackList.contains(activity)) {
            activityStackList.remove(activity);
        }
    }


    /**
     * 退出指定界面
     */
    public void popActivity(Class... activityClss) {
        if (activityClss == null) {
            return;
        }
        try {
            Activity activities[] = activityStackList.toArray(new Activity[activityStackList.size()]);
            if (activities != null && activities.length != 0) {
                int count = activities.length;
                for (int i = 0; i < count; i++) {
                    Activity activity = activities[i];
                    if (activity != null) {
                        for (Class activityCls : activityClss) {
                            if (activity.getClass().getName().equals(activityCls.getName())) {
                                activityStackList.remove(activity);
                                activity.finish();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出所有界面
     */
    public void popAllActivityExcept(Activity exceptActivity) {
        try {
            Activity activities[] = activityStackList.toArray(new Activity[activityStackList.size()]);
            if (activities != null && activities.length != 0) {
                int count = activities.length;
                for (int i = 0; i < count; i++) {
                    Activity activity = activities[i];
                    if (activity != null) {
                        if (exceptActivity != null && !activity.getClass().getName().equals(exceptActivity.getClass().getName())) {
                            activityStackList.remove(activity);
                            activity.finish();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = this.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前服务端时间
     *
     * @return
     */
    public String getCurrSystemTime() {
        if (systemTime == null || TextUtils.isEmpty(systemTime.getSystemTime())) {
            return DateUtil.getNowString();
        }
        long currTimeStamp = SystemClock.elapsedRealtime();
        String currDateTime = DateUtil.getDateAfterMilliseconds(systemTime.getSystemTime(), currTimeStamp - systemTime.getUpdateTimeStamp());
        if (TextUtils.isEmpty(currDateTime)) {
            return DateUtil.getNowString();
        }
        return currDateTime;
    }

    /**
     * 获取当前服务端当前时间戳
     *
     * @return
     */
    public long getCurrSystemTimeStamp() {
        Date date = DateUtil.toDateTime(getCurrSystemTime());
        if (date != null) {
            return date.getTime();
        }
        return 0;
    }

    /**
     * 获取当前服务端当前时间戳
     *
     * @return
     */
    public String getCurrSystemTimeStampStr() {
        long timeStamp = getCurrSystemTimeStamp();
        if (timeStamp > 0) {
            return String.valueOf(timeStamp);
        }
        return null;
    }

    /**
     * 设置服务端获取到的时间
     *
     * @param systemTime
     */
    public void setSystemTime(SystemTime systemTime) {
        this.systemTime = systemTime;
    }

    public SystemTime getSystemTime() {
        return systemTime;
    }

    /**
     * 获取地址
     *
     * @return
     */
    public List<Address> getCacheAddressList() {
        if (serverAddressList != null && serverAddressList.size() != 0 && cacheAddressList.size() == 0) {
            try {
                cacheAddressList.clear();
                cacheAddressList.addAll(serverAddressList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cacheAddressList == null || cacheAddressList.size() == 0) {
            StringBuilder sb = new StringBuilder();
            AssetManager am = getAssets();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(am.open("address.json")));
                String next = "";
                while (null != (next = br.readLine())) {
                    sb.append(next);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sb.delete(0, sb.length());
            }
            String json = sb.toString().trim();
            try {
                List<Address> addressList = new Gson().fromJson(json, new TypeToken<List<Address>>() {
                }.getType());
                if (addressList != null) {
                    cacheAddressList.clear();
                    cacheAddressList.addAll(addressList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cacheAddressList;
    }

    /**
     * @return
     */
    public List<Address> getServerAddressList() {
        return serverAddressList;
    }

    /**
     * @param serverAddressList
     */
    public void setServerAddressList(List<Address> serverAddressList) {
        if (serverAddressList != null) {
            this.serverAddressList = serverAddressList;
        }
    }

    /**
     * @param areaAddressList
     */
    public void setAreaAddressList(List<Address> areaAddressList) {
        if (areaAddressList != null) {
            this.areaAddressList = areaAddressList;
        }
    }

    public List<Address> getAreaAddressList() {
        return areaAddressList;
    }

    /**
     * @return
     */
    public ThirdSDKManager.Location getLocation() {
        return location;
    }

    /**
     * @param location
     */
    public void setLocation(ThirdSDKManager.Location location) {
        this.location = location;
    }
}
