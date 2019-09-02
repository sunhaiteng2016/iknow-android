package com.beyond.popscience;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.MD5Util;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.LoginRestUsage;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.MainLocation;
import com.beyond.popscience.module.home.RegisterActivity;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.personal.FindPwdActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 * <p>
 * Created by yao.cui on 2017/6/9.
 */
public class LoginActivity extends BaseActivity {

    /**
     * 登录
     */
    private int LOGIN_TASK_ID = 101;

    @BindView(R.id.tvLogin)
    TextView mTvLogin;
    @BindView(R.id.editName)
    EditText mEditName;
    @BindView(R.id.editPwd)
    EditText mEditPwd;
    @BindView(R.id.llRegister)
    LinearLayout mLlRegister;



    @Request
    private LoginRestUsage loginRestUsage;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);

        if (taskId == LOGIN_TASK_ID) {
            if (msg.getIsSuccess()) {
                UserInfo info = (UserInfo) msg.getObj();
                BeyondApplication.currtMoblie=info.getMobile();

                if (info != null) {
                    UserInfoUtil.getInstance().saveUserInfo(info);
                    String areaname = info.getAreaName();
                    SPUtils.put(LoginActivity.this,"score",info.getScore());
                    SPUtils.put(LoginActivity.this,"Mobile",info.getMobile());
                    SPUtils.put(LoginActivity.this,"areaName",info.getAreaName());
                    SPUtils.put(LoginActivity.this,"detailedArea",info.getDetailedArea());
                    PopularMainActivity.startActivity(this);
                    WelcomeActivity.seletedAdress="台州市-"+areaname;
                    WelcomeActivity.seletedAdress_two="浙江省-台州市-"+areaname;
                    //通知首页列表刷新
                    EventBus.getDefault().post(new MainLocation());
                    finish();
                } else {
                   // showToastDilaog(msg.getMsg());
                    ToastUtil.showCenter(this, msg.getMsg());
                }
            } else {
                if ("8".equals(msg.getCode())) {  //
                    D.show(this, null, "老用户密码已重置为手机号后六位", null, "知道了", null);
                } else {
                    ToastUtil.showCenter(this, "账号密码错误!");
                }
            }
            dismissProgressDialog();
        }
    }

    @OnClick(R.id.tvLogin)
    public void login(View view) {
        if (TextUtils.isEmpty(mEditName.getText()) || TextUtils.isEmpty(mEditPwd.getText())) {
            ToastUtil.showCenter(this, getString(R.string.please_input_login_info));
        } else {
            showProgressDialog();
            String pwdMd5 = MD5Util.md5(mEditPwd.getText().toString());
            loginRestUsage.login(LOGIN_TASK_ID, mEditName.getText().toString(), pwdMd5);
        }
    }

    @OnClick(R.id.llRegister)
    public void register(View view) {
        RegisterActivity.startActivit(this);
    }

    @OnClick(R.id.tvForgotPwd)
    public void forgotPwd(View view) {
        FindPwdActivity.startActivity(this);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }
    public AlertDialog.Builder builder;
    public void showToastDilaog(String Toaststr) {
        if (builder!=null){
            builder.setMessage(Toaststr);
            AlertDialog mdialog = builder.create();
            mdialog.show();
            try {
                Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
                mAlert.setAccessible(true);
                Object mAlertController = mAlert.get(mdialog);
                Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
                mMessage.setAccessible(true);
                TextView mMessageView = (TextView) mMessage.get(mAlertController);
                mMessageView.setTextSize(25);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return;
        }
        builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(Toaststr);
        AlertDialog mdialog = builder.create();
        mdialog.show();
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(mdialog);
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            mMessage.setAccessible(true);
            TextView mMessageView = (TextView) mMessage.get(mAlertController);
            mMessageView.setTextSize(25);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
