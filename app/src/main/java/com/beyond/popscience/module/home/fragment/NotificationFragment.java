package com.beyond.popscience.module.home.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.platform.comapi.map.A;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.locationgoods.NotificationDetailActivity;
import com.beyond.popscience.locationgoods.bean.MessageCountThree;
import com.beyond.popscience.locationgoods.bean.MessageShuaxinThree;
import com.beyond.popscience.locationgoods.bean.MessageShuaxinTwo;
import com.beyond.popscience.locationgoods.bean.MessageShuaxinTwot;
import com.beyond.popscience.locationgoods.bean.NotificationBean;
import com.beyond.popscience.locationgoods.http.NotificationRestUsage;
import com.beyond.popscience.locationgoods.view.delrlv.DelAdapter;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotificationFragment extends BaseFragment implements DelAdapter.IonSlidingViewClickListener {
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private List<NotificationBean> lists = new ArrayList<>();
    private DelAdapter mAdapter;
    @Request
    NotificationRestUsage notificationRestUsage;
    int pageSize = 10, page = 1, type = 1;
    private int curPosition;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_notification;
    }

    @Override
    public void initUI() {
        super.initUI();
        initListView();
        getData();
    }

    public void XXX() {
        lists.get(curPosition).setIsRead(1);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("page", page);
        map.put("type", type);
        notificationRestUsage.pushList(1008611, map);
    }


    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(getActivity(), rlv);
        rlv.setItemAnimator(new DefaultItemAnimator());//设置控制Item增删的动画
        rlv.setAdapter(mAdapter = new DelAdapter(this, lists));//设置适配器

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                page = 1;
                getData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                page++;
                getData();
            }
        });
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    List<NotificationBean> bean = (List<NotificationBean>) msg.getObj();
                    if (bean.size() > 0) {
                        if (page == 1) {
                            lists.clear();
                        }
                        lists.addAll(bean);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {

                        } else {
                            ToastUtil.showCenter(getActivity(), "没有更多数据了！");
                        }
                    }
                }
                break;
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

    /**
     * item正文的点击事件
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        curPosition=position;
        MessagessFragment.flag=1;
        Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
        intent.putExtra("title", lists.get(position).getTitle());
        intent.putExtra("id", lists.get(position).getId());
        intent.putExtra("detail", lists.get(position).getDetail());
        intent.putExtra("time", lists.get(position).getCreateTime());
        startActivity(intent);//1：新闻  2：乡镇新闻  3：公告 4：添加好友//5 提现记录
    }


    /**
     * item的左滑设置
     *
     * @param view
     * @param position
     */
    @Override
    public void onSetBtnCilck(View view, int position) {
        //“设置”点击事件的代码逻辑
        //Toast.makeText(getActivity(), "请设置", Toast.LENGTH_LONG).show();
    }


    /**
     * item的左滑删除
     *
     * @param view
     * @param position
     */
    @Override
    public void onDeleteBtnCilck(View view, int position) {
        //同时删除消息
        notificationRestUsage.delete(1008612, lists.get(position).getId() + "");
        EventBus.getDefault().post(new MessageShuaxinTwot());
        mAdapter.removeData(position);
    }

}
