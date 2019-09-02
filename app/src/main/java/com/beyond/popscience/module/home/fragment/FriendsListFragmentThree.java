package com.beyond.popscience.module.home.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.SearchTwoActivity;
import com.beyond.popscience.module.home.AddFriendsActivity;
import com.beyond.popscience.module.home.AddressListActivity;
import com.beyond.popscience.module.home.GroupChatListActivity;
import com.beyond.popscience.module.home.LaunchGroupChatActivity;
import com.beyond.popscience.module.home.adapter.ContactAdapter;
import com.beyond.popscience.module.home.entity.FriendsBeans;
import com.beyond.popscience.module.home.entity.ShuaXin;
import com.beyond.popscience.module.home.fragment.view.AlphabetAdp;
import com.beyond.popscience.module.home.fragment.view.ClearEditText;
import com.beyond.popscience.module.home.fragment.view.ContactBean;
import com.beyond.popscience.module.home.fragment.view.PinYinStyle;
import com.beyond.popscience.module.home.fragment.view.PinYinUtil;
import com.beyond.popscience.module.home.fragment.view.SideLetterBar;
import com.beyond.popscience.module.home.fragment.view.SwipeManager;
import com.beyond.popscience.utils.sun.util.AnimUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 好友列表
 */

public class FriendsListFragmentThree extends BaseFragment {
    @BindView(R.id.tv_add_number)
    TextView tvAddNumber;
    @BindView(R.id.new_friends)
    LinearLayout newFriends;
    @BindView(R.id.group_chat)
    LinearLayout groupChat;
    @BindView(R.id.ll_location_address_list)
    LinearLayout llLocationAddressList;
    @BindView(R.id.lv_contact)
    ListView lvContact;
    @BindView(R.id.tv_alphabet)
    TextView tvAlphabet;
    @BindView(R.id.et_clear)
    ClearEditText etClear;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.lv_alphabet)
    ListView lvAlphabet;
    @BindView(R.id.rel_notice)
    RelativeLayout relNotice;
    @BindView(R.id.sideLetterBar)
    SideLetterBar sideLetterBar;
    @BindView(R.id.iv_pop)
    ImageView iv_pop;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    Unbinder unbinder;
    @BindView(R.id.ll_sarech)
    LinearLayout llSarech;
    private ArrayList<ContactBean> contactList = new ArrayList<>();
    private List<String> alphabetList;
    private ContactAdapter adapter;
    private List<FriendsBeans.DataBean> mDatas;
    private String titles, newsId, pics;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_friend_two;
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        if (getArguments() != null) {
            titles = getArguments().getString("titles");
            newsId = getArguments().getString("link");
            pics = getArguments().getString("pics");
        }

        initView();
        initEvent();
        initData();
        getDatas();
        showPop();
        //数据刷新
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(ShuaXin event) {
        getDatas();
    }


    private void getDatas() {
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", UserInfoUtil.getInstance().getUserInfo().getUserId());
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/queryMyFriend", map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                FriendsBeans FriendsBeans = JSON.parseObject(responseStr, FriendsBeans.class);
                if (FriendsBeans.getCode() == 0) {
                    mDatas = FriendsBeans.getData();
                    ArrayList<ContactBean> totalData = dataList();
                    contactList.clear();
                    contactList.addAll(totalData);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });
    }

    private void initData() {
        //3.设置Adapter
        adapter = new ContactAdapter(getActivity(), contactList, true);
        lvContact.setAdapter(adapter);
        alphabetList = new ArrayList<>();
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                boolean checks = contactList.get(i).isCheck();
                if (checks) {
                    contactList.get(i).setCheck(false);
                } else {
                    contactList.get(i).setCheck(true);
                }
                adapter.notifyDataSetChanged();

            }
        });
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

                AlphabetAdp alphabetAdp = new AlphabetAdp(getActivity(), alphabetList);
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
        // 虚拟数据
        if (TextUtils.isEmpty(str)) {
            sideLetterBar.setVisibility(View.VISIBLE);
            filterDateList = dataList();
        } else {
            filterDateList.clear();
            sideLetterBar.setVisibility(View.GONE);
            for (ContactBean contactBean : dataList()) {
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
        adapter = new ContactAdapter(getActivity(), filterDateList, true);
        lvContact.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //测试=========================================================================================================================================
    private ArrayList<ContactBean> dataList() {
        ArrayList<ContactBean> mSortList = new ArrayList<>();
        if (null != mDatas) {
            for (FriendsBeans.DataBean beans : mDatas) {
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
        }
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

    @OnClick({R.id.new_friends, R.id.iv_pop, R.id.group_chat, R.id.ll_location_address_list,R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_friends:
                Intent intents = new Intent(getActivity(), AddFriendsActivity.class);
                startActivity(intents);
                break;
            case R.id.group_chat:
                Intent intent = new Intent(getActivity(), GroupChatListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_location_address_list:
                Intent intent1 = new Intent(getActivity(), AddressListActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_pop:
                mPopupWindow.showAsDropDown(iv_pop, -100, 0);
                toggleBright();
                break;
            case R.id.tv_submit:
                for (ContactBean bean : contactList) {
                    boolean check = bean.isCheck();
                    if (check) {
                        EMMessage message = EMMessage.createTxtSendMessage("[新闻分享]", bean.getUserid() + "");
                        //增加自己的属性
                        message.setAttribute("associationCards", true);
                        message.setAttribute("userHead", UserInfoUtil.getInstance().getUserInfo().getAvatar());
                        message.setAttribute("userName", UserInfoUtil.getInstance().getUserInfo().getNickName());
                        message.setAttribute("type", "NewsShare");
                        message.setAttribute("newsUrl", newsId);
                        message.setAttribute("title", titles);
                        message.setAttribute("content", "科普中国户户通");
                        message.setAttribute("image", pics);
                        EMClient.getInstance().chatManager().sendMessage(message);
                    }
                }
                com.beyond.library.util.ToastUtil.showCenter(getActivity(),"转发成功");
                getActivity().finish();
                break;
        }
    }

    private PopupWindow mPopupWindow;
    private AnimUtil animUtil;
    private float bgAlpha = 1f;
    private boolean bright = false;

    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;

    //底部弹窗
    public void showPop() {
        mPopupWindow = new PopupWindow(getActivity());
        animUtil = new AnimUtil();
        // 设置布局文件
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_add_friends, null);
        mPopupWindow.setContentView(mView);
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        mPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量

        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });
        LinearLayout addNewFriends = mView.findViewById(R.id.add_new_friends);
        LinearLayout groupChatPop = mView.findViewById(R.id.group_chat_pop);
        addNewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
                startActivity(intent);
            }
        });
        groupChatPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                if (contactList.size() > 0) {
                    Intent intent = new Intent(getActivity(), LaunchGroupChatActivity.class);
                    intent.putExtra("mData", contactList);
                    intent.putExtra("type", "crate");
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "请先添加好友", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }

    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @OnClick(R.id.rlss)
    public void onViewClicked() {
        getActivity().finish();
    }

    @OnClick(R.id.ll_sarech)
    public void onViewClickedsssss() {
        //搜索好友
        Intent intent = new Intent(getActivity(), SearchTwoActivity.class);
        intent.putExtra("mData", contactList);
        startActivity(intent);
    }
}
