package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.Grxx;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 绿币转账
 * Created by YAO.CUI on 2017/6/12.
 */

public class LbzzOneActivity extends BaseActivity {
    private final int TASK_HELP = 1901;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.sys)
    TextView sys;
    @BindView(R.id.setting_pwd)
    TextView settingPwd;
    @BindView(R.id.my_code)
    TextView myCode;
    @BindView(R.id.forgetPwd)
    TextView forgetPwd;

    @Request
    private CommonRestUsage mRestUsage;
    private static  String md5pay;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LbzzOneActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("绿币转账");
        getisPwd();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_HELP:
                break;
        }
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_lbzz_one;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ib_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.sys, R.id.setting_pwd, R.id.my_code, R.id.zzjl, R.id.forgetPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sys:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.setting_pwd:
                if (null != md5pay) {
                    SetPwdActivity.startActivity(this, md5pay);
                }
                break;
            case R.id.my_code:
                LbzzActivity.startActivity(this);
                break;
            case R.id.zzjl:
                LbzzActivity.startActivity(this);
                break;
            case R.id.forgetPwd:
                LbzzActivity.startActivity(this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


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
                        SPUtils.put(LbzzOneActivity.this, "scores", grxx.getData().getScore());
                        md5pay = grxx.getData().getMd5Pay();
                        if (md5pay.equals("true")) {
                            //设置过了
                            settingPwd.setText("修改支付密码");
                        } else {
                            //没有设置
                            settingPwd.setText("设置支付密码");
                        }
                    }
                } catch (Exception e) {

                }

            }
        });


    }
}
