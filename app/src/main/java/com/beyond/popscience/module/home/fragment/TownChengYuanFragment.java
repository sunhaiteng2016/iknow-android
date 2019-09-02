package com.beyond.popscience.module.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.TownUser;
import com.beyond.popscience.module.social.SocialCircleContentV2Activity;
import com.beyond.popscience.module.social.adapter.ChengyuanListAdapter;
import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lenovo on 2017/6/23.
 */

public class TownChengYuanFragment extends BaseFragment {

    public static final String EXTRA_ADDRESS_KEY = "address";
    /**
     * 请求简介
     */
    private final int REQUEST_NEWS_DETAIL_TASK_ID = 101;
    /**
     *
     */
    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    @Request
    private TownRestUsage townRestUsage;
    private List<TownUser> datas;
    private CommonAdapter<TownUser> adapter;

    public static TownChengYuanFragment getInstance(Address address) {
        TownChengYuanFragment introFragment = new TownChengYuanFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ADDRESS_KEY, address);

        introFragment.setArguments(bundle);
        return introFragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_changyuan;
    }

    @Override
    public void initUI() {
        super.initUI();
        initListView();
        requestNewsDetail();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_NEWS_DETAIL_TASK_ID:   //新闻详情
                if (msg.getIsSuccess()) {
                    List<TownUser> newsDetailContentList = (List<TownUser>) msg.getObj();
                    /*if (newsDetailContentList != null) {
                        datas.clear();
                        datas.addAll(newsDetailContentList);
                        adapter.notifyDataSetChanged();
                    }*/
                }
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        datas = new ArrayList<>();
        datas.add(new TownUser());
        datas.add(new TownUser());
        datas.add(new TownUser());
        datas.add(new TownUser());
        datas.add(new TownUser());
        adapter = new CommonAdapter<TownUser>(getActivity(), R.layout.item_user, datas) {

            @Override
            protected void convert(ViewHolder holder, TownUser townUser, int position) {
                Glide.with(getActivity()).load(townUser.getAvatar()).into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.contextTxtViewss, "张三");
            }
        };
        recyclerView.setAdapter(adapter);
    }

    /**
     * 请求新闻详情
     */
    private void requestNewsDetail() {
        if (getArguments() != null) {
            Address address = (Address) getArguments().getSerializable(EXTRA_ADDRESS_KEY);
            if (address != null) {
                String villageId = String.valueOf(address.getId());
                if (TextUtils.equals(villageId, "0") && address.getParentAddress() != null) {
                    villageId = String.valueOf(address.getParentAddress().getId());
                }
                if (address.getId() > 5000) {
                    //那就是村
                    townRestUsage.getTownUser(REQUEST_NEWS_DETAIL_TASK_ID, "2", villageId, 1);
                } else {
                    //那就是镇
                    townRestUsage.getTownUser(REQUEST_NEWS_DETAIL_TASK_ID, "1", villageId, 1);
                }
            }
        }
    }


}
