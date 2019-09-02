package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.bumptech.glide.Glide;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绿币转账
 * Created by YAO.CUI on 2017/6/12.
 */

public class LbzzActivity extends BaseActivity {
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
    @BindView(R.id.iv_erweima)
    ImageView flMyContainer;
    @BindView(R.id.nick_name)
    TextView nickName;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.ewm_head)
    ImageView ewm_head;

    @Request
    private CommonRestUsage mRestUsage;
    private Bitmap mBitmap;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LbzzActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("我的二维码");
        //生成二维码
        String nickNames = UserInfoUtil.getInstance().getUserInfo().getNickName();
        String address_ss = (String) SPUtils.get(this, "Mobile", "");
        String headimg = UserInfoUtil.getInstance().getUserInfo().getAvatar();
        Glide.with(this).load(headimg).error(R.drawable.default_header_icon).into(ewm_head);
        nickName.setText(nickNames);

        if (!TextUtils.isEmpty(address_ss) && address_ss.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address_ss.length(); i++) {
                char c = address_ss.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            address.setText(sb.toString());
        }

        Erweima erweima = new Erweima();
        erweima.setUserId(UserInfoUtil.getInstance().getUserInfo().getUserId());
        erweima.setHeadimg(headimg);
        erweima.setName(nickNames);
        erweima.setAddress(address_ss);
        erweima.setPhone((String) SPUtils.get(this, "Mobile", ""));
        String json =  JSON.toJSONString(erweima);
        mBitmap = CodeUtils.createImage(json, 400, 400, null);
        flMyContainer.setImageBitmap(mBitmap);
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
        return R.layout.activity_lbzz;
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

    @OnClick(R.id.iv_erweima)
    public void onViewClickeds() {
        //ZhuanZhangActivity.startActivity(this,"1000");
    }
}
