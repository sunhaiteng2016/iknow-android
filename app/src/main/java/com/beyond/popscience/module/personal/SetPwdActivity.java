package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.beyond.library.util.MD5Util;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.fragment.MyFragment;
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

public class SetPwdActivity extends BaseActivity {
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
    @BindView(R.id.ed7)
    EditText ed7;
    @BindView(R.id.ed8)
    EditText ed8;
    @BindView(R.id.ed9)
    EditText ed9;
    @BindView(R.id.ed10)
    EditText ed10;
    @BindView(R.id.ed11)
    EditText ed11;
    @BindView(R.id.ed12)
    EditText ed12;
    @BindView(R.id.ll_two)
    LinearLayout llTwo;
    private int isFrist = 1;
    @Request
    private CommonRestUsage mRestUsage;
    private String pwd1, pwd2;
    private String code;
    private String phonemy;
    private String md5pay;
    private String sys;

    public static void startActivity(Context context, String md5pay) {
        Intent intent = new Intent(context, SetPwdActivity.class);
        intent.putExtra("md5pay", md5pay);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String md5pay,String sys) {
        Intent intent = new Intent(context, SetPwdActivity.class);
        intent.putExtra("md5pay", md5pay);
        intent.putExtra("sys", sys);
        context.startActivity(intent);
    }
    @Override
    public void initUI() {
        super.initUI();
        md5pay = getIntent().getStringExtra("md5pay");
        sys=getIntent().getStringExtra("sys");
        addTextChanged(ed1, ed2);
        addTextChanged(ed2, ed3);
        addTextChanged(ed3, ed4);
        addTextChanged(ed4, ed5);
        addTextChanged(ed5, ed6);
        addTextChanged(ed6, ed7);
        addTextChanged(ed7, ed8);
        addTextChanged(ed8, ed9);
        addTextChanged(ed9, ed10);
        addTextChanged(ed10, ed11);
        addTextChanged(ed11, ed12);
        String sss = (String) SPUtils.get(this, "Mobile", "");
        phonemy=sss;
        if (!TextUtils.isEmpty(sss) && sss.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sss.length(); i++) {
                char c = sss.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            edPhoneMy.setText(sb.toString());
        }
        edPhoneMy.setEnabled(false);

        if (!md5pay.isEmpty()) {
            if (md5pay.equals("3")||md5pay.equals("1")) {
                //忘记密码
                tvTitle.setText("设置支付密码");
            }
            if (md5pay.equals("2")) {
                tvTitle.setText("修改支付密码");
                ts.setText("请输入旧密码");
                llPhone.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void addTextChanged(final EditText ed_one, final EditText ed_two) {
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
                }else{
                    ed_one.requestFocus();
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
        finish();
    }

    @OnClick(R.id.get_code)
    public void onView() {

        if (phonemy.length() == 11) {
            try {
                getpin();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getpin() throws JSONException {
        JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        obj.put("mobile", phonemy);
        obj.put("pinflag", "2");
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/requestPIN", obj.toString(), map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {


                try {
                    BaseResponse baseResponse = JSON.parseObject(response, BaseResponse.class);
                    if (baseResponse.getCode() == 0) {
                        TimerUtils.startTimer(SetPwdActivity.this, getCode);
                    } else {
                        ToastUtil.getInstance(SetPwdActivity.this).showToast(baseResponse.getMessage());
                    }

                } catch (Exception e) {

                }

            }
        });
    }

    @OnClick(R.id.submit)
    public void onViewClickeds() {
        String ed1s = ed1.getText().toString();
        String ed2s = ed2.getText().toString();
        String ed3s = ed3.getText().toString();
        String ed4s = ed4.getText().toString();
        String ed5s = ed5.getText().toString();
        String ed6s = ed6.getText().toString();
        String ed7s = ed7.getText().toString();
        String ed8s = ed8.getText().toString();
        String ed9s = ed9.getText().toString();
        String ed10s = ed10.getText().toString();
        String ed11s = ed11.getText().toString();
        String ed12s = ed12.getText().toString();
        code=edCode.getText().toString();
        pwd1 = ed1s + ed2s + ed3s + ed4s + ed5s + ed6s;
        pwd2 = ed7s + ed8s + ed9s + ed10s + ed11s + ed12s;

        if (md5pay.equals("1") || md5pay.equals("3")) {

            if (pwd1.length() == 6 && pwd2.length() == 6) {
                if (check()){
                    setpwd();
                }
            } else {
                ToastUtil.getInstance(this).showToast("请输入6位支付密码");
            }
        } else if (md5pay.equals("2")) {
                upDatapwd();
        }
}

    private void upDatapwd() {
        JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        try {
            obj.put("password", MD5Util.md5(pwd1));
            obj.put("newpassword", MD5Util.md5(pwd2));
            OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/updatePayPass", obj.toString(), map, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    try {
                        BaseResponse baseResponse = JSON.parseObject(response, BaseResponse.class);
                        if (baseResponse.getCode() == 0) {
                            ToastUtil.getInstance(SetPwdActivity.this).showToast("设置成功");
                            QRcodeActivity.ispwd=true;
                            MyFragment.issetpwd=true;
                            ed1.setText("");
                            ed2.setText("");
                            ed3.setText("");
                            ed4.setText("");
                            ed5.setText("");
                            ed6.setText("");
                            ed7.setText("");
                            ed8.setText("");
                            ed9.setText("");
                            ed10.setText("");
                            ed11.setText("");
                            ed12.setText("");
                            ed1.requestFocus();
                            if (null!=sys&&sys.equals("sys")){
                                QRcodeActivity.startActivity(SetPwdActivity.this);
                            }
                            finish();
                        } else {
                            ToastUtil.getInstance(SetPwdActivity.this).showToast(baseResponse.getMessage());
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
    private void setpwd() {

        JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        try {
            String sss = MD5Util.md5(pwd1);
            obj.put("password", MD5Util.md5(pwd1));
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
                            ToastUtil.getInstance(SetPwdActivity.this).showToast("设置成功");
                            MyFragment.issetpwd=true;
                            QRcodeActivity.ispwd=true;
                            ed1.setText("");
                            ed2.setText("");
                            ed3.setText("");
                            ed4.setText("");
                            ed5.setText("");
                            ed6.setText("");
                            ed7.setText("");
                            ed8.setText("");
                            ed9.setText("");
                            ed10.setText("");
                            ed11.setText("");
                            ed12.setText("");
                            ed1.requestFocus();
                            if (null!=sys&&sys.equals("sys")){
                                QRcodeActivity.startActivity(SetPwdActivity.this);
                            }
                            finish();
                        } else {
                            ToastUtil.getInstance(SetPwdActivity.this).showToast(baseResponse.getMessage());
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
