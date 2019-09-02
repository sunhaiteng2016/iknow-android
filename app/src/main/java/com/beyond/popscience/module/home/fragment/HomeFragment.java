package com.beyond.popscience.module.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.HuaWeiActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.base.CustomFragmentPagerAdapter;
import com.beyond.popscience.frame.net.NewsRestUsage;
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.pojo.point.SignCheckBean;
import com.beyond.popscience.frame.pojo.point.SignCheckBeanss;
import com.beyond.popscience.frame.thirdsdk.gtpush.GeTuiPushSDK;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.bean.MainLocation;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.entity.HomeMessage;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.beyond.popscience.utils.SystemUtil;
import com.beyond.popscience.view.ShowSignSuccessView;
import com.beyond.popscience.widget.MenuManagerPop;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;


/**
 * 首页 新闻列表
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, MenuManagerPop.IManagerLitener {

    private final String KEY_SUB_NAV = "subscribe_nav";

    private static final int TASK_GET_NAVS = 1501;

    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout mLlPopTitle;
    private TextView mTvOrderDelete;
    private TextView mTvComplete;
    private ImageView mIvTop;

    private MenuManagerPop mMenuManagerPop;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<NavObj> navs = new ArrayList<>();
    private List<NavObj> navSub = new ArrayList<>();//订阅
    private List<NavObj> navUnSub = new ArrayList<>();//为订阅
    private int canDragIndex = 0;
    private LinearLayout dingwei;
    private MyPagerAdapter mAdapter;

    private int position = 0;

    @Request
    private NewsRestUsage mRestUsage;
    @Request
    private PointRestUsage pointRestUsage;
    private TextView dingwei2;
    private LinearLayout topTab;
    private LinearLayout llqiandao;
    private TextView tvSignTv;
    private LinearLayout llSignll;
    private List<SignCheckBean> signCheckResultBeanList;
    private TextView lxSign;
    @BindView(R.id.oneDay)
    TextView oneDay;
    @BindView(R.id.twoDay)
    TextView twoDay;
    @BindView(R.id.threeDay)
    TextView threeDay;
    @BindView(R.id.fourDay)
    TextView fourDay;
    @BindView(R.id.fiveDay)
    TextView fiveDay;
    @BindView(R.id.sixDay)
    TextView sixDay;
    @BindView(R.id.sevenDay)
    TextView sevenDay;
    private LinearLayout llSignback;
    private int keystass;
    private int daysss;

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            for (Fragment fragment : mFragments) {
                if (fragment.isAdded()) {
                    fragment.onHiddenChanged(hidden);
                }
            }
        } else {
            try {
                int currTabIndex = mTabLayout.getCurrentTab();
                if (currTabIndex >= 0 && currTabIndex < mFragments.size()) {
                    Fragment fragment = mFragments.get(currTabIndex);
                    if (fragment.isAdded()) {
                        fragment.onHiddenChanged(hidden);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        signViewList = new ArrayList<>();
        mTabLayout = (SlidingTabLayout) mRootView.findViewById(R.id.slidTab);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.vpView);
        llqiandao = (LinearLayout) mRootView.findViewById(R.id.ll_qiandao);
        tvSignTv = (TextView) mRootView.findViewById(R.id.tv_sign_tv);
        mLlPopTitle = (LinearLayout) mRootView.findViewById(R.id.llPopTitle);
        mTvComplete = (TextView) mRootView.findViewById(R.id.tvComplete);
        mTvOrderDelete = (TextView) mRootView.findViewById(R.id.tvOrderDelete);
        mIvTop = (ImageView) mRootView.findViewById(R.id.ivToTop);
        topTab = (LinearLayout) mRootView.findViewById(R.id.top_tab);
        dingwei = (LinearLayout) mRootView.findViewById(R.id.dingwei);
        dingwei.setAlpha(1f);
        dingwei2 = (TextView) mRootView.findViewById(R.id.dingwei2);
        dingwei2.setTextColor(getActivity().getResources().getColor(R.color.blue));

        llSignll = (LinearLayout) mRootView.findViewById(R.id.ll_sign_ll);
        llSignback = (LinearLayout) mRootView.findViewById(R.id.ll_sign_back);
        //签到
        lxSign = (TextView) mRootView.findViewById(R.id.lx_sign);
        mRootView.findViewById(R.id.ivMore).setOnClickListener(this);
        mRootView.findViewById(R.id.ivClose).setOnClickListener(this);
        mTvComplete.setOnClickListener(this);
        mTvOrderDelete.setOnClickListener(this);
        mRootView.findViewById(R.id.ivSearch).setOnClickListener(this);
        userInfo = UserInfoUtil.getInstance().getUserInfo();
        getNavs();
        if (WelcomeActivity.seletedAdress != null) {
            String[] addresss = WelcomeActivity.seletedAdress.split("-");
            if (addresss.length > 0) {
                dingwei2.setText(addresss[1]);
            }
        }
        dingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressPickTask task = new AddressPickTask(getActivity());
                task.setHideProvince(false);
                task.setHideCounty(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                        } else {
                            WelcomeActivity.seletedAdress = city.getAreaName() + "-" + county.getAreaName();
                            WelcomeActivity.seletedAdress_two = province.getAreaName() + "-" + city.getAreaName() + "-" + county.getAreaName();
                            //重新的刷新  列表的數據
                            dingwei2.setText(county.getAreaName());
                            //重新加载viewpager
                            initNav();
                        }
                    }
                });
                task.execute(WelcomeActivity.seletedAdress_two.split("-")[0], WelcomeActivity.seletedAdress_two.split("-")[1], WelcomeActivity.seletedAdress_two.split("-")[2]);
            }
        });
        //注册广播
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //签到
        addAllSignView();
        requestCheckBeforeSign();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(HomeMessage.homeRefresh event) {
        float alpha = calAlpha(Math.abs(event.type), event.headTop);
        if (alpha < 0.2) {
            llqiandao.setVisibility(View.VISIBLE);
            dingwei.setVisibility(View.VISIBLE);
            topTab.setVisibility(View.GONE);
        } else {
            llqiandao.setVisibility(View.GONE);
            dingwei.setVisibility(View.GONE);
            topTab.setVisibility(View.VISIBLE);
        }
        topTab.setAlpha(calAlpha(Math.abs(event.type), event.headTop));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeLocation(MainLocation event) {
        if (WelcomeActivity.seletedAdress != null) {
            String[] addresss = WelcomeActivity.seletedAdress.split("-");
            if (addresss.length > 0) {
                dingwei2.setText(addresss[1]);
            }
        }
    }

    private float calAlpha(int x, int headViewHight) {
        float b = 0;
        float a = 1.0f / 250;
        float y = a * x + b;
        return y;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (taskId == TASK_GET_NAVS && msg.getIsSuccess() && mFragments.isEmpty()) {
            navs = (List<NavObj>) msg.getObj();
            if (navs != null) {
                processNav();
                initNav();
            }
        }
        if (taskId == SIGN_CHECK) {
            if (msg.getIsSuccess()) {
                //这个是检查签到
                SignCheckBeanss base = (SignCheckBeanss) msg.getObj();
                daysss = base.getDay();
                int singSize = base.getSignSize();
                for (int i = 0; i < singSize; i++) {
                    signViewList.get(i).setBackground(getResources().getDrawable(R.drawable.bg_circle_red));
                    signViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                    signViewList.get(i).setClickable(false);
                }
                int isSigin = base.getIsSign();//0未签  1未签
                if (isSigin == 0) {
                    signViewList.get(singSize).setBackground(getResources().getDrawable(R.drawable.bg_circle_blue));
                    signViewList.get(singSize).setTextColor(getResources().getColor(R.color.white));
                    signViewList.get(singSize).setText("签到");
                    signViewList.get(singSize).setTextSize(12);
                    signViewList.get(singSize).setClickable(true);
                } else {
                    signViewList.get(singSize).setBackground(getResources().getDrawable(R.drawable.bg_circle_red));
                    signViewList.get(singSize).setTextColor(getResources().getColor(R.color.white));
                    signViewList.get(singSize).setText("已签到");
                    signViewList.get(singSize).setTextSize(12);
                    signViewList.get(singSize).setClickable(false);
                    tvSignTv.setText("已签到");
                    tvSignTv.setEnabled(false);
                }
                int day = base.getDay() + 1;
                lxSign.setText("你已连续签到" +day  + "天");
                keystass = base.getKeysta();
                //这个地方紧接着签到
                int days = 0;
                if (signCheckBean != null) {
                    if ("7".equals(signCheckBean.getKeysta()))
                        days = Integer.parseInt(signCheckBean.getDays()) + 1;
                    else days = Integer.parseInt(signCheckBean.getDays());
                }

                llSignll.setVisibility(View.VISIBLE);
                requestSign(singSize+1,days);
            }
        }
        if (taskId == SIGN) {
            dismissProgressDialog();
            if (msg.getIsSuccess()) {
                String mmsg = msg.getMsg();
                if (mmsg.equals("1")) {//1:签到成功  0:签到失败
                    ShowSignSuccessView showSignSuccessView = null;

                    if (isduanqian) {
                        ShowSignSuccessView showSignSuccessViews = new ShowSignSuccessView(getActivity(), "+" + 1 + "绿币");
                        displayCurrentDays(1);
                      //  showSignSuccessViews.showPopupWindow();
                        final ShowSignSuccessView finalShowSignSuccessView = showSignSuccessViews;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finalShowSignSuccessView.dismiss();
                            }
                        }, 3000);
                        requestCheckBeforeSign();
                        //本地加上
                        String mscore = (String) SPUtils.get(getActivity(), "score", "");
                        double socree;
                        if (signCheckBean == null) {
                            socree = Double.parseDouble(mscore) + 1.0;
                        } else if (Integer.parseInt(signCheckBean.getDays()) < 7) {
                            socree = Double.parseDouble(mscore) + keys;
                        } else {
                            socree = Double.parseDouble(mscore) + 7;
                        }
                        SPUtils.put(getActivity(), "score", socree + "");
                        return;
                    }

                    if (signCheckBean == null) {
                        if (daysss > 6) {
                            showSignSuccessView = new ShowSignSuccessView(getActivity(), "+" + 7 + "绿币");
                            displayCurrentDays(7);
                        } else {
                            int ssss = daysss + 1;
                            showSignSuccessView = new ShowSignSuccessView(getActivity(), "+" + ssss + "绿币");
                            displayCurrentDays(daysss + 1);
                        }
                    } else if (Integer.parseInt(signCheckBean.getScore()) < 7) {
                        showSignSuccessView = new ShowSignSuccessView(getActivity(), "+" + keys + "绿币");
                        displayCurrentDays(keys);
                    } else {
                        showSignSuccessView = new ShowSignSuccessView(getActivity(), "+" + 7 + "绿币");
                        displayCurrentDays(keys);
                    }
                    //showSignSuccessView.showPopupWindow();
                    final ShowSignSuccessView finalShowSignSuccessView = showSignSuccessView;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finalShowSignSuccessView.dismiss();
                        }
                    }, 3000);
                    String mscore = (String) SPUtils.get(getActivity(), "score", "");
                    double socree;
                    if (signCheckBean == null) {
                        socree = Double.parseDouble(mscore) + 1.0;
                    } else {
                        socree = Double.parseDouble(mscore) + Double.parseDouble(signCheckBean.getScore());
                    }
                    SPUtils.put(getActivity(), "score", socree + "");
                    // requestIndexData();
                    requestCheckBeforeSign();
                } else {
                  //  ToastUtil.show(getActivity(), "今日已签,明天继续!");
                }

               new Thread(){
                   @Override
                   public void run() {
                       super.run();
                       try {
                           sleep(2000);
                           getActivity().runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   llSignll.setVisibility(View.GONE);
                               }
                           });
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }.start();
                mViewPager.setEnabled(true);
            }

        }
    }

    /**
     * 从网络获取标签项
     */
    public void getNavs() {
        mRestUsage.getNavs(TASK_GET_NAVS);
    }

    /**
     * 从sp获取本地保存的订阅标签项
     */
    private List<NavObj> getSubNavsLocal() {
        String substr = (String) SPUtils.get(BeyondApplication.getInstance(), KEY_SUB_NAV, "{}");
        List<NavObj> subLocal;
        try {
            subLocal = new Gson().fromJson(substr, new TypeToken<List<NavObj>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
            subLocal = new ArrayList<>();
        }

        return subLocal;
    }

    @OnClick(R.id.ivToTop)
    public void toTop() {
        int currIndex = mViewPager.getCurrentItem();
        if (mFragments != null && currIndex >= 0 && currIndex < mFragments.size()) {
            ((NewsListFragment) mFragments.get(mViewPager.getCurrentItem())).toTop();
        }
    }

    /**
     * 初始化 顶部标签
     */
    private void initNav() {
        mFragments.clear();//清除
        for (int i = 0; i < navSub.size(); i++) {
            if (i == 0) {
                mFragments.add(NewsListFragment.getInstance(navSub.get(i), News.TYPE_HOME_NEWS, 1,0));
            } else {
                mFragments.add(NewsListFragment.getInstance(navSub.get(i), News.TYPE_HOME_NEWS));
            }
        }
        if (!mFragments.isEmpty()) {
            mIvTop.setVisibility(View.VISIBLE);
        } else {
            mIvTop.setVisibility(View.GONE);
        }
        mAdapter = new MyPagerAdapter(getChildFragmentManager(), mFragments, mViewPager);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);

        mAdapter.notifyDataSetChanged();
        mTabLayout.notifyDataSetChanged();
    }

    /**
     * 处理标签数据 区分 已订阅和为订阅
     */
    private void processNav() {
        List<NavObj> local = getSubNavsLocal();
        getFiexedCount();
        if (local.isEmpty()) {//如果本地没有存储 订阅内容 则默认 固定的nav 为已订阅
            for (NavObj navObj : navs) {
                if (navObj.isFiexed()) {
                    navSub.add(navObj);
                } else {
                    navUnSub.add(navObj);
                }
            }
        } else {//如果本地有 已订阅nav 按照已订阅顺序额排序
            for (NavObj obj : navs) {
                if (obj.isFiexed()) {
                    navSub.add(obj);
                }
            }
            for (NavObj navObj : local) {
                if (navs.contains(navObj) && !navSub.contains(navObj)) { //判断该nav是否存在
                    navSub.add(navObj);
                }
            }
            navUnSub.addAll(navs);
            navUnSub.removeAll(navSub);
        }
    }

    /**
     * 获取固定nav的数量
     */
    private void getFiexedCount() {
        for (int i = 0, size = navs.size(); i < size; i++) {
            if (navs.get(i).isFiexed()) {
                ++canDragIndex;
            }
        }
    }

    /**
     * 搜索
     *
     * @param view
     */
    @OnClick(R.id.ivSearch)
    public void searchClick(View view) {
        GlobalSearchActivity.startActivity(getActivity());
    }

    @OnClick(R.id.tv_sign_tv)
    public void searchClicks(View view) {
        if (tvSignTv.getText().toString().trim().equals("签到")) {
            requestCheckBeforeSign();
            llSignll.setVisibility(View.VISIBLE);
            mViewPager.setEnabled(false);
        }
    }

    @OnClick(R.id.ll_sign_back)
    public void searchClickss(View view) {
        llSignll.setVisibility(View.GONE);
        mViewPager.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivMore:
                mLlPopTitle.setVisibility(View.VISIBLE);
                mTvOrderDelete.setVisibility(View.VISIBLE);
                mTvComplete.setVisibility(View.GONE);
                if (mMenuManagerPop == null) {
                    mMenuManagerPop = new MenuManagerPop(this, getContext(), navSub, navUnSub, canDragIndex);
                    mMenuManagerPop.setManagerListener(this);
                    mMenuManagerPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mLlPopTitle.setVisibility(View.GONE);
                            mMenuManagerPop.confirm();
                        }
                    });

                }
                mMenuManagerPop.show(v);
                position = mViewPager.getCurrentItem();//获取当前位置
                break;
            case R.id.ivClose:
                if (mMenuManagerPop != null) {
                    mMenuManagerPop.dismiss();
                }
                break;
            case R.id.tvOrderDelete:
                mTvOrderDelete.setVisibility(View.GONE);
                mTvComplete.setVisibility(View.VISIBLE);
                mMenuManagerPop.canOrder();
                break;
            case R.id.tvComplete:
                mTvOrderDelete.setVisibility(View.VISIBLE);
                mTvComplete.setVisibility(View.GONE);
                mMenuManagerPop.confirm();
                break;
            case R.id.ivSearch:
                GlobalSearchActivity.startActivity(getActivity());
                break;
        }
    }

    @Override
    public void canManager(boolean isEditable) {
        if (isEditable) {//如果可编辑则展示完成按钮
            mTvComplete.setVisibility(View.VISIBLE);
            mTvOrderDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChange(List<NavObj> navs) {
        initNav();
        SPUtils.put(BeyondApplication.getInstance(), KEY_SUB_NAV, navs);
        mViewPager.setCurrentItem(position >= mAdapter.getCount() ? 0 : position);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_home;
    }

    private class MyPagerAdapter extends CustomFragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments, ViewPager viewPager) {
            super(fm, fragments, viewPager);
        }

        @Override
        public int getCount() {
            return navSub.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.i("position---", "--" + position + "---" + mFragments.size());
            return navSub.get(position).getClassName();
        }
    }
//==================================================================================================
    //检查签到

    private static final int SIGN_CHECK = 800003;
    private static final int SIGN = 800004;
    private UserInfo userInfo;
    private List<TextView> signViewList;
    private SignCheckBean signCheckBean;
    private int keys;
    private boolean isduanqian = false;

    /**
     * 签到前的检查
     */
    private void requestCheckBeforeSign() {
        Map<String, String> paramMap = new HashMap<>();
        //paramMap.put("userId", userInfo.getUserId());
        pointRestUsage.signChecks(SIGN_CHECK, paramMap, userInfo.getUserId());
    }

    @OnClick({R.id.oneDay, R.id.twoDay, R.id.threeDay, R.id.fourDay, R.id.fiveDay, R.id.sixDay, R.id.sevenDay})
    public void onViewClicked(View view) {
        int days = 0;
        if (signCheckBean != null) {
            if ("7".equals(signCheckBean.getKeysta()))
//                    && signCheckBean.getSqlsigndate().equals(signCheckBean.getAjxsigndate()))
                days = Integer.parseInt(signCheckBean.getDays()) + 1;
            else days = Integer.parseInt(signCheckBean.getDays());
        }
        switch (view.getId()) {
            case R.id.oneDay:
                keys = 1;
                requestSign(keys, days);
                break;
            case R.id.twoDay:
                keys = 2;
                requestSign(keys, days);
                break;
            case R.id.threeDay:
                keys = 3;
                requestSign(keys, days);
                break;
            case R.id.fourDay:
                keys = 4;
                requestSign(keys, days);
                break;
            case R.id.fiveDay:
                keys = 5;
                requestSign(keys, days);
                break;
            case R.id.sixDay:
                keys = 6;
                requestSign(keys, days);
                break;
            case R.id.sevenDay:
                keys = 7;
                requestSign(keys, days);
                break;
        }
    }

    /**
     * 签到接口
     */
    private void requestSign(int keys, int days) {
        showProgressDialog();
//        if (days > 0) keys = 7;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("keysta", keys);
        // paramMap.put("days", days);
        paramMap.put("userId", userInfo.getUserId());
        pointRestUsage.sign(SIGN, paramMap);
    }

    /**
     * 将7个签到的View添加到集合中
     */
    private void addAllSignView() {
        signViewList.add(oneDay);
        signViewList.add(twoDay);
        signViewList.add(threeDay);
        signViewList.add(fourDay);
        signViewList.add(fiveDay);
        signViewList.add(sixDay);
        signViewList.add(sevenDay);
    }

    /**
     * 点击签到.成功后,对应的当天要显示为已签到
     *
     * @param postion
     */
    private void displayCurrentDays(int postion) {
        signViewList.get(postion - 1).setBackground(getResources().getDrawable(R.drawable.bg_circle_gray));
        signViewList.get(postion - 1).setTextColor(getResources().getColor(R.color.white));
        signViewList.get(postion - 1).setText("已签到");
        signViewList.get(postion - 1).setTextSize(12);
        signViewList.get(postion - 1).setClickable(false);
    }
}
