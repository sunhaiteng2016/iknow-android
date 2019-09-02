package com.beyond.popscience.module.news;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.beyond.popscience.frame.pojo.CommentResponse;
import com.beyond.popscience.frame.view.PublishedCommentView;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.news.adapter.CommentListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评论列表
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class CommentListActivity extends BaseActivity {

    /**
     * 请求新闻评论列表
     */
    private final int REQUEST_NEWS_COMMENT_TASK_ID = 101;
    /**
     * 点赞
     */
    private final int REQUEST_PRAISE_TASK_ID = 102;
    /**
     * 举报
     */
    private final int REQUEST_REPORT_TASK_ID = 103;
    /**
     * 发表评论
     */
    private final int REQUEST_NEWS_PUBLISHED_COMMENT_TASK_ID = 104;
    /**
     * 评论回复
     */
    private final int REQUEST_REPLY_COMMENT_TASK_ID = 105;
    /**
     * 回复列表
     */
    private final int REQUEST_CODE_REPLY = 10;
    /**
     *
     */
    private final static String EXTRA_NEWS_KEY = "news";
    /**
     *
     */
    @BindView(R.id.bottomLinLay)
    protected LinearLayout bottomLinLay;
    /**
     *
     */
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;
    /**
     *
     */
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    /**
     *
     */
    @BindView(R.id.publishedCommentView)
    protected PublishedCommentView publishedCommentView;
    /**
     *
     */
    @BindView(R.id.listView)
    protected ListView listView;
    /**
     *
     */
    private CommentListAdapter commentListAdapter;

    private News news;
    private int page = 1;
    @Request
    private CommentRestUsage commentRestUsage;
    @Request
    private TownRestUsage townRestUsage;
    /**
     * 要回复的评论
     */
    private Comment currReplyComment;

    /**
     * @param context
     */
    public static void startActivity(Context context, News news) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra(EXTRA_NEWS_KEY, news);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_REPLY:    //回复
                if (resultCode == RESULT_OK) {
                    Comment newComment = (Comment) data.getSerializableExtra(CommentDetailActivity.EXTRA_COMMENT_KEY);
                    if (newComment != null) {
                        int count = commentListAdapter.getCount();
                        for (int i = 0; i < count; i++) {
                            Comment comment = commentListAdapter.getItem(i);
                            if (comment != null && !TextUtils.isEmpty(comment.getCommentId()) && comment.getCommentId().equals(newComment.getCommentId())) {
                                commentListAdapter.getDataList().remove(i);
                                commentListAdapter.getDataList().add(i, newComment);
                                commentListAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commentListAdapter != null) {
            commentListAdapter.release();
        }
    }

    @Override
    public void initUI() {
        super.initUI();

        news = (News) getIntent().getSerializableExtra(EXTRA_NEWS_KEY);
        if (news == null || TextUtils.isEmpty(news.newsId)) {
            backNoAnim();
            return;
        }

        titleTxtView.setText("评论");

        initListView();
        initPublishCommentView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_NEWS_COMMENT_TASK_ID:  //新闻评论
                CommentResponse commentResponse = null;
                if (msg.getIsSuccess()) {
                    commentResponse = (CommentResponse) msg.getObj();
                    if (commentResponse != null) {
                        if (commentResponse.getCommentList() != null && commentResponse.getCommentList().size() != 0) {
                            if (page == 1) {
                                commentListAdapter.getDataList().clear();
                            } else {
                                List<Comment> ss = commentResponse.getCommentList();
                                if (commentResponse.getCommentList().size() <= 0) {
                                    Toast.makeText(CommentListActivity.this, "已经到底了！", Toast.LENGTH_SHORT).show();
                                }
                            }
                            commentListAdapter.getDataList().addAll(commentResponse.getCommentList());
                            commentListAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(CommentListActivity.this, "已经到底了！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case REQUEST_PRAISE_TASK_ID:    //点赞
                if (msg.getIsSuccess()) {
                    Comment comment = (Comment) msg.getTargetObj();
                    if (comment != null) {
                        comment.setPraiseNum(String.valueOf(comment.getPraiseNumLong() + 1));
                        comment.setPraised(true);
                        commentListAdapter.notifyDataSetChanged();
                    }
                }
                dismissProgressDialog();
                break;
            case REQUEST_REPORT_TASK_ID:    //举报
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "举报成功");
                }
                dismissProgressDialog();
                break;
            case REQUEST_NEWS_PUBLISHED_COMMENT_TASK_ID:    //发表评论
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "恭喜您, + 1 科普绿币!");
                    publishedCommentView.hidden();
                    publishedCommentView.setText("");

                    listView.smoothScrollToPosition(0);
                    //listView.setTopRefreshing();
                    refreshLayout.autoRefresh();
                }
                dismissProgressDialog();
                break;
            case REQUEST_REPLY_COMMENT_TASK_ID:    //评论回复
                if (msg.getIsSuccess()) {
                    if (currReplyComment != null) {
                        currReplyComment.setReplyNum(String.valueOf(currReplyComment.getReplyNumLong() + 1));
                        commentListAdapter.notifyDataSetChanged();
                    }

                    publishedCommentView.hidden();
                    publishedCommentView.setText("");
                    currReplyComment = null;
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getHeaderViewsCount();
                Comment comment = commentListAdapter.getItem(position);

                if (news.isTownNews()) {
                    comment.setAppNewsType(News.TYPE_TOWN_NEWS);
                }

                CommentDetailActivity.startActivityForResult(CommentListActivity.this, comment, REQUEST_CODE_REPLY);
            }
        });

        commentListAdapter = new CommentListAdapter(this);
        listView.setAdapter(commentListAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                page = 1;
                requestComment();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                page++;
                requestComment();
            }
        });
        refreshLayout.autoRefresh();
    }

    /**
     * 初始化 发表评论、回复
     */
    private void initPublishCommentView() {
        publishedCommentView.setOnPublishedOnClickListener(new PublishedCommentView.SimplePublishedClickListener() {
            @Override
            public void onOk() {
                if (TextUtils.isEmpty(publishedCommentView.getText().trim())) {
                    ToastUtil.showCenter(CommentListActivity.this, "请输入评论");
                    return;
                }
                publishedCommentView.hiddenSoftInput();
                if (currReplyComment == null) {   //评论
                    requestPublishedComment();
                } else {  //回复
                    requestPublishedCommentReply();
                }
            }

            @Override
            public void onCancel() {
                currReplyComment = null;
            }
        });
    }

    /**
     * 请求评论
     */
    private void requestComment() {
        if (news.isTownNews()) {  //乡镇
            townRestUsage.getNewsComment(REQUEST_NEWS_COMMENT_TASK_ID, news.newsId, page);
        } else {
            commentRestUsage.getNewsComment(REQUEST_NEWS_COMMENT_TASK_ID, news.newsId, page);
        }
    }

    /**
     * 发表评论
     */
    private void requestPublishedComment() {
        showProgressDialog();
        if (news.isTownNews()) {  //乡镇
            townRestUsage.sendComment(REQUEST_NEWS_PUBLISHED_COMMENT_TASK_ID, news.newsId, publishedCommentView.getText());
        } else {
            commentRestUsage.sendComment(REQUEST_NEWS_PUBLISHED_COMMENT_TASK_ID, news.newsId, publishedCommentView.getText());
        }
    }

    /**
     * 发表评论
     */
    private void requestPublishedCommentReply() {
        if (currReplyComment != null) {
            showProgressDialog();
            if (news.isTownNews()) {  //乡镇
                townRestUsage.replyComment(REQUEST_REPLY_COMMENT_TASK_ID, currReplyComment.getCommentId(), publishedCommentView.getText());
            } else {
                commentRestUsage.replyComment(REQUEST_REPLY_COMMENT_TASK_ID, currReplyComment.getCommentId(), publishedCommentView.getText());
            }
        }
    }

    /**
     * 点赞
     */
    public void requestPraise(Comment comment) {
        if (comment != null) {
            showProgressDialog();
            if (news.isTownNews()) {  //乡镇
                townRestUsage.praise(REQUEST_PRAISE_TASK_ID, "1", comment.getCommentId(), comment);
            } else {
                commentRestUsage.praise(REQUEST_PRAISE_TASK_ID, "1", comment.getCommentId(), comment);
            }
        }
    }

    /**
     * 举报
     */
    public void requestReport(Comment comment) {
        if (comment != null) {
            showProgressDialog();
            if (news.isTownNews()) {  //乡镇
                townRestUsage.report(REQUEST_REPORT_TASK_ID, comment.getCommentId());
            } else {
                commentRestUsage.report(REQUEST_REPORT_TASK_ID, comment.getCommentId());
            }
        }
    }

    /**
     * 显示发表评论
     *
     * @param view
     */
    @OnClick(R.id.bottomLinLay)
    public void showPublishedCommentClick(View view) {
        currReplyComment = null;
        publishedCommentView.show();
    }

    /**
     * 显示回复
     */
    public void showReplyPublishedComment(Comment comment, int position) {
        listView.smoothScrollToPosition(position);
        currReplyComment = comment;
        publishedCommentView.show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (publishedCommentView.isShow()) {
                publishedCommentView.hidden();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
