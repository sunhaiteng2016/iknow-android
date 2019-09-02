package com.beyond.popscience.module.town.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ListView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.NewsDetailContent;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.XXX;
import com.beyond.popscience.module.news.adapter.LinearAdapter;
import com.beyond.popscience.module.news.adapter.NewsDetailListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lenovo on 2017/6/23.
 */

public class IntroFragment extends BaseFragment {

    public static final String EXTRA_ADDRESS_KEY = "address";
    /**
     * 请求简介
     */
    private final int REQUEST_NEWS_DETAIL_TASK_ID = 101;
    /**
     *
     */
    @BindView(R.id.listView)
    protected RecyclerView listView;
    private LinearAdapter newsDetailListAdapter;

    @Request
    private TownRestUsage townRestUsage;
    private String villageId;
    private List<NewsDetailContent> listsss = new ArrayList<>();

    public static IntroFragment getInstance(Address address) {
        IntroFragment introFragment = new IntroFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ADDRESS_KEY, address);

        introFragment.setArguments(bundle);
        return introFragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_intro_three;
    }

    @Override
    public void initUI() {
        super.initUI();
        initListView();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(XXX messageEvent) {
        townRestUsage.getVillageMessage(REQUEST_NEWS_DETAIL_TASK_ID, villageId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_NEWS_DETAIL_TASK_ID:   //新闻详情
                if (msg.getIsSuccess()) {
                    List<NewsDetailContent> newsDetailContentList = (List<NewsDetailContent>) msg.getObj();
                    if (newsDetailContentList != null) {
//                        switchListEmptyView(false);
                        listsss.clear();
                        listsss.addAll(newsDetailContentList);
                        newsDetailListAdapter.notifyDataSetChanged();
                    }
                } else {
//                    switchListEmptyView(true);
                }
                //listView.onRefreshComplete();
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        newsDetailListAdapter = new LinearAdapter(getActivity(), listsss);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        listView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        //文字大小 限制死
        newsDetailListAdapter.setTxtSize(DensityUtil.sp2px(getContext(), 17));
        listView.setAdapter(newsDetailListAdapter);
        requestNewsDetail();
    }

    /**
     * 请求新闻详情
     */
    private void requestNewsDetail() {
        if (getArguments() != null) {
            Address address = (Address) getArguments().getSerializable(EXTRA_ADDRESS_KEY);
            if (address != null) {
                villageId = String.valueOf(address.getId());
                ;
                if (TextUtils.equals(villageId, "0") && address.getParentAddress() != null) {
                    villageId = String.valueOf(address.getParentAddress().getId());
                }
                townRestUsage.getVillageMessage(REQUEST_NEWS_DETAIL_TASK_ID, villageId);
            }
        }
    }


}
