package com.beyond.popscience.module.home.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class newTestTownFragment extends BaseFragment {
    @BindView(R.id.icon_iv)
    ImageView iconIv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.smooth_app_bar_layout)
    AppBarLayout smoothAppBarLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    Unbinder unbinder;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_test_town;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void initUI() {
        super.initUI();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl,TownChengYuanFragment.getInstance(null)).commit();
    }
}
