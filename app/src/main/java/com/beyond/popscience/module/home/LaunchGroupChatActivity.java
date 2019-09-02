package com.beyond.popscience.module.home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.beyond.popscience.module.home.adapter.ContactAdapter;
import com.beyond.popscience.module.home.entity.Groups;
import com.beyond.popscience.module.home.fragment.Constant;
import com.beyond.popscience.module.home.fragment.view.AlphabetAdp;
import com.beyond.popscience.module.home.fragment.view.ClearEditText;
import com.beyond.popscience.module.home.fragment.view.ContactBean;
import com.beyond.popscience.module.home.fragment.view.PinYinUtil;
import com.beyond.popscience.module.home.fragment.view.SideLetterBar;
import com.beyond.popscience.module.home.fragment.view.SwipeManager;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LaunchGroupChatActivity extends BaseActivity {


    @BindView(R.id.releases)
    TextView releases;
    @BindView(R.id.et_clear)
    ClearEditText etClear;
    @BindView(R.id.rl1)
    LinearLayout rl1;
    @BindView(R.id.lv_contact)
    ListView lvContact;
    @BindView(R.id.tv_alphabet)
    TextView tvAlphabet;
    @BindView(R.id.sideLetterBar)
    SideLetterBar sideLetterBar;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.lv_alphabet)
    ListView lvAlphabet;
    @BindView(R.id.rel_notice)
    RelativeLayout relNotice;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.go_back)
    RelativeLayout goBack;
    private ArrayList<ContactBean> mData;
    private ArrayList<ContactBean> contactList = new ArrayList<>();
    private ContactAdapter adapter;
    private ArrayList<String> alphabetList;
    private String mType;
    private String groupId;
    private Loading_view loadingDialog;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_launch_group_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("群组管理");
        mData = (ArrayList<ContactBean>) getIntent().getSerializableExtra("mData");
        mType = getIntent().getStringExtra("type");  //reduce  add  crate
        groupId = getIntent().getStringExtra("group_id");
        //所有的都默认选择
        for (ContactBean bean : mData) {
            bean.setCheck(false);
        }
        contactList.clear();
        contactList.addAll(mData);
        initView();
        initEvent();
        initData();
        loadingDialog = new Loading_view(this, R.style.CustomDialog);
    }

    private void initView() {
        relNotice.post(new Runnable() {
            @Override
            public void run() {
                relNotice.getHeight();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relNotice.getLayoutParams();
                params.height = tvNotice.getHeight() * 5;
                params.width = tvNotice.getWidth();
                relNotice.setLayoutParams(params);
            }
        });
    }

    private void initData() {

        //3.设置Adapter
        adapter = new ContactAdapter(this, contactList, true);
        lvContact.setAdapter(adapter);
        alphabetList = new ArrayList<>();

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean checks = mData.get(i).isCheck();
                if (mType.equals("reduce")) {
                    //不能点击自己
                    int userId = mData.get(i).getUserid();
                    int mUserId = Integer.parseInt(UserInfoUtil.getInstance().getUserInfo().getUserId());
                    if (userId == mUserId) {
                        Toast.makeText(LaunchGroupChatActivity.this, "不能移除自己", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (checks) {
                    mData.get(i).setCheck(false);
                } else {
                    mData.get(i).setCheck(true);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void initEvent() {
        sideLetterBar.setOnTouchLetterListener(new SideLetterBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                alphabetList.clear();
                ViewPropertyAnimator.animate(relNotice).alpha(1f).setDuration(0).start();
                //根据当前触摸的字母，去集合中找那个item的首字母和letter一样，然后将对应的item放到屏幕顶端
                for (int i = 0; i < contactList.size(); i++) {
                    String firstAlphabet = contactList.get(i).getPinyin().charAt(0) + "";
                    if (letter.equals(firstAlphabet)) {
                        lvContact.setSelection(i);
                        relNotice.setVisibility(View.VISIBLE);
                        break;
                    }
                    if (letter.equals("#")) {
                        lvContact.setSelection(0);
                        relNotice.setVisibility(View.GONE);
                    }
                }
                for (int i = 0; i < contactList.size(); i++) {
                    String firstAlphabet = contactList.get(i).getPinyin().toString().trim().charAt(0) + "";

                    if (letter.equals(firstAlphabet)) {
                        //说明找到了，那么应该讲当前的item放到屏幕顶端
                        tvNotice.setText(letter);
                        if (!alphabetList.contains(String.valueOf(contactList.get(i).getName().trim().charAt(0)))) {
                            alphabetList.add(String.valueOf(contactList.get(i).getName().trim().charAt(0)));
                        }
                    }

                }
                showCurrentWord(letter);
                //显示当前触摸的字母

                AlphabetAdp alphabetAdp = new AlphabetAdp(LaunchGroupChatActivity.this, alphabetList);
                lvAlphabet.setAdapter(alphabetAdp);
                alphabetAdp.notifyDataSetChanged();
            }
        });
        lvContact.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果垂直滑动，则需要关闭已经打开的layout
                    SwipeManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int pos = lvContact.getFirstVisiblePosition();
                if (contactList.size() > 0) {
                    tvAlphabet.setVisibility(View.VISIBLE);
                    String text = contactList.get(pos).getPinyin().charAt(0) + "";
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m1 = p.matcher(text);
                    if (m1.matches()) {
                        tvAlphabet.setText("#");
                    } else {
                        tvAlphabet.setText(text);
                    }
                } else {
                    tvAlphabet.setVisibility(View.GONE);
                }
            }
        });
        etClear.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                fuzzySearch(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lvAlphabet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String alphabet = alphabetList.get(position).trim();
                setIsVisiable();
                for (int i = 0; i < contactList.size(); i++) {
                    if (alphabet.equals(String.valueOf(contactList.get(i).getName().trim().charAt(0)))) {
                        int pos = i % lvAlphabet.getChildCount();
                        int childCount = lvContact.getChildCount();
                        if (position == 0 && pos - position == 1 || childCount - pos == 1) {
                            lvContact.setSelection(i);
                        } else {
                            lvContact.setSelection(i - 1);
                        }
                        break;
                    }
                }
            }
        });
    }

    protected void showCurrentWord(String letter) {
        tvNotice.setText(letter);
        setIsVisiable();
    }

    private Handler handler = new Handler();

    private void setIsVisiable() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimator.animate(relNotice).alpha(0f).setDuration(1000).start();
            }
        }, 4000);
    }

    private void fuzzySearch(String str) {
        ArrayList<ContactBean> filterDateList = new ArrayList<ContactBean>();
        if (TextUtils.isEmpty(str)) {
            sideLetterBar.setVisibility(View.VISIBLE);
            filterDateList = mData;
        } else {
            filterDateList.clear();
            sideLetterBar.setVisibility(View.GONE);
            for (ContactBean contactBean : mData) {
                String name = contactBean.getName();
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = p.matcher(str);
                if (m.matches()) {
                    str = PinYinUtil.getPinyin(str);
                }
                if (PinYinUtil.getPinyin(name).contains(str.toUpperCase()) || contactBean.pinYinStyle.briefnessSpell.toUpperCase().contains(str.toUpperCase())
                        || contactBean.pinYinStyle.completeSpell.toUpperCase().contains(str.toUpperCase())) {
                    filterDateList.add(contactBean);
                }
            }
        }
        contactList = filterDateList;
        adapter = new ContactAdapter(LaunchGroupChatActivity.this, filterDateList, true);
        lvContact.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.releases})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.releases:
                loadingDialog.show();
                //遍历 选中集合
                StringBuffer stringBuffer = new StringBuffer();
                for (ContactBean bean : mData) {
                    boolean check = bean.isCheck();
                    if (check) {
                        stringBuffer.append(bean.getUserid() + ",");
                    }
                }
                if (mType.equals("reduce")) {
                    //减
                    stringBuffer.toString().replace(UserInfoUtil.getInstance().getUserInfo().getUserId() + ",", "");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("groupId", groupId);
                    map.put("Userlist", stringBuffer.toString());
                    AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
                    appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/laoutGroup", map, new NewCustomResponseHandler() {
                        @Override
                        public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                            super.onSuccess(httpStatusCode, headerMap, responseStr);
                            loadingDialog.dismiss();
                            BaseResponse base = JSON.parseObject(responseStr, BaseResponse.class);
                            LoadingBar.cancel(lvContact);
                            if (base.getCode() == 0) {
                                ToastUtil.show(LaunchGroupChatActivity.this, "删除成功");
                                finish();
                            } else {
                                ToastUtil.show(LaunchGroupChatActivity.this, "删除失败，请重试！");
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headerMap, responseString, throwable);
                            loadingDialog.dismiss();
                        }
                    });
                }
                if (mType.equals("add")) {
                    //加
                    HashMap<String, String> map = new HashMap<>();
                    map.put("groupId", groupId);
                    map.put("Userlist", stringBuffer.toString());
                    AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
                    appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/joinMeGroup", map, new NewCustomResponseHandler() {
                        @Override
                        public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                            super.onSuccess(httpStatusCode, headerMap, responseStr);
                            loadingDialog.dismiss();
                            BaseResponse base = JSON.parseObject(responseStr, BaseResponse.class);
                            LoadingBar.cancel(lvContact);
                            if (base.getCode() == 0) {
                                ToastUtil.show(LaunchGroupChatActivity.this, "添加成功");
                                finish();
                            } else {
                                ToastUtil.show(LaunchGroupChatActivity.this, "加入失败，请重试！");
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headerMap, responseString, throwable);
                            loadingDialog.dismiss();
                        }
                    });
                }
                if (mType.equals("crate")) {

                    AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
                    HashMap<String, String> map = new HashMap();
                    map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId() + "");
                    map.put("Userlist", stringBuffer.toString());
                    appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/createGroup", map, new NewCustomResponseHandler() {
                        @Override
                        public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                            super.onSuccess(httpStatusCode, headerMap, responseStr);
                            Groups baseRespones = JSON.parseObject(responseStr, Groups.class);
                            loadingDialog.dismiss();
                            LoadingBar.cancel(lvContact);
                            if (baseRespones.getCode() == 0) {
                                ToastUtil.show(LaunchGroupChatActivity.this, "创建成功！");
                                //开启群聊
                                Intent intent = new Intent(LaunchGroupChatActivity.this, ChatActivity.class);
                                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                                intent.putExtra("name", UserInfoUtil.getInstance().getUserInfo().getNickName()+"的群组");
                                // it's single chat
                                intent.putExtra(Constant.EXTRA_USER_ID, baseRespones.getData().getGroupid());
                                startActivity(intent);
                            } else {
                                ToastUtil.show(LaunchGroupChatActivity.this, "创建失败，请重试！");
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headerMap, responseString, throwable);
                            loadingDialog.dismiss();
                        }
                    });
                }
                break;
        }
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }
}
