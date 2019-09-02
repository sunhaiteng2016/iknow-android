package com.beyond.popscience.module.home.fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.PopularMainActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.FromDataRestUsage;
import com.beyond.popscience.frame.net.NewsRestUsage;
import com.beyond.popscience.frame.net.TownNoticeRestUsage;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.NewsListObg;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.locationgoods.ZczxActivity;
import com.beyond.popscience.locationgoods.bean.MainLocation;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.adapter.ImgPagerAdapter;
import com.beyond.popscience.module.home.adapter.MenuCategoryAdapter;
import com.beyond.popscience.module.home.adapter.MenuGridviewAdapter;
import com.beyond.popscience.module.home.adapter.MenusAdapter;
import com.beyond.popscience.module.home.adapter.NewsListAdapter;
import com.beyond.popscience.module.home.adapter.OpenlistCityElv;
import com.beyond.popscience.module.home.entity.HomeMessage;
import com.beyond.popscience.module.home.entity.MyMenu;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.home.entity.OpenCity;
import com.beyond.popscience.module.home.entity.XXX;
import com.beyond.popscience.module.home.shopcart.MoreMenuActivity;
import com.beyond.popscience.module.home.shopcart.NewFragmentActivity;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.mservice.GoodsListActivity;
import com.beyond.popscience.module.mservice.WebViewActivity;
import com.beyond.popscience.module.mservice.X5WebViewActivity;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.beyond.popscience.module.point.PonitShopActivity;
import com.beyond.popscience.module.square.SquareActivity;
import com.beyond.popscience.view.MyGridView;
import com.beyond.popscience.widget.GlideImageLoader;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 新闻列表页
 * Created by yao.cui on 2017/6/8.
 */

public class NewsListFragment extends BaseFragment implements OnBannerListener {

    public static final String KEY_NAVS = "navs";

    private static final int TASK_GET_CAROUSEL = 1401;//获取轮播图
    private static final int TASK_GET_NEWS = 1402;//获取新闻列表

    @BindView(R.id.pull_to_refresh)
    ListView mListView;


    @BindView(R.id.rl_mysj_list)
    RelativeLayout rl_mysj_list;
    @BindView(R.id.iv_mysj)
    ImageView ivMysj;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;


    private NewsListAdapter mNewsAdapter;
    private ImgPagerAdapter mImgPageAdapter;

    private NavObj nav;
    private int type;

    @Request
    private NewsRestUsage mRestUsage;

    @Request
    private TownRestUsage townRestUsage;

    @Request
    private TownNoticeRestUsage townNoticeRestUsage;
    @Request
    private FromDataRestUsage fromDataRestUsage;

    private List<Carousel> mCarousels;
    private NewsListObg mNews;

    private int mCurrentPage = 1;
    private boolean isRefresh = true;
    private List<String> newsReaded;
    private View slideImgView;
    private static ExpandableListView elv_opencity;
    public static AlertDialog mdilaog;
    private static List<OpenCity.DataBean> mlisyt = new ArrayList<>();
    private static OpenlistCityElv madapter;
    private float mFirstY;
    private View customMenus;
    private MyGridView menuLv;
    private MenusAdapter menuAdapter;
    private List<MyMenu> menuList = new ArrayList<>();
    private MyGridView mRvService;
    private MenuCategoryAdapter mCategoryAdapter;
    private int mTypee;
    private MenuGridviewAdapter mennuGridviewAdapter;
    private Banner banner;
    private int style;


    public static NewsListFragment getInstance(NavObj nav) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle params = new Bundle();
        params.putSerializable(KEY_NAVS, nav);
        fragment.setArguments(params);
        return fragment;
    }
    public static NewsListFragment getInstance(NavObj nav, int type) {
        NewsListFragment fragment = new NewsListFragment();

        Bundle params = new Bundle();
        params.putSerializable(KEY_NAVS, nav);
        params.putInt("type", type);
        fragment.setArguments(params);
        return fragment;
    }
    public static NewsListFragment getInstances(NavObj nav, int type) {
        NewsListFragment fragment = new NewsListFragment();

        Bundle params = new Bundle();
        params.putSerializable(KEY_NAVS, nav);
        params.putInt("style", type);
        fragment.setArguments(params);
        return fragment;
    }
    public static NewsListFragment getInstance(NavObj nav, int type, int mType) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle params = new Bundle();
        params.putSerializable(KEY_NAVS, nav);
        params.putInt("type", type);
        params.putInt("mType", mType);
        fragment.setArguments(params);
        return fragment;
    }
    public static NewsListFragment getInstance(NavObj nav, int type, int mType,int style) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle params = new Bundle();
        params.putSerializable(KEY_NAVS, nav);
        params.putInt("type", type);
        params.putInt("mType", mType);
        params.putInt("style", style);
        fragment.setArguments(params);
        return fragment;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (!isHidden()) {
            if (mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0) {
                mViewPager.startAutoScroll();
            }
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (mViewPager != null) {
            mViewPager.stopAutoScroll();
        }*/
    }

    @Override
    public void initUI() {
        super.initUI();
        nav = (NavObj) getArguments().getSerializable(KEY_NAVS);
        type = getArguments().getInt("type", 0);
        mTypee = getArguments().getInt("mType", 0);
        style = getArguments().getInt("style", 0);
        showProgressDialog();
        getLocalReaded();
        getCarousel();
        getNews();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - mListView.getHeaderViewsCount();
                News news = mNewsAdapter.getItem(position);

                if (isTown()) {   //设置成乡镇新闻类型
                    news.appNewsType = News.TYPE_TOWN_NEWS;
                } else if (isTownAnnouncement()) { //乡镇公告
                    news.appNewsType = News.TYPE_TOWN_ANNOUNCEMENT;
                }

                if (isTownAnnouncement()) {   //乡镇公告
                    AnnouncementActivity.startActivity(NewsListFragment.this, news);
                } else {
                    if (isTownNews()) {//是否是乡镇新闻
                        NewsDetailActivity.startActivity(NewsListFragment.this, news, true);
                    } else if (news.newsStyle == 5) {
                        //跳转到 外链
                        X5WebViewActivity.startActivity(getActivity(), news.link, news.title);
                    } else {
                        NewsDetailActivity.startActivity(NewsListFragment.this, news);
                        // NewsDetailActivity.startActivity(NewsListFragment.this, news, false);
                    }
                }
                newsReaded.add(news.newsId);
                TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                if (tvTitle != null) {
                    tvTitle.setTextColor(Color.parseColor("#999999"));
                }
            }
        });

        mNewsAdapter = new NewsListAdapter(this);
        mNewsAdapter.setReaded(newsReaded);
        mListView.setAdapter(mNewsAdapter);
        // mListView.setTopRefreshing();

        //检查开放城市的弹窗
        //showOpenCityDialog();
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (slideImgView != null) {
                    EventBus.getDefault().post(new HomeMessage.homeRefresh(slideImgView.getTop(), slideImgView.getMeasuredHeight() - 150));
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                mCurrentPage++;
                getNews();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                mCurrentPage = 1;
                getNews();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(HomeMessage.menuRefresh messageEvent) {
        mRestUsage.getMyMenu(10086, SPUtils.get(getActivity(), "detailedArea", "").toString().split("-")[2]);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX1(MainLocation event) {
        getLocalReaded();
        getCarousel();
        getNews();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(XXX messageEvent) {
        getNews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

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
                actituty.switchFragment(0);
                mdilaog.dismiss();
                return false;
            }
        });
    }

    /**
     * 给listview添加header 轮播
     */
    private void createHeaderView(List<Carousel> carousels) {
        slideImgView = LayoutInflater.from(getContext()).inflate(R.layout.service_header3, null, false);
        slideImgView.setPadding(0, 0, 0, DensityUtil.dp2px(getContext(), 8));

        banner = (Banner) slideImgView.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
      /*  //设置图片集合
        banner.setImages(images);*/
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        /*banner.setBannerTitles(titles);*/
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(this);
        //准备数据源
        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        for (Carousel bean : carousels) {
            list.add(bean.getPic());
            list1.add(bean.getTitle());
        }
        if (carousels.size() == 0) {
            String sss = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getContext().getResources().getResourcePackageName(R.drawable.ic_launcher);
            list.add(sss);
            list1.add("");
        }
        banner.setImages(list);
        banner.setBannerTitles(list1);
        banner.start();


        mRvService = slideImgView.findViewById(R.id.rvService);

        mennuGridviewAdapter = new MenuGridviewAdapter(getActivity(), menuList);
        mRvService.setAdapter(mennuGridviewAdapter);
        if (type == News.TYPE_HOME_NEWS) {
            if (mTypee == 1) {
                mRestUsage.getMyMenu(10086, SPUtils.get(getActivity(), "detailedArea", "").toString().split("-")[2]);
            }
        }
        mListView.addHeaderView(slideImgView);

        mRvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tabName = menuList.get(i).getTabname();
                int tabId = menuList.get(i).getId();
                if ("更多".equals(tabName)) {
                    Intent intent = new Intent(getActivity(), MoreMenuActivity.class);
                    getActivity().startActivity(intent);
                }
                //外链moveCamera
                if (menuList.get(i).getType() == 1) {
                    WebViewActivity.startActivity(getActivity(), menuList.get(i).getTaburl(), menuList.get(i).getTabname());
                }
                //文章菜单
                if (menuList.get(i).getType() == 2) {
                    int classId = menuList.get(i).getClassify();
                    NavObj obj = new NavObj();
                    obj.setClassId(classId + "");
                    obj.setClassName(tabName);
                    Intent intent = new Intent(getActivity(), NewFragmentActivity.class);
                    intent.putExtra("name", "文章");
                    intent.putExtra("nav", obj);
                    getActivity().startActivity(intent);
                }
                //主菜单
                if (menuList.get(i).getType() == 3) {
                    if (tabId == 0) {
                        //自产自销
                        Intent intent = new Intent(getActivity(), ZczxActivity.class);
                        startActivity(intent);
                    }
                    if (1 == tabId) {
                        ServiceCategory category = new ServiceCategory();
                        category.setTabId("5");
                        category.setTabType("2");
                        category.setTabName("商品买卖");
                        category.setTabPic("'http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/7d1c6754e74941fa8d3e45208e8032c1.jpg");
                        GoodsListActivity.startActivity(getActivity(), category);
                    }
                    if (2 == tabId) {
                        //fromDataRestUsage.StatusByArea(1002, WelcomeActivity.seletedAdress, "2");
                        Intent intent = new Intent(getActivity(), NewFragmentActivity.class);
                        intent.putExtra("name", "社团");
                        getActivity().startActivity(intent);
                    }
                    if (3 == tabId) {
                        //乡镇开通的接口
                        // fromDataRestUsage.StatusByArea(1001, WelcomeActivity.seletedAdress, "1");
                        Intent intent = new Intent(getActivity(), NewFragmentActivity.class);
                        intent.putExtra("name", "乡镇");
                        getActivity().startActivity(intent);
                    }
                    if (4 == tabId) {
                        BuildingActivity.startActivity(getActivity());
                    }
                    if (5 == tabId) {
                        JobActivity.startActivity(getActivity());
                    }
                    if (6 == tabId) {
                        SquareActivity.startActivity(getActivity());
                    }
                    if (7 == tabId) {
                        // fromDataRestUsage.StatusByArea(1003, WelcomeActivity.seletedAdress, "3");
                        Intent intent = new Intent(getActivity(), PonitShopActivity.class);
                        intent.putExtra("score", SPUtils.get(getActivity(), "score", "") + "");
                        getActivity().startActivity(intent);
                    }
                    if (22 == tabId) {
                        Intent intent = new Intent(getActivity(), NewFragmentActivity.class);
                        NavObj obj = new NavObj();
                        obj.setClassId("1");
                        obj.setClassName(tabName);
                        intent.putExtra("name", "科普");
                        intent.putExtra("nav", obj);
                        getActivity().startActivity(intent);
                    }
                    if (24 == tabId) {
                        Intent intent = new Intent(getActivity(), NewFragmentActivity.class);
                        intent.putExtra("name", "服务");
                        getActivity().startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case TASK_GET_CAROUSEL:
                if (msg.getIsSuccess() && mCarousels == null) {
                    rl_mysj_list.setVisibility(View.GONE);
                    mCarousels = (List<Carousel>) msg.getObj();
                    if (mCarousels != null && !mCarousels.isEmpty()) {

                        if (banner != null) {
                            banner.setIndicatorGravity(BannerConfig.CENTER);
                            //准备数据源
                            List<String> list = new ArrayList<>();
                            List<String> list1 = new ArrayList<>();
                            for (Carousel bean : mCarousels) {
                                list.add(bean.getPic());
                                list1.add(bean.getTitle());
                            }
                            if (list.size() == 0) {

                                list.add(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getContext().getResources().getResourcePackageName(R.drawable.ic_launcher));
                                list1.add("");
                            }
                            banner.setImages(list);
                            banner.setBannerTitles(list1);
                            banner.start();
                        } else {
                            if (style ==0) {
                                createHeaderView(mCarousels);
                            }

                        }
                    } else {
                        if (banner != null) {
                            banner.setIndicatorGravity(BannerConfig.CENTER);
                            //准备数据源
                            List<String> list = new ArrayList<>();
                            List<String> list1 = new ArrayList<>();
                            for (Carousel bean : mCarousels) {
                                list.add(bean.getPic());
                                list1.add(bean.getTitle());
                            }
                            if (list.size() == 0) {
                                String sss = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getContext().getResources().getResourcePackageName(R.drawable.ic_launcher);
                                list.add(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getContext().getResources().getResourcePackageName(R.drawable.ic_launcher));
                                list1.add("");
                            }
                            banner.setImages(list);
                            banner.setBannerTitles(list1);
                            banner.start();
                        } else {
                            if (style == 0) {
                                createHeaderView(mCarousels);
                            }
                        }

                    }
                }
                break;
            case TASK_GET_NEWS:
                if (msg.getIsSuccess()) {
                    rl_mysj_list.setVisibility(View.GONE);
                    if (mCurrentPage == 1) {
                        mNewsAdapter.getDataList().clear();
                    }
                    mNews = (NewsListObg) msg.getObj();

                    if (mNews != null) {
                        mNewsAdapter.getDataList().addAll(mNews.getNewsList());
                        mNewsAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case 10086:
                if (msg.getIsSuccess()) {
                    List<MyMenu> menus = (List<MyMenu>) msg.getObj();
                    MyMenu myMenu = new MyMenu();
                    myMenu.setTabname("更多");
                    menus.add(myMenu);
                    if (mennuGridviewAdapter != null) {
                        menuList.clear();
                        menuList.addAll(menus);
                        mennuGridviewAdapter.notifyDataSetChanged();
                    }
                } else {
                    mRestUsage.getMyMenu(10086, SPUtils.get(getActivity(), "detailedArea", "").toString().split("-")[2]);
                }
                break;
        }
    }

    /**
     * 是否城镇
     *
     * @return
     */
    private boolean isTown() {
        if (type == News.TYPE_TOWN_NEWS) {
            return true;
        }
        return false;
    }

    /**
     * 是否乡镇公告
     *
     * @return
     */
    private boolean isTownAnnouncement() {
        if (type == News.TYPE_TOWN_ANNOUNCEMENT) {
            return true;
        }
        return false;
    }

    /**
     * 是否是乡镇新闻
     *
     * @return
     */
    private boolean isTownNews() {
        if (type == News.TYPE_TOWN_NEWS) {
            return true;
        }
        return false;
    }

    /**
     * 获取轮播图
     */
    private void getCarousel() {
        if (isTown()) {   //城镇
            townRestUsage.getCarousel(TASK_GET_CAROUSEL, nav.getClassId());
        } else if (isTownAnnouncement()) {   //城镇公告
            townNoticeRestUsage.getCarousel(TASK_GET_CAROUSEL, nav.getClassId());
        } else {
            mRestUsage.getCarouseltwo(TASK_GET_CAROUSEL, nav.getClassId(), WelcomeActivity.seletedAdress);
        }
    }

    private void getopenlist() {
        JSONObject obj = new JSONObject();
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/queryOpenArea", obj.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                String string = e.toString();
                Log.e("", "");
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
     * 获取新闻列表
     */
    private void getNews() {

        if (isTown()) {   //城镇
            townRestUsage.getNews(TASK_GET_NEWS, nav.getClassId(), mCurrentPage);
        } else if (isTownAnnouncement()) {   //城镇公告
            townNoticeRestUsage.getNews(TASK_GET_NEWS, nav.getClassId(), mCurrentPage);
        } else {
            Map<String, String> mmp = new HashMap<>();
            mmp.put("address", WelcomeActivity.seletedAdress);
            if (mCurrentPage > 1) {
                if (mNewsAdapter.getCount() > 0) {
                    mmp.put("createTime", mNewsAdapter.getDataList().get(mNewsAdapter.getCount() - 1).publishTime);
                }
            }
            mRestUsage.getNewsCache(TASK_GET_NEWS, nav.getClassId(), mCurrentPage, mmp);
        }
    }

    private void getNewsNoCache() {
        if (isTown()) {   //城镇
            townRestUsage.getNews(TASK_GET_NEWS, nav.getClassId(), mCurrentPage);
        } else if (isTownAnnouncement()) {   //城镇公告
            townNoticeRestUsage.getNews(TASK_GET_NEWS, nav.getClassId(), mCurrentPage);
        } else {
            mRestUsage.getNewsNoCache(TASK_GET_NEWS, nav.getClassId(), mCurrentPage);
        }
    }

    public void toTop() {
        // mListView.getRefreshableView().smoothScrollToPosition(0);
    }

    private void getLocalReaded() {
        if (nav == null) {
            newsReaded = new ArrayList<>();
        } else {
            String str = (String) SPUtils.get(BeyondApplication.getInstance(), getNewsReadKey(), "{}");
            try {
                newsReaded = new Gson().fromJson(str, new TypeToken<List<String>>() {
                }.getType());
            } catch (Exception e) {
                newsReaded = new ArrayList<>();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SPUtils.put(BeyondApplication.getInstance(), getNewsReadKey(), newsReaded);
        unbinder.unbind();
    }

    /**
     * @return
     */
    private String getNewsReadKey() {
        if (isTown()) {
            return "townNewsReadList";
        }
        if (isTownAnnouncement()) {   //公告
            return "townNoticeNewsReadList";
        }
        return nav != null ? nav.getClassName() : "";
    }

    /**
     * 刷新
     */
    public void refresh() {
        if (getArguments() != null && mListView != null) {
            nav = (NavObj) getArguments().getSerializable(KEY_NAVS);
            getCarousel();
            //mListView.setTopRefreshing();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_news_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void OnBannerClick(int position) {
        if (mCarousels.size() > 0) {
            Carousel carousel1 = mCarousels.get(position);
            if ("2".equals(carousel1.type)) { //外链
                X5WebViewActivity.startActivity(getActivity(), carousel1.link, carousel1.getTitle());
                return;
            }
            News news = new News();
            news.newsId = carousel1.newsId;
            news.pics = carousel1.getPic();
            news.title = carousel1.getTitle();
            news.appNewsType = type;

            if (type == News.TYPE_TOWN_ANNOUNCEMENT) {   //公告
                AnnouncementActivity.startActivity(getActivity(), news);
            } else {
                NewsDetailActivity.startActivity(getActivity(), news);
            }
        }
    }
}
