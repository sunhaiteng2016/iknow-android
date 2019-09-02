package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.MD5Util;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.AccountRestUsage;
import com.beyond.popscience.frame.net.LoginRestUsage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * 找回密码
 * Created by yao.cui on 2017/6/11.
 */

public class FindPwdActivity extends BaseActivity {
    private final int TASK_FIND_PWD = 1203;
    private final int TASK_SEND_CODE = 1204;

    @BindView(R.id.editPhone)
    EditText etPhone;
    @BindView(R.id.editCode)
    EditText etCode;
    @BindView(R.id.editPwd)
    EditText etNewPwd;
    @BindView(R.id.editConfirmPwd)
    EditText etConfirmPwd;
    @BindView(R.id.tvSendCode)
    TextView tvScendCode;
    @BindView(R.id.ivTitle)
    TextView tvTitle;

    private int iSeconds;//发送验证码倒计时


    @Request
    private AccountRestUsage mRequest;


    public static void startActivity(Context context){
        Intent intent = new Intent(context,FindPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText(getString(R.string.find_pwd));
    }

    @OnClick(R.id.imgClose)
    public void close(View view){
        finish();
    }

    @OnClick(R.id.tvSure)
    public void findPwd(View view){
        if (canSubmit()){
            mRequest.findPwd(TASK_FIND_PWD,etPhone.getText().toString(), MD5Util.md5(etNewPwd.getText().toString()),etCode.getText().toString());
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case TASK_FIND_PWD:
                if (msg.getIsSuccess()){
                    ToastUtil.showCenter(this,getString(R.string.pwd_change_suc));
                    finish();
                } else {
                    ToastUtil.showCenter(this,getString(R.string.pwd_change_fail));

                }
                break;
            case TASK_SEND_CODE:
                if (msg.getIsSuccess()){
                    ToastUtil.showCenter(this,getString(R.string.send_code_success));
                } else {
                    ToastUtil.showCenter(this,getString(R.string.send_code_error));
                }
                break;
        }
    }

    /**
     * 检查表单项
     * @return
     */
    public boolean canSubmit(){
        String msg="";

        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.startsWith("1") || phone.trim().length()<11){
            msg = getString(R.string.phone_format_error);
        } else if (TextUtils.isEmpty(etCode.getText())){
            msg = getString(R.string.hint_code);
        } else if (TextUtils.isEmpty(etNewPwd.getText())){
            msg = getString(R.string.please_new_pwd);
        } else if (TextUtils.isEmpty(etConfirmPwd.getText())){
            msg = getString(R.string.please_new_pwd_again);
        }

        if (TextUtils.isEmpty(msg) && !TextUtils.equals(etNewPwd.getText(),etConfirmPwd.getText())){
            msg = getString(R.string.pwd_dif);
        }

        if (TextUtils.isEmpty(msg)){
            return true;
        } else {
            ToastUtil.showCenter(this,msg);
            return false;
        }

    }

    @OnClick(R.id.tvSendCode)
    public void sendCode(View view){

         if (!etPhone.getText().toString().startsWith("1")|| etPhone.getText().toString().length()<11){
            ToastUtil.showCenter(this,getString(R.string.phone_format_error));
            return;
        }

        mRequest.sendCodePwd(TASK_SEND_CODE,etPhone.getText().toString());

        if (TextUtils.equals(getString(R.string.send_code),tvScendCode.getText())||
                TextUtils.equals(getString(R.string.resend_code),tvScendCode.getText())){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    iSeconds = 10;
                    while (iSeconds>=-1 && !isFinishing()) {
                        mHandler.sendEmptyMessage(0);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        --iSeconds;
                    }
                }
            }).start();

        }

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (iSeconds == -1) {
                        tvScendCode.setText(getString(R.string.resend_code));
                    } else {
                        tvScendCode.setText(iSeconds+"S");
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_find_pwd;
    }
}
