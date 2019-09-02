package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.PayResult;
import com.beyond.popscience.locationgoods.bean.OrderLsitTwoBean;
import com.beyond.popscience.locationgoods.dIalog.PayPopWindow;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.shopcart.PaySuccessActivity;
import com.bumptech.glide.Glide;

import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 买家订单详情
 */
public class MjOrderDetailActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_attr)
    TextView tvAttr;
    @BindView(R.id.tv_ps_server)
    TextView tvPsServer;
    @BindView(R.id.tv_ps_address)
    TextView tvPsAddress;
    @BindView(R.id.tv_ps_price)
    TextView tvPsPrice;
    @BindView(R.id.tv_dd_num)
    TextView tvDdNum;
    @BindView(R.id.tv_dd_time)
    TextView tvDdTime;
    @BindView(R.id.tv_dd_pay_type)
    TextView tvDdPayType;
    @BindView(R.id.tv_price_bottom)
    TextView tvPriceBottom;
    @BindView(R.id.ll_dfk)
    LinearLayout llDfk;
    @BindView(R.id.ll_sfje)
    LinearLayout llSfje;
    @BindView(R.id.tv_bottom_btn1)
    TextView tvBottomBtn1;
    @BindView(R.id.tv_bottom_btn2)
    TextView tvBottomBtn2;
    @BindView(R.id.tv_num)
    TextView tvNum;

    private OrderLsitTwoBean data;
    private int curPosition;
    private String zfbObj;
    @Request
    AddressRestUsage addressRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_mj_order_detail;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("订单详情");
        data = (OrderLsitTwoBean) getIntent().getSerializableExtra("data");
        curPosition = getIntent().getIntExtra("curPosition", 0);
        if (curPosition == 0) {
            tvOrderStatus.setText("待付款");
            llDfk.setVisibility(View.VISIBLE);
            llSfje.setVisibility(View.VISIBLE);
            tvBottomBtn2.setText("确认付款");
            tvBottomBtn1.setVisibility(View.GONE);
        }
        if (curPosition == 2) {
            tvOrderStatus.setText("待发货");
            tvBottomBtn2.setText("取消订单");
            tvBottomBtn1.setVisibility(View.GONE);
            tvBottomBtn2.setVisibility(View.GONE);
        }
        if (curPosition == 3) {
            tvOrderStatus.setText("已发货");
            tvBottomBtn1.setText("查看物流");
            tvBottomBtn2.setText("确认收货");
        }
        if (curPosition == 4) {
            tvBottomBtn1.setVisibility(View.GONE);
            tvOrderStatus.setText("已完成");
            tvBottomBtn2.setText("去评价");
        }
        if (curPosition == 5) {
            llDfk.setVisibility(View.GONE);
            tvOrderStatus.setText("已关闭");
        }
        if (null != data) {
            setData();
        }
    }

    private void setData() {
        tvShopName.setText(data.getShopName() + "  >");
        tvName.setText(data.getItemList().get(0).getProductName());
        tvAttr.setText(data.getItemList().get(0).getSp1());
        tvPrice.setText("¥ " + data.getItemList().get(0).getProductPrice() + "");
        Glide.with(this).load(data.getItemList().get(0).getProductPic()).into(ivImg);
        tvPsPrice.setText("¥ " + data.getFreightAmount() + "");
        tvPsAddress.setText(data.getReceiverProvince() + data.getReceiverCity() + data.getReceiverRegion() + data.getReceiverDetailAddress());
        //订单信息
        tvDdNum.setText(data.getOrderSn() + "");
        tvDdTime.setText(data.getCreateTime());
        tvNum.setText("数量："+data.getItemList().get(0).getProductQuantity());
        if (data.getPayType() == 0) {
            tvDdPayType.setText("未支付");
        } else if (data.getPayType()==1){
            tvDdPayType.setText("支付宝");
        }else{
            tvDdPayType.setText("微信");
        }
        tvPriceBottom.setText("￥ " + data.getPayAmount());
        if (data.getDeleteStatus() == 0) {
            tvPsServer.setText("自提");
        }
        if (data.getDeleteStatus() == 1) {
            tvPsServer.setText("卖家配送");
        }
        if (data.getDeleteStatus() == 2) {
            tvPsServer.setText("快递");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back, R.id.tv_bottom_btn2, R.id.tv_bottom_btn1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.tv_bottom_btn1:
                if (curPosition == 2) {
                    //取消订单

                }
                if (curPosition == 3) {
                    //查看物流
                    Intent intent = new Intent(this, LogisticsActivity.class);
                    intent.putExtra("kddh", data.getOrderSn());
                    startActivity(intent);
                }
                break;
            case R.id.tv_bottom_btn2:
                if (curPosition == 0) {
                    PayPopWindow payPopWindow = new PayPopWindow(this, data.getItemList().get(0).getProductName(), data.getPayAmount() + "");
                    payPopWindow.setSubmitPayClickLinterset(new PayPopWindow.submitPayClickLinster() {
                        @Override
                        public void OnClick(int pay) {
                            if (pay == 1) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("orderNo", data.getOrderSn());
                                addressRestUsage.ZfbOrder(1008622, map);
                            }
                        }
                    });
                    payPopWindow.show(tvBottomBtn1);
                }
                if (curPosition == 3) {
                    //确认收货
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("deliveryCompany", data.getDeliveryCompany());
                    map.put("deliverySn", data.getDeliverySn());
                    map.put("orderSn", data.getOrderSn());
                    addressRestUsage.qrsh(1008633, map);
                }
                if (curPosition == 4) {
                    Intent intent = new Intent(this, GoodsEvaluateActivity.class);
                    intent.putExtra("data", data.getOrderSn() + "&" + data.getItemList().get(0).getProductPic());
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008622:
                if (msg.getIsSuccess()) {
                    zfbObj = (String) msg.getObj();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(MjOrderDetailActivity.this);
                            Map<String, String> result = alipay.payV2(zfbObj, true);
                            Log.i("msp", result.toString());
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
                break;
            case 1008633:
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "确认收货成功！");
                    finish();
                } else {
                    ToastUtil.showCenter(this, msg.getMsg());
                }
                break;
        }
    }

    private static final int SDK_PAY_FLAG = 1;//支付宝支付请求id

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    LogUtil.e("TAG" + "orderInfo:" + resultInfo + "---" + resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show(MjOrderDetailActivity.this, "支付成功!");
                        ToastUtil.showCenter(MjOrderDetailActivity.this, "恭喜您, + 1 科普绿币!");
                        Intent intent = new Intent(MjOrderDetailActivity.this, PaySuccessActivity.class);
                        startActivity(intent);
                    } else {
//                        blance_tv.setText("¥ " + blance);
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if ("6001".equals(resultStatus)) {
                            ToastUtil.show(MjOrderDetailActivity.this, "取消支付！");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };
}
