package com.beyond.popscience.locationgoods;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDateActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.listview)
    RecyclerView listview;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_date;
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
        title.setText("订单详情");
        iniListView();
    }

    private void iniListView() {
        CreatLayoutUtils.creatLinearLayout(this,listview);
        List<String> list= new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        CommonAdapter<String> adapter = new CommonAdapter<String>(this, R.layout.item_oder_date, list) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };

        listview.setAdapter(adapter);
    }
}
