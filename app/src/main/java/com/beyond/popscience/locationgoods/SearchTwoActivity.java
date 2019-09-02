package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import com.beyond.popscience.module.home.fragment.view.ContactBean;
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
public class SearchTwoActivity extends BaseActivity {

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
    private CommonAdapter<ContactBean> adapter;
    private String flag, id;
    @Request
    SearchRestUsage searchRestUsage;
    private List<ContactBean> list = new ArrayList<>();
    private List<ContactBean> mData;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_serach;
    }

    @Override
    public void initUI() {
        super.initUI();
        mData = (List<ContactBean>) getIntent().getSerializableExtra("mData");
        initListView();
        title.setText("搜索");
    }

    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(this, rlv);

        adapter = new CommonAdapter<ContactBean>(this, R.layout.item_search_all, list) {

            @Override
            protected void convert(ViewHolder holder, ContactBean s, int position) {
                holder.setText(R.id.tv_name, s.getNickname());
                Glide.with(SearchTwoActivity.this).load(s.getHeadImg()).into((ImageView) holder.getView(R.id.iv_head));
            }
        };
        rlv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SearchTwoActivity.this, UserDetailsActivity.class);
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
                String inputs = edSearch.getText().toString().trim();
                list.clear();
                for (ContactBean bean : mData) {
                    if (bean.getName().contains(inputs) || bean.getMobile().contains(inputs)) {
                        list.add(bean);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

}
