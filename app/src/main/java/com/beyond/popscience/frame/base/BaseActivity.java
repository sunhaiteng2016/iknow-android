package com.beyond.popscience.frame.base;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.beyond.library.network.NetWorkInject;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.task.ITask;
import com.beyond.library.network.task.IUIController;
import com.beyond.library.network.task.TaskManager;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.LoginActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.net.AccountRestUsage;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.pojo.AppInfo;
import com.beyond.popscience.frame.pojo.SystemTime;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.DTwo;
import com.beyond.popscience.frame.util.DownloadFileFromURL;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.home.entity.Address;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public abstract class BaseActivity extends AppCompatActivity implements IUIController {

    /**
     * 获取服务器时间
     */
    private final int REQUEST_SYSTEM_TIME_TASK_ID = 7001;
    /**
     * 获取用户信息
     */
    private final int REQUEST_GET_USER_INFO_TASK_ID = 7002;
    /**
     * 检测版本更新
     */
    private final int REQUEST_VERSION_TASK_ID = 7003;
    /**
     * 乡镇json
     */
    private final int REQUEST_GET_AREA_TASK_ID = 7004;
    /**
     *
     */
    private final int REQUEST_GET_AREA_ADDRESS_ID = 7005;

    /**
     * 加载中Dialog
     */
    private Dialog progressDialog;
    private boolean isSwitchFragmenting = false;
    private DisplayImageOptions disImageOptions;
    private DisplayImageOptions avatarDisplayImageOptions;
    private Unbinder unbinder;
    public Bundle mSavedInstanceState;


    public void startActivity(Class classStr) {
        Intent intent = new Intent(this, classStr);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(savedInstanceState, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void onCreate(Bundle savedInstanceState, int requestedOrientation) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (requestedOrientation != -2) {
            setRequestedOrientation(requestedOrientation);
        }
       /* if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }*/
        NetWorkInject.init(this);
        mSavedInstanceState = savedInstanceState;
        TaskManager.getInstance().registerUIController(this);
        BeyondApplication.getInstance().pushActivity(this);

//  适配底部导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        int layoutResId = getLayoutResID();
        if (layoutResId != View.NO_ID) {
            setContentView(layoutResId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                View decorView = window.getDecorView();
                getWindow().setNavigationBarColor(Color.parseColor("#000000"));
                decorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
                    @Override
                    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                        WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                        return defaultInsets.replaceSystemWindowInsets(
                                defaultInsets.getSystemWindowInsetLeft(),
                                0,
                                defaultInsets.getSystemWindowInsetRight(),
                                defaultInsets.getSystemWindowInsetBottom());
                    }
                });
                ViewCompat.requestApplyInsets(decorView);
                //将状态栏设成透明，如不想透明可设置其他颜色
                window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
            }
            unbinder = ButterKnife.bind(this);
            initUI();
        }
        requestSystemTime();
        requestUserInfo();
        requestAreaJson();
        requestAreaAddressJson();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            try {
                unbinder.unbind();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BeyondApplication.getInstance().popActivity(this);
        TaskManager.getInstance().unRegisterUIController(this);
    }

    protected abstract int getLayoutResID();

    @Override
    public void initUI() {

    }

    public void closrKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 处理 MSG
     *
     * @param msg
     * @return
     */
    protected boolean dealMSG(MSG msg) {
        if (!msg.getIsSuccess() && (msg.getIsCallSuperRefreshUI() == null || (msg.getIsCallSuperRefreshUI() != null && msg.getIsCallSuperRefreshUI()))) {
            if (!msg.isFromCache() && "2".equals(msg.getCode())) {  //token过期或者未登录    忽略缓存的 code
                UserInfoUtil.getInstance().removeUserInfo();
                SPUtils.remove(this, "score");
                BeyondApplication.getInstance().popAllActivityExcept(this);
                LoginActivity.startActivity(this);
                finish();
                return true;
            }

            if (!msg.isFromCache()) {
                //  ToastUtil.showCenter(this, msg.getMsg());
            }
        }
        return false;
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        if (null != msg.getCode() && msg.getCode().equals("2")) {
            LoginActivity.startActivity(BaseActivity.this);
        }
        if (dealMSG(msg)) {
            return;
        }
        switch (taskId) {
            case REQUEST_SYSTEM_TIME_TASK_ID:   //获取服务器时间
                if (msg.getIsSuccess()) {
                    SystemTime systemTime = (SystemTime) msg.getObj();
                    if (systemTime != null && !TextUtils.isEmpty(systemTime.getSystemTime())) {
                        systemTime.setUpdateTimeStamp(SystemClock.elapsedRealtime());
                        BeyondApplication.getInstance().setSystemTime(systemTime);
                    }
                }
                break;
            case REQUEST_GET_AREA_TASK_ID:   //乡镇json
                if (msg.getIsSuccess()) {
                    List<Address> addressList = (List<Address>) msg.getObj();
                    if (addressList != null && addressList.size() != 0) {
                        BeyondApplication.getInstance().setServerAddressList(addressList);
                    }
                }
                break;
            case REQUEST_GET_AREA_ADDRESS_ID:
                if (msg.getIsSuccess()) {
                    List<Address> addressList = (List<Address>) msg.getObj();
                    if (addressList != null && addressList.size() > 0) {
                        BeyondApplication.getInstance().setAreaAddressList(addressList);
                    }
                }
                break;
            case REQUEST_GET_USER_INFO_TASK_ID: //获取用户信息
                if (msg.getIsSuccess()) {
                    UserInfo userInfo = (UserInfo) msg.getObj();
                    if (userInfo != null) {
                        UserInfo saveUserInfo = UserInfoUtil.getInstance().getUserInfo();
                        userInfo.setToken(saveUserInfo.getToken());
                        userInfo.setVillageId(saveUserInfo.getVillageId());
                        userInfo.setVillageName(saveUserInfo.getVillageName());
                        UserInfoUtil.getInstance().saveUserInfo(userInfo);
                    }
                }
                break;
            case REQUEST_VERSION_TASK_ID:   //请求最新版本
                if (msg.getIsSuccess()) {
                    final AppInfo appInfo = (AppInfo) msg.getObj();
                    String codeuel = appInfo.getUrl();
                    if (appInfo != null) {
                        StringBuffer sb = new StringBuffer();
                        Boolean isShowToast = (boolean) msg.getTargetObj();
                        //后台的版本号与app本身对比，提示更新
                        String hint = appInfo.getHint();
                        if (null != hint) {
                            String[] hints = hint.split("&");
                            if (hints.length > 0) {
                                for (String mHint : hints) {
                                    sb.append(mHint + "\n");
                                }
                            } else {
                                sb.append(appInfo.getHint());
                            }
                        } else {
                            sb.append("");
                        }
                        if (appInfo.getVersionCode() > Util.getVersionCode(BeyondApplication.getInstance())) {

                            DTwo.show(this, null, "检测到最新版本,是否立即更新?\n\n" + sb.toString(), "取消", "更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        if (!TextUtils.isEmpty(appInfo.getUrl())) {
                                            new DownloadFileFromURL(BaseActivity.this).execute(appInfo.getUrl());
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            if (isShowToast != null && isShowToast) {
                                ToastUtil.showCenter(this, "已是最新版本");
                            }
                        }
                    }
                }
                dismissProgressDialog();
                break;
        }
    }


    /**
     * 请求服务端时间
     */
    private void requestSystemTime() {
        if (BeyondApplication.getInstance().getSystemTime() == null) {
            CommonRestUsage commonRestUsage = new CommonRestUsage();
            commonRestUsage.setIdentification(getIdentification());
            commonRestUsage.getSystemTime(REQUEST_SYSTEM_TIME_TASK_ID);
        }
    }

    /**
     * 请求用户信息
     */
    private void requestUserInfo() {
        AccountRestUsage accountRestUsage = new AccountRestUsage();
        accountRestUsage.setIdentification(getIdentification());
        accountRestUsage.getUserInfo(REQUEST_GET_USER_INFO_TASK_ID);
    }

    /**
     * 乡镇json
     */
    private void requestAreaJson() {
        if (BeyondApplication.getInstance().getServerAddressList().size() == 0) {
            AccountRestUsage accountRestUsage = new AccountRestUsage();
            accountRestUsage.setIdentification(getIdentification());
            accountRestUsage.getAreaJson(REQUEST_GET_AREA_TASK_ID);
        }
    }

    /**
     *
     */
    private void requestAreaAddressJson() {
        if (BeyondApplication.getInstance().getAreaAddressList().size() == 0) {
            AccountRestUsage accountRestUsage = new AccountRestUsage();
            accountRestUsage.setIdentification(getIdentification());
            accountRestUsage.getProductAreaJson(REQUEST_GET_AREA_ADDRESS_ID);
        }
    }

    /**
     * 检测版本更新
     */
    public void requestCheckVersion(boolean isShowToast) {
        showProgressDialog();
        CommonRestUsage commonRestUsage = new CommonRestUsage();
        commonRestUsage.setIdentification(getIdentification());
        commonRestUsage.getVersion(REQUEST_VERSION_TASK_ID, isShowToast);
    }

    @Override
    public String getIdentification() {
        return getClass().toString() + this;
    }

    /**
     * 执行Task任务
     *
     * @param task
     */
    protected void execuTask(ITask task) {
        if (task == null)
            return;
        task.setContext(this);
        task.setmIdentification(getIdentification());
        TaskManager.getInstance().addTask(task);
    }

    /**
     * 提示消息并退出
     */
    protected void showToastMsgAndFinish(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtil.showCenter(this, msg);
        }
        dismissProgressDialog();
        backNoAnim();
    }

    /**
     * 返回
     *
     * @param view
     */
    public void backClick(View view) {
        finish();
    }

    /**
     * 无动画返回
     */
    protected void backNoAnim() {
        finish();
        overridePendingTransition(0, 0);
    }

    /**
     * 显示Progress   返回键可以dismiss
     */
    protected void showProgressDialog() {
        showProgressDialog("加载中...");
    }


    /**
     * 显示Progress   返回键可以dismiss
     */
    protected void showProgressDialog(DialogInterface.OnDismissListener onDismissListener) {
        showProgressDialog("加载中...", true, onDismissListener);
    }

    /**
     * 显示Progress   返回键可以dismiss
     */
    protected void showProgressDialogNoCancel() {
        showProgressDialog("加载中...", false);
    }

    /**
     * 显示Progress   返回键可以dismiss
     */
    protected void showProgressDialog(String message) {
        showProgressDialog(message, true);
    }

    /**
     * 显示Progress
     *
     * @param isCancelable 返回键是否可以dismiss
     */
    protected void showProgressDialog(String message, boolean isCancelable) {
        showProgressDialog(message, isCancelable, null);
    }

    /**
     * 显示Progress
     *
     * @param isCancelable 返回键是否可以dismiss
     */
    protected void showProgressDialog(String message, boolean isCancelable, DialogInterface.OnDismissListener onDismissListener) {
        dismissProgressDialog();
        if (isFinishing()) {
            return;
        }
        progressDialog = D.getProgressDialog(this, message, isCancelable);
        if (progressDialog != null) {
            progressDialog.setOnDismissListener(onDismissListener);
            progressDialog.show();
        }
    }

    /**
     * 关闭Progress
     */
    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    /**
     * 替换Fragment (默认有动画效果)
     *
     * @param resLayId
     * @param fragment
     * @param isAddBackStack 是否加入返回栈
     */
    protected void replaceFragment(int resLayId, Fragment fragment,
                                   boolean isAddBackStack) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right, R.anim.slide_in_left,
                R.anim.slide_out_right);
        fragmentTransaction.replace(resLayId, fragment);
        if (isAddBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        isSwitchFragmenting = false;
    }

    /**
     * 替换Fragment
     *
     * @param resLayId
     * @param fragment
     * @param isAddBackStack
     * @param isAnimation
     */
    protected void replaceFragment(int resLayId, Fragment fragment,
                                   boolean isAddBackStack, boolean isAnimation) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (isAnimation)
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right, R.anim.slide_in_left,
                    R.anim.slide_out_right);
        fragmentTransaction.replace(resLayId, fragment);
        if (isAddBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        isSwitchFragmenting = false;
    }

    /**
     * 添加Fragment
     *
     * @param resLayId
     * @param showFragment
     * @param isAddBackStack
     * @param hideFragments  要隐藏的Fragment数组
     */
    protected void addFragment(int resLayId, Fragment showFragment,
                               boolean isAnimation, boolean isAddBackStack,
                               Fragment... hideFragments) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (isAnimation)
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right, R.anim.slide_in_left,
                    R.anim.slide_out_right);
        if (hideFragments != null) {
            for (Fragment hideFragment : hideFragments) {
                if (hideFragment != null)
                    fragmentTransaction.hide(hideFragment);
            }
        }
        fragmentTransaction.add(resLayId, showFragment);
        if (isAddBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        isSwitchFragmenting = false;
    }

    /**
     * 显示隐藏Fragment
     *
     * @param showFragment
     * @param hideFragments  要隐藏的Fragment数组
     * @param isAddBackStack 是否加入返回栈
     */
    protected void showHideFragment(Fragment showFragment, boolean isAnimation,
                                    boolean isAddBackStack, Fragment... hideFragments) {
        if (isSwitchFragmenting) {
            return;
        }
        isSwitchFragmenting = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (isAnimation)
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right, R.anim.slide_in_left,
                    R.anim.slide_out_right);
        if (hideFragments != null) {
            for (Fragment hideFragment : hideFragments) {
                if (hideFragment != null && hideFragment.isAdded())
                    fragmentTransaction.hide(hideFragment);
            }
        }
        if (showFragment != null)
            fragmentTransaction.show(showFragment);
        if (isAddBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        isSwitchFragmenting = false;
    }

    /**
     * 头像
     *
     * @return
     */
    protected DisplayImageOptions getAvatarDisplayImageOptions() {
        if (avatarDisplayImageOptions == null) {
            avatarDisplayImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_header_icon).showImageOnFail(R.drawable.default_header_icon).showImageForEmptyUri(R.drawable.default_header_icon).displayer(new FadeInBitmapDisplayer(200, true, true, false)).considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).build();
        }
        return avatarDisplayImageOptions;
    }

    /**
     * @return
     */
    protected DisplayImageOptions getDisplayImageOptions() {
        if (disImageOptions == null) {
            disImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_bg_img_loading).showImageOnFail(R.drawable.default_bg_img_loading).showImageForEmptyUri(R.drawable.default_bg_img_loading).displayer(new FadeInBitmapDisplayer(200, true, true, false)).considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).build();
        }
        return disImageOptions;
    }

    /**
     * 返回键
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
        }
        return super.dispatchKeyEvent(event);
    }


}
