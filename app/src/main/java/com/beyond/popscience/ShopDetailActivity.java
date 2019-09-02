package com.beyond.popscience;

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

import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.fragment.SGoosListFragment;
import com.beyond.popscience.locationgoods.fragment.SShopDateilFragment;
import com.beyond.popscience.locationgoods.fragment.SUserTakeFragment;
import com.beyond.popscience.locationgoods.fragment.ShopGoosListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店铺详情
 */
public class ShopDetailActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.iv_dw)
    ImageView ivDw;
    @BindView(R.id.lay_header)
    RelativeLayout layHeader;
    @BindView(R.id.system_bar_view)
    View systemBarView;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.go_shop_car)
    LinearLayout goShopCar;
    @BindView(R.id.take_about)
    LinearLayout takeAbout;
    @BindView(R.id.buy_now)
    TextView buyNow;
    @BindView(R.id.tv_qpt)
    TextView tvQpt;
    @BindView(R.id.buy_now_totager)
    RelativeLayout buyNowTotager;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    private String[] titles = new String[]{"商品", "用户评价", "商家详情"};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shop_detail;
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
        init();
    }

    private void init() {
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
        }
        fragments.add(new ShopGoosListFragment());
        fragments.add(new SUserTakeFragment());
        fragments.add(new SShopDateilFragment());
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(new FmPagerAdapter(fragments, getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewpager);
        for (int j = 0; j < titles.length; j++) {
            tablayout.getTabAt(j).setText(titles[j]);
        }
    }

    @OnClick({R.id.go_back, R.id.go_shop_car, R.id.take_about, R.id.buy_now_totager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.go_shop_car:
                break;
            case R.id.take_about:
                break;
            case R.id.buy_now_totager:
                break;
        }
    }
}
