package com.beyond.popscience.module.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.mdialog.Loading_view;
import com.beyond.popscience.module.home.entity.FriendsBean;
import com.beyond.popscience.module.home.entity.GroupDa;
import com.beyond.popscience.module.home.entity.GroupUserInfos;
import com.beyond.popscience.module.home.fragment.view.ContactBean;
import com.beyond.popscience.module.home.fragment.view.PinYinStyle;
import com.beyond.popscience.module.home.fragment.view.PinYinUtil;
import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupManagertActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.tv_disturb)
    TextView tvDisturb;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.set_group_name)
    LinearLayout setGroupName;
    private String groupId;
    private List<GroupUserInfos.DataBean> list = new ArrayList<>();
    private CommonAdapter<GroupUserInfos.DataBean> adapter;
    private List<GroupUserInfos.DataBean> mData;
    private int isMe;
    private boolean mIsTop = false, mIsDisturb = false;
    private Loading_view loadingDialog;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_group_management;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("群组管理");
        submit.setText("解散群组");
        groupId = getIntent().getStringExtra("group_id");
        initView();
        getGroupData();

    }

    private void getGroupData() {
        loadingDialog = new Loading_view(this, R.style.CustomDialog);
        loadingDialog.show();
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        HashMap<String, String> map = new HashMap<>();
        map.put("groupId", groupId + "");
        map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/queryGroupDetail", map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                GroupDa groupda = JSON.parseObject(responseStr, GroupDa.class);
                loadingDialog.dismiss();
                if (groupda.getCode() == 0) {
                    if (null != groupda.getData()) {
                        isMe = (int) groupda.getData().getIsMe(); //1是 2不是
                        if (isMe == 1) {
                            creatData(isMe);
                        } else {
                            submit.setText("退出群组");
                        }
                        int isTop = groupda.getData().getZhiding();
                        if (isTop == 1) {
                            //置顶
                            tvTop.setBackgroundResource(R.drawable.kg);
                            mIsTop = true;
                        } else {
                            tvTop.setBackgroundResource(R.drawable.kg1);
                            mIsTop = false;
                        }
                        int isDisturb = groupda.getData().getDarao();
                        if (isDisturb == 1) {
                            tvDisturb.setBackgroundResource(R.drawable.kg);
                            mIsDisturb = true;
                        } else {
                            tvDisturb.setBackgroundResource(R.drawable.kg1);
                            mIsDisturb = false;
                        }
                    }
                } else {
                    ToastUtil.show(GroupManagertActivity.this, groupda.getMessage());
                }
                getDatas();
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
                loadingDialog.dismiss();
            }
        });
    }

    private void getDatas() {

        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        HashMap<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/queryGroupUser", map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                GroupUserInfos groupuserinfo = JSON.parseObject(responseStr, GroupUserInfos.class);
                if (groupuserinfo.getCode() == 0) {
                    mData = groupuserinfo.getData();
                    list.clear();
                    list.addAll(groupuserinfo.getData());
                    creatData(isMe);
                    adapter.notifyDataSetChanged();
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

    private void initView() {
        //会默认加两个数据源
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, LinearLayoutManager.VERTICAL, false);
        rlv.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<GroupUserInfos.DataBean>(this, R.layout.item_img, list) {

            @Override
            protected void convert(ViewHolder holder, GroupUserInfos.DataBean groupUserInfo, int position) {
                int flag = groupUserInfo.getFlag();
                if (flag == 0) {
                    holder.setText(R.id.name, groupUserInfo.getNickname());
                    Glide.with(GroupManagertActivity.this).load(groupUserInfo.getAvatar()).into((ImageView) holder.getView(R.id.img));
                }
                if (flag == 1) {
                    Glide.with(GroupManagertActivity.this).load(R.drawable.jia).into((ImageView) holder.getView(R.id.img));
                }
                if (flag == 2) {
                    Glide.with(GroupManagertActivity.this).load(R.drawable.jian111).into((ImageView) holder.getView(R.id.img));
                }
            }
        };
        rlv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                int flag = list.get(position).getFlag();
                if (flag == 0) {
                    Intent intent = new Intent(GroupManagertActivity.this, UserDetailsActivity.class);
                    intent.putExtra("userId", list.get(position).getUserid());
                    startActivity(intent);
                }
                if (flag == 1) {
                    getFriendsDataList();
                }
                if (flag == 2) {
                    //减成员  是传递 群组成员
                    ArrayList<ContactBean> mlist = dataList();
                    Intent intent = new Intent(GroupManagertActivity.this, LaunchGroupChatActivity.class);
                    intent.putExtra("type", "reduce");
                    intent.putExtra("group_id", groupId);
                    intent.putExtra("mData", mlist);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private ArrayList<ContactBean> dataList() {
        ArrayList<ContactBean> mSortList = new ArrayList<>();
        for (GroupUserInfos.DataBean beans : mData) {
            ContactBean bean = new ContactBean(beans.getNickname());
            bean.setAddress(beans.getAddress());
            bean.setMobile(beans.getMobile());
            bean.setName(beans.getNickname());
            bean.setNickname(beans.getNickname());
            bean.setUserid(beans.getUserid());
            bean.pinYinStyle = parsePinYinStyle(beans.getNickname());
            mSortList.add(bean);
        }
        Collections.sort(mSortList);
        return mSortList;
    }

    public PinYinStyle parsePinYinStyle(String content) {
        PinYinStyle pinYinStyle = new PinYinStyle();
        if (content != null && content.length() > 0) {
            //其中包含的中文字符
            String[] enStrs = new String[content.length()];
            for (int i = 0; i < content.length(); i++) {
                enStrs[i] = PinYinUtil.getPinyin(String.valueOf(content.charAt(i)));
            }
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    //拼接简拼
                    pinYinStyle.briefnessSpell += enStrs[i].charAt(0);
                }
            }
        }
        return pinYinStyle;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getDatas();
    }

    /**
     * 加减数据
     */
    private void creatData(int isMe) {
        GroupUserInfos.DataBean info = new GroupUserInfos.DataBean();
        info.setAddress("");
        info.setMobile("");
        info.setNickname("");
        info.setUserid(0);
        info.setFlag(1);
        list.add(info);
        if (isMe == 1) {
            GroupUserInfos.DataBean info2 = new GroupUserInfos.DataBean();
            info2.setAddress("");
            info2.setMobile("");
            info2.setNickname("");
            info2.setUserid(0);
            info2.setFlag(2);
            list.add(info2);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取好友列表
     */
    private void getFriendsDataList() {
        loadingDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());

        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/queryMyFriend", map, new NewCustomResponseHandler() {
            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
                loadingDialog.dismiss();
            }

            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                loadingDialog.dismiss();
                FriendsBean friends = JSON.parseObject(responseStr, FriendsBean.class);
                if (friends.getCode() == 0) {
                    List<FriendsBean.DataBean> mDatas = friends.getData();
                    ArrayList<ContactBean> mSortList = new ArrayList<>();
                    for (FriendsBean.DataBean beans : mDatas) {
                        ContactBean bean = new ContactBean(beans.getNickname());
                        bean.setAddress(beans.getAddress());
                        bean.setHeadImg(beans.getAvatar());
                        bean.setMobile(beans.getMobile());
                        bean.setName(beans.getNickname());
                        bean.setNickname(beans.getNickname());
                        bean.setUserid(beans.getUserid());
                        bean.pinYinStyle = parsePinYinStyle(beans.getNickname());
                        mSortList.add(bean);
                    }
                    Collections.sort(mSortList);
                    //成功
                    Intent intent = new Intent(GroupManagertActivity.this, LaunchGroupChatActivity.class);
                    intent.putExtra("type", "add");
                    intent.putExtra("group_id", groupId);
                    intent.putExtra("mData", mSortList);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.go_back, R.id.save, R.id.submit, R.id.tv_top, R.id.tv_disturb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_top:
                if (mIsTop) {
                    tvTop.setBackgroundResource(R.drawable.kg1);
                    mIsTop = false;
                } else {
                    tvTop.setBackgroundResource(R.drawable.kg);
                    mIsTop = true;
                }
                break;
            case R.id.tv_disturb:
                if (mIsDisturb) {
                    tvDisturb.setBackgroundResource(R.drawable.kg1);
                    mIsDisturb = false;
                } else {
                    mIsDisturb = true;
                    tvDisturb.setBackgroundResource(R.drawable.kg);
                }
                break;
            case R.id.go_back:
                finish();
                break;
            case R.id.save:
                //置顶  打扰信息
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("groupId", groupId);
                map1.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());
                if (mIsTop) {
                    map1.put("zhiding", "1");
                } else {
                    map1.put("zhiding", "2");
                }
                if (mIsDisturb) {
                    map1.put("darao", "1");
                } else {
                    map1.put("darao", "2");
                }
                loadingDialog.show();
                AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
                appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/updateGroupDetail", map1, new NewCustomResponseHandler() {
                    @Override
                    public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                        super.onSuccess(httpStatusCode, headerMap, responseStr);
                        BaseResponse base = JSON.parseObject(responseStr, BaseResponse.class);
                        loadingDialog.dismiss();
                        if (base.getCode() == 0) {
                            ToastUtil.show(GroupManagertActivity.this, "修改成功！");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headerMap, responseString, throwable);
                        loadingDialog.dismiss();
                    }
                });
                break;
            case R.id.submit:
                if (isMe == 1) {
                    loadingDialog.show();
                    //解散群组
                    HashMap<String, String> map = new HashMap<>();
                    map.put("groupId", groupId);
                    AppBaseRestUsageV1 appBaseRestUsageV11 = new AppBaseRestUsageV1();
                    appBaseRestUsageV11.post(BeyondApplication.BaseUrl + "/im/deleteGroup", map, new NewCustomResponseHandler() {
                        @Override
                        public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                            super.onSuccess(httpStatusCode, headerMap, responseStr);
                            BaseResponse base = JSON.parseObject(responseStr, BaseResponse.class);
                            loadingDialog.dismiss();
                            if (base.getCode() == 0) {
                                ToastUtil.show(GroupManagertActivity.this, "解散成功！");
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headerMap, responseString, throwable);
                            loadingDialog.dismiss();
                        }
                    });
                } else {
                    quitGroup();
                }
                break;
        }
    }

    private void quitGroup() {
        loadingDialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("Userlist", UserInfoUtil.getInstance().getUserInfo().getUserId() + "");
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/laoutGroup", map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                loadingDialog.dismiss();
                BaseResponse base = JSON.parseObject(responseStr, BaseResponse.class);
                if (base.getCode() == 0) {
                    ToastUtil.show(GroupManagertActivity.this, "退出成功！");
                    finish();
                } else {
                    ToastUtil.show(GroupManagertActivity.this, base.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
                loadingDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.set_group_name)
    public void onViewClickedss() {
            Intent  intent = new Intent(GroupManagertActivity.this,SetGroupNameActivity.class);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
    }
}
