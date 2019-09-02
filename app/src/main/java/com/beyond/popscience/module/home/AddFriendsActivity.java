package com.beyond.popscience.module.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.entity.ApplyFriendList;
import com.beyond.popscience.module.home.entity.ShuaXin;
import com.beyond.popscience.utils.sun.util.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendsActivity extends BaseActivity {


    @BindView(R.id.ed_search)
    TextView edSearch;
    @BindView(R.id.my_count)
    TextView myCount;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    private List<ApplyFriendList.DataBean> list = new ArrayList<>();
    private CommonAdapter<ApplyFriendList.DataBean> adapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_friends;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("新的朋友");
        CreatLayoutUtils.creatLinearLayout(this, rlv);
        initListView();
        getDate();

        myCount.setText("我的账号："+SPUtils.get(AddFriendsActivity.this,"Mobile",""));
    }

    private void getDate() {
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        HashMap<String, String> map = new HashMap();
        map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());
        map.put("type", "2");
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/queryAddFriendList", map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                ApplyFriendList applyFriends = JSON.parseObject(responseStr, ApplyFriendList.class);
                if (applyFriends.getCode() == 0) {
                    list.clear();
                    list.addAll(applyFriends.getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });
    }

    private void initListView() {
        adapter = new CommonAdapter<ApplyFriendList.DataBean>(this, R.layout.item_apply_list, list) {

            @Override
            protected void convert(ViewHolder holder, ApplyFriendList.DataBean applyFriendList, final int position) {
                int type = list.get(position).getStatus();
                if (type == 1) {
                    holder.setVisible(R.id.ll, true);
                    holder.setVisible(R.id.state, false);
                } else {
                    holder.setVisible(R.id.ll, false);
                    holder.setVisible(R.id.state, true);
                    if (type == 2) {
                        holder.setText(R.id.state, "已拒绝");
                    } else {
                        holder.setText(R.id.state, "已同意");
                    }
                }
                holder.setText(R.id.name, list.get(position).getNickName() + "")
                        .setText(R.id.verify, list.get(position).getMessage())
                        .setOnClickListener(R.id.tv_reject, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                verifyData(list.get(position).getId() + "", "2");
                            }
                        }).setOnClickListener(R.id.tv_agree, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        verifyData(list.get(position).getId() + "", "3");
                    }
                });
                Glide.with(AddFriendsActivity.this).load(list.get(position).getHeadImg()).into((ImageView) holder.getView(R.id.img));
            }
        };
        rlv.setAdapter(adapter);
    }

    private void verifyData(String s, String s1) {
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        HashMap<String, String> map = new HashMap();
        map.put("status", s1);
        map.put("id", s);
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/verificationFriend", map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                BaseResponse base = JSON.parseObject(responseStr, BaseResponse.class);
                if (base.getCode() == 0) {
                    Toast.makeText(AddFriendsActivity.this, base.getMessage(), Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new ShuaXin());
                    getDate();
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ed_search)
    public void onViewClicked() {
        //查找好友
        Intent intent = new Intent(AddFriendsActivity.this, SearchFriendsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.go_back)
    public void onViewClickeds() {
        finish();
    }
}
