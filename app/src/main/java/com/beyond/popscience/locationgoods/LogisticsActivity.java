package com.beyond.popscience.locationgoods;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.bean.WlBean;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.google.gson.Gson;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 物流信息
 */
public class LogisticsActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    private CommonAdapter<WlBean.ResultBean.ListBean> adapter;
    private List<WlBean.ResultBean.ListBean> list = new ArrayList<>();
    private String kddh;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_logistics;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("物流信息");
        kddh=getIntent().getStringExtra("kddh");
        initListView();
        getData();
    }

    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(this, rlv);
        adapter = new CommonAdapter<WlBean.ResultBean.ListBean>(this, R.layout.item_wl, list) {

            @Override
            protected void convert(ViewHolder holder, WlBean.ResultBean.ListBean mybean, int position) {
                holder.setText(R.id.tv_people, mybean.getStatus()).setText(R.id.tv_time, mybean.getTime());

                if (position == 0) {
                    holder.getView(R.id.view_up).setVisibility(View.INVISIBLE);
                    holder.setBackgroundRes(R.id.iv_circle, R.drawable.bg_circle_red_full);
                    holder.setTextColor(R.id.tv_people, mContext.getResources().getColor(R.color.red)).setTextColor(R.id.tv_time,  mContext.getResources().getColor(R.color.red));
                } else {
                    holder.getView(R.id.view_up).setVisibility(View.VISIBLE);
                    holder.setBackgroundRes(R.id.iv_circle, R.drawable.selector_circle1111);
                    holder.setTextColor(R.id.tv_people,  mContext.getResources().getColor(R.color.text153)).setTextColor(R.id.tv_time, mContext.getResources().getColor(R.color.text153));
                }
            }
        };
        rlv.setAdapter(adapter);
    }

    private void getData() {
        Map<String, String> headmap = new HashMap<>();
        headmap.put("Authorization", "APPCODE c0d583c68e04427db67ed746e1d7924f");
        OkhttpUtil.okHttpGet("https://wuliu.market.alicloudapi.com/kdi?no="+kddh, new HashMap<String, String>(), headmap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                ToastUtil.showCenter(LogisticsActivity.this, "网络不稳定，请稍后再试！");
            }

            @Override
            public void onResponse(String response) {
                if (isFinishing()) {
                    return;
                }
                WlBean bean = new Gson().fromJson(response, WlBean.class);
                if (bean.getStatus().equals("0")) {
                    List<WlBean.ResultBean.ListBean> listss = bean.getResult().getList();
                    //设置数据源
                    list.clear();
                    list.addAll(listss);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
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

}
