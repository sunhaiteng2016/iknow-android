package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.pojo.MoreService;
import com.beyond.popscience.frame.pojo.MoreSeviceResponse;
import com.beyond.popscience.module.home.adapter.MoreServiceCategoryAdapter;
import com.beyond.popscience.widget.GridSpacingItemDecoration;

import butterknife.BindView;


/**
 * "服务"模块中的页面"更多"
 * Created by zhouhuakang on 2017/9/12.
 */

public class MoreServiceActivity extends BaseActivity {

    private final int TASK_GET_MORE_SERVICE = 1408;//获取更多服务
    private static final String KEY_TITLE = "title";

    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    /**
     *
     */
    @BindView(R.id.serviceLinLay)
    protected LinearLayout serviceLinLay;

    @Request
    private ServiceRestUsage mRestUsage;
    private MoreSeviceResponse moreSeviceResponse;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_more;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText(getIntent().getStringExtra(KEY_TITLE));
        getMoreService();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_GET_MORE_SERVICE:
                if (msg.getIsSuccess()) {

                    moreSeviceResponse = (MoreSeviceResponse) msg.getObj();
                    if (moreSeviceResponse != null) {
                        addServiceView();
                    }
                }
                break;

        }
    }

    /**
     * 添加服务相关view
     */
    private void addServiceView(){
        serviceLinLay.removeAllViews();
        if(moreSeviceResponse!=null && moreSeviceResponse.getList()!=null && moreSeviceResponse.getList().size()!=0){
            for(MoreService moreService : moreSeviceResponse.getList()){
                View view = View.inflate(this, R.layout.adapter_more_item, null);
                serviceLinLay.addView(view);

                TextView serviceGroupNameTxtView = (TextView) view.findViewById(R.id.serviceGroupNameTxtView);
                RecyclerView serviceOneRecyclerView = (RecyclerView) view.findViewById(R.id.serviceOneRecyclerView);

                serviceGroupNameTxtView.setText(moreService.getServiceName());

                if(moreService.getMoreServiceContentList()!=null && moreService.getMoreServiceContentList().size()!=0){
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
                    serviceOneRecyclerView.setLayoutManager(gridLayoutManager);
                    serviceOneRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 10, false));
                    MoreServiceCategoryAdapter mCategoryAdapter = new MoreServiceCategoryAdapter(this);
                    mCategoryAdapter.getDataList().addAll(moreService.getMoreServiceContentList());
                    serviceOneRecyclerView.setAdapter(mCategoryAdapter);
                }
            }
        }
    }

    public static void startActivity(Context context, String title) {
        Intent intent = new Intent(context, MoreServiceActivity.class);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }

    /**
     * 获取更多服务
     */
    private void getMoreService() {
        mRestUsage.getMoreService(TASK_GET_MORE_SERVICE);
    }
}
