package com.beyond.popscience.module.home;

import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.L;
import com.beyond.popscience.LoginActivity;
import com.beyond.popscience.PopularMainActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.GuideRestUsage;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.TimeCount;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.personal.PersonalActivity;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 欢迎界面
 * <p>
 * Created by yao.cui on 2017/6/8.
 */

public class WelcomeActivity extends BaseActivity {
    private final int TASK_GET_WELCOME = 1001;
    private static final String IS_FIRST = "is_first";//是否第一次启动

    @BindView(R.id.ivWelcome)
    ImageView ivWelcome;

    @Request
    private GuideRestUsage mRestUsage;

    public static String seletedAdress = "台州市-仙居县";
    public static String seletedAdress_two = "浙江省-台州市-仙居县";

    @Override
    public void initUI() {
        super.initUI();
        getWelcomeImg();
        startLocation();
        new TimeCount(1000, 10, new TimeCount.TimeListener() {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                countFinish();
            }
        }).start();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (taskId == TASK_GET_WELCOME && msg.getIsSuccess()) {
            HashMap<String, String> dataHashMap = (HashMap<String, String>) msg.getObj();
            if (dataHashMap != null) {
                ImageLoaderUtil.display(this, dataHashMap.get("pic_url"), ivWelcome);
            }
        }
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
               // seletedAdress = location.city + "-" + location.district;
               // seletedAdress_two = location.province + "-" + location.city + "-" + location.district;
                destroyLocation();
            }

            @Override
            public void onFailure(String errCode, String errInfo, String errDetail) {
                destroyLocation();

                Log.e("XXXX", "errCode " + errCode + " errInfo " + errInfo + " errDetail " + errDetail);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtil.showCenter(PopularMainActivity.this, "定位失败");
                    }
                });
            }
        });
    }

    /**
     * 销毁定位相关
     */
    private void destroyLocation() {
        ThirdSDKManager.getInstance().destroyLocation();
    }

    /**
     * 获取欢迎界面 图片
     */
    private void getWelcomeImg() {
        mRestUsage.getWelcomeImg(TASK_GET_WELCOME);
    }

    /**
     * 倒计时结束
     */
    private void countFinish() {
        if (isFirst()) { // 如果是第一次运行，跳转到引导页
            startActivity(GuideActivity.class);
            SPUtils.put(BeyondApplication.getInstance(), IS_FIRST, false);
        } else {
            if (UserInfoUtil.getInstance().isHasLogin()) {//已经登录则跳转到主页
                //看看注册地
                String address = (String) SPUtils.get(WelcomeActivity.this, "detailedArea", "");
                String[] mAddress = address.split("-");
                if (mAddress.length > 0) {
                    seletedAdress = mAddress[1] + "-" + mAddress[2];
                    seletedAdress_two = mAddress[0] + "-" + mAddress[1] + "-" + mAddress[2];
                }
                startActivity(PopularMainActivity.class);
            } else {//否则 进入登录
                //没有登录默认仙居
                LoginActivity.startActivity(WelcomeActivity.this);
            }
        }
        finish();
    }

    /**
     * 获取该应用是否第一次运行
     *
     * @return
     */
    public boolean isFirst() {
        return (boolean) SPUtils.get(BeyondApplication.getInstance(), IS_FIRST, true);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_welcome;
    }
}
