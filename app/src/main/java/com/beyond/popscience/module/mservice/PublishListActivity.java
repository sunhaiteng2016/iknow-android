package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshSwipeMenuListView;
import com.beyond.library.view.swipemenulistview.SwipeMenu;
import com.beyond.library.view.swipemenulistview.SwipeMenuCreator;
import com.beyond.library.view.swipemenulistview.SwipeMenuItem;
import com.beyond.library.view.swipemenulistview.SwipeMenuListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.frame.pojo.JobDetail;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.module.building.BuildingDetailActivity;
import com.beyond.popscience.module.building.RentDetailActivity;
import com.beyond.popscience.module.job.JobApplyDetailActivity;
import com.beyond.popscience.module.job.JobProvideDetailActivity;
import com.beyond.popscience.module.mservice.adapter.BuildingListV2Adapter;
import com.beyond.popscience.module.mservice.adapter.GoodsAdapter;
import com.beyond.popscience.module.mservice.adapter.JobListV2Adapter;

import butterknife.BindView;

/**
 * Created by lenovo on 2017/6/30.
 */

public class PublishListActivity extends BaseActivity {

    private final String[] TABS = new String[]{
            "商品", "技能", "任务",
            "出租\n出售", "求租\n求购", "求职", "招聘"
    };

    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ID = "user_id";

    private static final int TASK_GET_GOODS = 1801;
    /**
     * 删除
     */
    private static final int REQUEST_DELETE_TASK_ID = 1802;

    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.lvGoods)
    protected PullToRefreshSwipeMenuListView lvGoods;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;

    /**
     *
     */
    private GoodsAdapter mGoodsAdapter;
    /**
     *
     */
    private BuildingListV2Adapter buildingListV2Adapter;
    /**
     *
     */
    private JobListV2Adapter jobListV2Adapter;
    /**
     *
     */
    private CustomBaseAdapter currAdapter;

    private int mCurrentPage = 1;
    private boolean isRefresh = true;
    private String userId;
    private String userName;

    private ServiceGoodsList mGoodsList;

    @Request
    private ServiceRestUsage mRestUsage;

    public static void startActivity(Context context,String userName,String userId){
        Intent intent =new Intent(context,PublishListActivity.class);
        intent.putExtra(KEY_USER_NAME,userName);
        intent.putExtra(KEY_USER_ID,userId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_list;
    }

    @Override
    public void initUI() {
        super.initUI();
        userId = getIntent().getStringExtra(KEY_USER_ID);
        userName = getIntent().getStringExtra(KEY_USER_NAME);
        if (TextUtils.isEmpty(userName)){
            tv_title.setText("我的发布");
            tabLayout.setVisibility(View.VISIBLE);
        } else{
            tv_title.setText(userName+"的发布");
            tabLayout.setVisibility(View.GONE);
        }

        initList();
        initTabBar();
    }

    /**
     * 初始化TabLayout
     */
    private void initTabBar(){
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ActivityCompat.getDrawable(this, R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(20);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchListData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for(int i=0;i<TABS.length;i++){
            tabLayout.addTab(tabLayout.newTab().setText(TABS[i]), i == 0);
        }

    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (taskId == TASK_GET_GOODS){
            if(msg.getIsSuccess()){
                mGoodsList = (ServiceGoodsList) msg.getObj();
                if (mGoodsList!= null){
                    if (isRefresh){
                        currAdapter.getDataList().clear();
                        mCurrentPage = 2;
                    } else {
                        mCurrentPage ++;
                    }

                    refreshAdapter();
                }
            }
            lvGoods.onRefreshComplete();
        }
        switch (taskId){
            case REQUEST_DELETE_TASK_ID:    //删除
                if(msg.getIsSuccess()){
                    Object itemObj = msg.getTargetObj();
                    currAdapter.getDataList().remove(itemObj);
                    currAdapter.notifyDataSetChanged();
                }
                dismissProgressDialog();
                break;
        }
    }
    private void initList(){
        lvGoods.getRefreshableView().setEnableSwipeMenu(TextUtils.isEmpty(userName));
        lvGoods.getRefreshableView().setMenuCreator(new SwipeMenuCreatorImpl(getApplicationContext()));

        mGoodsAdapter = new GoodsAdapter(this);
        lvGoods.setAdapter(mGoodsAdapter);
        lvGoods.setMode(PullToRefreshBase.Mode.BOTH);
        lvGoods.getRefreshableView().setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    Object itemObj = currAdapter.getItem(position);
                    requestDel(itemObj);
                    return true;
                }
                return false;
            }
        });

        lvGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                mCurrentPage = 1;
                isRefresh = true;
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                isRefresh = false;
                requestData();
            }
        });

        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - lvGoods.getRefreshableView().getHeaderViewsCount();
                Object itemObj = currAdapter.getItem(position);
                itemClick(itemObj);
            }
        });
    }

    /**
     * 切换列表数据
     */
    private void switchListData(){
        switch (tabLayout.getSelectedTabPosition()){
            case 0: //商品
            case 1: //技能
            case 2: //任务
                if(mGoodsAdapter == null){
                    mGoodsAdapter = new GoodsAdapter(this);
                }
                currAdapter = mGoodsAdapter;
                break;
            case 3: //出租出售
                if(buildingListV2Adapter == null){
                    buildingListV2Adapter = new BuildingListV2Adapter(this);
                }
                buildingListV2Adapter.setType(1);
                currAdapter = buildingListV2Adapter;
                break;
            case 4: //求租求购
                if(buildingListV2Adapter == null){
                    buildingListV2Adapter = new BuildingListV2Adapter(this);
                }
                buildingListV2Adapter.setType(2);
                currAdapter = buildingListV2Adapter;
                break;
            case 5: //求职
                if(jobListV2Adapter == null){
                    jobListV2Adapter = new JobListV2Adapter(this);
                }
                jobListV2Adapter.setType(2);
                currAdapter = jobListV2Adapter;
                break;
            case 6: //招聘
                if(jobListV2Adapter == null){
                    jobListV2Adapter = new JobListV2Adapter(this);
                }
                jobListV2Adapter.setType(1);
                currAdapter = jobListV2Adapter;
                break;
        }
        currAdapter.getDataList().clear();
        currAdapter.notifyDataSetChanged();
        lvGoods.setAdapter(currAdapter);

        lvGoods.setTopRefreshing();
    }

    /**
     * 更新adapter数据
     */
    private void refreshAdapter(){
        switch (tabLayout.getSelectedTabPosition()){
            case 0: //商品
                if(mGoodsList.getProductList()!=null){
                    currAdapter.getDataList().addAll(mGoodsList.getProductList());
                }
                break;
            case 1: //技能
                if(mGoodsList.getSkillList()!=null){
                    currAdapter.getDataList().addAll(mGoodsList.getSkillList());
                }
                break;
            case 2: //任务
                if(mGoodsList.getTaskList()!=null){
                    currAdapter.getDataList().addAll(mGoodsList.getTaskList());
                }
                break;
            case 3: //出租出售
                if(mGoodsList.getBuildingList()!=null){
                    currAdapter.getDataList().addAll(mGoodsList.getBuildingList());
                }
                break;
            case 4: //求租求购
                if(mGoodsList.getRentList()!=null){
                    currAdapter.getDataList().addAll(mGoodsList.getRentList());
                }
                break;
            case 5: //求职
                if(mGoodsList.getJobApplyList()!=null){
                    currAdapter.getDataList().addAll(mGoodsList.getJobApplyList());
                }
                break;
            case 6: //招聘
                if(mGoodsList.getJobList()!=null){
                    currAdapter.getDataList().addAll(mGoodsList.getJobList());
                }
                break;
        }
        currAdapter.notifyDataSetChanged();
    }

    /**
     * itemclick
     */
    private void itemClick(Object itemObj){
        if(itemObj!=null){
            ServiceGoodsItem serviceGoodsItem = null;
            BuildingDetail buildingDetail = null;
            JobDetail jobDetail = null;
            if(itemObj instanceof ServiceGoodsItem){
                serviceGoodsItem = (ServiceGoodsItem) itemObj;
            }
            if(itemObj instanceof BuildingDetail){
                buildingDetail = (BuildingDetail) itemObj;
            }
            if(itemObj instanceof JobDetail){
                jobDetail = (JobDetail) itemObj;
            }

            switch (tabLayout.getSelectedTabPosition()){
                case 0: //商品
                  GoodsDetailV2Activity.startActivityGoods(this, serviceGoodsItem);
                    //GoodsDetailV2Activity2.startActivityGoods(this, serviceGoodsItem);
                    break;
                case 1: //技能
                    if(serviceGoodsItem!=null){
                        serviceGoodsItem.setAppGoodsType("1");
                        serviceGoodsItem.setProductId(serviceGoodsItem.getUid());
                        GoodsDetailV2Activity.startActivity(this, serviceGoodsItem);
                     //   GoodsDetailV2Activity2.startActivity(this, serviceGoodsItem);
                    }
                    break;
                case 2: //任务
                    if(serviceGoodsItem!=null){
                        serviceGoodsItem.setAppGoodsType("2");
                        serviceGoodsItem.setProductId(serviceGoodsItem.getUid());
                       GoodsDetailV2Activity.startActivity(this, serviceGoodsItem);
                      //  GoodsDetailV2Activity2.startActivity(this, serviceGoodsItem);
                    }
                    break;
                case 3: //出租出售
                    if(buildingDetail!=null){
                        BuildingDetailActivity.startActivity(this, buildingDetail.uid);
                    }
                    break;
                case 4: //求租求购
                    if(buildingDetail!=null){
                        RentDetailActivity.startActivity(this, buildingDetail.rentId);
                    }
                    break;
                case 5: //求职
                    if(jobDetail!=null){
                        JobApplyDetailActivity.startActivty(this, jobDetail.applyId,"1");
                    }
                    break;
                case 6: //招聘
                    if(jobDetail!=null){
                        JobProvideDetailActivity.startActivity(this, jobDetail.jobId,"1");
                    }
                    break;
            }
        }
    }

    /**
     * 请求数据
     */
    private void requestData(){
        switch (tabLayout.getSelectedTabPosition()){
            case 0: //商品
                getGoods();
                break;
            case 1: //技能
                getMySkill();
                break;
            case 2: //任务
                getMyTask();
                break;
            case 3: //出租出售
                getMyBuilding();
                break;
            case 4: //求租求购
                getMyRent();
                break;
            case 5: //求职
                getApplyList();
                break;
            case 6: //招聘
                getMyJobList();
                break;
        }
    }

    /**
     * 删除
     */
    private void requestDel(Object itemObj){
        showProgressDialog();
        String type = null;
        String typeId = null;
        ServiceGoodsItem serviceGoodsItem = null;
        BuildingDetail buildingDetail = null;
        JobDetail jobDetail = null;
        if(itemObj instanceof ServiceGoodsItem){
            serviceGoodsItem = (ServiceGoodsItem) itemObj;
        }
        if(itemObj instanceof BuildingDetail){
            buildingDetail = (BuildingDetail) itemObj;
        }
        if(itemObj instanceof JobDetail){
            jobDetail = (JobDetail) itemObj;
        }
        switch (tabLayout.getSelectedTabPosition()){
            case 0: //商品
                type = "5";

                typeId = serviceGoodsItem != null ? serviceGoodsItem.getProductId() : null;
                break;
            case 1: //技能
                type = "1";
                typeId = serviceGoodsItem != null ? serviceGoodsItem.getUid() : null;
                break;
            case 2: //任务
                type = "2";
                typeId = serviceGoodsItem != null ? serviceGoodsItem.getUid() : null;
                break;
            case 3: //出租出售
                type = "3";
                typeId = buildingDetail != null ? buildingDetail.uid : null;
                break;
            case 4: //求租求购
                type = "4";
                typeId = buildingDetail != null ? buildingDetail.uid : null;
                break;
            case 5: //求职
                type = "7";
                typeId = jobDetail != null ? jobDetail.applyId : null;
                break;
            case 6: //招聘
                type = "6";
                typeId = jobDetail != null ? jobDetail.jobId : null;
                break;
        }
        mRestUsage.deltete(REQUEST_DELETE_TASK_ID, type, typeId, itemObj);
    }

    /**
     * 获取商品列表
     */
    private void getGoods(){
        mRestUsage.getProductByUser(TASK_GET_GOODS,mCurrentPage,userId);
    }

    /**
     * 获取技能
     */
    private void getMySkill(){
        mRestUsage.getMySkill(TASK_GET_GOODS, mCurrentPage);
    }

    /**
     * 获取任务
     */
    private void getMyTask(){
        mRestUsage.getMyTask(TASK_GET_GOODS, mCurrentPage);
    }

    /**
     * 我发布的出租出售
     */
    private void getMyBuilding(){
        mRestUsage.getMyBuilding(TASK_GET_GOODS, mCurrentPage);
    }

    /**
     * 我发布的求租求购
     */
    private void getMyRent(){
        mRestUsage.getMyRent(TASK_GET_GOODS, mCurrentPage);
    }

    /**
     * 我发布的求职
     */
    private void getApplyList(){
        mRestUsage.getMyApplyList(TASK_GET_GOODS, mCurrentPage);
    }

    /**
     * 我发布的招聘
     */
    private void getMyJobList(){
        mRestUsage.getMyJobList(TASK_GET_GOODS, mCurrentPage);
    }

    /**
     * 横向滑动菜单
     */
    class SwipeMenuCreatorImpl implements SwipeMenuCreator {

        private Context context;

        public SwipeMenuCreatorImpl(Context context) {
            this.context = context;
        }

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(context);
            deleteItem.setBackground(new ColorDrawable(Color.RED));
            deleteItem.setIcon(R.drawable.xx_icon_12);
            deleteItem.setWidth(DensityUtil.dp2px(context, 90));
            menu.addMenuItem(deleteItem);
        }

    }
}
