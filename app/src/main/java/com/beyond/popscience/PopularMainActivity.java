package com.beyond.popscience;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.util.L;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.TabClickCountRestUsage;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.MessageCount;
import com.beyond.popscience.locationgoods.bean.MessageCountThree;
import com.beyond.popscience.locationgoods.bean.MessageCountTwo;
import com.beyond.popscience.locationgoods.bean.MessageShuaxin;
import com.beyond.popscience.locationgoods.bean.MessageShuaxinTwo;
import com.beyond.popscience.locationgoods.bean.sel;
import com.beyond.popscience.locationgoods.http.NotificationRestUsage;
import com.beyond.popscience.module.home.entity.HomeMessage;
import com.beyond.popscience.module.home.fragment.FriendsListFragment;
import com.beyond.popscience.module.home.fragment.HomeFragment;
import com.beyond.popscience.module.home.fragment.MessagessFragment;
import com.beyond.popscience.module.home.fragment.MyFragment;
import com.beyond.popscience.module.home.fragment.NewsListFragment;
import com.beyond.popscience.module.home.fragment.ServiceFragment;
import com.beyond.popscience.module.home.shopcart.NewFragmentActivity;
import com.beyond.popscience.module.mservice.GoodsListActivity;
import com.beyond.popscience.utils.sun.util.ScreenUtils;
import com.beyond.popscience.widget.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.helper.HxEaseuiHelper;
import com.helper.utils.APPConfig;
import com.helper.utils.SharedPreferencesUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 项目主页
 * <p>
 * Created by yao.cui on 2017/6/8.
 */

public class PopularMainActivity extends BaseActivity {

    private static int notificationId = 1;
    private final int REQUEST_PERMISSION = 101;
    private static final int TASK_POST_TAB_CLICK = 102;
    @BindView(R.id.flChange)
    FrameLayout flChange;
    @BindView(R.id.tl)
    CommonTabLayout tl;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_bdc)
    RadioButton rbBdc;
    @BindView(R.id.rb_xx)
    RadioButton rbXx;
    @BindView(R.id.rb_fw)
    RadioButton rbFw;
    @BindView(R.id.rb_txl)
    RadioButton rbTxl;
    @BindView(R.id.rb_wd)
    RadioButton rbWd;

    @Request
    private TabClickCountRestUsage mTabClickCountRestUsage;

    private final TabEntity[] TABS = {
            new TabEntity("首页", R.drawable.icon_home_blue, R.drawable.icon_home, HomeFragment.class),
            new TabEntity("本地产", R.drawable.icon_town_blue, R.drawable.icon_town, PhysicalStoreFragment.class),
            new TabEntity("消息", R.drawable.message111, R.drawable.message222, MessagessFragment.class),
            new TabEntity("服务", R.drawable.fwcheck, R.drawable.fwuncheck, ServiceFragment.class),
            new TabEntity("通讯录", R.drawable.faxian111, R.drawable.faxian222, FriendsListFragment.class),
            new TabEntity("我的", R.drawable.icon_my_blue, R.drawable.icon_my, MyFragment.class),
    };
    private Fragment showFragment;

    /**
     * 设置页面最外层布局 FitsSystemWindows 属性
     *
     * @param activity
     * @param value
     */
    public static void setFitsSystemWindows(Activity activity, boolean value) {
        ViewGroup contentFrameLayout = (ViewGroup) activity.findViewById(android.R.id.content);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(value);
        }
    }

    /**
     * 缓存已经初始化过的Fragment
     */
    private ArrayMap<String, Fragment> mCacheFragmentMap = new ArrayMap<>();
    private String localareaName;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PopularMainActivity.class);
        context.startActivity(intent);
    }

    private CommonTabLayout mTabLayout;

    @Override
    protected void onResume() {
        if (BeyondApplication.getInstance().getLocation() == null) {
            startLocation();
        }
        super.onResume();
        String id = getIntent().getStringExtra("fragment_id");
        if (id != null && !id.isEmpty()) {
            switchTab(Integer.valueOf(id));
            getIntent().putExtra("fragment_id", "");
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                //注册环信
                AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
                Map<String, String> map = new HashMap<>();
                appBaseRestUsageV1.get(BeyondApplication.BaseUrl + "/im/registerIm", map, new NewCustomResponseHandler() {
                    @Override
                    public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                        super.onSuccess(httpStatusCode, headerMap, responseStr);
                        EMClient.getInstance().login(UserInfoUtil.getInstance().getUserInfo().getUserId(), "123456", new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                SharedPreferencesUtils.setParam(PopularMainActivity.this, APPConfig.USER_NAME, UserInfoUtil.getInstance().getUserInfo().getUserId());
                                SharedPreferencesUtils.setParam(PopularMainActivity.this, APPConfig.PASS_WORD, "123456");
                                EMClient.getInstance().updateCurrentUserNick(UserInfoUtil.getInstance().getUserInfo().getNickName());
                            }

                            @Override
                            public void onError(int code, String error) {

                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headerMap, responseString, throwable);
                    }
                });
            }
        }.start();
        // 获取华为 HMS 推送 token
        HMSPushHelper.getInstance().getHMSToken(this);
        //setBadgeNum(0, this);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);

        initDialog();
    }

    private void initDialog() {
        NotificationManagerCompat notification = NotificationManagerCompat.from(this);
        boolean isEnabled = notification.areNotificationsEnabled();
        if (!isEnabled) {
            //未打开通知
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请在“通知”中打开通知权限,才能正常使用推送功能！")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("android.provider.extra.APP_PACKAGE", PopularMainActivity.this.getPackageName());
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", PopularMainActivity.this.getPackageName());
                                intent.putExtra("app_uid", PopularMainActivity.this.getApplicationInfo().uid);
                                startActivity(intent);
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + PopularMainActivity.this.getPackageName()));
                            } else if (Build.VERSION.SDK_INT >= 15) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", PopularMainActivity.this.getPackageName(), null));
                            }
                            startActivity(intent);
                        }
                    })
                    .create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        }
    }

    private void getPushSize() {
        //这个地方还要把总和累加起来
        OkhttpUtil.okHttpGet("https://shop.appwzd.cn/push/pushSize/0/" + UserInfoUtil.getInstance().getUserInfo().getUserId(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                BaseResponse baseResponse = new Gson().fromJson(response, BaseResponse.class);
                Object data = baseResponse.getData();
                int sizess = new Double((Double) data).intValue();
                int count = EMClient.getInstance().chatManager().getUnreadMessageCount();
                EventBus.getDefault().post(new MessageCount(count));
                int totailSize = sizess + count;
                if (totailSize > 0) {
                    number.setText(String.valueOf(totailSize));
                    number.setVisibility(View.VISIBLE);
                } else {
                    number.setVisibility(View.INVISIBLE);
                }
                EventBus.getDefault().post(new MessageShuaxin());

                //同时设置小米华为数量
                setBadgeNum(totailSize, PopularMainActivity.this);
                setNotificationBadge(totailSize, PopularMainActivity.this);
            }
        });
        //这个地方还要把总和累加起来
        OkhttpUtil.okHttpGet("https://shop.appwzd.cn/push/pushSize/1/" + UserInfoUtil.getInstance().getUserInfo().getUserId(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseResponse baseResponse = new Gson().fromJson(response, BaseResponse.class);
                Object data = baseResponse.getData();
                int sizess = new Double((Double) data).intValue();
                EventBus.getDefault().post(new MessageCountTwo(sizess));
            }
        });
        //这个地方还要把总和累加起来
        OkhttpUtil.okHttpGet("https://shop.appwzd.cn/push/pushSize/2/" + UserInfoUtil.getInstance().getUserInfo().getUserId(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseResponse baseResponse = new Gson().fromJson(response, BaseResponse.class);
                Object data = baseResponse.getData();
                int sizess = new Double((Double) data).intValue();
                EventBus.getDefault().post(new MessageCountThree(sizess));
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void switchTab(int id) {
        if (id == 3) {
            //购物
            ServiceCategory category = new ServiceCategory();
            category.setTabId("5");
            category.setTabType("2");
            category.setTabName("商品买卖");
            category.setTabPic("'http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/7d1c6754e74941fa8d3e45208e8032c1.jpg");
            GoodsListActivity.startActivity(PopularMainActivity.this, category);
        }
        if (id == 2) {
            Intent intent = new Intent(PopularMainActivity.this, NewFragmentActivity.class);
            intent.putExtra("name", "乡镇");
            intent.putExtra("position", 3);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        destroyLocation();
        ThirdSDKManager.getInstance().destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void initUI() {
        BeyondApplication.getInstance().getCacheAddressList();
        //初始化第三方sdk
        ThirdSDKManager.getInstance().init(this);

        mTabLayout = (CommonTabLayout) findViewById(R.id.tl);

        mTabLayout.setTabData(new ArrayList<CustomTabEntity>(Arrays.asList(TABS)));
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                switchFragment(i);
                if (i != 4) {//排除"我的"模块的统计
                    //统计底部栏的用户点击次数
                    mTabClickCountRestUsage.postTabClick(TASK_POST_TAB_CLICK, i + 1);
                }
            }

            @Override
            public void onTabReselect(int i) {

            }
        });
        mTabLayout.setCurrentTab(0);
        switchFragment(0);

        checkPermission();//检查权限
        requestCheckVersion(false);
        if ("10086".equals(getIntent().getStringExtra("flag"))) {
            mTabLayout.setCurrentTab(0);
            switchFragment(0);
        }
        if ("5".equals(getIntent().getStringExtra("flag"))) {
            mTabLayout.setCurrentTab(2);
            rbXx.setChecked(true);
            rbHome.setChecked(false);
            switchFragment(2);
            EventBus.getDefault().post(new sel(1));
        }
        if ("6".equals(getIntent().getStringExtra("flag"))) {
            mTabLayout.setCurrentTab(2);
            rbXx.setChecked(true);
            rbHome.setChecked(false);
            switchFragment(2);
            EventBus.getDefault().post(new sel(2));
        }
        setFitsSystemWindows(this, true);
        int width = ScreenUtils.displayMetrics(PopularMainActivity.this).widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(width / 3, 4, 0, 0);
        number.setLayoutParams(params);
        getPushSize();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(MainMessage message) {
        mTabLayout.setCurrentTab(2);
        rbXx.setChecked(true);
        rbHome.setChecked(false);
        switchFragment(2);
        EventBus.getDefault().post(new sel(message.flag));
    }


    public void ss(int flag) {
        mTabLayout.setCurrentTab(2);
        switchFragment(2);
        EventBus.getDefault().post(new sel(flag));
    }

    /**
     * 设置华为角标
     */
    public void setBadgeNum(int num, Context context) {
        try {
            Bundle bunlde = new Bundle();
            bunlde.putString("package", "com.gymj.apk.xj");
            bunlde.putString("class", "com.beyond.popscience.module.home.WelcomeActivity");
            bunlde.putInt("badgenumber", num);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //小米 设置方法
    public void setNotificationBadge(int count, Context context) {
        try {
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, String.valueOf(count == 0 ? "" : count));  // 设置信息数-->这种发送必须是miui 6才行
        } catch (Exception e) {
            e.printStackTrace();
            Intent localIntent = new Intent(
                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra(
                    "android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + this.getClass().getName());
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
            context.sendBroadcast(localIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_POST_TAB_CLICK:
                if (msg.getIsSuccess()) {
                    if (msg.getIsSuccess()) {
                        Log.e("post tab click", "success");
                    } else {
                        Log.e("post tab click", "false");
                    }
                }
                break;
        }

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //>=6.0检查运行时权限
            int grantCode = PermissionChecker.checkSelfPermission(PopularMainActivity.this, "android.permission.RECORD_AUDIO,android.permission.CAMERA,android.permission.WRITE_EXTERNAL_STORAGE,android.permission.READ_CONTACTS,android.permission.WRITE_CONTACTS");
            if (grantCode != PackageManager.PERMISSION_GRANTED) { //未授权
                ActivityCompat.requestPermissions(PopularMainActivity.this, new String[]{"android.permission.RECORD_AUDIO,android.permission.CAMERA,android.permission.WRITE_EXTERNAL_STORAGE,android.permission.READ_CONTACTS,android.permission.WRITE_CONTACTS"}, REQUEST_PERMISSION);
            }
        }
    }

    /**
     * 切换Fragment
     *
     * @param index
     */
    public void switchFragment(int index) {
        //每次都重建 城镇的fragment
        showFragment = mCacheFragmentMap.get(String.valueOf(index));
        if (showFragment == null) {   //初始化Fragment
            TabEntity tabEntity = TABS[index];
            showFragment = Fragment.instantiate(this, tabEntity.fragmentCls.getName());
            if (showFragment != null) {
                mCacheFragmentMap.put(String.valueOf(index), showFragment);
            }
        }
        if (showFragment != null && mCacheFragmentMap.size() > 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment hideFragment : mCacheFragmentMap.values()) {
                if (hideFragment != null && hideFragment.isAdded())
                    fragmentTransaction.hide(hideFragment);
            }
            if (!showFragment.isAdded()) {
                fragmentTransaction.add(R.id.flChange, showFragment);
            } else {
                fragmentTransaction.show(showFragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getPushSize();
        EventBus.getDefault().post(new MessageShuaxinTwo());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ThirdSDKManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //允许
            } else {
                //拒绝
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_popular_main;
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        ThirdSDKManager.getInstance().startLocation(this, new ThirdSDKManager.ILocationListener() {
            @Override
            public void onSuccess(ThirdSDKManager.Location location) {
                L.v("定位成功=======" + (location != null ? location.longitude : ""));
                BeyondApplication.getInstance().setLocation(location);
                destroyLocation();
            }

            @Override
            public void onFailure(String errCode, String errInfo, String errDetail) {
                destroyLocation();
                Log.e("XXXX", "errCode " + errCode + " errInfo " + errInfo + " errDetail " + errDetail);
            }
        });
    }

    /**
     * 销毁定位相关
     */
    private void destroyLocation() {
        ThirdSDKManager.getInstance().destroyLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NewsListFragment.mdilaog != null) {
            NewsListFragment.mdilaog = null;
        }
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                HxEaseuiHelper.getInstance().easeUI.getNotifier().vibrateAndPlayTone(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    @Request
    NotificationRestUsage notificationRestUsage;

    private void refreshUIWithMessage() {
        getPushSize();
    }

    @OnClick({R.id.rb_home, R.id.rb_bdc, R.id.rb_xx, R.id.rb_fw,R.id.rb_txl, R.id.rb_wd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_home:
                switchFragment(0);
                break;
            case R.id.rb_bdc:
                switchFragment(1);
                break;
            case R.id.rb_xx:
                switchFragment(2);
                break;
            case R.id.rb_fw:
                switchFragment(3);
                break;
            case R.id.rb_txl:
                switchFragment(4);
                break;
            case R.id.rb_wd:
                switchFragment(5);
                break;
        }
    }

}
