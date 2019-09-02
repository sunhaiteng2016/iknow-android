package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.ArticleRemind;
import com.beyond.popscience.frame.pojo.ArticleRemindResponse;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.social.adapter.SocialMessageNoticeAdapter;

import butterknife.BindView;

/**
 * 消息通知
 * Created by linjinfa on 2017/6/24.
 * email 331710168@qq.com
 */
public class SocialMessageNoticeActivity extends BaseActivity {

    /**
     *
     */
    private final String KEY_COMMUNITY_DETAIL_LAST_TIMESTAMP = "communityDetail_%s_%s";
    /**
     * 消息提醒列表
     */
    private final int REQUEST_GET_REMIND_TASK_ID = 1001;
    private final static String EXTRA_SOCIAL_INFO_KEY = "socialInfo";
    /**
     *
     */
    private final static String SP_LAST_TIME_STAMP_KEY = "socialMessageNoticeLastTimeStamp";

    @BindView(R.id.tv_title)
    protected TextView titleTxtView;

    @BindView(R.id.listView)
    protected PullToRefreshListView listView;
    @Request
    private SocialRestUsage socialRestUsage;
    private SocialMessageNoticeAdapter socialMessageNoticeAdapter;
    private SocialInfo socialInfo;

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, SocialInfo socialInfo){
        Intent intent = new Intent(context, SocialMessageNoticeActivity.class);
        intent.putExtra(EXTRA_SOCIAL_INFO_KEY, socialInfo);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_social_message_notice;
    }

    @Override
    public void initUI() {
        super.initUI();

        socialInfo = (SocialInfo) getIntent().getSerializableExtra(EXTRA_SOCIAL_INFO_KEY);
        if(socialInfo == null){
            backNoAnim();
            return ;
        }

        titleTxtView.setText("消息");
        initListView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQUEST_GET_REMIND_TASK_ID:    //消息提醒列表
                if(msg.getIsSuccess()){
                    ArticleRemindResponse articleRemindResponse = (ArticleRemindResponse) msg.getObj();
                    if(articleRemindResponse!=null && articleRemindResponse.getRemindList()!=null && articleRemindResponse.getRemindList().size()!=0){
                        socialMessageNoticeAdapter.getDataList().clear();
                        socialMessageNoticeAdapter.getDataList().addAll(articleRemindResponse.getRemindList());
                        socialMessageNoticeAdapter.notifyDataSetChanged();

//                        if(socialMessageNoticeAdapter.getCount()>0){
//                            ArticleRemind articleRemind = socialMessageNoticeAdapter.getItem(0);
//                            if(articleRemind!=null && !TextUtils.isEmpty(articleRemind.getPublishTime())){
//                                Date date = DateUtil.toDateTime(articleRemind.getPublishTime());
//                                if(date!=null){
//                                    SPUtils.put(this, SP_LAST_TIME_STAMP_KEY, date.getTime());
//                                }
//                            }
//                        }
                    }
                }
                listView.onRefreshComplete();

                String currTimeStamp = BeyondApplication.getInstance().getCurrSystemTimeStampStr();
                if(!TextUtils.isEmpty(currTimeStamp)){
                    SPUtils.put(this, getCommunityDetailLastTimeStampKey(), BeyondApplication.getInstance().getCurrSystemTimeStampStr());
                }
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView(){
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestGetMyRemind();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getRefreshableView().getHeaderViewsCount();
                ArticleRemind articleRemind = socialMessageNoticeAdapter.getItem(position);
                if(articleRemind!=null){
                    SocialCommentDetailActivity.startActivityForResult(SocialMessageNoticeActivity.this, articleRemind.getArticleId(), 100);
                }
            }
        });

        socialMessageNoticeAdapter = new SocialMessageNoticeAdapter(this);
        listView.getRefreshableView().setAdapter(socialMessageNoticeAdapter);
        listView.setTopRefreshing();
    }

    /**
     *
     * @return
     */
    private String getCommunityDetailLastTimeStampKey(){
        String userId = UserInfoUtil.getInstance().getUserInfo().getUserId();
        return String.format(KEY_COMMUNITY_DETAIL_LAST_TIMESTAMP, userId == null ? "" : userId, socialInfo.getCommunityId() == null ? "" : socialInfo.getCommunityId());
    }

    /**
     * 消息提醒列表
     */
    private void requestGetMyRemind(){
        String timeStamp = (String) SPUtils.get(this, getCommunityDetailLastTimeStampKey(), "");
        socialRestUsage.getMyRemind(REQUEST_GET_REMIND_TASK_ID, socialInfo.getCommunityId(), timeStamp);
    }

}
