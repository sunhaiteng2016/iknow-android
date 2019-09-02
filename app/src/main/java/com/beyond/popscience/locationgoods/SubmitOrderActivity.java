package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.MAddressListBean;
import com.beyond.popscience.locationgoods.bean.OrderBean;
import com.beyond.popscience.locationgoods.bean.ProductDetail;
import com.beyond.popscience.locationgoods.dIalog.PayPopWindow;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.shopcart.ChoosePayTypeActivity;
import com.beyond.popscience.module.home.shopcart.PaySuccessActivity;
import com.bumptech.glide.Glide;

import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmitOrderActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ed_name)
    TextView edName;
    @BindView(R.id.ed_phone)
    TextView edPhone;
    @BindView(R.id.ed_shdz)
    TextView edShdz;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.iv_good_img)
    ImageView ivGoodImg;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_attr)
    TextView tvAttr;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_jian)
    ImageView ivJian;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.iv_jia)
    ImageView ivJia;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_yf)
    TextView tvYf;
    @BindView(R.id.tv1111111)
    TextView tv1111111;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.ed_remark)
    EditText edRemark;
    private int CurPosition;
    private ProductDetail.ProductBean data;
    private String num;
    private MAddressListBean address;

    @Request
    AddressRestUsage addressRestUsage;
    public String zfbObj;
    private String type;
    private ArrayList<ProductDetail.ProductBean.SkuListBean> skulists;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_submit_order;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("确认订单");
        data = (ProductDetail.ProductBean) getIntent().getSerializableExtra("data");
        CurPosition = getIntent().getIntExtra("position", 0);
        num = getIntent().getStringExtra("num");
        type=getIntent().getStringExtra("type");
        setData();
        addressRestUsage.getAddressList(10001, UserInfoUtil.getInstance().getUserInfo().getUserId());
    }


    private void setData() {
        Glide.with(this).load(data.getPic()).into(ivGoodImg);
        tvGoodName.setText(data.getName());
        tvNum.setText(num);
        tvAttr.setText(data.getSkuList().get(CurPosition).getSp1());
        skulists = new ArrayList<ProductDetail.ProductBean.SkuListBean>();
        for (ProductDetail.ProductBean.SkuListBean bean : data.getSkuList()) {
            if (null!=bean.getGroupPrice()){
                skulists.add(bean);
            }
        }
        if (type.equals("0")){
            double intNum = Double.parseDouble(num);
            double mPrice = (double) data.getSkuList().get(CurPosition).getPrice();
            double expressFee = (double) data.getExpressFee();
            BigDecimal bA1 = new BigDecimal(Double.toString(intNum));
            BigDecimal ba2 = new BigDecimal(Double.toString(mPrice));
            BigDecimal pricessss = bA1.multiply(ba2);
            BigDecimal b2 = new BigDecimal(Double.toString(expressFee));
            double totalePrice = pricessss.add(b2).doubleValue();
            tvTotalPrice.setText(new DecimalFormat("0.00").format(pricessss) + "");
            tvPrice.setText(mPrice + "");
            tv1111111.setText(totalePrice + "");
        }
        if (type.equals("1")){
            double expressFee = (double) data.getExpressFee();
            BigDecimal bA1 = new BigDecimal(num);
            BigDecimal ba2 = new BigDecimal(skulists.get(CurPosition).getGroupPrice()+"");
            BigDecimal pricessss = bA1.multiply(ba2);

            BigDecimal b2 = new BigDecimal(Double.toString(expressFee));
            double totalePrice = pricessss.add(b2).doubleValue();
            tvTotalPrice.setText(new DecimalFormat("0.00").format(pricessss) + "");
            tvPrice.setText(skulists.get(CurPosition).getGroupPrice() + "");
            tv1111111.setText(totalePrice + "");
        }

        tvYf.setText(data.getExpressFee() + "");
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
                        ToastUtil.show(SubmitOrderActivity.this, "支付成功!");
                        ToastUtil.showCenter(SubmitOrderActivity.this, "恭喜您, + 1 科普绿币!");
                        Intent intent = new Intent(SubmitOrderActivity.this, PaySuccessActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
//                        blance_tv.setText("¥ " + blance);
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if ("6001".equals(resultStatus)) {
                            ToastUtil.show(SubmitOrderActivity.this, "取消支付！");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    @Override
    public void refreshUI(int taskId, final MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008612:
                if (msg.getIsSuccess()) {
                    zfbObj = (String) msg.getObj();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(SubmitOrderActivity.this);
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
            case 1008611:
                if (msg.getIsSuccess()) {
                    final OrderBean obj = (OrderBean) msg.getObj();
                    final String orderSn = obj.getOrderSn();
                    PayPopWindow payPopWindow = new PayPopWindow(this, data.getName(), tv1111111.getText().toString());
                    payPopWindow.setSubmitPayClickLinterset(new PayPopWindow.submitPayClickLinster() {
                        @Override
                        public void OnClick(int pay) {
                            if (pay == 1) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("orderNo", orderSn);
                                addressRestUsage.ZfbOrder(1008612, map);
                            }
                        }
                    });
                    payPopWindow.show(submit);
                } else {
                    ToastUtil.showCenter(this, msg.getMsg());
                }
                break;
            case 10001:
                if (msg.getIsSuccess()) {
                    List<MAddressListBean> listBeans = (List<MAddressListBean>) msg.getObj();
                    //赛选出来 默认地址
                    for (MAddressListBean bean : listBeans) {
                        if (bean.getDefaultStatus() == 1) {
                            //设置数据
                            address = bean;
                            edName.setText(address.getName());
                            edPhone.setText(address.getPhoneNumber());
                            edShdz.setText(address.getDetailAddress());
                            break;
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.ll_address)
    public void onViewClickedss() {
        Intent intent = new Intent(SubmitOrderActivity.this, ReceivingAddressActivity.class);
        intent.putExtra("flag", "1");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 100) {
            address = (MAddressListBean) data.getSerializableExtra("address");
            edName.setText(address.getName());
            edPhone.setText(address.getPhoneNumber());
            edShdz.setText(address.getDetailAddress());
        }
    }

    @OnClick(R.id.submit)
    public void onViewClickeds() {
        if (null == address) {
            ToastUtil.showCenter(this, "请选择地址！");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("addressId", address.getId() + "");
        map.put("note", edRemark.getText().toString().trim());
        map.put("orderType", type);//0正常购买  1拼团b
        map.put("productId", data.getId() + "");
        map.put("price", tv1111111.getText().toString().trim());
        map.put("productSize", num);
        map.put("skuId", data.getSkuList().get(CurPosition).getId() + "");
        if (type.equals("0")){
            addressRestUsage.creatOrder(1008611, map);
        }else{
            addressRestUsage.creatOrderGroup(1008611, map);
        }
    }

}
