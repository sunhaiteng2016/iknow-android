package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.beyond.popscience.locationgoods.bean.ProductListTwo;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
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

/**
 * 商品编辑
 */
public class GoodsEditActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tv_all_check)
    TextView tvAllCheck;
    private List<ProductListTwo> list = new ArrayList<>();
    private CommonAdapter<ProductListTwo> adapter;
    @Request
    AddressRestUsage addressRestUsage;
    private int shopId;
    private int page = 1, pageSize = 10;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_edit;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("商品管理");
        shopId = getIntent().getIntExtra("shopId", 0);
        initListView();
        getData();
    }

    private void getData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("pageSize", pageSize);
        addressRestUsage.getShopTables(1008611, map);
    }

    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(this, rlv);
        adapter = new CommonAdapter<ProductListTwo>(this, R.layout.iten_goods_edit, list) {

            @Override
            protected void convert(final ViewHolder holder, final ProductListTwo s, final int position) {

                if (s.getPublishStatus() == 1) {
                    holder.setText(R.id.tv_zt, "已上架").setTextColor(R.id.tv_zt, mContext.getResources().getColor(R.color.blue));
                    holder.setText(R.id.tv_sxj, "下架").setTextColor(R.id.tv_sxj, mContext.getResources().getColor(R.color.red));
                } else {
                    holder.setText(R.id.tv_zt, "已下架").setTextColor(R.id.tv_zt, mContext.getResources().getColor(R.color.red));
                    holder.setText(R.id.tv_sxj, "上架").setTextColor(R.id.tv_sxj, mContext.getResources().getColor(R.color.blue));
                }
                holder.setText(R.id.tv_name, s.getName())
                        .setText(R.id.tv_group_price, "拼团价：￥" + s.getSku().getGroupPrice())
                        .setText(R.id.tv_price, "原价：￥" + s.getSku().getPrice())
                        .setText(R.id.tv_sale, "已售" + s.getSku().getSale() + "单")
                        .setText(R.id.tv_create_time, s.getCreateTime())
                .setText(R.id.tv_pt_num,s.getGroupSize()+"人拼团");
                Glide.with(GoodsEditActivity.this).load(s.getPic()).into((ImageView) holder.getView(R.id.iv_img));
                holder.setOnClickListener(R.id.tv_sxj, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (s.getPublishStatus() == 1) {
                            addressRestUsage.producDown(1008622, s.getId() + "");
                        } else {
                            addressRestUsage.productOpen(1008622, s.getId() + "");
                        }
                    }
                });
                RatingStarView starbar = holder.getView(R.id.rsv_rating);
                starbar.setRating((float) s.getScore());
                if (s.isEdit()) {
                    holder.setVisible(R.id.tv_edit, true);
                } else {
                    holder.setVisible(R.id.tv_edit, false);
                }
                holder.setOnClickListener(R.id.tv_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (s.isCheck()) {
                            list.get(position).setCheck(false);
                            holder.setBackgroundRes(R.id.tv_edit, R.drawable.ic_pay_unselected);
                        } else {
                            list.get(position).setCheck(true);
                            holder.setBackgroundRes(R.id.tv_edit, R.drawable.ic_home_selected);
                        }
                    }
                });
                if (s.isCheck()) {
                    holder.setBackgroundRes(R.id.tv_edit, R.drawable.ic_home_selected);
                } else {
                    holder.setBackgroundRes(R.id.tv_edit, R.drawable.ic_pay_unselected);
                }
            }
        };
        rlv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(GoodsEditActivity.this, GoodsDatesActivity.class);
                intent.putExtra("Id", list.get(position).getId()+"");
                intent.putExtra("flag","edit");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
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
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                page = 1;
                getData();
            }
        });
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    List<ProductListTwo> obj = (List<ProductListTwo>) msg.getObj();
                    if (obj.size() > 0) {
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(obj);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                        } else {
                            ToastUtil.showCenter(this, "没有更多数据了！");
                        }
                    }
                }
                break;
            case 1008622:
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, msg.getMsg());
                    getData();
                } else {
                    ToastUtil.showCenter(this, msg.getMsg());
                }
                break;
            case 1008612:
                if (msg.getIsSuccess()) {
                    isCheck = false;
                    tvEdit.setText("编辑");
                    rlBottom.setVisibility(View.GONE);
                    page=1;
                    ToastUtil.showCenter(this, msg.getMsg());
                    getData();
                } else {
                    ToastUtil.showCenter(this, msg.getMsg());
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    public boolean isCheck = false;

    @OnClick(R.id.tv_edit)
    public void onViewClickedsss() {
        if (isCheck) {
            //变成未选择
            isCheck = false;
            tvEdit.setText("编辑");
            rlBottom.setVisibility(View.GONE);
        } else {
            isCheck = true;
            tvEdit.setText("取消");
            rlBottom.setVisibility(View.VISIBLE);
        }
        //设置编辑状态
        for (ProductListTwo bean : list) {
            bean.setEdit(isCheck);
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_delete)
    public void onViewClickedsssss() {
        List<String> idList = new ArrayList<>();
        for (ProductListTwo two : list) {
            if (two.isCheck()) {
                idList.add(two.getId() + "");
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("idList", idList);
        addressRestUsage.deleteList(1008612, map);
    }

    public boolean isAllCheck = false;

    @OnClick(R.id.tv_all_check)
    public void onViewClickedsssssss() {
        if (isAllCheck) {
            isAllCheck = false;
            tvAllCheck.setBackgroundResource(R.drawable.ic_pay_unselected);
            for (ProductListTwo two : list) {
                two.setCheck(false);
            }
        } else {
            isAllCheck = true;
            tvAllCheck.setBackgroundResource(R.drawable.ic_home_selected);
            for (ProductListTwo two : list) {
                two.setCheck(true);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
