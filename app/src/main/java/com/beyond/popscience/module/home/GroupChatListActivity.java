package com.beyond.popscience.module.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.GroupBean;
import com.beyond.popscience.module.home.fragment.Constant;
import com.beyond.popscience.utils.sun.util.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupChatListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    private List<GroupBean.DataBean> list = new ArrayList<>();
    private CommonAdapter<GroupBean.DataBean> adapter;
    private int page = 1;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_group_chat_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore();
        page++;
        getDatas();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
        page = 1;
        getDatas();
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("群组列表");
        initView();
        getDatas();
    }

    private void getDatas() {
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/queryGroupByUser", map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                GroupBean groupBean = JSON.parseObject(responseStr, GroupBean.class);
                if (groupBean.getCode() == 0) {
                    list.clear();
                    list.addAll(groupBean.getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });
    }

    private void initView() {
        CreatLayoutUtils.creatLinearLayout(this, rlv);
        adapter = new CommonAdapter<GroupBean.DataBean>(this, R.layout.item_group_list, list) {

            @Override
            protected void convert(ViewHolder holder, GroupBean.DataBean groupBean, int position) {
                Glide.with(GroupChatListActivity.this).load(groupBean.getHeadimg()).into((ImageView) holder.getView(R.id.img));
                holder.setText(R.id.name, groupBean.getGroupname());
            }
        };
        rlv.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //开启群组聊天
                Intent intent = new Intent(GroupChatListActivity.this, ChatActivity.class);
                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                intent.putExtra("name",list.get(position).getGroupname());
                // it's single chat
                intent.putExtra(Constant.EXTRA_USER_ID, list.get(position).getGroupid());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }
}
