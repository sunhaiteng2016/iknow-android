package com.beyond.popscience.locationgoods.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.locationgoods.bean.ProductDetail;
import com.beyond.popscience.utils.sun.util.CreatLayoutUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShopGoosListFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.left_listView)
    RecyclerView leftListView;
    @BindView(R.id.right_listView)
    RecyclerView rightListView;
    private CommonAdapter<String> leftAdapter;
    private CommonAdapter<String> rightAdapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_address_three_one;
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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initDateListView();
    }

    private void initDateListView() {
        CreatLayoutUtils.creatLinearLayout(getActivity(), leftListView);
        CreatLayoutUtils.creatLinearLayout(getActivity(), rightListView);
        List<String> leftList = new ArrayList<>();
        leftList.add("");
        leftList.add("");
        leftList.add("");
        leftList.add("");
        leftAdapter = new CommonAdapter<String>(getActivity(), R.layout.item_shop_left, leftList) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        leftListView.setAdapter(leftAdapter);
        //右边初始化
        List<String> rightList = new ArrayList<>();
        rightList.add("");
        rightList.add("");
        rightList.add("");
        rightList.add("");
        rightList.add("");
        rightAdapter = new CommonAdapter<String>(getActivity(), R.layout.item_shop_right, rightList) {


            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        rightListView.setAdapter(rightAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(ProductDetail messageEvent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public class Describe {
        public String title;
        public String img;
    }

}
