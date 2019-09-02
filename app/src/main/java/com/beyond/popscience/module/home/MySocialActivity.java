package com.beyond.popscience.module.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
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
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.ArticleInfo;
import com.beyond.popscience.frame.pojo.ArticleResponse;
import com.beyond.popscience.module.home.adapter.SocialCircleContentListAdapter;
import com.beyond.popscience.module.social.SocialCommentDetailActivity;

import butterknife.BindView;

/**
 * 我的圈子
 * Created by linjinfa on 2017/11/5.
 * email 331710168@qq.com
 */
public class MySocialActivity extends BaseActivity {

    /**
     *
     */
    private final int REQUEST_GET_MY_ARTICLE_TASK_ID = 1001;
    /**
     *
     */
    private final int REQUEST_DELETE_ARTICLE_TASK_ID = 1002;
    /**
     *
     */
    private final int REQUEST_EDIT_ARTICLE_CODE = 101;

    private final String[] TABS = new String[]{
            "社团圈子", "乡镇圈子"
    };

    /**
     *
     * @param context
     */
    public static void startActivity(Context context){
        Intent intent = new Intent(context, MySocialActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.listView)
    protected PullToRefreshSwipeMenuListView listView;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;
    /**
     *
     */
    private int mCurrentPage = 1;
    /**
     *
     */
    private SocialCircleContentListAdapter socialCircleContentListAdapter;
    @Request
    private SocialRestUsage socialRestUsage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_EDIT_ARTICLE_CODE:
                if(resultCode == RESULT_OK){
                    listView.setTopRefreshing();
                }
                break;
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_my_social;
    }

    @Override
    public void initUI() {
        super.initUI();
        tv_title.setText("我的圈子");

        initList();
        initTabBar();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQUEST_GET_MY_ARTICLE_TASK_ID:    //我发布的社团(乡镇社团)帖子
                if(msg.getIsSuccess()){
                    ArticleResponse articleResponse = (ArticleResponse) msg.getObj();
                    if(articleResponse!=null){
                        if(listView.isPullDownToRefresh()){
                            socialCircleContentListAdapter.getDataList().clear();
                            if(articleResponse.getArticleList()!=null && articleResponse.getArticleList().size()!=0){
                                socialCircleContentListAdapter.getDataList().addAll(articleResponse.getArticleList());
                            }
                            socialCircleContentListAdapter.notifyDataSetChanged();
                        }else{
                            if(articleResponse.getArticleList()!=null && articleResponse.getArticleList().size()!=0){
                                socialCircleContentListAdapter.getDataList().addAll(articleResponse.getArticleList());
                                socialCircleContentListAdapter.notifyDataSetChanged();
                            }
                        }
                        mCurrentPage++;
                    }
                }
                listView.onRefreshComplete();
                break;
            case REQUEST_DELETE_ARTICLE_TASK_ID:    //删除社团帖子(含乡镇社团)
                if(msg.getIsSuccess()){
                    ArticleInfo articleInfo = (ArticleInfo) msg.getTargetObj();
                    if(articleInfo!=null){
                        socialCircleContentListAdapter.getDataList().remove(articleInfo);
                        socialCircleContentListAdapter.notifyDataSetChanged();
                    }
                }
                dismissProgressDialog();
                break;
        }
    }

    private void initList(){
//        listView.getRefreshableView().setMenuCreator(new SwipeMenuCreatorImpl(getApplicationContext()));

        socialCircleContentListAdapter = new SocialCircleContentListAdapter(this);
        listView.setAdapter(socialCircleContentListAdapter);

        listView.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getRefreshableView().setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    ArticleInfo articleInfo = socialCircleContentListAdapter.getItem(position);
                    requestDeleteArticle(articleInfo);
                    return true;
                }
                return false;
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                mCurrentPage = 1;
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                requestData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getRefreshableView().getHeaderViewsCount();
                ArticleInfo articleInfo = socialCircleContentListAdapter.getItem(position);
                if(articleInfo!=null){
                    SocialCommentDetailActivity.startActivityForResult(MySocialActivity.this, articleInfo, 1);
//                    PublishedActivity.startActivityEditForResult(MySocialActivity.this, REQUEST_EDIT_ARTICLE_CODE, articleInfo);
                }
            }
        });
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
                listView.setTopRefreshing();
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

    /**
     * 请求数据
     */
    private void requestData(){
        int type = 1;
        switch (tabLayout.getSelectedTabPosition()){
            case 0: //社团
                type = 1;
                break;
            case 1: //乡镇
                type = 2;
                break;
        }
        socialRestUsage.getMyArticle(REQUEST_GET_MY_ARTICLE_TASK_ID, type, mCurrentPage);
    }

    /**
     * 删除社团帖子(含乡镇社团)
     * @param articleInfo
     */
    public void requestDeleteArticle(ArticleInfo articleInfo){
        if(articleInfo!=null){
            showProgressDialog();
            socialRestUsage.deleteArticle(REQUEST_DELETE_ARTICLE_TASK_ID, articleInfo.getArticleId(), articleInfo);
        }
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
