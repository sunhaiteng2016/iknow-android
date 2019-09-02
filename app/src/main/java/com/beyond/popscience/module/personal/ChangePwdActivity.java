package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.MD5Util;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.AccountRestUsage;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * change password
 * Created by yao.cui on 2017/6/11.
 */

public class ChangePwdActivity extends BaseActivity {

    private final int TASK_RESET_PWD = 1801;

    @BindView(R.id.ib_back)
    ImageButton backImgBtn;
    @BindView(R.id.leftTxtView)
    TextView leftTxtView;
    @BindView(R.id.tv_title)
    TextView titleTxtView;
    @BindView(R.id.tv_right)
    TextView sureTxtView;
    @BindView(R.id.etOldPwd)
    EditText etOldPwd;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.etConformNewPwd)
    EditText etNewPwdConfirm;

    @Request
    private AccountRestUsage mRestUsage;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChangePwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();

        backImgBtn.setVisibility(View.GONE);
        leftTxtView.setVisibility(View.VISIBLE);
        sureTxtView.setVisibility(View.VISIBLE);
        leftTxtView.setText(getString(R.string.cancel));
        titleTxtView.setText(getString(R.string.change_pwd));
        sureTxtView.setText(getString(R.string.confirm));
    }


    @OnClick(R.id.leftTxtView)
    public void back(View view) {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void submit(View view) {
        if (canCommit()) {
            showProgressDialog();
            mRestUsage.resetPwd(TASK_RESET_PWD, MD5Util.md5(etOldPwd.getText().toString()),
                    MD5Util.md5(etNewPwd.getText().toString()));
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        if (taskId == TASK_RESET_PWD && msg.getIsSuccess()) {
            ToastUtil.showCenter(this, "密码重置成功");
            finish();
        }
    }

    private boolean canCommit() {
        boolean result = true;
        if (TextUtils.isEmpty(etOldPwd.getText())) {
            ToastUtil.showCenter(this, etOldPwd.getHint().toString());
            result = false;
        } else if (TextUtils.isEmpty(etNewPwd.getText())) {
            ToastUtil.showCenter(this, etNewPwd.getHint().toString());
            result = false;

        } else if (TextUtils.isEmpty(etNewPwdConfirm.getText())) {
            ToastUtil.showCenter(this, etNewPwdConfirm.getHint().toString());
            result = false;

        } else if (!TextUtils.equals(etNewPwd.getText(), etNewPwdConfirm.getText())) {
            result = false;
            ToastUtil.showCenter(this, "密码输入不一致");
        }

        return result;
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_change_pwd;
    }
}
