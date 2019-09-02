package com.beyond.popscience;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.locationgoods.ReleaseGoodActivity;
import com.beyond.popscience.locationgoods.ShopCarActivity;
import com.beyond.popscience.locationgoods.bean.DpxqBean;
import com.beyond.popscience.locationgoods.bean.ProductDetail;
import com.beyond.popscience.locationgoods.dIalog.GoodAttrPopGroupWindow;
import com.beyond.popscience.locationgoods.fragment.SGoosListFragment;
import com.beyond.popscience.locationgoods.fragment.SShopDateilFragment;
import com.beyond.popscience.locationgoods.fragment.SUserTakeFragment;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.fragment.Constant;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情
 */
public class GoodsDatesActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.kd)
    TextView kd;
    @BindView(R.id.ll_personal_me)
    LinearLayout llPersonalMe;
    @BindView(R.id.lay_header)
    RelativeLayout layHeader;
    @BindView(R.id.system_bar_view)
    View systemBarView;
    @BindView(R.id.take_about)
    LinearLayout takeAbout;
    @BindView(R.id.go_shop_car)
    LinearLayout goShopCar;
    @BindView(R.id.buy_now)
    TextView buyNow;
    @BindView(R.id.buy_now_totager)
    RelativeLayout buyNowTotager;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_group_name)
    TextView tvGroupName;
    @BindView(R.id.tv_sxl)
    TextView tvSxl;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_pt_num)
    TextView tvPtNum;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_qpt)
    TextView tvQpt;
    private String[] titles = new String[]{"商品详情", "用户评价", "卖家详情"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    @Request
    AddressRestUsage addressRestUsage;
    private String id;
    private ProductDetail obj;
    private String[] addressss;
    private String shopType;
    public boolean hasGroupPirce = false;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_dates;
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

        id = getIntent().getStringExtra("Id");
        String edit = getIntent().getStringExtra("flag");
        if (null != edit && edit.equals("edit")) {
            tvEdit.setVisibility(View.VISIBLE);
        } else {
            tvEdit.setVisibility(View.GONE);
        }
        String address = (String) SPUtils.get(GoodsDatesActivity.this, "detailedArea", "");
        addressss = address.split("-");
        init();
        getDates();
        addressRestUsage.Dpxq(1008612, new HashMap<String, Object>());
    }

    private void getDates() {
        addressRestUsage.getProductDateis(1008611, id);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 10098:
                if (msg.getIsSuccess()) {
                    String msgObj = (String) msg.getObj();
                    if (msgObj.equals("0")) {
                        ToastUtil.showCenter(this, msg.getMsg());
                    } else {
                        //商品弹窗
                        initPopUpWindows();
                    }
                }
                break;
            case 10099:
                if (msg.getIsSuccess()) {
                    String msgObj = (String) msg.getObj();
                    if (msgObj.equals("0")) {
                        ToastUtil.showCenter(this, msg.getMsg());
                    } else {
                        initPopUpWindowsTwo();
                    }
                }
                break;
            case 1008612:
                if (msg.getIsSuccess()) {
                    DpxqBean objs = (DpxqBean) msg.getObj();
                    shopType = objs.getShopType();
                }
                break;
            case 1008611:
                if (msg.getIsSuccess()) {
                    obj = (ProductDetail) msg.getObj();
                    title.setText(obj.getProduct().getName());
                    List<ProductDetail.ProductBean.SkuListBean> skuList = obj.getProduct().getSkuList();
                    for (ProductDetail.ProductBean.SkuListBean bean : skuList) {
                        if (null != bean.getGroupPrice()) {
                            tvPtNum.setText(obj.getProduct().getGroupSize() + "人拼团");
                            tvGroupName.setText("拼团价：￥" + bean.getGroupPrice());
                            tvPrice.setText("原价：￥" + bean.getPrice());
                            tvSxl.setText("销售量：" + bean.getSale());
                            hasGroupPirce = true;
                            break;
                        } else {
                            hasGroupPirce = false;
                            tvGroupName.setText("");
                            tvPtNum.setText("");
                            tvPrice.setText("原价：￥" + bean.getPrice());
                            tvSxl.setText("销售量：" + bean.getSale());
                        }
                    }
                    tvShopName.setText(obj.getProduct().getName());
                    kd.setText("快递：￥" + obj.getProduct().getExpressFee());
                    Glide.with(this).load(obj.getProduct().getPic()).into(ivBg);
                    EventBus.getDefault().post(obj);
                    ProductDetail.PtBean pt = obj.getPt();
                    if (null != pt) {
                        tvQpt.setText("加入拼团");
                    } else {
                        tvQpt.setText("发起拼团");
                    }
                }
                break;
        }
    }

    private void init() {
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
        }
        fragments.add(new SGoosListFragment());
        fragments.add(new SUserTakeFragment());
        SShopDateilFragment shopDetialsFragemnt = new SShopDateilFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        shopDetialsFragemnt.setArguments(bundle);
        fragments.add(shopDetialsFragemnt);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(new FmPagerAdapter(fragments, getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewpager);
        for (int j = 0; j < titles.length; j++) {
            tablayout.getTabAt(j).setText(titles[j]);
        }
    }

    @OnClick({R.id.take_about, R.id.go_shop_car, R.id.buy_now, R.id.buy_now_totager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_about:
                int userid = obj.getShop().getUserId();
                Intent intent1 = new Intent(GoodsDatesActivity.this, ChatActivity.class);
                intent1.putExtra(Constant.EXTRA_USER_ID, userid + "");
                startActivity(intent1);
                break;
            case R.id.go_shop_car:
                Intent intent = new Intent(GoodsDatesActivity.this, ShopCarActivity.class);
                startActivity(intent);
                break;
            case R.id.buy_now:
                HashMap<String, Object> objectHashMap = new HashMap<>();
                objectHashMap.put("areaName", addressss[2]);
                addressRestUsage.payStatus(10098, objectHashMap);
                break;
            case R.id.buy_now_totager:
                if (hasGroupPirce) {
                    HashMap<String, Object> objectHashMap1 = new HashMap<>();
                    objectHashMap1.put("areaName", addressss[2]);
                    addressRestUsage.payStatus(10099, objectHashMap1);
                }else{
                    ToastUtil.showCenter(this,"该商品暂不支持拼团！");
                }
                break;
        }
    }

    private void initPopUpWindowsTwo() {
        GoodAttrPopGroupWindow popWindow = new GoodAttrPopGroupWindow(GoodsDatesActivity.this, obj,1);
        popWindow.show(buyNowTotager);
    }

    private void initPopUpWindows() {
        GoodAttrPopGroupWindow popWindow = new GoodAttrPopGroupWindow(GoodsDatesActivity.this, obj,0);
        popWindow.show(buyNowTotager);
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.tv_edit)
    public void onViewClickedss() {
        Intent intent = new Intent(GoodsDatesActivity.this, ReleaseGoodActivity.class);
        intent.putExtra("mData", obj);
        intent.putExtra("shopType", shopType);
        startActivity(intent);
    }
}
