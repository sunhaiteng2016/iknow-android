package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SearchRestUsage;
import com.beyond.popscience.locationgoods.bean.SearchUserbean;
import com.beyond.popscience.locationgoods.shopcar.util.ToastUtil;
import com.beyond.popscience.module.home.UserDetailsActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索乡镇成员,好友列表
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.iv_submit)
    ImageView ivSubmit;
    @BindView(R.id.ll_sarech)
    LinearLayout llSarech;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private CommonAdapter<SearchUserbean> adapter;
    private String flag, id;
    @Request
    SearchRestUsage searchRestUsage;
    List<SearchUserbean> list = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_serach;
    }

    @Override
    public void initUI() {
        super.initUI();
        flag = getIntent().getStringExtra("flag");
        id = getIntent().getStringExtra("id");
        initListView();
        title.setText("搜索");
    }

    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(this, rlv);

        adapter = new CommonAdapter<SearchUserbean>(this, R.layout.item_search_all, list) {

            @Override
            protected void convert(ViewHolder holder, SearchUserbean s, int position) {
                holder.setText(R.id.tv_name, s.getNickname());
                Glide.with(SearchActivity.this).load(s.getAvatar()).into((ImageView) holder.getView(R.id.iv_head));
            }
        };
        rlv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SearchActivity.this, UserDetailsActivity.class);
                intent.putExtra("userId", list.get(position).getUserid());
                intent.putExtra("flag", "1");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back, R.id.iv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.iv_submit:
                if (flag.equals("shetuan")) {
                    //社团成员查询
                    searchRestUsage.queryUserByTeam(1008611, id, edSearch.getText().toString());
                }
                if (flag.equals("xiangzhen")) {
                    //乡镇
                    searchRestUsage.queryUserByVillages(1008611, edSearch.getText().toString());
                }
                break;
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    List<SearchUserbean> bean = (List<SearchUserbean>) msg.getObj();
                    list.clear();
                    list.addAll(bean);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.makeText(this, msg.getMsg());
                }
                break;
        }
    }
}
