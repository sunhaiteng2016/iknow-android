package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.bean.Flbean;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.locationgoods.shopcar.util.ToastUtil;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BdtcClassActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    private List<Flbean> list = new ArrayList<>();
    private CommonAdapter<Flbean> adapter;
    @Request
    AddressRestUsage addressRestUsage;
    private String className;
    private String shopType;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_bdtc_class;
    }

    @Override
    public void initUI() {
        super.initUI();
        className = getIntent().getStringExtra("className");
        shopType = getIntent().getStringExtra("shopType");
        iniListView();
        if (shopType.equals("1")) {
            getData();
            title.setText("本地特产");
        }
        if (shopType.equals("5")){
            getDatas();
            title.setText("鞋服");
        }
        if (shopType.equals("6")){
            getDatas();
            title.setText("数码");
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    List<Flbean> obj = (List<Flbean>) msg.getObj();
                    list.clear();
                    list.addAll(obj);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void getData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("area", WelcomeActivity.seletedAdress.split("-")[1]);
        addressRestUsage.getFl(1008611, map);
    }

    private void getDatas() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("area", className);
        addressRestUsage.getFl(1008611, map);
    }

    private void iniListView() {
        CreatLayoutUtils.cretGridViewLayout(this, rlv, 4);
        adapter = new CommonAdapter<Flbean>(this, R.layout.item_only_textview_three_ss, list) {
            @Override
            protected void convert(ViewHolder holder, Flbean flbean, int position) {
                holder.setText(R.id.tv, flbean.getName());
            }
        };
        rlv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (shopType.equals("1")){
                    if (className.equals(list.get(position).getName())) {
                        //返回数据
                        Intent intent = new Intent();
                        intent.putExtra("name", list.get(position).getName());
                        setResult(101, intent);
                        finish();
                    } else {
                        com.beyond.library.util.ToastUtil.showCenter(BdtcClassActivity.this, "你发布的商品，不是本地特产，不能发布到本地特产类别！");
                    }
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("name", list.get(position).getName());
                    setResult(101, intent);
                    finish();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
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
