package com.beyond.popscience.module.home.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.InputMethodUtil;
import com.beyond.popscience.PopularMainActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.TownNoticeRestUsage;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.frame.view.CustomCommonTabLayout;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.adapter.ImgPagerAdapter;
import com.beyond.popscience.module.home.adapter.OpenlistCityElv;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.home.entity.OpenCity;
import com.beyond.popscience.module.home.entity.XXX;
import com.beyond.popscience.module.home.shopcart.NewFragmentActivity;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.beyond.popscience.module.social.fragment.SocialCircleContentFragment;
import com.beyond.popscience.module.town.fragment.IntroFragment;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;
import com.beyond.popscience.widget.TabEntity;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class TownFragment extends BaseFragment {

    /**
     *
     */
    private final TabEntity[] TABS = {
            new TabEntity("资讯", 0, 0),
            new TabEntity("公告", 0, 0),
            new TabEntity("说说", 0, 0),
            new TabEntity("成员", 0, 0),
            new TabEntity("简介", 0, 0)

    };

    @BindView(R.id.publishedTxtView)
    TextView publishedTxtView;
    Unbinder unbinder;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rightImgView)
    ImageView rightImgView;
    @BindView(R.id.shadowView)
    View shadowView;
    @BindView(R.id.topReLay)
    RelativeLayout topReLay;
    @BindView(R.id.vp)
    AutoViewPager vp;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.recycleIndicator)
    RecyclingCirclePageIndicator recycleIndicator;
    @BindView(R.id.bannerReLay)
    LinearLayout bannerReLay;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.ctlHeader)
    CollapsingToolbarLayout ctlHeader;

    /**
     * 缓存已经初始化过的Fragment
     */
    private ArrayMap<String, Fragment> mCacheFragmentMap = new ArrayMap<>();

    /**
     * 选择地址
     */
    private final int REUQEST_TOWN_ADDRESS_CODE = 101;

    @BindView(R.id.tv_title)
    protected TextView titleTxtView;

    @BindView(R.id.leftTxtView)
    protected TextView tvAddress;

    @BindView(R.id.tabs)
    protected CustomCommonTabLayout mTab;

    @BindView(R.id.tabContent)
    protected FrameLayout mTabContent;

    @BindView(R.id.publishedLinLay)
    protected LinearLayout publishedLinLay;


    /**
     * 当前的地址
     */
    private Address currAddress;
    /**
     *
     */
    private Fragment currFragment;
    private Address seletAddress;
    private int type;
    private List<Carousel> mCarousels;
    private ImgPagerAdapter mImgPageAdapter;
    public static AppBarLayout appBarLayout;


    public static TownFragment getInstance() {
        TownFragment fragment = new TownFragment();

        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) { //隐藏
            for (Fragment fragment : mCacheFragmentMap.values()) {
                fragment.onHiddenChanged(hidden);
            }
        } else {
            Fragment fragment = mCacheFragmentMap.get(String.valueOf(mTab.getCurrentTab()));
            if (fragment != null) {
                fragment.onHiddenChanged(hidden);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REUQEST_TOWN_ADDRESS_CODE: //选择地址
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Address address = (Address) data.getSerializableExtra("address");
                    if (address != null) {
                        this.currAddress = address;
                        setresultAddress();
                        switchFragment(mTab.getCurrentTab());
                    }
                }
                break;
        }
    }


    public void A() {
        this.currAddress = seletAddress;
        setresultAddress();
        switchFragment(mTab.getCurrentTab());
    }

    @Override
    public void initUI() {
        super.initUI();
        appBarLayout= (AppBarLayout) getView().findViewById(R.id.appBarLayout);

        showOpenCityDialog();
        // =============================================================================================================
        //请求是否开通城市服务
        JSONObject objs = new JSONObject();
        try {
            objs.put("address", WelcomeActivity.seletedAdress);
            OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/isOpen", objs.toString(), new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    Log.e("s", "------------------------------------------------------------");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(String response) {
                    //{"code":0,"message":"当前选择城市已经开通服务权限","data":true}
                    try {
                        JSONObject obj = new JSONObject(response);
                        boolean data = obj.getBoolean("data");
                        if (data == true) {
                        } else {
                            getopenlist();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //这边是初始化列表
        if (getArguments() != null)
            seletAddress = (Address) getArguments().getSerializable("seletAddress");
        if (null == seletAddress) {
            UserInfo userInfo = UserInfoUtil.getInstance().getUserInfo();
            List<Address> addressList = BeyondApplication.getInstance().getCacheAddressList();
            Address firstParentAddress = null;
            if (addressList != null && addressList.size() != 0) {

                for (Address parentAddress : addressList) {
                    if (parentAddress.getChild() != null && parentAddress.getChild().size() != 0) {
                        if (firstParentAddress == null) {
                            firstParentAddress = parentAddress;
                        }
                        for (Address childAddress : parentAddress.getChild()) {
                            if (String.valueOf(childAddress.getId()).equals(userInfo.getVillageId())) {
                                currAddress = Address.assembledAddress(parentAddress, childAddress);
                                break;
                            }
                        }
                    }
                    if (currAddress != null) {
                        break;
                    }
                }
            }

            if (currAddress == null) {
                Address a = new Address();
                a.setId(Integer.parseInt(userInfo.getVillageId()));
                a.setName(userInfo.getVillageName());
                currAddress = a;
            }
            setresultAddress();
        } else {
            A();
        }
        initTab();
        type = News.TYPE_TOWN_NEWS;
        getCarousel();
    }

    private ExpandableListView elv_opencity;
    public AlertDialog mdilaog;
    private List<OpenCity.DataBean> mlisyt = new ArrayList<>();
    private OpenlistCityElv madapter;

    private void showOpenCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View cityview = LayoutInflater.from(getActivity()).inflate(R.layout.item_is_open_service, null);
        elv_opencity = (ExpandableListView) cityview.findViewById(R.id.elv_opencity);
        madapter = new OpenlistCityElv(getActivity(), mlisyt);
        elv_opencity.setAdapter(madapter);
        builder.setView(cityview);
        builder.setTitle("该城市没有开通服务");
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        if (mdilaog == null) {
            mdilaog = builder.create();
        }
        elv_opencity.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                WelcomeActivity.seletedAdress = mlisyt.get(i).getAreaName() + "-" + mlisyt.get(i).getChild().get(i1).getAreaName();
                PopularMainActivity actituty = (PopularMainActivity) getActivity();
                actituty.switchFragment(1);
                mdilaog.dismiss();
                return false;
            }
        });
    }

    private void getopenlist() {
        JSONObject obj = new JSONObject();
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/queryOpenArea", obj.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                mlisyt.clear();
                OpenCity opencity = gson.fromJson(response, OpenCity.class);
                if (opencity.getData().size() > 0) {
                    mlisyt.addAll(opencity.getData());
                    madapter.notifyDataSetChanged();
                    if (!mdilaog.isShowing()) {
                        mdilaog.show();
                        mdilaog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getActivity().getResources().getColor(R.color.blue));
                        mdilaog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getActivity().getResources().getColor(R.color.blue));
                    }
                }
            }
        });
    }

    /**
     * 设置显示地址
     */
    private void setresultAddress() {

        if (currAddress != null) {
            String name = currAddress.getName();
            if (currAddress.getParentAddress() != null && currAddress.getId() == currAddress.getParentAddress().getId()) {
                name = currAddress.getParentAddress().getName();
            }
            tvAddress.setText(name);
            titleTxtView.setText(name);
        } else {
            tvAddress.setText("");
            titleTxtView.setText("乡镇");
        }
    }


    /**
     * @return
     */
    private NavObj getNavObj() {
        NavObj nav = new NavObj();
        String classId = null;
        if (currAddress != null) {
            classId = String.valueOf(currAddress.getId());
            if (TextUtils.equals(classId, "0") && currAddress.getParentAddress() != null) {
                classId = String.valueOf(currAddress.getParentAddress().getId());
            }
        }

        nav.setClassId(classId);
        nav.setAddress(currAddress);
        return nav;
    }

    private void initTab() {
        mTab.setTabData(new ArrayList<CustomTabEntity>(Arrays.asList(TABS)));
        mTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                switchFragment(i);
            }

            @Override
            public void onTabReselect(int i) {

            }
        });
        mTab.setCurrentTab(0);
        switchFragment(0);
        List<Fragment> fragmentslist = new ArrayList<>();
        fragmentslist.add(NewsListNoBannerFragment.getInstance(getNavObj(), News.TYPE_TOWN_NEWS));
        fragmentslist.add(NewsListNoBannerFragment.getInstance(getNavObj(), News.TYPE_TOWN_ANNOUNCEMENT));
        SocialInfo socialInfo = new SocialInfo();
        socialInfo.setCommunityId(getNavObj().getClassId());
        socialInfo.setAppType(1);
        fragmentslist.add(SocialCircleContentFragment.newInstance(socialInfo));
        fragmentslist.add(TownMemberFragment.getInstance(currAddress));
        fragmentslist.add(IntroFragment.getInstance(currAddress));
        if (NewFragmentActivity.position==3){
            switchFragment(2);
            mTab.setCurrentTab(2);
        }
    }

    /**
     * 切换Fragment
     *
     * @param index
     */
    private void switchFragment(int index) {
        InputMethodUtil.hiddenSoftInput(getActivity());
        hiddenPublishView();
        switch (index) {
            case 0: //最新消息
                type = News.TYPE_TOWN_NEWS;
                getCarousel();
                currFragment = NewsListNoBannerFragment.getInstance(getNavObj(), News.TYPE_TOWN_NEWS);
                EventBus.getDefault().post(new XXX());
                break;
            case 1: //公告
                type = News.TYPE_TOWN_ANNOUNCEMENT;
                getCarousel();
                currFragment = NewsListNoBannerFragmentTwo.getInstance(getNavObj(), News.TYPE_TOWN_ANNOUNCEMENT);
                break;
            case 2: //圈子
                publishedLinLay.setVisibility(View.VISIBLE);
                SocialInfo socialInfo = new SocialInfo();
                socialInfo.setCommunityId(getNavObj().getClassId());
                socialInfo.setAppType(1);
                currFragment = SocialCircleContentFragment.newInstance(socialInfo);
                if (currAddress != null && String.valueOf(currAddress.getId()).equals(UserInfoUtil.getInstance().getUserInfo().getVillageId())) {
                    showPublishView();
                } else {
                    hiddenPublishView();
                }
                break;
            case 3: //最新消息
                currFragment = TownMemberFragment.getInstance(currAddress);
                break;
            case 4:
                currFragment = IntroFragment.getInstance(currAddress);
                break;
        }
        if (currFragment != null) {
            replaceFragment(R.id.tabContent, currFragment, false, false);
        }
    }

    /**
     * 显示发布
     */
    public void showPublishView() {
        publishedLinLay.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏发布
     */
    public void hiddenPublishView() {
        publishedLinLay.setVisibility(View.GONE);
    }

    @OnClick(R.id.leftTxtView)
    public void toAddressSelect() {
        if (getActivity() instanceof NewFragmentActivity) {
            NewFragmentActivity mActivity = (NewFragmentActivity) getActivity();
            mActivity.switchFragmentmys();
        }
    }

    @OnClick(R.id.rightImgView)
    public void searchClick() {
        GlobalSearchActivity.startActivityTown(getActivity());
    }

    /**
     * 发布
     */
    @OnClick(R.id.publishedLinLay)
    public void publishedClick() {
        if (currFragment != null && currFragment instanceof SocialCircleContentFragment) {
            ((SocialCircleContentFragment) currFragment).startPublishedActivity();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_town;
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
    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        getActivity().finish();
    }

    @Request
    private TownRestUsage townRestUsage;

    @Request
    private TownNoticeRestUsage townNoticeRestUsage;
    private static final int TASK_GET_CAROUSEL = 1401;//获取轮播图

    /**
     * 获取轮播
     */
    private void getCarousel() {
        if (type == News.TYPE_TOWN_NEWS) {   //城镇
            townRestUsage.getCarousel(TASK_GET_CAROUSEL, getNavObj().getClassId());
        } else if (type == News.TYPE_TOWN_ANNOUNCEMENT) {   //城镇公告
            townNoticeRestUsage.getCarousel(TASK_GET_CAROUSEL, getNavObj().getClassId());
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_GET_CAROUSEL:
                if (msg.getIsSuccess() && mCarousels == null) {
                    mCarousels = (List<Carousel>) msg.getObj();
                    if (mCarousels != null && !mCarousels.isEmpty()) {
                        if (mImgPageAdapter != null) {
                            vp.stopAutoScroll();
                            mImgPageAdapter.setmCarousels(mCarousels);
                            mImgPageAdapter.notifyDataSetChanged();
                            vp.startAutoScroll();
                        } else {
                            createHeaderView();
                        }
                    }
                    return;
                }
                break;
        }

    }

    //创建头部
    private void createHeaderView() {

        mImgPageAdapter = new ImgPagerAdapter(this, true, mCarousels);
        mImgPageAdapter.setType(type);
        vp.setAdapter(mImgPageAdapter);
        vp.setInterval(2000);
        vp.setCurrentItem(mImgPageAdapter.getRealCount() * 1000, false);
        vp.startAutoScroll();
        recycleIndicator.setViewPager(vp);
        recycleIndicator.setCentered(true);
        tvTitle.setText(mCarousels.get(0).getTitle());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (mImgPageAdapter != null) {
                    tvTitle.setText(mCarousels.get(mImgPageAdapter.getRealPosition(position)).getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 设置轮播图的比例
        vp.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                vp.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewGroup.LayoutParams layoutParams = vp.getLayoutParams();
                layoutParams.height = (vp.getWidth() * 3) / 4;
                vp.setLayoutParams(layoutParams);
                return true;
            }
        });
    }
}
