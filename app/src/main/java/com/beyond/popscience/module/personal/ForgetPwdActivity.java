package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.utils.sun.util.TimerUtils;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置支付密码
 * Created by YAO.CUI on 2017/6/12.
 */

public class ForgetPwdActivity extends BaseActivity {
    private final int TASK_HELP = 1901;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.leftTxtView)
    TextView leftTxtView;
    @BindView(R.id.rightImgView)
    ImageView rightImgView;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.topReLay)
    RelativeLayout topReLay;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.ed1)
    EditText ed1;
    @BindView(R.id.ed2)
    EditText ed2;
    @BindView(R.id.ed3)
    EditText ed3;
    @BindView(R.id.ed4)
    EditText ed4;
    @BindView(R.id.ed5)
    EditText ed5;
    @BindView(R.id.ed6)
    EditText ed6;
    @BindView(R.id.ts)
    TextView ts;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_mima)
    LinearLayout llMima;
    @BindView(R.id.ed_phone_my)
    EditText edPhoneMy;
    private int isFrist = 1;
    @Request
    private CommonRestUsage mRestUsage;
    private String pwd1, pwd2;
    private String code;
    private String phonemy;
    private String md5pay;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();

        tvTitle.setText("忘记支付密码");
        ts.setText("请输入旧密码");
        addTextChanged(ed1, ed2);
        addTextChanged(ed2, ed3);
        addTextChanged(ed3, ed4);
        addTextChanged(ed4, ed5);
        addTextChanged(ed5, ed6);
        String sss = (String) SPUtils.get(this, "Mobile", "");
        edPhoneMy.setText(sss);
        edPhoneMy.setEnabled(false);
    }

    private void addTextChanged(EditText ed_one, final EditText ed_two) {
        ed_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    ed_two.requestFocus();
                }
            }
        });
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
        return R.layout.activity_set_pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ib_back)
    public void onViewClicked() {
        if (isFrist == 1) {
            finish();
        }
        if (isFrist == 2) {
            isFrist = 1;
            submit.setText("确认");
        }
        if (isFrist == 3) {
            llPhone.setVisibility(View.GONE);
            llMima.setVisibility(View.VISIBLE);
            submit.setText("再一次确认密码");
            isFrist = 2;
        }
    }



    @OnClick(R.id.submit)
    public void onViewClickeds() {
            if (isFrist == 1) {
                //第一次设置密码
                ts.setText("请输入新密码");
                String ed1s = ed1.getText().toString();
                String ed2s = ed2.getText().toString();
                String ed3s = ed3.getText().toString();
                String ed4s = ed4.getText().toString();
                String ed5s = ed5.getText().toString();
                String ed6s = ed6.getText().toString();
                pwd1 = ed1s + ed2s + ed3s + ed4s + ed5s + ed6s;
                if (pwd1.length() == 6) {
                    ed1.setText("");
                    ed2.setText("");
                    ed3.setText("");
                    ed4.setText("");
                    ed5.setText("");
                    ed6.setText("");
                    submit.setText("确认");
                    ed1.requestFocus();
                    isFrist = 2;
                } else {
                    ToastUtil.getInstance(this).showToast("请输入6位支付密码");
                }

            } else if (isFrist == 2) {
                ts.setText("请输入新密码");
                String ed1s = ed1.getText().toString();
                String ed2s = ed2.getText().toString();
                String ed3s = ed3.getText().toString();
                String ed4s = ed4.getText().toString();
                String ed5s = ed5.getText().toString();
                String ed6s = ed6.getText().toString();
                pwd2 = ed1s + ed2s + ed3s + ed4s + ed5s + ed6s;
                if (pwd2.length() == 6) {
                    upDatapwd();
                } else {
                    ToastUtil.getInstance(this).showToast("请输入6位支付密码");
                }

            }

    }

    private void upDatapwd() {
        JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        try {
            obj.put("password", pwd1);
            obj.put("newpassword", pwd2);
            OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/updatePayPass", obj.toString(), map, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    try {
                        BaseResponse baseResponse = JSON.parseObject(response, BaseResponse.class);
                        if (baseResponse.getCode() == 0) {
                            ToastUtil.getInstance(ForgetPwdActivity.this).showToast("设置成功");
                            finish();
                        } else {
                            ToastUtil.getInstance(ForgetPwdActivity.this).showToast(baseResponse.getMessage());
                        }
                    } catch (Exception e) {

                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新支付密码
     */
    private void upDatepwd() {

        JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        try {
            obj.put("password", pwd1);
            obj.put("pin", code);
            OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/setPayPass", obj.toString(), map, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    try {
                        BaseResponse baseResponse = JSON.parseObject(response, BaseResponse.class);
                        if (baseResponse.getCode() == 0) {
                            ToastUtil.getInstance(ForgetPwdActivity.this).showToast("设置成功");
                            finish();
                        } else {
                            ToastUtil.getInstance(ForgetPwdActivity.this).showToast(baseResponse.getMessage());
                        }

                    } catch (Exception e) {

                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean check() {
        if (code.isEmpty()) {
            ToastUtil.getInstance(this).showToast("请输入验证码");
            return false;
        }
        return true;
    }
}
