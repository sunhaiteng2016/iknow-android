package com.beyond.popscience.module.home.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.PopularMainActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.NewsRestUsage;
import com.beyond.popscience.frame.net.TownNoticeRestUsage;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.NewsListObg;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.adapter.ImgPagerAdapter;
import com.beyond.popscience.module.home.adapter.MenuCategoryAdapter;
import com.beyond.popscience.module.home.adapter.MenusAdapter;
import com.beyond.popscience.module.home.adapter.OpenlistCityElv;
import com.beyond.popscience.module.home.adapter.newListRecyViewAdapter;
import com.beyond.popscience.module.home.entity.HomeMessage;
import com.beyond.popscience.module.home.entity.MyMenu;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.home.entity.OpenCity;
import com.beyond.popscience.module.home.entity.XXX;
import com.beyond.popscience.module.mservice.X5WebViewActivity;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.beyond.popscience.view.MyGridView;
import com.beyond.popscience.widget.GridSpacingItemDecoration;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
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

public class NewsListNoBannerFragment extends BaseFragment {

    public static final String KEY_NAVS = "navs";

    private static final int TASK_GET_CAROUSEL = 1401;//获取轮播图
    private static final int TASK_GET_NEWS = 1402;//获取新闻列表

    @BindView(R.id.pull_to_refresh)
    RecyclerView mListView;


    @BindView(R.id.rl_mysj_list)
    RelativeLayout rl_mysj_list;
    @BindView(R.id.iv_mysj)
    ImageView ivMysj;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;


    private AutoViewPager mViewPager;
    private newListRecyViewAdapter mNewsAdapter;
    private ImgPagerAdapter mImgPageAdapter;

    private NavObj nav;
    private int type;

    @Request
    private NewsRestUsage mRestUsage;

    @Request
    private TownRestUsage townRestUsage;

    @Request
    private TownNoticeRestUsage townNoticeRestUsage;

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
    private RecyclerView mRvService;
    private MenuCategoryAdapter mCategoryAdapter;


    public static NewsListNoBannerFragment getInstance(NavObj nav) {
        NewsListNoBannerFragment fragment = new NewsListNoBannerFragment();
        Bundle params = new Bundle();
        params.putSerializable(KEY_NAVS, nav);
        fragment.setArguments(params);
        return fragment;
    }

    public static NewsListNoBannerFragment getInstance(NavObj nav, int type) {
        NewsListNoBannerFragment fragment = new NewsListNoBannerFragment();

        Bundle params = new Bundle();
        params.putSerializable(KEY_NAVS, nav);
        params.putInt("type", type);
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mViewPager != null) {
                mViewPager.stopAutoScroll();
            }
        } else {
            if (mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0) {
                mViewPager.startAutoScroll();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            if (mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0) {
                mViewPager.startAutoScroll();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewPager != null) {
            mViewPager.stopAutoScroll();
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        nav = (NavObj) getArguments().getSerializable(KEY_NAVS);
        type = getArguments().getInt("type", 0);
        showProgressDialog();
        getLocalReaded();
        getCarousel();
        getNews();
        mListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mNewsAdapter = new newListRecyViewAdapter(this);
        mNewsAdapter.setReaded(newsReaded);
        mListView.setAdapter(mNewsAdapter);
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
                mCurrentPage=1;
                getNews();
            }
        });

        mNewsAdapter.setItemOnClick(new newListRecyViewAdapter.itemOnClick() {
            @Override
            public void onMyClick(int position, View view) {
                News news = mNewsAdapter.getItem(position);

                if (isTown()) {   //设置成乡镇新闻类型
                    news.appNewsType = News.TYPE_TOWN_NEWS;
                } else if (isTownAnnouncement()) { //乡镇公告
                    news.appNewsType = News.TYPE_TOWN_ANNOUNCEMENT;
                }

                if (isTownAnnouncement()) {   //乡镇公告
                    AnnouncementActivity.startActivity(NewsListNoBannerFragment.this, news);
                } else {
                    if (isTownNews()) {//是否是乡镇新闻
                        NewsDetailActivity.startActivity(NewsListNoBannerFragment.this, news, true);
                    } else if (news.newsStyle == 5) {
                        //跳转到 外链
                        X5WebViewActivity.startActivity(getActivity(), news.link, news.title);
                    } else {
                        NewsDetailActivity.startActivity(NewsListNoBannerFragment.this, news, false);
                    }
                }
                newsReaded.add(news.newsId);
                if (view != null && view instanceof TextView) {
                    TextView mview = (TextView) view;
                    mview.setTextColor(Color.parseColor("#999999"));
                }
            }

        });

        //检查开放城市的弹窗
        showOpenCityDialog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mListView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (slideImgView != null) {
                        EventBus.getDefault().post(new HomeMessage.homeRefresh(slideImgView.getTop(), slideImgView.getMeasuredHeight()));
                    }
                }
            });
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(HomeMessage.menuRefresh messageEvent) {
        mRestUsage.getMyMenu(10086,SPUtils.get(getActivity(), "detailedArea", "").toString().split("-")[2]);

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
    private void createHeaderView() {
        slideImgView = LayoutInflater.from(getContext()).inflate(R.layout.service_header, null, false);
        slideImgView.setPadding(0, 0, 0, DensityUtil.dp2px(getContext(), 8));

        mViewPager = (AutoViewPager) slideImgView.findViewById(R.id.vp);
        final TextView tvTitle = (TextView) slideImgView.findViewById(R.id.tvTitle);

        RecyclingCirclePageIndicator mIndicator = (RecyclingCirclePageIndicator) slideImgView.findViewById(R.id.recycleIndicator);

        mImgPageAdapter = new ImgPagerAdapter(this, true, mCarousels);
        mImgPageAdapter.setType(type);


        mViewPager.setAdapter(mImgPageAdapter);
        mViewPager.setInterval(2000);
        mViewPager.setCurrentItem(mImgPageAdapter.getRealCount() * 1000, false);
        mViewPager.startAutoScroll();
        mIndicator.setViewPager(mViewPager);
        mIndicator.setCentered(true);
        tvTitle.setText(mCarousels.get(0).getTitle());

        //设置轮播图标题
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        mViewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
                layoutParams.height = (mViewPager.getWidth() * 3) / 4;
                mViewPager.setLayoutParams(layoutParams);
                return true;
            }
        });


        //你在讲什么东西
        mRvService = (RecyclerView) slideImgView.findViewById(R.id.rvService);

        GridLayoutManager glManager = new GridLayoutManager(getContext(), 5);
        mRvService.setLayoutManager(glManager);
        mRvService.addItemDecoration(new GridSpacingItemDecoration(4, 10, false));
        mCategoryAdapter = new MenuCategoryAdapter(this);
        mRvService.setAdapter(mCategoryAdapter);

        if (!isTown() && !isTownAnnouncement() && !isTownNews()) {
            mRestUsage.getMyMenu(10086,SPUtils.get(getActivity(), "detailedArea", "").toString().split("-")[2]);
        }
        // mListView.addHeaderView(slideImgView);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case TASK_GET_CAROUSEL:
                break;
            case TASK_GET_NEWS:
                if (msg.getIsSuccess()) {
                    rl_mysj_list.setVisibility(View.GONE);
                    if (mCurrentPage==1) {
                        mNewsAdapter.getDataList().clear();
                    }
                    mNews = (NewsListObg) msg.getObj();
                    if (mNews != null) {

                        mNewsAdapter.getDataList().addAll(mNews.getNewsList());
                        mNewsAdapter.notifyDataSetChanged();
                    }

                } else {

                }

                // mListView.onRefreshComplete();
                break;
            case 10086:
                if (msg.getIsSuccess()) {
                    List<MyMenu> menus = (List<MyMenu>) msg.getObj();
                    MyMenu myMenu = new MyMenu();
                    myMenu.setTabname("更多");
                    menus.add(myMenu);
                    mCategoryAdapter.getDataList().clear();
                    mCategoryAdapter.getDataList().addAll(menus);
                    mCategoryAdapter.notifyDataSetChanged();
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
            //请求是否开通城市服务
            JSONObject obj = new JSONObject();
            try {
                obj.put("address", WelcomeActivity.seletedAdress);
                OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/isOpen", obj.toString(), new CallBackUtil.CallBackString() {
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
                mRestUsage.getCarouseltwo(TASK_GET_CAROUSEL, nav.getClassId(), WelcomeActivity.seletedAdress);
                //只有首页才去请求菜单
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
        mListView.smoothScrollToPosition(0);
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
            // mListView.setTopRefreshing();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_news_list_recy_list_two;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
