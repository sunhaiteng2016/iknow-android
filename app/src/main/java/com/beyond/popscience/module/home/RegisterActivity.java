package com.beyond.popscience.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.MD5Util;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.AccountRestUsage;
import com.beyond.popscience.frame.util.DeviceUtil;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.widget.AddressPopWindow;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * 注册
 * Created by yao.cui on 2017/6/9.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, AddressPopWindow.IAddressChangeListener {

    private static final int TASK_SEND_CODE = 11001;
    private static final int TASK_REGISTER = 11002;

    private final int TOTAL_SECONDS = 60;
    @BindView(R.id.ed_yq_code)
    EditText edYqCode;

    private TextView mTvRegister;
    private TextView mTvSendCode;

    private EditText mEditName;
    private EditText mEditPhone;
    private EditText mEditCode;
    private EditText mEditPwd;
    private TextView mTvAddress;
    private ImageView mIvClose;
    private LinearLayout mllAddress, mllAddresss;

    private int iSeconds;//发送验证码倒计时
    private String mAddressId = "";
    private String mAddressDetail = "";
    private String mAreaStr;
    private AddressPopWindow mAddressWindow;
    private TextView mTvAddresss;

    @Request
    private AccountRestUsage mAccountRestUsage;
    private String maddress;


    public static void startActivit(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (iSeconds == -1) {
                        mTvSendCode.setText(getString(R.string.resend_code));
                    } else {
                        mTvSendCode.setText(iSeconds + "S");
                    }
                    break;
            }
        }
    };

    @Override
    public void initUI() {
        super.initUI();
        mTvRegister = (TextView) findViewById(R.id.tvRegister);
        mTvSendCode = (TextView) findViewById(R.id.tvSendCode);
        mEditName = (EditText) findViewById(R.id.editName);
        mEditPhone = (EditText) findViewById(R.id.editPhone);
        mEditCode = (EditText) findViewById(R.id.editCode);
        mEditPwd = (EditText) findViewById(R.id.editPwd);
        mTvAddress = (TextView) findViewById(R.id.tvAddress);
        mTvAddresss = (TextView) findViewById(R.id.tvAddresss);

        mIvClose = (ImageView) findViewById(R.id.imgClose);
        mllAddress = (LinearLayout) findViewById(R.id.llAddress);
        mllAddresss = (LinearLayout) findViewById(R.id.llAddresss);

        mTvSendCode.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mllAddress.setOnClickListener(this);
        mllAddresss.setOnClickListener(this);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);

        switch (taskId) {
            case TASK_REGISTER:
                if (msg.getIsSuccess()) {
                    // ToastUtil.showCenter(this, getString(R.string.register_success));
                    ToastUtil.showCenter(RegisterActivity.this, getString(R.string.register_success));
                    finish();
                } else {
                    ToastUtil.showCenter(RegisterActivity.this, msg.getMsg());
                    //showToastDilaog( msg.getMsg());
                }
                break;
            case TASK_SEND_CODE:
                ToastUtil.showCenter(RegisterActivity.this, msg.getMsg());
                //showToastDilaog(msg.getMsg());
                break;
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvRegister:
                if (canSubmit()) {
                    register();
                }
                break;
            case R.id.tvSendCode:
                sendCode();
                break;
            case R.id.llAddress:
                if (maddress == null || "".equals(maddress)) {
                    Toast.makeText(RegisterActivity.this, "请选择你所在的省市区", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAddressWindow = new AddressPopWindow(this, maddress);
                mAddressWindow.setAddressChangeListener(this);
                mAddressWindow.show(mTvAddress);
                break;
            case R.id.llAddresss:
                AddressPickTask task = new AddressPickTask(RegisterActivity.this);
                task.setHideProvince(false);
                task.setHideCounty(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        //showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                            //showToast(province.getAreaName() + city.getAreaName());
                            mTvAddresss.setText(province.getAreaName() + city.getAreaName());
                        } else {
                            //showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
                            maddress = city.getAreaName() + "-" + county.getAreaName();
                            mTvAddresss.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                        }
                    }
                });
                task.execute("浙江省", "台州市", "仙居县");
                break;
            case R.id.imgClose:
                finish();
                break;
        }

    }

    /**
     * 注册
     */
    private void register() {
        String deviceId = DeviceUtil.getDeviceId(this);
        deviceId = TextUtils.isEmpty(deviceId) ? Build.ID : deviceId;

        mAccountRestUsage.register(TASK_REGISTER, mEditName.getText().toString(),
                mEditPhone.getText().toString(), MD5Util.md5(mEditPwd.getText().toString()),
                mAddressId, mAddressDetail,
                mEditCode.getText().toString(), deviceId,edYqCode.getText().toString().trim());
    }

    /**
     * 检查是否可以提交
     *
     * @return
     */
    private boolean canSubmit() {
        String msg = "";
        if (TextUtils.isEmpty(mEditName.getText())) {
            msg = mEditName.getHint().toString();
        } else if (TextUtils.isEmpty(mEditPhone.getText())) {
            msg = mEditPhone.getHint().toString();
        } else if (TextUtils.isEmpty(mEditCode.getText())) {
            msg = mEditCode.getHint().toString();
        } else if (TextUtils.isEmpty(mEditPwd.getText())) {
            msg = mEditPwd.getHint().toString();
        } else if (TextUtils.isEmpty(mTvAddress.getText())) {
            msg = getString(R.string.please_add_address);
        }

        if (TextUtils.isEmpty(msg)) {
            if (AddressPopWindow.cunname == null) {
                ToastUtil.showCenter(this, "请选择村（街道）");
                return false;
            }
            return true;
        } else {
            ToastUtil.showCenter(this, msg);
            return false;
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode() {
        if (TextUtils.isEmpty(mEditPhone.getText())) {
            ToastUtil.showCenter(this, mEditPhone.getHint().toString());
            return;
        } else {
            String phone = mEditPhone.getText().toString().trim();
            // String regExp = "^((13[0-9])|(15[^4])|(166)|199|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
            String regExp = "^1\\d{10}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(phone);
            if (!m.matches()) {
                String test = "";
                ToastUtil.showCenter(this, getString(R.string.phone_format_error));
                return;
            } else {
                String test = "";
            }

        }

        if (TextUtils.equals(getString(R.string.send_code), mTvSendCode.getText()) ||
                TextUtils.equals(getString(R.string.resend_code), mTvSendCode.getText())) {

            mAccountRestUsage.sendCoderegister(TASK_SEND_CODE, mEditPhone.getText().toString());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    iSeconds = TOTAL_SECONDS;
                    while (iSeconds >= -1 && !isFinishing()) {
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

    @Override
    public void onAddressChange(Address city, Address zone) {
        if (city != null && zone != null) {
            mTvAddress.setText(city.getName() + " " + zone.getName());
            mAddressId = zone.getId() + "";
            ToastUtil.showCenter(RegisterActivity.this, "地址注册后无法更改，请确认");
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_register;
    }

    public AlertDialog.Builder builder;

    public void showToastDilaog(String Toaststr) {
        if (builder != null) {
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
        builder = new AlertDialog.Builder(RegisterActivity.this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
