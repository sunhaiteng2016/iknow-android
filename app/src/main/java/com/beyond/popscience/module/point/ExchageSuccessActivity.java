package com.beyond.popscience.module.point;

import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.ProductSucBean;
import com.beyond.popscience.frame.util.TimeUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;

public class ExchageSuccessActivity extends BaseActivity {

    @BindView(R.id.success_name)
    TextView tvSuccessName;

    @BindView(R.id.success_phone)
    TextView tvSuccessPhone;

    @BindView(R.id.success_address)
    TextView tvSuccessAddress;

    @BindView(R.id.success_goods_name)
    TextView tvSuccessGoodsName;

    @BindView(R.id.success_price)
    TextView tvSuccessPrice;

    @BindView(R.id.success_post_way)
    TextView tvSuccessPostWay;

    @BindView(R.id.success_date)
    TextView tvSuccessDate;

    @BindView(R.id.success_order_no)
    TextView tvSuccessOrderNo;

    @BindView(R.id.success_image)
    ImageView success_image;

    @BindView(R.id.success_sum_money)
    TextView tvSuccessSumMoney;
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_exchage_success;
    }

    @Override
    public void initUI() {
        super.initUI();
        ProductSucBean bean = (ProductSucBean) getIntent().getSerializableExtra("productSucBean");
        tvSuccessAddress.setText(bean.getOrde().getOrderAddress());
        tvSuccessPhone.setText(bean.getOrde().getOrderPhone());
        tvSuccessName.setText(bean.getOrde().getOrderUser());
        tvSuccessGoodsName.setText(bean.getSt().getCommodityname());
        tvSuccessPrice.setText(bean.getSt().getIntegral() + "绿币");
       // tvSuccessPostWay.setText(bean.getOrde().getOrderLogiscompanyCode());
        tvSuccessOrderNo.setText(bean.getOrde().getOrderCode());
        tvSuccessDate.setText(TimeUtils.getStrTime(bean.getOrde().getCreateTime() + ""));
        tvSuccessSumMoney.setText(bean.getSt().getIntegral() + "绿币");
        Glide.with(ExchageSuccessActivity.this).load(bean.getSt().getDisplaythepicture()).into(success_image);
    }
}
