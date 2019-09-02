package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.beyond.popscience.locationgoods.adapter.AllOrderTwoAdapter;
import com.beyond.popscience.locationgoods.bean.OrderLsitTwoBean;
import com.beyond.popscience.locationgoods.dIalog.PayPopWindow;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.shopcart.PaySuccessActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.common.util.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderTwoActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_zwsj)
    LinearLayout llZwsj;
    private List<OrderLsitTwoBean> list = new ArrayList<>();
    public int tabPosition;
    @Request
    AddressRestUsage addressRestUsage;
    private int orderStatus = 0, page = 1, pageSize = 10;
    private AllOrderTwoAdapter adapter;
    public String zfbObj;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shop_order;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("我的订单");
        initTab();
        initListView(0);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        getOrderData();
    }

    private void getOrderData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderStatus", orderStatus);
        map.put("page", page);
        map.put("pageSize", pageSize);
        addressRestUsage.sjddlb(1008611, map);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getOrderData();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008633:
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, msg.getMsg());
                    getOrderData();
                } else {
                    ToastUtil.showCenter(this, msg.getMsg());
                }
                break;
            case 1008611:
                if (msg.getIsSuccess()) {
                    List<OrderLsitTwoBean> obj = (List<OrderLsitTwoBean>) msg.getObj();
                    if (obj.size() > 0) {
                        llZwsj.setVisibility(View.GONE);
                        rlv.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(obj);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                            llZwsj.setVisibility(View.VISIBLE);
                            rlv.setVisibility(View.GONE);
                        } else {
                            ToastUtil.showCenter(this, "没有更多数据了！");
                        }
                    }
                }
                break;
            case 1008622:
                if (msg.getIsSuccess()) {
                    zfbObj = (String) msg.getObj();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(OrderTwoActivity.this);
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
                        ToastUtil.show(OrderTwoActivity.this, "支付成功!");
                        getOrderData();
                        ToastUtil.showCenter(OrderTwoActivity.this, "恭喜您, + 1 科普绿币!");
                        Intent intent = new Intent(OrderTwoActivity.this, PaySuccessActivity.class);
                        startActivity(intent);
                    } else {
//                        blance_tv.setText("¥ " + blance);
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if ("6001".equals(resultStatus)) {
                            ToastUtil.show(OrderTwoActivity.this, "取消支付！");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    private void initListView(final int position) {
        CreatLayoutUtils.creatLinearLayout(this, rlv);
        adapter = new AllOrderTwoAdapter(list, this, position);
        rlv.setAdapter(adapter);

        adapter.setBottomButtonClick(new AllOrderTwoAdapter.BottomButtonClickLinster() {
            @Override
            public void onClick(int type, String orderSign) {
                if (type == 1) {
                    iniWlPop(orderSign);
                }
                if (type == 2) {
                    //
                    String[] ordersss = orderSign.split("/");
                    if (ordersss.length == 3) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("deliveryCompany", ordersss[0]);
                        map.put("deliverySn", ordersss[1]);
                        map.put("orderSn", ordersss[2]);
                        addressRestUsage.qrsh(1008633, map);
                    } else {
                        ToastUtil.showCenter(OrderTwoActivity.this, "信息有误，请重试！");
                    }
                }
                if (type == 3) {
                    //查看物流
                    Intent intent = new Intent(OrderTwoActivity.this, LogisticsActivity.class);
                    intent.putExtra("kddh", orderSign);
                    startActivity(intent);
                }
                if (type == 4) {
                    Intent intent = new Intent(OrderTwoActivity.this, GoodsEvaluateActivity.class);
                    intent.putExtra("data", orderSign);
                    startActivity(intent);
                }
                if (type == 6) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("orderSn", orderSign);
                    addressRestUsage.cancleOrder(1008633, map);
                }
            }
        });
        //item  点击事件
        adapter.setOnClickLinster(new AllOrderTwoAdapter.ItemOnClickLinster() {
            @Override
            public void onClick(int itemPosition) {
                Intent intent = new Intent(OrderTwoActivity.this, MjOrderDetailActivity.class);
                intent.putExtra("data", (Serializable) list.get(itemPosition));
                intent.putExtra("curPosition", tabPosition);
                startActivity(intent);
            }
        });
    }

    //立即付款
    private void iniWlPop(final String orderSign) {
        final String[] orderSignss = orderSign.split("/");
        PayPopWindow payPopWindow = new PayPopWindow(this, orderSignss[0], orderSignss[1]);
        payPopWindow.setSubmitPayClickLinterset(new PayPopWindow.submitPayClickLinster() {
            @Override
            public void OnClick(int pay) {
                if (pay == 1) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("orderNo", orderSignss[2]);
                    addressRestUsage.ZfbOrder(1008622, map);
                }
            }
        });
        payPopWindow.show(rlv);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initTab() {
        tab.addTab(tab.newTab().setText("待付款"));
        tab.addTab(tab.newTab().setText("正在拼团"));
        tab.addTab(tab.newTab().setText("待发货"));
        tab.addTab(tab.newTab().setText("已发货"));
        tab.addTab(tab.newTab().setText("已完成"));
        tab.addTab(tab.newTab().setText("已取消"));
        //
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                initListView(tabPosition);
                if (tabPosition == 0) {
                    orderStatus = 0;
                }
                if (tabPosition == 1) {
                    orderStatus = 5;
                }
                if (tabPosition == 2) {
                    orderStatus = 1;
                }
                if (tabPosition == 3) {
                    orderStatus = 2;
                }
                if (tabPosition == 4) {
                    orderStatus = 3;
                }
                if (tabPosition == 5) {
                    orderStatus = 4;
                }
                page = 1;
                getOrderData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore();
        page++;
        getOrderData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();

        page = 1;
        getOrderData();
    }
}
