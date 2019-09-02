package com.beyond.popscience.module.home.shopcart;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 产品参数  item_layout_canshu
 */
public class GoodsCanShuActivity extends BaseActivity {


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
    @BindView(R.id.tv_out)
    TextView tvOut;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.rl_canshu)
    RecyclerView rlCanshu;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_canshu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
