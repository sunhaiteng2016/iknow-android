package com.beyond.popscience.module.home.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.locationgoods.bean.MessageCount;
import com.beyond.popscience.locationgoods.bean.MessageCountThree;
import com.beyond.popscience.locationgoods.bean.MessageCountTwo;
import com.beyond.popscience.locationgoods.bean.MessageShuaxin;
import com.beyond.popscience.locationgoods.bean.MessageShuaxinTwo;
import com.beyond.popscience.locationgoods.bean.MessageShuaxinTwot;
import com.beyond.popscience.locationgoods.bean.sel;
import com.beyond.popscience.locationgoods.http.NotificationRestUsage;
import com.beyond.popscience.module.home.adapter.ContentPagerAdapter;
import com.beyond.popscience.utils.sun.util.ScreenUtils;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MessagessFragment extends BaseFragment {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.number1)
    TextView number1;
    @BindView(R.id.number2)
    TextView number2;
    @BindView(R.id.number3)
    TextView number3;
    @BindView(R.id.rl_tb)
    RelativeLayout rlTb;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    Unbinder unbinder;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    @Request
    NotificationRestUsage notificationRestUsage;
    private MessageFragment mMessageFragment;
    private NotificationFragment notificationFragment;
    private InformFragment informFragment;
    public static int flag =0;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_messages;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("消息");
        goBack.setVisibility(View.GONE);
        initTab();
        initCounts();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getPushSize();
        int count = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (count > 0) {
            number1.setText(count + "");
            number1.setVisibility(View.VISIBLE);
        } else {
            number1.setVisibility(View.GONE);
        }
    }

    private void getPushSize() {
        notificationRestUsage.pushSize(1008612, 1);
        notificationRestUsage.pushSize(1008613, 2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX1(MessageCount messageCount) {
        if (messageCount.count > 0) {
            number1.setText(messageCount.count + "");
            number1.setVisibility(View.VISIBLE);
        } else {
            number1.setVisibility(View.GONE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX2(MessageShuaxinTwo messageCount) {
        refreshTwo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX2(MessageShuaxinTwot messageCount) {
        getPushSize();
    }

    private void refreshTwo() {
        if (flag==1){
            notificationFragment.XXX();
        }
        if (flag==2){
            informFragment.XXXX();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(MessageCountTwo messageCount) {
        if (messageCount.count > 0) {
            number2.setText(messageCount.count + "");
            number2.setVisibility(View.VISIBLE);
        } else {
            number2.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(MessageCountThree messageCount) {
        if (messageCount.count > 0) {
            number3.setText(messageCount.count + "");
            number3.setVisibility(View.VISIBLE);
        }else {
            number3.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX3(sel messageCount) {
        if (messageCount.flag==1){
            tlTab.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tlTab.getTabAt(1).select();
                }
            }, 100);
            vpContent.setCurrentItem(1);
        }
        if (messageCount.flag==2){
            tlTab.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tlTab.getTabAt(2).select();
                }
            }, 100);
            vpContent.setCurrentItem(2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(MessageShuaxin messageCount) {
        refresh();
    }


    private void initCounts() {
        tabIndicators = new ArrayList<>();
        tabIndicators.add("聊天");
        tabIndicators.add("系统消息");
        tabIndicators.add("乡镇社团");
        tabFragments = new ArrayList<>();
        mMessageFragment = new MessageFragment();
        tabFragments.add(mMessageFragment);
        notificationFragment = new NotificationFragment();
        informFragment = new InformFragment();
        tabFragments.add(notificationFragment);
        tabFragments.add(informFragment);
        contentAdapter = new ContentPagerAdapter(getActivity().getSupportFragmentManager(), tabFragments, tabIndicators);
        vpContent.setAdapter(contentAdapter);
    }

    private void initTab() {
        tlTab.setTabMode(TabLayout.FOCUSABLES_TOUCH_MODE);
        tlTab.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.text102), ContextCompat.getColor(getActivity(), R.color.blue));
        tlTab.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.blue));
        //ViewCompat.setElevation(tlTab, 10);
        tlTab.setupWithViewPager(vpContent);

       /* //获取屏幕的宽度
        int screenWidth = ScreenUtils.heightPixels(getActivity());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(number1.getLayoutParams());
        lp.setMargins(screenWidth / 4 , 10, 0, 0);
        number1.setLayoutParams(lp);*/
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

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        getActivity().finish();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008612:
                if (msg.getIsSuccess()) {
                    String sizess = (String) msg.getObj();
                    if (sizess.equals("0")) {
                        number2.setVisibility(View.GONE);
                    } else {
                        number2.setText(sizess);
                        number2.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case 1008613:
                if (msg.getIsSuccess()) {
                    String sizess = (String) msg.getObj();
                    if (sizess.equals("0")) {
                        number3.setVisibility(View.GONE);
                    } else {
                        number3.setText(sizess);
                        number3.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    public void refresh() {
        mMessageFragment.refresh();
    }
}
