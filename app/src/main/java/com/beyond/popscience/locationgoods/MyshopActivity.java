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
import com.beyond.popscience.locationgoods.bean.DpxqBean;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.bumptech.glide.Glide;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyshopActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.ll_dpxx)
    LinearLayout llDpxx;
    @BindView(R.id.ll_spgl)
    LinearLayout llSpgl;
    @BindView(R.id.ll_dpgl)
    LinearLayout llDpgl;
    @BindView(R.id.ll_xpsj)
    LinearLayout llXpsj;
    @Request
    AddressRestUsage addressRestUsage;
    @BindView(R.id.tv_name)
    TextView tvName;
    private DpxqBean obj;
    private String shopType;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_myshop;
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
        title.setText("我的店铺");
        addressRestUsage.Dpxq(1008611, new HashMap<String, Object>());
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    obj = (DpxqBean) msg.getObj();
                    Glide.with(this).load(obj.getShopImg()).into(ivHead);
                    tvName.setText(obj.getShopName());
                    shopType=obj.getShopType();
                }
                break;
        }
    }

    @OnClick({R.id.go_back, R.id.ll_dpxx, R.id.ll_spgl, R.id.ll_dpgl, R.id.ll_jycx, R.id.ll_xpsj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.ll_dpxx:
                Intent intent1 = new Intent(this, ShopXXActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_spgl:
                if (null!=obj){
                    Intent intent2 = new Intent(this, GoodsEditActivity.class);
                    intent2.putExtra("shopId", obj.getId());
                    startActivity(intent2);
                }
                break;
            case R.id.ll_dpgl:
                Intent intent = new Intent(this, ShopOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_xpsj:
                Intent intentssss = new Intent(this, ReleaseGoodActivity.class);
                intentssss.putExtra("shopType",shopType);
                startActivity(intentssss);
                break;
            case R.id.ll_jycx:
                Intent intentss = new Intent(this, TransactionQueryActivity.class);
                startActivity(intentss);
                break;
        }
    }
}
