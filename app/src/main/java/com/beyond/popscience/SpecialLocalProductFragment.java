package com.beyond.popscience;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.locationgoods.LocalSpecialListActivity;
import com.beyond.popscience.locationgoods.bean.BannerBean;
import com.beyond.popscience.locationgoods.bean.ProductList;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.beyond.popscience.widget.GlideImageLoader;
import com.bumptech.glide.Glide;
import com.idlestar.ratingstar.RatingStarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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

/**
 * 本地特产
 */
public class SpecialLocalProductFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rcy_button)
    RecyclerView rcyButton;
    @BindView(R.id.listView)
    RecyclerView listView;
    Unbinder unbinder;
    @BindView(R.id.viewpagers)
    Banner viewpagers;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ralaser_good)
    ImageView ralaserGood;
    @BindView(R.id.tv_dingwei)
    TextView tvDingwei;
    private List<String> list = new ArrayList<>();
    private CommonAdapter<String> buttonAdapter;
    private CommonAdapter<ProductList> adapter;
    @Request
    AddressRestUsage addressRestUsage;
    private int page = 1;
    private List<ProductList> lists = new ArrayList<>();
    private List<BannerBean> bannerlistssss;
    private String area = "";
    private int sale = 0;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_address;
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

    @Override
    public void initUI() {
        super.initUI();
        title.setText("自产自销");
        initBanner();
        initListView();
        initTabLayout();
        initDateListView();
        getData();
        getBanner();
        tvDingwei.setText("选择区域");
    }

    private void initBanner() {
        viewpagers.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        viewpagers.setImageLoader(new GlideImageLoader());
      /*  //设置图片集合
        banner.setImages(images);*/
        //设置banner动画效果
        viewpagers.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        /*banner.setBannerTitles(titles);*/
        //设置自动轮播，默认为true
        viewpagers.isAutoPlay(true);
        //设置轮播时间
        viewpagers.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        viewpagers.setIndicatorGravity(BannerConfig.CENTER);
        viewpagers.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getActivity(), GoodsDatesActivity.class);
                intent.putExtra("Id", bannerlistssss.get(position).getId() + "");
                startActivity(intent);
            }
        });
    }

    private void getBanner() {
        addressRestUsage.getBanner(1008610);
    }

    private void getData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("area", area);
        map.put("page", page);
        map.put("pageSize", "10");
        map.put("sale", sale);
        addressRestUsage.productList(10086, map);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 10086:
                if (msg.getIsSuccess()) {
                    List<ProductList> list = (List<ProductList>) msg.getObj();
                    if (page == 1) {
                        lists.clear();
                    }
                    lists.addAll(list);
                    adapter.notifyDataSetChanged();
                }
                break;
            case 1008610:
                if (msg.getIsSuccess()) {
                    bannerlistssss = (List<BannerBean>) msg.getObj();
                    List<String> bannerList = new ArrayList<>();
                    List<String> bannerListss = new ArrayList<>();
                    for (BannerBean bean : bannerlistssss) {
                        bannerList.add(bean.getPic());
                        bannerListss.add(bean.getName());
                    }
                    viewpagers.setImages(bannerList);
                    viewpagers.setBannerTitles(bannerListss);
                    viewpagers.start();
                }
                break;
        }
    }

    private void initDateListView() {
        CreatLayoutUtils.creatLinearLayout(getActivity(), listView);
        adapter = new CommonAdapter<ProductList>(getActivity(), R.layout.item_list_view_goods, lists) {

            @Override
            protected void convert(ViewHolder holder, ProductList s, int position) {
                Glide.with(getActivity()).load(s.getPic()).into((ImageView) holder.getView(R.id.iv_img));
                holder.setText(R.id.tv_name, s.getName());
                if (0.0 == s.getSku().getGroupPrice()) {
                    holder.setVisible(R.id.tv_group_price, false).setVisible(R.id.tv_pt_num, false);
                } else {
                    holder.setVisible(R.id.tv_group_price, true).setVisible(R.id.tv_pt_num, true);
                    holder.setText(R.id.tv_group_price, "拼团价:￥" + s.getSku().getGroupPrice()).setText(R.id.tv_pt_num, s.getGroupSize() + "人拼团");
                }
                holder.setText(R.id.tv_price, "单价:￥" + s.getSku().getPrice() + "");
                RatingStarView starbar = holder.getView(R.id.rsv_rating);
                starbar.setRating((float) s.getScore());
                holder.setText(R.id.tv_sale, "已售" + s.getSale() + "单").setText(R.id.tv_address, s.getArea() + s.getShopAddress());
            }
        };
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //跳转详情界面
                Intent intent = new Intent(getActivity(), GoodsDatesActivity.class);
                intent.putExtra("Id", lists.get(position).getId() + "");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initTabLayout() {
        tab.addTab(tab.newTab().setText("综合"));
        tab.addTab(tab.newTab().setText("销量"));
        tab.addTab(tab.newTab().setText("评分"));
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                sale = tab.getPosition();
                page = 1;
                getData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initListView() {
        //八个按钮
        for (int i = 0; i < 7; i++) {
            list.add("菜单" + i);
        }
        CreatLayoutUtils.cretGridViewLayout(getActivity(), rcyButton, 4);
        buttonAdapter = new CommonAdapter<String>(getActivity(), R.layout.item_botton_menu, list) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position == 0) {
                    Glide.with(getActivity()).load(R.drawable.bdtc2x).into((ImageView) holder.getView(R.id.iv_img));
                    holder.setText(R.id.tv_title, "本地特产");
                }
                if (position == 1) {
                    Glide.with(getActivity()).load(R.drawable.gp2x).into((ImageView) holder.getView(R.id.iv_img));
                    holder.setText(R.id.tv_title, "果品");
                }
                if (position == 2) {
                    Glide.with(getActivity()).load(R.drawable.sc2x).into((ImageView) holder.getView(R.id.iv_img));
                    holder.setText(R.id.tv_title, "蔬菜");
                }
                if (position == 3) {
                    Glide.with(getActivity()).load(R.drawable.shuichan2x).into((ImageView) holder.getView(R.id.iv_img));
                    holder.setText(R.id.tv_title, "水产");
                }
                if (position == 4) {
                    Glide.with(getActivity()).load(R.drawable.rqd2x).into((ImageView) holder.getView(R.id.iv_img));
                    holder.setText(R.id.tv_title, "肉禽蛋");
                }
                if (position == 5) {
                    Glide.with(getActivity()).load(R.drawable.ly2x).into((ImageView) holder.getView(R.id.iv_img));
                    holder.setText(R.id.tv_title, "粮油");
                }
                if (position == 6) {
                    Glide.with(getActivity()).load(R.drawable.qtttt).into((ImageView) holder.getView(R.id.iv_img));
                    holder.setText(R.id.tv_title, "其它");
                }
                if (position == 7) {
                    Glide.with(getActivity()).load(R.drawable.more2x).into((ImageView) holder.getView(R.id.iv_img));
                    holder.setText(R.id.tv_title, "更多");
                }
            }
        };
        rcyButton.setAdapter(buttonAdapter);

        buttonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), LocalSpecialListActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 发布商品
     */
    @OnClick(R.id.ralaser_good)
    public void onViewClicked() {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore();
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
        page = 1;
        getData();
    }

    @OnClick(R.id.tv_dingwei)
    public void onViewClickedq() {
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
                    tvDingwei.setText(county.getAreaName());
                    //重新加载viewpager
                    area = county.getAreaName();
                    page = 1;
                    getData();
                }
            }
        });
        task.execute(WelcomeActivity.seletedAdress_two.split("-")[0], WelcomeActivity.seletedAdress_two.split("-")[1], WelcomeActivity.seletedAdress_two.split("-")[2]);
    }

    @OnClick(R.id.go_back)
    public void onViewClickedsss() {
        getActivity().finish();
    }
}
