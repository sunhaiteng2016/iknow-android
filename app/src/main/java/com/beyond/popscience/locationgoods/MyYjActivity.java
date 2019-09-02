package com.beyond.popscience.locationgoods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.DpxqBean;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.bumptech.glide.Glide;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyYjActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.ll_dpxx)
    LinearLayout llDpxx;
         @BindView(R.id.ll_dpgl)
    LinearLayout llDpgl;
    @BindView(R.id.ll_xpsj)
    LinearLayout llXpsj;
    @Request
    AddressRestUsage addressRestUsage;
    @BindView(R.id.tv_name)
    TextView tvName;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_myyj;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("我的业绩");
        Glide.with(this).load(UserInfoUtil.getInstance().getUserInfo().getAvatar()).into(ivHead);
        tvName.setText(UserInfoUtil.getInstance().getUserInfo().getNickName());
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);

    }

    @OnClick({R.id.go_back, R.id.ll_dpxx, R.id.ll_spgl, R.id.ll_dpgl, R.id.ll_jycx, R.id.ll_xpsj, R.id.ll_jn_yq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.ll_dpxx:
                Intent intent6 = new Intent(this, TransactionQueryActivity.class);
                intent6.putExtra("flag", 4);
                startActivity(intent6);
                break;
            case R.id.ll_spgl:
                Intent intent5 = new Intent(this, TransactionQueryActivity.class);
                intent5.putExtra("flag", 5);
                startActivity(intent5);
                break;
            case R.id.ll_dpgl:
                Intent intent3 = new Intent(this, TransactionQueryActivity.class);
                intent3.putExtra("flag", 3);
                startActivity(intent3);
                break;
            case R.id.ll_xpsj:
                Intent intent2 = new Intent(this, TransactionQueryActivity.class);
                intent2.putExtra("flag", 2);
                startActivity(intent2);
                break;
            case R.id.ll_jycx:
                Intent intentss = new Intent(this, TransactionQueryActivity.class);
                intentss.putExtra("flag", 1);
                startActivity(intentss);
                break;
            case R.id.ll_jn_yq:
                Intent intentss5 = new Intent(this, TransactionQueryActivity.class);
                intentss5.putExtra("flag", 6);
                startActivity(intentss5);
                break;
        }
    }
}
