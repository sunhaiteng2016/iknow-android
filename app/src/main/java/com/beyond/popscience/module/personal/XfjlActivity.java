package com.beyond.popscience.module.personal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommonRestUsage;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.point.adapter.RecordListAdapterTwo;
import com.beyond.popscience.widget.GridSpacingItemDecoration;
import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 绿币转账
 * Created by YAO.CUI on 2017/6/12.
 */

public class XfjlActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {
    private final int TASK_HELP = 1901;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_record)
    RecyclerView lvRecord;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Request
    private CommonRestUsage mRestUsage;
    private Bitmap mBitmap;
    private int page = 1;
    private Zzjl zzjl;
    private List<Zzjl.DataBean> recordDetails = new ArrayList();
    private RecordListAdapterTwo adapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, XfjlActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("交易记录");
        lvRecord.setLayoutManager(new LinearLayoutManager(this));
        lvRecord.addItemDecoration(new GridSpacingItemDecoration(1,
                DensityUtil.dp2px(this, 1), false));
        adapter = new RecordListAdapterTwo(XfjlActivity.this);
        adapter.getDataList().addAll(recordDetails);
        lvRecord.setAdapter(adapter);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        requestRecordList();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_HELP:
                break;
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_xfjl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ib_back)
    public void onViewClicked() {
        finish();
    }
    /**
     * 请求兑换记录接口
     */
    private void requestRecordList() {
        // restUsage.recordList(TASK_RECORD_LIST, "1");
        String obj = " {\"page\": \"" + page + "\"}";
        HashMap<String, String> map = new HashMap<>();
        map.put("baseParams", UserInfoUtil.getInstance().getUserInfo().getToken());
        OkhttpUtil.okHttpPostJson(BeyondApplication.BaseUrl + "/user/jiaoyijilu", obj, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    zzjl = JSON.parseObject(response, Zzjl.class);
                    if (zzjl.getCode() == 0) {
                        if (page == 1) {
                            adapter.getDataList().clear();
                        }
                        adapter.getDataList().addAll(zzjl.getData());
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore();
        page++;
        requestRecordList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
        page = 1;
        requestRecordList();
    }
}
