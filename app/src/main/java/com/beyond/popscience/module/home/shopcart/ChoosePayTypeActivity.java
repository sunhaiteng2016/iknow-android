package com.beyond.popscience.module.home.shopcart;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.api.CreatOrderApi;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.PayResult;

import org.xutils.common.util.LogUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择支付方式
 */
public class ChoosePayTypeActivity extends BaseActivity {

    @BindView(R.id.tv_tv)
    TextView tvTv;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_zhifubao)
    ImageView ivZhifubao;
    @BindView(R.id.ll_zhifubao)
    LinearLayout llZhifubao;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
    @BindView(R.id.ll_weixin)
    LinearLayout llWeixin;
    @BindView(R.id.iv_yue)
    ImageView ivYue;
    @BindView(R.id.ll_yue)
    LinearLayout llYue;
    @BindView(R.id.iv_yue2)
    ImageView ivYue2;
    @BindView(R.id.ll_yue2)
    LinearLayout llYue2;
    @BindView(R.id.iv_yue3)
    ImageView ivYue3;
    @BindView(R.id.ll_yue3)
    LinearLayout llYue3;
    @BindView(R.id.tv_pay_pop)
    TextView tvPayPop;
    @BindView(R.id.ll_body)
    LinearLayout llBody;
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

    private int payType = 2;// 1 余额, 2 支付宝 3, 微信
    private static final int SDK_PAY_FLAG = 1;//支付宝支付请求id
    @Request
    private CreatOrderApi creatOrderApi;//用来请求支付数据的
    private static final int PAY_TASKID = 1006;//支付id
    private String order_des;
    private String order_code;
    private String address;
    private String numss;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_choose_pay_type;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("选择支付方式");
        rightImgView.setVisibility(View.GONE);
        rightImgView.setImageResource(R.drawable.icon_dots);
        if (null != getIntent()) {
            order_des = getIntent().getStringExtra("order_des");
            order_code = getIntent().getStringExtra("order_code");
            address = getIntent().getStringExtra("address");
            numss = getIntent().getStringExtra("num");
        }

    }

   @OnClick(R.id.ll_yue)
   public void setLlYue(){//余额支付
       ivYue.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_selected));
       ivWeixin.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_unselected));
       ivZhifubao.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_unselected));
       payType = 1;
   }

   @OnClick(R.id.ll_zhifubao)
   public void setLlZhifubao(){//支付宝
       ivZhifubao.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_selected));
       ivWeixin.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_unselected));
       ivYue.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_unselected));
       payType = 2;

   }

   @OnClick(R.id.ll_weixin)
   public void setLlWeixin(){
       ivWeixin.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_selected));
       ivZhifubao.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_unselected));
       ivYue.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_unselected));
       payType = 3;
   }

   @OnClick(R.id.iv_close)
   public void setIvClose(){
       finish();
   }

   @OnClick(R.id.tv_pay_pop)
   public void setTvPayPop(){//立即支付
       if (2 == payType){//支付宝
           creatOrderApi.payAli(PAY_TASKID,order_des + "不想讲话",order_code,address,numss);
       }else if (3 == payType){ // 微信
           ToastUtil.show(ChoosePayTypeActivity.this,"功能升级中");
           return;
       }else {//其他
           ToastUtil.show(ChoosePayTypeActivity.this,"功能升级中");
           return;
       }
   }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case PAY_TASKID://支付宝支付
                if (msg.getIsSuccess()){
//                    AliPayBean aliPayBean = (AliPayBean) msg.getObj();
                    String aliPayBean = (String) msg.getObj();
                    aliPayFunc(aliPayBean);
                }
                break;
        }
    }

    //支付宝支付方法
    public void aliPayFunc(final String orderInfo) {

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         * orderInfo的获取必须来自服务端；
         */
        Log.e("支付参数s", "payV2: orderinfo-----" + orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ChoosePayTypeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
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
                        ToastUtil.show(ChoosePayTypeActivity.this,"支付成功!");
                        ToastUtil.showCenter(ChoosePayTypeActivity.this,"恭喜您, + 1 科普绿币!");
                        Intent intent = new Intent(ChoosePayTypeActivity.this, PaySuccessActivity.class);
                        startActivity(intent);

                    } else {
//                        blance_tv.setText("¥ " + blance);
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if ("6001".equals(resultStatus)){
                            ToastUtil.show(ChoosePayTypeActivity.this,"取消支付！");
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
