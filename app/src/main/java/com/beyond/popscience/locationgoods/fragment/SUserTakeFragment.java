package com.beyond.popscience.locationgoods.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.locationgoods.bean.EventionBean;
import com.beyond.popscience.locationgoods.bean.ProductDetail;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.locationgoods.view.StarBar;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SUserTakeFragment extends BaseFragment {
    @BindView(R.id.listView)
    RecyclerView listView;
    Unbinder unbinder;
    @BindView(R.id.nsc)
    NestedScrollView nsc;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private CommonAdapter<EventionBean> adapter;

    @Request
    AddressRestUsage addressRestUsage;
    private int page = 1, pageSize = 10;
    private int productId;
    List<EventionBean> lists = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_address_two;
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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initDateListView();
    }

    private void getData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("page", page);
        map.put("pageSize", pageSize);
        addressRestUsage.getProductEvaluation(1008611, map);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    List<EventionBean> obj = (List<EventionBean>) msg.getObj();
                    if (obj.size() > 0) {
                        if (page == 1) {
                            lists.clear();
                        }
                        lists.addAll(obj);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                        } else {
                            ToastUtil.showCenter(getActivity(), "没有更多数据了");
                        }
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(ProductDetail messageEvent) {
        productId = messageEvent.getProduct().getId();
        getData();
    }
    private void initDateListView() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                nsc.scrollTo(0, 0);
            }
        });

        CreatLayoutUtils.creatLinearLayout(getActivity(), listView);

        adapter = new CommonAdapter<EventionBean>(getActivity(), R.layout.item_user_take, lists) {

            @Override
            protected void convert(ViewHolder holder, EventionBean s, int position) {

                StarBar startBar = holder.getView(R.id.startBar);
                startBar.setClickAble(false);
                startBar.setStarMark((float) s.getScore());
                Glide.with(getActivity()).load(s.getAvatar()).into((ImageView) holder.getView(R.id.head_img));
                holder.setText(R.id.name, s.getNickName()).setText(R.id.content, s.getDetail());
                RecyclerView rlv2 = holder.getView(R.id.rlv2);
                String imgList = s.getImglist();
                if (null != imgList && imgList.length() > 0) {
                    String[] imgLists = imgList.split(",");
                    List<String> lissss = new ArrayList<>();
                    for (String bean : imgLists) {
                        lissss.add(bean);
                    }
                    CreatLayoutUtils.cretGridViewLayout(getActivity(), rlv2, 3);
                    CommonAdapter<String> adapter11 = new CommonAdapter<String>(getActivity(), R.layout.item_only_img, lissss) {
                        @Override
                        protected void convert(ViewHolder holder, String s, int position) {
                            Glide.with(getActivity()).load(s).into((ImageView) holder.getView(R.id.img));
                        }
                    };
                    rlv2.setAdapter(adapter11);
                }
            }
        };
        listView.setAdapter(adapter);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                page++;
                getData();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                page = 1;
                getData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
