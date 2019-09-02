package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.adapter.AllOrderAdapter;
import com.beyond.popscience.locationgoods.bean.OrderLsitTwoBean;
import com.beyond.popscience.locationgoods.bean.OrderlsitBean;
import com.beyond.popscience.locationgoods.dIalog.WlPopWindow;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopOrderActivity extends BaseActivity {


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
    private List<OrderlsitBean> list = new ArrayList<>();
    public int tabPosition;
    @Request
    AddressRestUsage addressRestUsage;
    private int orderStatus = 0, page = 1, pageSize = 10;
    private AllOrderAdapter adapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shop_order;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("订单管理");
        initTab();
        initListView(0);
        getOrderData();
    }

    private void getOrderData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderStatus", orderStatus);
        map.put("page", page);
        map.put("pageSize", pageSize);
        addressRestUsage.Ddlb(1008611, map);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    List<OrderlsitBean> obj = (List<OrderlsitBean>) msg.getObj();
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
                    getOrderData();
                    ToastUtil.showCenter(this, "发货成功!");
                } else {
                    ToastUtil.showCenter(this, msg.getMsg());
                }
                break;
            case 1008623:
                if (msg.getIsSuccess()) {
                    getOrderData();
                }
                ToastUtil.showCenter(this, msg.getMsg());
                break;
        }
    }

    private void initListView(final int position) {
        CreatLayoutUtils.creatLinearLayout(this, rlv);
        adapter = new AllOrderAdapter(list, this, position);
        rlv.setAdapter(adapter);
        adapter.setBottomButtonClick(new AllOrderAdapter.BottomButtonClickLinster() {
            @Override
            public void onClick(int type, String orderSign) {
                if (type == 1) {
                    //立即发货
                    iniWlPop(orderSign);
                }
                if (type == 2) {
                    //取消订单
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("orderSn", orderSign);
                    addressRestUsage.jvJue(1008623, map);
                }
                if (type == 3) {
                    //查看物流
                    Intent intent = new Intent(ShopOrderActivity.this, LogisticsActivity.class);
                    startActivity(intent);
                }
            }
        });
        //item  点击事件
        adapter.setOnClickLinster(new AllOrderAdapter.ItemOnClickLinster() {
            @Override
            public void onClick(int itemPosition) {
                Intent intent = new Intent(ShopOrderActivity.this, ShopOrderDetailActivity.class);
                intent.putExtra("data", list.get(itemPosition));
                intent.putExtra("curPosition", tabPosition);
                startActivity(intent);
            }
        });

    }

    private void iniWlPop(final String orderSign) {
        WlPopWindow popWindow = new WlPopWindow(this);
        popWindow.show(rlv);
        popWindow.setAddressChangeListener(new WlPopWindow.IAddressChangeListener() {
            @Override
            public void onClick(String wlgs, String whdh) {
                //请求发货的接口
                HashMap<String, Object> map = new HashMap<>();
                map.put("deliveryCompany", wlgs);
                map.put("deliverySn", whdh);
                map.put("orderSn", orderSign);
                addressRestUsage.fahuo(1008622, map);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initTab() {
        tab.addTab(tab.newTab().setText("待付款"));
        tab.addTab(tab.newTab().setText("待发货"));
        tab.addTab(tab.newTab().setText("已发货"));
        tab.addTab(tab.newTab().setText("已完成"));
        tab.addTab(tab.newTab().setText("已取消"));
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                initListView(tabPosition);
                orderStatus = tabPosition;
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
}
