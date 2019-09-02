package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.GoodsDatesActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.bean.ProductList;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.bumptech.glide.Glide;
import com.idlestar.ratingstar.RatingStarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * 八哥按钮的点击具体跳转
 */
public class LocalSpecialListActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.lay_header)
    RelativeLayout layHeader;
    @BindView(R.id.system_bar_view)
    View systemBarView;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_dingwei)
    TextView tvDingwei;
    private List<ProductList> list = new ArrayList<>();
    private CommonAdapter<ProductList> adapter;
    private int position, page = 1, pageSize = 10;
    private String categoryOne = "";
    @Request
    AddressRestUsage addressRestUsage;
    private String area = "";
    private int sale = 0;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_local_special_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
                refreshLayout.autoRefresh();
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
        CreatLayoutUtils.cretGridViewLayout(this, listView, 1);
        adapter = new CommonAdapter<ProductList>(this, R.layout.item_list_view_goods, list) {

            @Override
            protected void convert(ViewHolder holder, ProductList s, int position) {
                Glide.with(LocalSpecialListActivity.this).load(s.getPic()).into((ImageView) holder.getView(R.id.iv_img));
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
                Intent intent = new Intent(LocalSpecialListActivity.this, GoodsDatesActivity.class);
                intent.putExtra("Id", list.get(position).getId() + "");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                page = 1;
                getData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                page++;
                getData();
            }
        });
    }

    @Override
    public void initUI() {
        super.initUI();
        position = getIntent().getIntExtra("position", 0);
        if (position == 0) {
            //本地特产
            categoryOne = "本地特产";
        }
        if (position == 1) {
            categoryOne = "果品";
        }
        if (position == 2) {
            categoryOne = "蔬菜";
        }
        if (position == 3) {
            categoryOne = "水产";
        }
        if (position == 4) {
            categoryOne = "肉禽蛋";
        }
        if (position == 5) {
            categoryOne = "粮油";
        }
        if (position == 6) {
            categoryOne = "其它";
        }
        title.setText(categoryOne);
        initTabLayout();
        initListView();
        getData();
        tvDingwei.setText("选择区域");
    }

    private void getData() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("area", area);
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("categoryOne", categoryOne);
        map.put("sale", sale);
        addressRestUsage.getTables(1008611, map);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    List<ProductList> obj = (List<ProductList>) msg.getObj();
                    if (obj.size() > 0) {
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(obj);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showCenter(this, "没有更多数据了！");
                        }
                    }
                }
                break;
        }
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.tv_dingwei)
    public void onViewClickedq() {
        AddressPickTask task = new AddressPickTask(this);
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
}
