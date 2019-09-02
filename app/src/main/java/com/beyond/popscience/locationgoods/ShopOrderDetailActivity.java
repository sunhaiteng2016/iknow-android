package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.bean.OrderlsitBean;
import com.beyond.popscience.locationgoods.dIalog.WlPopWindow;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.locationgoods.view.TimeTools;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 卖家订单详情
 */
public class ShopOrderDetailActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_title)
    TextView tvOrderStatus;
    @BindView(R.id.tv_address_name)
    TextView tvAddressName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_attr)
    TextView tvAttr;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_dd_num)
    TextView tvDdNum;
    @BindView(R.id.tv_dd_time)
    TextView tvDdTime;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_yf)
    TextView tvYf;
    @BindView(R.id.tv_dd_pay_type)
    TextView tvDdPayType;
    @BindView(R.id.tv_submit_order)
    TextView tvSubmitOrder;
    @BindView(R.id.tv_price_bottom)
    TextView tv_price_bottom;
    @BindView(R.id.tv_title_time)
    TextView tvTitleTime;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_ssss)
    TextView tvSsss;
    private OrderlsitBean data;
    private int curPosition;
    private String zfbObj;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_detail;
    }


    @Request
    AddressRestUsage addressRestUsage;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private long MAX_TIME = 12000;
    private long curTime = 0;
    private boolean isPause = false;

    /**
     * 初始化Timer
     */
    public void initTimer() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (curTime == 0) {
                    curTime = MAX_TIME;
                } else {
                    curTime -= 1000;
                }
                Message message = new Message();
                message.what = WHAT;
                message.obj = curTime;
                mHandler.sendMessage(message);
            }
        };
        mTimer = new Timer();
    }

    private static final int WHAT = 101;

    @Override
    public void initUI() {
        super.initUI();
        title.setText("订单详情");
        initTimer();
        data = (OrderlsitBean) getIntent().getSerializableExtra("data");
        curPosition = getIntent().getIntExtra("curPosition", 0);
        if (curPosition == 0) {
            tvOrderStatus.setText("待付款");
            tvTitleTime.setVisibility(View.VISIBLE);
        }
        if (curPosition == 1) {
            tvOrderStatus.setText("待发货");
            tvSubmitOrder.setVisibility(View.VISIBLE);
            tvSubmitOrder.setText("立即发货");
            tvTitleTime.setVisibility(View.GONE);
        }
        if (curPosition == 2) {
            tvOrderStatus.setText("已发货");
            tvSubmitOrder.setVisibility(View.VISIBLE);
            tvSubmitOrder.setText("查看物流");
            tvTitleTime.setVisibility(View.GONE);
        }
        if (curPosition == 3) {
            tvOrderStatus.setText("已完成");
            tvTitleTime.setVisibility(View.GONE);
        }
        if (curPosition == 4) {
            tvOrderStatus.setText("已取消");
            tvTitleTime.setVisibility(View.GONE);
        }
        if (null != data) {
            setData();
        }
        //得到创建时间
        String createTime = data.getCreateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long createTimesss = sdf.parse(createTime).getTime();
            long currentTime = System.currentTimeMillis();
            MAX_TIME = createTimesss + (1000 * 60 * 60 * 24) - currentTime;
            mTimer.schedule(mTimerTask, 0, 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        tvAddressName.setText(data.getReceiverName());
        tvPhone.setText(data.getReceiverPhone());
        tvAddress.setText(data.getReceiverProvince() + data.getReceiverCity() + data.getReceiverRegion() + data.getReceiverDetailAddress());
        Glide.with(this).load(data.getItemList().get(0).getProductPic()).into(ivImg);
        tvName.setText(data.getItemList().get(0).getProductName());
        tvAttr.setText(data.getItemList().get(0).getSp1());
        tvDdNum.setText(data.getOrderSn() + "");
        tvDdTime.setText(data.getCreateTime());
        double payAmount = data.getPayAmount() - data.getFreightAmount();
        tvTotalPrice.setText("￥" + new DecimalFormat("0.00").format(payAmount));
        tvYf.setText("￥" + data.getFreightAmount() + "");
        tv_price_bottom.setText(data.getPayAmount() + "");
        tvPrice.setText("￥" + data.getItemList().get(0).getProductPrice() + "");
        tvNum.setText("数量：" + data.getItemList().get(0).getProductQuantity());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.tv_submit_order:
                if (curPosition == 1) {
                    //请求立即发货
                    WlPopWindow popWindow = new WlPopWindow(this);
                    popWindow.show(tvSubmitOrder);
                    popWindow.setAddressChangeListener(new WlPopWindow.IAddressChangeListener() {
                        @Override
                        public void onClick(String wlgs, String whdh) {
                            //请求发货的接口
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("deliveryCompany", wlgs);
                            map.put("deliverySn", whdh);
                            map.put("orderSn", data.getOrderSn());
                            addressRestUsage.fahuo(1008622, map);
                        }
                    });
                }
                if (curPosition == 2) {
                    //查看物流
                    Intent intent = new Intent(ShopOrderDetailActivity.this, LogisticsActivity.class);
                    intent.putExtra("kddh", data.getOrderSn());
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
                    ToastUtil.showCenter(this, "发货成功!");
                } else {
                    ToastUtil.showCenter(this, msg.getMsg());
                }
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    long sRecLen = (long) msg.obj;
                    //毫秒换成00:00:00格式的方式，自己写的。
                    if (null == tvTitleTime) return;
                    tvTitleTime.setText(TimeTools.getCountTimeByLong(sRecLen));
                    if (sRecLen <= 0) {
                        mTimer.cancel();
                        curTime = 0;
                    }
                    break;
            }
        }
    };
}
