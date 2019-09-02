package com.beyond.popscience.module.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.locationgoods.SearchActivity;
import com.beyond.popscience.module.home.UserDetailsActivity;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.TownUser;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2017/6/23.
 */

public class TownMemberFragment extends BaseFragment {

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
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.tv_wzc)
    TextView tvWzc;
    @BindView(R.id.ll_sarech)
    LinearLayout llSarech;

    @Request
    private TownRestUsage townRestUsage;
    private List<TownUser> datas = new ArrayList<>();
    private CommonAdapter<TownUser> adapter;
    private int page = 1;

    public static TownMemberFragment getInstance(Address address) {
        TownMemberFragment introFragment = new TownMemberFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ADDRESS_KEY, address);
        introFragment.setArguments(bundle);
        return introFragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_intro;
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
                    if (newsDetailContentList != null) {
                        if (newsDetailContentList.size() <= 0) {
                            Toast.makeText(getActivity(), "-----到底了-----", Toast.LENGTH_SHORT).show();
                        } else {
                            if (page == 1) {
                                datas.clear();
                            }
                            datas.addAll(newsDetailContentList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    String code = msg.getCode();
                    if (null != code) {
                        if (code.equals("3")) {
                            recyclerView.setVisibility(View.GONE);
                            tvWzc.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getActivity(), "-----到底了-----", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "-----到底了-----", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        adapter = new CommonAdapter<TownUser>(getActivity(), R.layout.item_user, datas) {

            @Override
            protected void convert(ViewHolder holder, TownUser townUser, int position) {
                Glide.with(getActivity()).load(townUser.getAvatar()).into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.contextTxtViewss, townUser.getNickname());
            }
        };
        recyclerView.setAdapter(adapter);
        //解决上下拉刷新冲突问题
        TownFragment.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (null != refreshLayout) {
                    if (verticalOffset >= 0) {
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.setEnableLoadMore(true);
                    } else {
                        refreshLayout.setEnableRefresh(false);
                        refreshLayout.setEnableLoadMore(true);
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                requestNewsDetail();
                refreshLayout.finishRefresh();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                requestNewsDetail();
                refreshLayout.finishLoadMore();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //
                Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                intent.putExtra("userId", datas.get(position).getUserid());

                intent.putExtra("flag", "1");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
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
                    townRestUsage.getTownUser(REQUEST_NEWS_DETAIL_TASK_ID, "2", villageId, page);
                } else {
                    //那就是镇
                    townRestUsage.getTownUser(REQUEST_NEWS_DETAIL_TASK_ID, "1", villageId, page);
                }
            }
        }
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

    @OnClick(R.id.ll_sarech)
    public void onViewClicked() {
        Intent intent=new Intent(getActivity(),SearchActivity.class);
        intent.putExtra("flag","xiangzhen");
        startActivity(intent);
    }
}
