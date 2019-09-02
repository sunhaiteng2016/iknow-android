package com.beyond.popscience.module.news;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshListView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommentRestUsage;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.frame.pojo.ReplyResponse;
import com.beyond.popscience.frame.view.PublishedCommentView;
import com.beyond.popscience.module.news.adapter.CommentListAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评论详情
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class CommentDetailActivity extends BaseActivity {

    /**
     * 请求新闻回复列表
     */
    private final int REQUEST_REPLY_LIST_TASK_ID = 101;
    /**
     * 点赞
     */
    private final int REQUEST_PRAISE_TASK_ID = 102;
    /**
     * 回复
     */
    private final int REQUEST_REPLY_COMMENT_TASK_ID = 103;
    /**
     *
     */
    public final static String EXTRA_COMMENT_KEY = "comment";

    /**
     *
     */
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
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
    private CommentListAdapter commentListAdapter;

    /**
     *
     */
    private Comment comment;
    private int page;
    @Request
    private CommentRestUsage commentRestUsage;
    @Request
    private TownRestUsage townRestUsage;

    /**commentRestUsage
     *
     */
    public static void startActivityForResult(Activity context, Comment comment, int requestCode){
        Intent intent = new Intent(context, CommentDetailActivity.class);
        intent.putExtra(EXTRA_COMMENT_KEY, comment);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_comment_detail;
    }

    @Override
    public void initUI() {
//        ToastUtil.showCenter(CommentDetailActivity.this,"恭喜您, + 1 科普绿币!");
        comment = (Comment) getIntent().getSerializableExtra(EXTRA_COMMENT_KEY);
        if(comment == null || TextUtils.isEmpty(comment.getCommentId())){
            backNoAnim();
            return ;
        }

        titleTxtView.setText("评论详情");
        commentTxtView.setText("回复"+comment.getNickName()+": ");

        initListView();
        initPublishCommentView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case REQUEST_REPLY_LIST_TASK_ID: //回复列表
                ReplyResponse replyResponse = null;
                if(msg.getIsSuccess()){
                    replyResponse = (ReplyResponse) msg.getObj();
                    if(replyResponse!=null){
                        if(replyResponse.getReplyList()!=null){
                            if(listView.isPullDownToRefresh()){
                                commentListAdapter.getDataList().clear();
                                commentListAdapter.getDataList().add(comment);
                            }
                            commentListAdapter.getDataList().addAll(replyResponse.getReplyList());
                            commentListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if(replyResponse == null || replyResponse.getReplyList() == null || replyResponse.getReplyList().size()==0 || commentListAdapter.getCount() >= replyResponse.getTotalcount()){   //加载结束
                    listView.onLoadMoreCompleteAndNoData();
                }else{
                    listView.onLoadMoreComplete();
                }
                break;
            case REQUEST_PRAISE_TASK_ID:    //点赞
                if(msg.getIsSuccess()){
                    Comment replyComment = (Comment) msg.getTargetObj();
                    if(replyComment!=null){
                        replyComment.setPraiseNum(String.valueOf(replyComment.getPraiseNumLong()+1));
                        comment.setPraised(true);
                        commentListAdapter.notifyDataSetChanged();

                        if(comment == replyComment){
                            setResult(RESULT_OK, new Intent().putExtra(EXTRA_COMMENT_KEY, comment));
                        }
                    }
                }
                dismissProgressDialog();
                break;
            case REQUEST_REPLY_COMMENT_TASK_ID:    //评论回复
                if(msg.getIsSuccess()){
                    comment.setReplyNum(String.valueOf(comment.getReplyNumLong()+1));
                    publishedCommentView.hidden();
                    publishedCommentView.setText("");

                    listView.getRefreshableView().smoothScrollToPosition(0);
                    listView.setTopRefreshing();

                    setResult(RESULT_OK, new Intent().putExtra(EXTRA_COMMENT_KEY, comment));
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView(){
        listView.setMode(PullToRefreshBase.Mode.BOTH);
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

        commentListAdapter = new CommentListAdapter(this).setReply(true);
        listView.getRefreshableView().setAdapter(commentListAdapter);
        listView.setTopRefreshing();
    }

    /**
     * 初始化 发表评论、回复
     */
    private void initPublishCommentView(){
        publishedCommentView.setOnPublishedOnClickListener(new PublishedCommentView.SimplePublishedClickListener() {
            @Override
            public void onOk() {
                if(TextUtils.isEmpty(publishedCommentView.getText().trim())){
                    ToastUtil.showCenter(CommentDetailActivity.this, "请输入评论");
                    return ;
                }
                publishedCommentView.hiddenSoftInput();
                requestPublishedCommentReply();
            }
        });
    }

    /**
     * 发表回复
     */
    private void requestPublishedCommentReply(){
        showProgressDialog();
        if(comment.isTownNews()){   //乡镇
            townRestUsage.replyComment(REQUEST_REPLY_COMMENT_TASK_ID, comment.getCommentId(), publishedCommentView.getText());
        }else{
            commentRestUsage.replyComment(REQUEST_REPLY_COMMENT_TASK_ID, comment.getCommentId(), publishedCommentView.getText());
        }
    }

    /**
     * 请求回复列表
     */
    private void requestComment(){
        if(comment.isTownNews()){   //乡镇
            townRestUsage.getReply(REQUEST_REPLY_LIST_TASK_ID, comment.getCommentId(), page);
        }else{
            commentRestUsage.getReply(REQUEST_REPLY_LIST_TASK_ID, comment.getCommentId(), page);
        }
    }

    /**
     * 点赞
     */
    public void requestPraise(Comment replyComment){
        if(replyComment != null){
            showProgressDialog();
            if(comment == replyComment){    //评论
                commentRestUsage.praise(REQUEST_PRAISE_TASK_ID, "1", replyComment.getCommentId(), replyComment);
            }else{  //回复
                commentRestUsage.praise(REQUEST_PRAISE_TASK_ID, "2", replyComment.getReplyId(), replyComment);
            }
        }
    }

    /**
     * 显示回复
     */
    @OnClick(R.id.bottomLinLay)
    public void showReplyPublishedCommentClick(View view){
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
