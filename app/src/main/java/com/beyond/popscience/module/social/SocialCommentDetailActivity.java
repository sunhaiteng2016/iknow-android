package com.beyond.popscience.module.social;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
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
import com.beyond.popscience.frame.pojo.ArticleInfo;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.frame.view.PublishedCommentView;
import com.beyond.popscience.module.social.adapter.SocialCircleContentListRecyclerAdapter;
import com.beyond.popscience.module.social.adapter.SocialCommentListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评论详情
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class SocialCommentDetailActivity extends BaseActivity {

    /**
     * 帖子详情
     */
    private final int REQUEST_ARTICLE_DETAIL_TASK_ID = 1001;
    /**
     * 发表帖子评论
     */
    private final int REQUEST_ARTICLE_COMMENT_TASK_ID = 1002;
    /**
     * 帖子评论回复
     */
    private final int REQUEST_SENT_ARTICLE_REPLY_TASK_ID = 1003;
    /**
     *
     */
    public final static String EXTRA_ARTICLE_INFO_KEY = "articleInfo";

    /**
     *
     */
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    /**
     *
     */
    @BindView(R.id.rightImgView)
    protected ImageView rightImgView;
    /**
     *
     */
    @BindView(R.id.commentTxtView)
    protected TextView commentTxtView;
    /**
     *
     */
    @BindView(R.id.publishedCommentView)
    protected PublishedCommentView publishedCommentView;
    /**
     *
     */
    @BindView(R.id.listView)
    protected PullToRefreshListView listView;
    /**
     *
     */
    private SocialCommentListAdapter commentListAdapter;
    /**
     *
     */
    private SocialCircleContentListRecyclerAdapter socialCircleContentListRecyclerAdapter;

    /**
     *
     */
    private ArticleInfo articleInfo;
    private int page;
    /**
     * 回复的Comment
     */
    private Comment replyComment;
    @Request
    private SocialRestUsage socialRestUsage;
    /**
     *
     */
    private RecyclerView.ViewHolder headerViewHolder;
    private RecyclerView.AdapterDataObserver headerAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            socialCircleContentListRecyclerAdapter.onBindViewHolder(headerViewHolder, 0);
        }
    };

    /**
     *
     */
    public static void startActivityForResult(Activity context, ArticleInfo articleInfo, int requestCode){
        Intent intent = new Intent(context, SocialCommentDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE_INFO_KEY, articleInfo);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     *
     */
    public static void startActivityForResult(Activity context, String articleId, int requestCode){
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setArticleId(articleId);

        Intent intent = new Intent(context, SocialCommentDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE_INFO_KEY, articleInfo);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socialCircleContentListRecyclerAdapter.unregisterAdapterDataObserver(headerAdapterDataObserver);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_social_comment_detail;
    }

    @Override
    public void initUI() {

//        ToastUtil.showCenter(SocialCommentDetailActivity.this,"恭喜您, + 1 科普绿币!");
        articleInfo = (ArticleInfo) getIntent().getSerializableExtra(EXTRA_ARTICLE_INFO_KEY);
        if(articleInfo == null){
            backNoAnim();
            return ;
        }

        rightImgView.setImageResource(R.drawable.icon_dots);
        rightImgView.setVisibility(View.INVISIBLE);

        initListView();
        initPublishCommentView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQUEST_ARTICLE_DETAIL_TASK_ID:    //帖子详情
                if(msg.getIsSuccess()){
                    ArticleInfo articleInfoResponse = (ArticleInfo) msg.getObj();
                    if(articleInfoResponse!=null){
                        socialCircleContentListRecyclerAdapter.getDataList().set(0, articleInfoResponse);
                        socialCircleContentListRecyclerAdapter.notifyDataSetChanged();

                        if(articleInfoResponse.getCommentList()!=null && articleInfoResponse.getCommentList().size()!=0){
                            List<Comment> commentList = new ArrayList<>();

                            for(Comment comment : articleInfoResponse.getCommentList()){
                                commentList.add(comment);
                                if(comment.getReplyList()!=null && comment.getReplyList().size()!=0){
                                    commentList.addAll(comment.getReplyList());

                                    comment.setReplyList(null);
                                }
                            }

                            commentListAdapter.getDataList().clear();
                            commentListAdapter.getDataList().addAll(commentList);
                            commentListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                listView.onRefreshComplete();
                break;
            case REQUEST_ARTICLE_COMMENT_TASK_ID: //发表帖子评论
                if(msg.getIsSuccess()){
                    publishedCommentView.hidden();
                    publishedCommentView.setText("");

                    listView.getRefreshableView().smoothScrollToPosition(0);
                    listView.setTopRefreshing();
                }
                dismissProgressDialog();
                break;
            case REQUEST_SENT_ARTICLE_REPLY_TASK_ID: //发表帖子回复
                if(msg.getIsSuccess()){
                    publishedCommentView.hidden();
                    publishedCommentView.setText("");

                    listView.getRefreshableView().smoothScrollToPosition(0);
                    listView.setTopRefreshing();
                }
                dismissProgressDialog();
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
                page = 1;
                requestComment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                requestComment();
            }
        });

        addHeaderView();
        commentListAdapter = new SocialCommentListAdapter(this);
        listView.getRefreshableView().setAdapter(commentListAdapter);
        listView.setTopRefreshing();
    }

    /**
     * 添加HeaderView
     */
    private void addHeaderView(){
        socialCircleContentListRecyclerAdapter = new SocialCircleContentListRecyclerAdapter(this);
        socialCircleContentListRecyclerAdapter.registerAdapterDataObserver(headerAdapterDataObserver);
        socialCircleContentListRecyclerAdapter.getDataList().add(articleInfo);
        socialCircleContentListRecyclerAdapter.setShowInDetail(true);
        headerViewHolder = socialCircleContentListRecyclerAdapter.onCreateViewHolder(null, 0);
        socialCircleContentListRecyclerAdapter.notifyDataSetChanged();

        View headerView = headerViewHolder.itemView;
        listView.addHeaderView(headerView, null, false);
    }

    /**
     * 初始化 发表评论、回复
     */
    private void initPublishCommentView(){
        publishedCommentView.setOnPublishedOnClickListener(new PublishedCommentView.SimplePublishedClickListener() {
            @Override
            public void onOk() {
                if(TextUtils.isEmpty(publishedCommentView.getText().trim())){
                    ToastUtil.showCenter(SocialCommentDetailActivity.this, "请输入评论");
                    return ;
                }
                publishedCommentView.hiddenSoftInput();
                if(replyComment!=null){ //回复
                    requestPublishedCommentReply(replyComment);
                    replyComment = null;
                }else{  //评论
                    requestPublishedComment();
                }
            }
        });
    }

    /**
     * 发表评论
     */
    private void requestPublishedComment(){
        showProgressDialog();
        socialRestUsage.sentArticleComment(REQUEST_ARTICLE_COMMENT_TASK_ID, articleInfo.getArticleId(), publishedCommentView.getText());
    }

    /**
     * 发表回复
     */
    private void requestPublishedCommentReply(Comment replyComment){
        showProgressDialog();
        socialRestUsage.sentArticleReply(REQUEST_SENT_ARTICLE_REPLY_TASK_ID, replyComment.getCommentId(), publishedCommentView.getText());
    }

    /**
     * 请求回复列表
     */
    private void requestComment(){
        socialRestUsage.articleDetail(REQUEST_ARTICLE_DETAIL_TASK_ID, articleInfo.getArticleId());
    }

    /**
     * 显示回复view
     */
    public void showReplyView(Comment comment){
        replyComment = comment;
        publishedCommentView.show();
    }

    /**
     * 显示评论
     */
    @OnClick(R.id.bottomLinLay)
    public void showCommentClick(View view){
        replyComment = null;
        publishedCommentView.show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(publishedCommentView.isShow()){
                publishedCommentView.hidden();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
