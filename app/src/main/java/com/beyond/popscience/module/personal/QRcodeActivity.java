package com.beyond.popscience.module.personal;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.Grxx;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class QRcodeActivity extends BaseActivity {


    private CaptureFragment captureFragment;
    private String md5pay;
    public static boolean ispwd = false;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, QRcodeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void initUI() {
        super.initUI();
        getisPwd();
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {

            //先看有没有设置密码
            if (md5pay.equals("true")) {
                try {
                        Erweima erweima = JSON.parseObject(result, Erweima.class);
                        //跳转转账页面
                        ZhuanZhangActivity.startActivity(QRcodeActivity.this, result);
                        finish();
                }catch (Exception e){
                    ToastUtil.getInstance(QRcodeActivity.this).showToast("这不是一个有效的二维码");
                }
            } else {
                if (ispwd) {
                    ZhuanZhangActivity.startActivity(QRcodeActivity.this, result);
                    finish();
                } else {
                    SetPwdActivity.startActivity(QRcodeActivity.this, "1");
                }
            }
        }

        @Override
        public void onAnalyzeFailed() {
            ToastUtil.getInstance(QRcodeActivity.this).showToast("扫描失败，请重新扫描");
        }
    };

    //是不是支付密码
    public void getisPwd() {
        JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/queryMeScore", obj.toString(), map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    Grxx grxx = JSON.parseObject(response, Grxx.class);
                    if (grxx.getCode() == 0) {
                        SPUtils.put(QRcodeActivity.this, "scores", grxx.getData().getScore());
                        md5pay = grxx.getData().getMd5Pay();
                    }
                } catch (Exception e) {

                }

            }
        });


    }
}
