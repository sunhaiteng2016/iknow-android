package com.beyond.popscience.locationgoods;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.FromDataRestUsage;
import com.beyond.popscience.locationgoods.bean.RegisterNumBean;
import com.beyond.popscience.locationgoods.shopcar.util.ToastUtil;
import com.beyond.popscience.utils.sun.util.CreatLayoutUtils;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的业绩
 */
public class TransactionQueryActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv_serch)
    TextView tvSerch;
    @Request
    private FromDataRestUsage commonRestUsage;
    private String startTime = "", endTime = "";
    private Date startDate;
    private int page = 1;
    List<RegisterNumBean.ListBean> lists = new ArrayList<>();
    private CommonAdapter<RegisterNumBean.ListBean> adapter;
    private int flag;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_transaction_query;
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008611:
                if (msg.getIsSuccess()) {
                    RegisterNumBean mDate = (RegisterNumBean) msg.getObj();
                    if (page == 1) {
                        lists.clear();
                    }
                    lists.addAll(mDate.getList());
                    adapter.notifyDataSetChanged();
                    tvTotal.setText("合计：" + mDate.getTotal());
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

    @OnClick({R.id.go_back, R.id.tv_start_time, R.id.tv_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                break;
            case R.id.tv_start_time:
                showPickerDialog();
                break;
            case R.id.tv_end_time:
                showPickerDialog1();
                break;
        }
    }

    private void showPickerDialog() {
        TimePickerView pvTime1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //Toast.makeText(ShopMainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                startDate = date;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                tvStartTime.setText(sdf.format(date));
                startTime = sdf.format(date);
            }
        }).build();
        //.setType(new boolean[]{true, true, true, true, true, true})
        pvTime1.show();
    }

    private void showPickerDialog1() {
        TimePickerView pvTime1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //Toast.makeText(ShopMainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                int compare = startDate.compareTo(date);
                if (compare == 1) {
                    ToastUtil.makeText(TransactionQueryActivity.this, "结束时间不能小于开始时间");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    tvEndTime.setText(sdf.format(date));
                    endTime = sdf.format(date);
                }
            }
        }).build();
        //.setType(new boolean[]{true, true, true, true, true, true})
        pvTime1.show();
    }

    @Override
    public void initUI() {
        super.initUI();

        flag = getIntent().getIntExtra("flag", 0);
        if (flag == 1) {
            title.setText("注册数");
        }
        if (flag == 2) {
            title.setText("自产自销");
            tv1.setText("店名（昵称）");
            tv2.setText("商品名称");
            tv3.setVisibility(View.GONE);
            tv4.setText("发布时间");
        }

        if (flag == 3) {
            title.setText("商品");
            tv1.setText("店名（昵称）");
            tv2.setText("用户电话");
            tv3.setVisibility(View.GONE);
            tv4.setText("发布时间");
        }
        if (flag == 4) {
            title.setText("房屋");
            tv1.setText("店名（昵称）");
            tv2.setText("用户电话");
            tv3.setText("供求");
            tv4.setText("发布时间");
        }
        if (flag == 5) {
            title.setText("招聘");
            tv1.setText("店名（昵称）");
            tv2.setText("用户电话");
            tv3.setText("招聘");
            tv4.setText("发布时间");
        }
        if (flag == 6) {
            title.setText("技能");
            tv1.setText("店名（昵称）");
            tv2.setText("用户电话");
            tv3.setVisibility(View.GONE);
            tv4.setText("发布时间");
        }
        initView();
    }

    private void initView() {
        CreatLayoutUtils.creatLinearLayout(this, rlv);
        adapter = new CommonAdapter<RegisterNumBean.ListBean>(this, R.layout.item_register_num, lists) {
            @Override
            protected void convert(ViewHolder holder, RegisterNumBean.ListBean registerNumBean, int position) {
                if (flag == 2) {
                    holder.setVisible(R.id.tv_register, false);
                    holder.setText(R.id.tv_nick_name, registerNumBean.getShopName()).setText(R.id.tv_phone, registerNumBean.getGoodsName()).setText(R.id.tv_register, registerNumBean.getAddress()).setText(R.id.tv_register_time, new SimpleDateFormat("yyyy-MM-dd").format(new Date(registerNumBean.getRegtime())));
                } else if (flag == 1) {
                    holder.setText(R.id.tv_nick_name, registerNumBean.getNickname()).setText(R.id.tv_phone, registerNumBean.getMobile()).setText(R.id.tv_register, registerNumBean.getAddress()).setText(R.id.tv_register_time, new SimpleDateFormat("yyyy-MM-dd").format(new Date(registerNumBean.getRegtime())));
                } else if (flag == 3) {
                    holder.setVisible(R.id.tv_register, false);
                    holder.setText(R.id.tv_nick_name, registerNumBean.getUsername()).setText(R.id.tv_phone, registerNumBean.getMobile()).setText(R.id.tv_register, registerNumBean.getAddress()).setText(R.id.tv_register_time, new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(registerNumBean.getCreatetime()))));
                } else if (flag == 4) {
                    holder.setText(R.id.tv_nick_name, registerNumBean.getRealname()).setText(R.id.tv_phone, registerNumBean.getMobile()).setText(R.id.tv_register, registerNumBean.getTrade().equals("1") ? "出租" : "出售").setText(R.id.tv_register_time, new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(registerNumBean.getCreatetime()))));
                } else if (flag == 5) {
                    holder.setText(R.id.tv_nick_name, registerNumBean.getRealname()).setText(R.id.tv_phone, registerNumBean.getMobile()).setText(R.id.tv_register, "招聘").setText(R.id.tv_register_time, new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(registerNumBean.getCreatetime()))));
                } else if (flag == 6) {
                    holder.setVisible(R.id.tv_register, false);
                    holder.setText(R.id.tv_nick_name, registerNumBean.getRealname()).setText(R.id.tv_phone, registerNumBean.getMobile()).setText(R.id.tv_register, registerNumBean.getAddress()).setText(R.id.tv_register_time, new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(registerNumBean.getCreatetime()))));
                }
            }
        };
        rlv.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getRegisterNumber();
                refreshLayout.finishRefresh();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getRegisterNumber();
                refreshLayout.finishLoadMore();
            }
        });
    }

    private void getRegisterNumber() {
        HashMap<String, String> map = new HashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("page", page + "");
        if (flag == 1) {
            commonRestUsage.getRegisterNumber(1008611, map);
        }
        if (flag == 2) {
            commonRestUsage.yqGoods(1008611, map);
        }
        if (flag == 3) {
            commonRestUsage.yqProduct(1008611, map);
        }
        if (flag == 4) {
            commonRestUsage.yqBuild(1008611, map);
        }
        if (flag == 5) {
            commonRestUsage.yqJob(1008611, map);
        }
        if (flag == 6) {
            commonRestUsage.yqSkill(1008611, map);
        }
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.tv_serch)
    public void onViewClickedsss() {
        if (TextUtils.isEmpty(startTime)) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请选择开始时间！");
            return;
        }
        if (TextUtils.isEmpty(endTime)) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请选择结束时间！");
            return;
        }
        getRegisterNumber();
    }
}
