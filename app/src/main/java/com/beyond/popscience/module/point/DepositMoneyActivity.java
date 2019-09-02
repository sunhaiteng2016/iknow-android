package com.beyond.popscience.module.point;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class DepositMoneyActivity extends BaseActivity {
    private static final int TASK_WITHDRAW_PAY = 800010;
    private static final int TASK_WITHDRAW_PAY_PAY = 800012;
    private static final int TASK_WITHDRAW_OK = 800011;
    private static final int GETCODE = 800013;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.deposit_money)
    TextView depositMoney;
    @BindView(R.id.deposit_alipay_account)
    EditText depositAlipayAccount;
    @BindView(R.id.deposit_real_name)
    EditText depositRealName;
    @BindView(R.id.deposit_input_code)
    EditText depositCode;
    @BindView(R.id.et_withdraw_money)
    EditText etWithdrawMoney;
    @BindView(R.id.deposit_right_now)
    Button depositRightNow;
    @BindView(R.id.deposit_get_code)
    TextView depositGetCode;

    @Request
    PointRestUsage restUsage;
    private String money;
    private String stexchang;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_deposit_money_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("提现");
        money = getIntent().getStringExtra("money");
        stexchang = getIntent().getStringExtra("stexchang");
        depositMoney.setText(money);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (msg.getIsSuccess()) {
            switch (taskId) {
                case TASK_WITHDRAW_PAY:
                    dismissProgressDialog();
                    String data = (String) msg.getObj();
                    if ("提现成功".equals(data)) {
                        finish();
                    } else {
                        ToastUtil.showCenter(this, data);
                    }
                    break;
                case TASK_WITHDRAW_PAY_PAY:
                    dismissProgressDialog();
                    String data2 = (String) msg.getObj();
                    if ("审核中...".equals(data2)) {
                        // withdrawOk();
                        finish();
                        startActivity(new Intent(this, ShenHeActivity.class));
                    } else {
                        ToastUtil.showCenter(this, data2);

                        depositRightNow.setBackgroundResource(R.drawable.shape_hui_corner);
                        depositRightNow.setEnabled(false);
                    }
                    break;
                case GETCODE:
                    dismissProgressDialog();
                    String data21 = (String) msg.getObj();
                    ToastUtil.showCenter(this, data21);
                    break;
                case TASK_WITHDRAW_OK:
                    String data1 = (String) msg.getObj();
                    if ("提现成功".equals(data1)) {
                        finish();
                    } else {
                        ToastUtil.showCenter(this, data1);
                    }
                    break;

            }
        }
    }

    @OnClick({R.id.ib_back, R.id.deposit_right_now, R.id.deposit_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.deposit_right_now:

                if (depositAlipayAccount.getText().toString().isEmpty()) {
                    ToastUtil.showCenter(this, "请输入支付宝账号");
                } else if (depositRealName.getText().toString().isEmpty()) {
                    ToastUtil.showCenter(this, "请输入真实姓名");
//                } else if (depositPhone.getText().toString().isEmpty()) {
//                    ToastUtil.showCenter(this, "请输入当前手机号码");
                } else if (depositCode.getText().toString().isEmpty()) {
                    ToastUtil.showCenter(this, "请输入验证码");
                } else {
                    //先去检查状态
                    withdrawPayState(depositRealName.getText().toString(),
                            depositAlipayAccount.getText().toString(),
                            stexchang,
                            depositCode.getText().toString());
                }
                break;
            case R.id.deposit_get_code:
                if (depositAlipayAccount.getText().toString().isEmpty()) {
                    ToastUtil.showCenter(this, "请输入支付宝账号");
                } else if (depositRealName.getText().toString().isEmpty()) {
                    ToastUtil.showCenter(this, "请输入真实姓名");
//                } else if (depositPhone.getText().toString().isEmpty()) {
//                    ToastUtil.showCenter(this, "请输入当前手机号码");
                } else if (SPUtils.get(DepositMoneyActivity.this,"Mobile","").equals("")) {
                    ToastUtil.showCenter(this, "请输入当前登录手机号");
                } else {
                    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60 * 1000, 1000);
                    myCountDownTimer.start();
                    getCode();
                }
                break;
        }
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            if (depositGetCode==null){
                return;
            }
            depositGetCode.setClickable(false);
            depositGetCode.setText(l / 1000 + "s后重发");
            depositGetCode.setBackground(getResources().getDrawable(R.color.gray153));
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            if (depositGetCode==null){
                return;
            }
            //重新给Button设置文字
            depositGetCode.setText("重新获取验证码");
            depositGetCode.setBackground(getResources().getDrawable(R.color.white));
            //设置可点击
            depositGetCode.setClickable(true);
        }
    }

    /**
     * @param username  支付宝真实姓名
     * @param account   支付宝账号
     * @param stexchang 兑换比例
     * @param code      验证码
     */
    private void withdrawPay(String username, String account, String stexchang, String code) {
        showProgressDialog();
        restUsage.withdrawPay(TASK_WITHDRAW_PAY, username, account, stexchang,
                UserInfoUtil.getInstance().getUserInfo().getMobile(),
                code,
                UserInfoUtil.getInstance().getUserInfo().getUserId());
    }

    /**
     * @param username  支付宝真实姓名
     * @param account   支付宝账号
     * @param stexchang 兑换比例
     * @param code      验证码
     */
    private void withdrawPayState(String username, String account, String stexchang, String code) {
        showProgressDialog();
        restUsage.withdrawPay(TASK_WITHDRAW_PAY_PAY, username, account, stexchang,
                SPUtils.get(DepositMoneyActivity.this, "Mobile", "")+"",
                code,
                UserInfoUtil.getInstance().getUserInfo().getUserId());
    }

    private void withdrawOk() {
        restUsage.withOk(TASK_WITHDRAW_OK, UserInfoUtil.getInstance().getUserInfo().getUserId(), "");
    }

    /**
     * 获取验证码
     */
    private void getCode() {
//        restUsage.getCode(GETCODE, depositPhone.getText().toString());
        String sss = UserInfoUtil.getInstance().getUserInfo().getUserId();
        Object ssss = SPUtils.get(DepositMoneyActivity.this, "Mobile", "");
        restUsage.getCode(GETCODE, SPUtils.get(DepositMoneyActivity.this,"Mobile","")+"",
                UserInfoUtil.getInstance().getUserInfo().getUserId());
    }
}
