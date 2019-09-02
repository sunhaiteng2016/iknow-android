package com.beyond.popscience.module.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.NewsRestUsage;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.FriendsBean;
import com.beyond.popscience.utils.sun.util.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFriendsActivity extends BaseActivity {

    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    private List<FriendsBean.DataBean> list = new ArrayList<>();
    private CommonAdapter<FriendsBean.DataBean> adapter;
    private String search;
    @Request
    private NewsRestUsage mRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_search_friends;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("搜索好友");
        initListView();
    }

    private void getDatas() {
        AppBaseRestUsageV2 appBaseRestUsageV2 = new AppBaseRestUsageV2();
        String url = MessageFormat.format("/im/queryUserByMobile/{0}/{1}", search, UserInfoUtil.getInstance().getUserInfo().getUserId());
        Map<String, String> paramMap = new HashMap<>();
        appBaseRestUsageV2.get(url, paramMap, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                FriendsBean firends = JSON.parseObject(responseStr, FriendsBean.class);
                if (firends.getCode()==0){
                    list.clear();
                    list.addAll(firends.getData());
                    adapter.notifyDataSetChanged();
                }else{
                    ToastUtil.show(SearchFriendsActivity.this,"无相关数据！");
                }
            }
            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });
    }

    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(this, rlv);
        adapter = new CommonAdapter<FriendsBean.DataBean>(this, R.layout.item_contact, list) {

            @Override
            protected void convert(ViewHolder holder, FriendsBean.DataBean friendsBean, int position) {
                holder.setText(R.id.contact_name, friendsBean.getNickname());
                Glide.with(SearchFriendsActivity.this).load(friendsBean.getAvatar()).into((ImageView) holder.getView(R.id.img));
            }
        };
        rlv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                int userId = list.get(position).getUserid();
                Intent intent = new Intent(SearchFriendsActivity.this, UserDetailsActivity.class);
                intent.putExtra("userId", userId);
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

    @OnClick(R.id.cancel)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.go_back)
    public void onViewClickeds() {
        finish();
    }

    @OnClick(R.id.submit)
    public void onViewClickedss() {
        search = edSearch.getText().toString().trim();
        getDatas();
    }
}
