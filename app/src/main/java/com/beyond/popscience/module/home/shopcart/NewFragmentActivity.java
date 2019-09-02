package com.beyond.popscience.module.home.shopcart;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.home.fragment.NewsListKePuFragment;
import com.beyond.popscience.module.home.fragment.NewsListNoMenuFragment;
import com.beyond.popscience.module.home.fragment.ServiceFragment;
import com.beyond.popscience.module.home.fragment.ServiceTwoFragment;
import com.beyond.popscience.module.home.fragment.SocialFragment;
import com.beyond.popscience.module.home.fragment.TownFragment;
import com.beyond.popscience.module.town.TownCategoryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewFragmentActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.topReLay)
    RelativeLayout topReLay;
    public static int position;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_new_fragment;
    }

    @Override
    public void initUI() {
        super.initUI();
        String name = getIntent().getStringExtra("name");
        NavObj classId = (NavObj) getIntent().getSerializableExtra("nav");
        position = getIntent().getIntExtra("position", 0);
        if ("乡镇".equals(name)) {
            topReLay.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().add(R.id.fl, TownFragment.getInstance()).commit();
        }
        if ("社团".equals(name)) {
            topReLay.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().add(R.id.fl, SocialFragment.getInstance()).commit();
        }
        if ("文章".equals(name)) {
            String namess = classId.getClassName();
            tvTitle.setText(namess);
            getSupportFragmentManager().beginTransaction().add(R.id.fl, NewsListNoMenuFragment.getInstance(classId, News.TYPE_HOME_NEWS)).commit();
        }
        if ("科普".equals(name)){
            tvTitle.setText(name);
            getSupportFragmentManager().beginTransaction().add(R.id.fl, NewsListKePuFragment.getInstance(classId, News.TYPE_HOME_NEWS)).commit();
        }
        if ("服务".equals(name)){
            topReLay.setVisibility(View.GONE);
            tvTitle.setText(name);
            getSupportFragmentManager().beginTransaction().add(R.id.fl,ServiceTwoFragment.getInstance()).commit();
        }
    }

    public void switchFragmentmys() {
        Fragment showFragment = new TownCategoryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl, showFragment).commit();
    }

    /**
     * 切换Fragment
     *
     * @param
     */
    public void switchFragmentmy(Address address) {
        Fragment showFragment = TownFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("seletAddress", address);
        showFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl, showFragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
