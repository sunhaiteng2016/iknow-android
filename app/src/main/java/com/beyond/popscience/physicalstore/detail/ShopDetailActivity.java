package com.beyond.popscience.physicalstore.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.beyond.popscience.R;
import com.beyond.popscience.physicalstore.BaseActivity;
import com.beyond.popscience.physicalstore.utils.ViewUtils;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by k on 2017/10/30.
 */

public class ShopDetailActivity extends BaseActivity implements View.OnClickListener {

    private SimpleDraweeView iv_shop;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iv_shop = (SimpleDraweeView) findViewById(R.id.iv_shop);
        ViewUtils.getBlurFresco(mContext, (SimpleDraweeView) findViewById(R.id.iv_shop_bg), "res:///" + R.drawable.icon_shop);

        //  ViewUtils.getFrescoController(mContext, iv_shop, "res:///" + R.drawable.icon_shop, 40, 40);
    }

    @Override
    public void onClick(View v) {

    }

}

