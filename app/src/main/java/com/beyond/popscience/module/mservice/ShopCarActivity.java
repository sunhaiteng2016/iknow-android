package com.beyond.popscience.module.mservice;



import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;

import com.beyond.popscience.frame.util.BroadCastReceiverUtil;
import com.beyond.popscience.frame.util.MyDividerGridItemDecoration;

import com.beyond.popscience.widget.MyRecyclerView;

import butterknife.BindView;


/**
 * Describe：购物车
 * Date：2018/3/10
 * Time: 11:05
 * Author: Bin.Peng
 */
// item_shopcar ->> item_item_shopcar
public class ShopCarActivity extends BaseActivity implements  View.OnClickListener {

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
    @BindView(R.id.exListView)
    ExpandableListView exListView;
    @BindView(R.id.all_chekbox)
    CheckBox allChekbox;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_go_to_pay)
    TextView tvGoToPay;
    @BindView(R.id.lay)
    LinearLayout lay;
    @BindView(R.id.topReLay)
    RelativeLayout topReLay;
    @BindView(R.id.tv_title_new)
    TextView tvTitleNew;
    @BindView(R.id.recy_group)
    MyRecyclerView recyGroup;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shop_car;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("购物车");
        tvRight.setVisibility(View.GONE);
        recyGroup.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyGroup.addItemDecoration(new MyDividerGridItemDecoration(this, 2));
        tvDelete.setOnClickListener(this);
        tvGoToPay.setOnClickListener(this);
        allChekbox.setOnClickListener(this);

    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);

    }


    private void loadData() {

    }


    @Override
    protected void onResume() {
       loadData();
        allChekbox.setChecked(false);
        tvGoToPay.setText("结算(" + 0 + ")");
        tvTotalPrice.setText("¥ 0.00");
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
