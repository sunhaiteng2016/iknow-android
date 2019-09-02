package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.pojo.SocialResponse;
import com.beyond.popscience.module.social.adapter.SocialSearchListAdapter;

import butterknife.BindView;

/**
 * 圈子搜索结果
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialCircleSearchResultActivity extends BaseActivity {

    /**
     *
     */
    private final int REQUEST_CODE_SOCIAL_DETAIL = 101;
    /**
     * 所有社区
     */
    private final int REQUEST_ALL_COMMUNITY_TASK_ID = 1001;
    /**
     * 申请加入普通社区
     */
    private final int REQUEST_JOIN_COMMUNITY_TASK_ID = 1002;
    /**
     * 搜索
     */
    private final int REQUEST_SEARCH_COMMUNITY_TASK_ID = 1003;
    /**
     *
     */
    private final static String EXTRA_SEARCH_TYPE_KEY = "searchType";
    private final static String EXTRA_KEY_WORD_KEY = "keyWord";
    /**
     * 默认 关键字搜索
     */
    private final int TYPE_DEFAULT = 0;
    /**
     * 搜索所有圈子
     */
    private final static int TYPE_ALL_SOCIAL_CIRCLE = 1;
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    @BindView(R.id.listView)
    protected PullToRefreshListView listView;
    private SocialSearchListAdapter socialListAdapter;
    /**
     *
     */
    private int searchType = TYPE_DEFAULT;
    private String keyWord;
    private int page = 1;
    @Request
    private SocialRestUsage socialRestUsage;

    /**
     * 所有圈子
     * @param context
     */
    public static void startAllSocialCircleActivity(Context context){
        Intent intent = new Intent(context, SocialCircleSearchResultActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_ALL_SOCIAL_CIRCLE);
        context.startActivity(intent);
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, String keyWord){
        Intent intent = new Intent(context, SocialCircleSearchResultActivity.class);
        intent.putExtra(EXTRA_KEY_WORD_KEY, keyWord);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SOCIAL_DETAIL:
                if(resultCode == RESULT_OK){
                    listView.setTopRefreshing();
                }
                break;
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_all_social_circle;
    }

    @Override
    public void initUI() {
        super.initUI();

        searchType = getIntent().getIntExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_DEFAULT);
        keyWord = getIntent().getStringExtra(EXTRA_KEY_WORD_KEY);

        titleTxtView.setText(isSearchAllSocialCircle()?"所有社团":"搜索结果");

        initListView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQUEST_ALL_COMMUNITY_TASK_ID: //所有社区
            case REQUEST_SEARCH_COMMUNITY_TASK_ID: //搜索
                SocialResponse socialResponse = null;
                if(msg.getIsSuccess()){
                    socialResponse = (SocialResponse) msg.getObj();
                    if(socialResponse!=null){
                        if(socialResponse.getCommunityList()!=null && socialResponse.getCommunityList().size()!=0){
                            if(listView.isPullDownToRefresh()){
                                socialListAdapter.getDataList().clear();
                            }
                            socialListAdapter.getDataList().addAll(socialResponse.getCommunityList());
                            socialListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if(socialResponse == null || socialResponse.getCommunityList() == null || socialResponse.getCommunityList().size()==0 || socialListAdapter.getCount() >= socialResponse.getTotalcount()){   //加载结束
                    listView.onLoadMoreCompleteAndNoData();
                }else{
                    listView.onLoadMoreComplete();
                }
                break;
            case REQUEST_JOIN_COMMUNITY_TASK_ID:    //申请加入普通社区
                if(msg.getIsSuccess()){
                    SocialInfo socialInfo = (SocialInfo) msg.getTargetObj();
                    if(socialInfo!=null){
                        socialInfo.setState(2); //0:未加入 1:申请中 2：已加入
                        socialListAdapter.notifyDataSetChanged();
                    }
                    ToastUtil.showCenter(this, "加入成功");
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 是否搜索全部圈子
     * @return
     */
    private boolean isSearchAllSocialCircle(){
        return searchType == TYPE_ALL_SOCIAL_CIRCLE;
    }

    /**
     * 初始化listview
     */
    private void initListView(){
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                requestData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getRefreshableView().getHeaderViewsCount();
                SocialInfo socialInfo = socialListAdapter.getItem(position);
                if(socialInfo!=null){
                    if(socialInfo.getState() == 2){ //0:未加入 1:申请中 2：已加入
                        SocialCircleContentV2Activity.startActivity(SocialCircleSearchResultActivity.this, socialInfo);
                    }else{
                        SocialDetailActivity.startActivityForResult(SocialCircleSearchResultActivity.this, REQUEST_CODE_SOCIAL_DETAIL, socialInfo);
                    }
                }
            }
        });

        socialListAdapter = new SocialSearchListAdapter(this);
        listView.setAdapter(socialListAdapter);
        listView.setTopRefreshing();
    }

    /**
     * 请求
     */
    private void requestData(){
        if(isSearchAllSocialCircle()){  //所有社团
            requestAllCommunity();
        }else{
            requestSearch();
        }
    }

    /**
     * 所有社区
     */
    private void requestAllCommunity(){
        socialRestUsage.getAllCommunity(REQUEST_ALL_COMMUNITY_TASK_ID, page);
    }

    /**
     * 搜索
     */
    private void requestSearch(){
        socialRestUsage.search(REQUEST_ALL_COMMUNITY_TASK_ID, page, keyWord);
    }

    /**
     * 申请加入普通社区
     * @param socialInfo
     */
    private void requestJoinCommunity(SocialInfo socialInfo){
        showProgressDialog();
        socialRestUsage.joinCommunity(REQUEST_JOIN_COMMUNITY_TASK_ID, socialInfo.getCommunityId(), socialInfo);
    }

    /**
     * 申请
     */
    public void apply(SocialInfo socialInfo){
        if(socialInfo.isNormal()){  //普通社区
            requestJoinCommunity(socialInfo);
        }else{  //正规社团
            PostAuditActivity.startActivity(this, socialInfo.getCommunityId());
        }
    }

}
