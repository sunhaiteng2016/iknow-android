package com.beyond.popscience.module.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.GoodsDatesActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.api.ProductApi;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.LazyFragment;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.view.AutoViewPager;
import com.beyond.popscience.locationgoods.bean.ProductList;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.home.adapter.ServiceCategoryTwoAdapter;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.home.entity.kepuMenu;
import com.beyond.popscience.module.mservice.PublishGoodsActivity;
import com.beyond.popscience.module.mservice.adapter.GoodsAdapterTwo;
import com.beyond.popscience.module.mservice.adapter.ServiceSlideAdapter;
import com.beyond.popscience.module.news.GlobalSearchActivity;
import com.beyond.popscience.widget.GridSpacingItemDecoration;
import com.beyond.popscience.widget.RecyclingCirclePageIndicator;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import okhttp3.Call;

/**
 * 服务
 */
public class ServiceTwoFragment extends LazyFragment {
    private static final int TASK_GET_CAROUSEL = 1401;//获取轮播图
    private static final int TASK_GET_SERVICE = 1402;//获取服务类型
    private static final int TASK_GET_GOODS = 1403;//获取商品

    /**
     * 选择地址
     */
    private final int REUQEST_TOWN_ADDRESS_CODE = 101;

    @BindView(R.id.leftTxtView)
    protected TextView leftTxtView;
    @BindView(R.id.gvContainer)
    protected PullToRefreshListView lvContainer;
    AutoViewPager mSlidePager;
    TextView tvTitle;
    RecyclingCirclePageIndicator mIndicator;
    RecyclerView mRvService;
    @BindView(R.id.tv_back)
    TextView tvBack;
    Unbinder unbinder;

    private ServiceSlideAdapter mImgPageAdapter;
    private List<Carousel> mCarousels = new ArrayList<>();
    private ServiceGoodsList mGoodsList;
    private GoodsAdapterTwo mGoodsAdapter;
    private ServiceCategoryTwoAdapter mCategoryAdapter;

    private int mCurrentPage = 1;
    private int totalPage = -1;

    private View headerView;
    @Request
    private ServiceRestUsage mRestUsage;
    @Request
    private ProductApi productApi;
    @Request
    private AddressRestUsage addressRestUsage;
    //商品列表
    private final int PRODUCT_LIST_ID = 1;
    private boolean isLoad = false;
    private ServiceGoodsList serviceGoodsList;

    /**
     * 当前的地址
     */
    private Address currAddress;
    private List<ProductList> mGoodsLists=new ArrayList<>();

    public static ServiceTwoFragment getInstance() {
        ServiceTwoFragment sf = new ServiceTwoFragment();
        return sf;
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
                        setAddressTxt();
                    }
                }
                break;
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        currAddress = new Address();
        currAddress.setName("仙居");
        setAddressTxt();
        initHeader();
        initGird();
        getCarousel();
        getService();
        getProductsList();//现在的
    }

    //获取商品列表
    private void getProductsList() {
        HashMap<String, Object> map=new HashMap<>();
        map.put("area", "");
        map.put("page", mCurrentPage);
        map.put("pageSize", "10");
        map.put("sale", 0);
        addressRestUsage.productList(PRODUCT_LIST_ID,map);
    }

    /**
     * 设置地址显示
     */
    private void setAddressTxt() {
        if (currAddress != null) {
            String name = currAddress.getName();
            if (currAddress.getParentAddress() != null && currAddress.getId() == currAddress.getParentAddress().getId()) {
                name = currAddress.getParentAddress().getName();
            }
            leftTxtView.setText(WelcomeActivity.seletedAdress.split("-")[1]);
        } else {
            leftTxtView.setText(WelcomeActivity.seletedAdress.split("-")[1]);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if (!isLoad) {
            isLoad = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mSlidePager != null) {
                mSlidePager.stopAutoScroll();
            }
        } else {
            if (mSlidePager != null && mSlidePager.getAdapter() != null && mSlidePager.getAdapter().getCount() > 0) {
                mSlidePager.startAutoScroll();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            if (mSlidePager != null && mSlidePager.getAdapter() != null && mSlidePager.getAdapter().getCount() > 0) {
                mSlidePager.startAutoScroll();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSlidePager != null) {
            mSlidePager.stopAutoScroll();
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_GET_CAROUSEL:
                if (msg.getIsSuccess()) {
                    mCarousels = (List<Carousel>) msg.getObj();
                    if (mCarousels != null && !mCarousels.isEmpty()) {
                        initSlide();
                    }
                }
                break;
            case TASK_GET_SERVICE:
                List<kepuMenu> mCategories = (ArrayList) msg.getObj();
                Log.e("", "");
                if (mCategories != null) {
                    mCategoryAdapter.getDataList().clear();
                    mCategoryAdapter.getDataList().addAll(mCategories);
                    mCategoryAdapter.notifyDataSetChanged();
                }
                break;
            case PRODUCT_LIST_ID://现在的商品列表
                lvContainer.onRefreshComplete();
                if (msg.getIsSuccess()) {
                    List<ProductList> mGoodsListss = (List<ProductList>) msg.getObj();
                    if (mCurrentPage==1){
                        mGoodsLists.clear();
                    }
                    mGoodsLists.addAll(mGoodsListss);
                    mGoodsAdapter.notifyDataSetChanged();
                    if (lvContainer.isLoadingMore()){
                        lvContainer.onLoadMoreComplete();
                    }
                }
                break;
        }
    }

    private void initGird() {
        lvContainer.setMode(PullToRefreshBase.Mode.BOTH);
        lvContainer.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrentPage = 1;
//                getGoods();
                getProductsList();
            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mCurrentPage == 1) {
                    mCurrentPage = 2;
                }
//                getGoods();
                getProductsList();
            }
        });

        mGoodsAdapter = new GoodsAdapterTwo(getActivity(),mGoodsLists);
        //可动态调整Item的layout
        //在gridviewpulltorefresh中用到padding方法，改成listview后，可以取消

        lvContainer.setAdapter(mGoodsAdapter);
        lvContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*ServiceGoodsItem goods = mGoodsAdapter.getItem((int) id);
                GoodsNewDetailActivity.startActivity(getActivity(), goods.getProductId());*/
                Intent intent = new Intent(getActivity(), GoodsDatesActivity.class);
                intent.putExtra("Id", mGoodsLists.get(position-2).getId() + "");
                startActivity(intent);
            }
        });
    }


    private void initHeader() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.service_header, null, false);
        mSlidePager = (AutoViewPager) headerView.findViewById(R.id.vp);
        mIndicator = (RecyclingCirclePageIndicator) headerView.findViewById(R.id.recycleIndicator);
        tvTitle = (TextView) headerView.findViewById(R.id.tvTitle);

        mRvService = (RecyclerView) headerView.findViewById(R.id.rvService);
        //推荐服务
        GridLayoutManager glManager = new GridLayoutManager(getContext(), 4);
        mRvService.setLayoutManager(glManager);
        mRvService.addItemDecoration(new GridSpacingItemDecoration(4, 10, false));
        mCategoryAdapter = new ServiceCategoryTwoAdapter(this);
        mRvService.setAdapter(mCategoryAdapter);
        lvContainer.getRefreshableView().addHeaderView(headerView);
    }

    /**
     * 初始化 轮播
     */
    private void initSlide() {
        mImgPageAdapter = new ServiceSlideAdapter(getActivity(), true, mCarousels);
        mImgPageAdapter.setmClassId("5");
        mSlidePager.setAdapter(mImgPageAdapter);
        mSlidePager.setInterval(2000);
        mSlidePager.setCurrentItem(mImgPageAdapter.getRealCount() * 1000, false);
        mSlidePager.startAutoScroll();
        mIndicator.setViewPager(mSlidePager);
        mIndicator.setCentered(true);
        //设置轮播图标题
        mSlidePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mImgPageAdapter == null) {
                    return;
                }
                if (mCarousels == null || mCarousels.size() <= 0) {
                    return;
                }
                tvTitle.setText(mCarousels.get(mImgPageAdapter.getRealPosition(position)).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 设置轮播图的比例
        mSlidePager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mSlidePager.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewGroup.LayoutParams layoutParams = mSlidePager.getLayoutParams();
                layoutParams.height = (mSlidePager.getWidth() * 3) / 4;
                mSlidePager.setLayoutParams(layoutParams);
                return true;
            }
        });
    }

    @OnClick(R.id.llPublish)
    public void toPublish(View view) {
        PublishGoodsActivity.startActivity(this);
    }

    @OnClick(R.id.tvSearch)
    public void toSearch(View view) {
        GlobalSearchActivity.startActivityService(getContext());
    }

    @OnClick({R.id.leftTxtView})
    public void toAddressSelect() {
        //功能开放的城市
        JSONObject obj = new JSONObject();
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/villages/queryOpenArea", obj.toString(), new CallBackUtil.CallBackString() {

            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject onj = new JSONObject(response);
                    JSONArray date = onj.getJSONArray("data");
                    String ss = date.toString();
                    String ssss = ss.replace("child", "counties");
                    String s = ssss.replace("id", "areaId");
                    String newjson = "[{\"areaId\": \"130000\",\"areaName\": \"河北省\",\"cities\":" + s + "}]";
                    ArrayList<Province> data = new ArrayList<>();
                    data.addAll(JSON.parseArray(newjson, Province.class));
                    AddressPicker picker = new AddressPicker(getActivity(), data);
                    picker.setShadowVisible(true);
                    picker.setHideProvince(true);
                    picker.setTextSize(16);
                    picker.setPadding(10);
                    picker.setSelectedItem("", "台州市", "仙居县");
                    picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {

                        @Override
                        public void onAddressPicked(Province province, City city, County county) {
                            WelcomeActivity.seletedAdress = city.getAreaName() + "-" + county.getAreaName();
                           /* //重新的刷新  列表的數據
                            switchFragment(1);
                            textView4.setText(county.getAreaName());*/
                            leftTxtView.setText(WelcomeActivity.seletedAdress.split("-")[1]);
                            //重新加载列表数据
                            getCarousel();
                            getService();
                            getProductsList();//现在的
                        }
                    });
                    picker.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取轮播图
     */
    private void getCarousel() {
        mRestUsage.getCarouselTwo(TASK_GET_CAROUSEL, "");
    }

    /**
     * 获取服务类型列表
     */
    private void getService() {
        mRestUsage.getServiceCategory(TASK_GET_SERVICE, SPUtils.get(getActivity(), "detailedArea", "").toString().split("-")[2]);
    }

    /**
     * 获取商品列表
     */
    private void getGoods() {
        mRestUsage.getProducts(TASK_GET_GOODS, mCurrentPage);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_service_two;
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
}
