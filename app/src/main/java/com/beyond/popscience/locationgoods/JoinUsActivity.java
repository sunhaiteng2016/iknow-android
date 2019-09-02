package com.beyond.popscience.locationgoods;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.FmPagerAdapter;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.fragment.InformationSubmitFragmentTwo;
import com.beyond.popscience.locationgoods.fragment.ShopOpenFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//商家入驻
public class JoinUsActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
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
    private String[] titles = new String[]{"信息提交", "开店要求"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_join_us;
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
    public void initUI() {
        super.initUI();
        title.setText("商家入驻");
        init();
    }

    private void init() {
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
        }
        fragments.add(new InformationSubmitFragmentTwo());
        fragments.add(new ShopOpenFragment());
        viewpager.setAdapter(new FmPagerAdapter(fragments, getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewpager);
        for (int j = 0; j < titles.length; j++) {
            tablayout.getTabAt(j).setText(titles[j]);
        }
    }
}
