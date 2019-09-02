package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.beyond.popscience.view.OnPasswordInputFinish;
import com.beyond.popscience.view.PasswordView;
import com.bumptech.glide.Glide;
import com.jack.bottompopupwindowview.BottomPopupWindowView;
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
 * 转账
 * Created by YAO.CUI on 2017/6/12.
 */

public class ZhuanZhangActivity extends BaseActivity {
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
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.topReLay)
    RelativeLayout topReLay;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.fenshu)
    EditText fenshu;
    @BindView(R.id.bottom_popup)
    BottomPopupWindowView bottomPopup;
    @BindView(R.id.remark)
    EditText remark;
    @Request
    private CommonRestUsage mRestUsage;
    private String result;
    private String codesss;
    private Erweima erweima;

    public static void startActivity(Context context, String result) {
        Intent intent = new Intent(context, ZhuanZhangActivity.class);
        intent.putExtra("result", result);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("绿币转账");
        result = getIntent().getStringExtra("result");
        try {
            erweima = JSON.parseObject(result, Erweima.class);
            tvName.setText(erweima.getName());
            if (!TextUtils.isEmpty(erweima.getPhone()) && erweima.getPhone().length() > 6) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < erweima.getPhone().length(); i++) {
                    char c = erweima.getPhone().charAt(i);
                    if (i >= 3 && i <= 6) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }

                tvNum.setText(sb.toString());
            }
            Glide.with(this).load(erweima.getHeadimg()).into(ivHead);
        } catch (Exception e) {
            ToastUtil.getInstance(this).showToast("解析错误");
        }
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
        return R.layout.activity_zhuanzhuang;
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

    @OnClick(R.id.submit)
    public void onViewClickeds() {

        codesss = fenshu.getText().toString().trim();
        if (codesss.length() <= 0) {
            ToastUtil.getInstance(this).showToast("请输入绿币！");
            return;
        }
        String scores = (String) SPUtils.get(ZhuanZhangActivity.this, "scores", "");
        if (Double.parseDouble(codesss + ".00") <= Double.parseDouble(scores)) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0); //强制隐藏键盘
            showPopupWindow(view1);
        } else {
            ToastUtil.getInstance(this).showToast("亲，您没有这么多绿币！！");
        }
    }

    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(ZhuanZhangActivity.this).inflate(
                R.layout.pop_window, null);
        bottomPopup.setContextView(contentView);
        bottomPopup.setVisibility(View.VISIBLE);
        bottomPopup.showPopouView();
        final PasswordView passwordView = (PasswordView) contentView.findViewById(R.id.PasswordView);

        passwordView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                sendCode(passwordView.getStrPassword());
            }
        });
    }

    /**
     * 转账绿币
     */
    private void sendCode(String pwd) {

        JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        try {
            obj.put("touserid", erweima.getUserId());
            obj.put("score", codesss);
            obj.put("password", MD5Util.md5(pwd));
            obj.put("remarks", remark.getText().toString().trim());

            OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/transferScore", obj.toString(), map, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                }

                @Override
                public void onResponse(String response) {
                    try {
                        BaseResponse baseResponse = JSON.parseObject(response, BaseResponse.class);
                        if (baseResponse.getCode() == 0) {
                            ToastUtil.getInstance(ZhuanZhangActivity.this).showToast("转账成功");
                            //每次转账 都扣除本地积分
                            String totalScore = SPUtils.get(ZhuanZhangActivity.this, "score", "") + "";
                            double score = Double.parseDouble(totalScore) - Double.parseDouble(codesss + ".00");
                            SPUtils.put(ZhuanZhangActivity.this, "score",""+score);
                            XfjlActivity.startActivity(ZhuanZhangActivity.this);

                            finish();
                        } else {
                            ToastUtil.getInstance(ZhuanZhangActivity.this).showToast(baseResponse.getMessage());
                        }
                    } catch (Exception e) {

                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
