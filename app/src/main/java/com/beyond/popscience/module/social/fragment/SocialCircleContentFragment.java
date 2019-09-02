package com.beyond.popscience.module.social.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.pullrefresh.PullToRefreshBase;
import com.beyond.library.view.pullrefresh.PullToRefreshRecycleView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.ArticleInfo;
import com.beyond.popscience.frame.pojo.ArticleResponse;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.view.PublishedCommentView;
import com.beyond.popscience.module.home.entity.XXX;
import com.beyond.popscience.module.home.fragment.TownFragment;
import com.beyond.popscience.module.mservice.WebViewActivity;
import com.beyond.popscience.module.social.PublishedActivity;
import com.beyond.popscience.module.social.SocialCircleContentV2Activity;
import com.beyond.popscience.module.social.SocialCommentDetailActivity;
import com.beyond.popscience.module.social.adapter.SocialCircleContentListV2RecyclerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by linjinfa on 2017/6/23.
 * email 331710168@qq.com
 */
public class SocialCircleContentFragment extends BaseFragment {

    /**
     * 发布社区
     */
    private final int REQUEST_CODE_PUBLISHED = 101;
    /**
     * 请求社团帖子列表
     */
    private final int REQUEST_GET_ARTICLE_TASK_ID = 1001;
    /**
     * 点赞
     */
    private final int REQUEST_PRAISE_TASK_ID = 1002;
    /**
     * 发表帖子评论
     */
    private final int REQUEST_ARTICLE_COMMENT_TASK_ID = 1003;
    /**
     * 帖子评论回复
     */
    private final int REQUEST_SENT_ARTICLE_REPLY_TASK_ID = 1004;
    /**
     *
     */
    private final static String EXTRA_SOCIAL_INFO_KEY = "socialInfo";

    @BindView(R.id.recyclerView)
    protected PullToRefreshRecycleView recyclerView;

    @BindView(R.id.emptyTxtView)
    protected TextView emptyTxtView;

    @BindView(R.id.publishedCommentView)
    protected PublishedCommentView publishedCommentView;

    @Request
    private SocialRestUsage socialRestUsage;
    private SocialCircleContentListV2RecyclerAdapter socialCircleContentListRecyclerAdapter;
    private SocialInfo socialInfo;
    private ArticleInfo commentArticleInfo;
    private Comment replyComment;
    private int page = 1;

    public static SocialCircleContentFragment newInstance(SocialInfo socialInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_SOCIAL_INFO_KEY, socialInfo);

        SocialCircleContentFragment socialCircleContentFragment = new SocialCircleContentFragment();
        socialCircleContentFragment.setArguments(bundle);

        return socialCircleContentFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PUBLISHED:    //发布社区
                if (resultCode == Activity.RESULT_OK) {
                    recyclerView.setTopRefreshing();
                }
                break;
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_social_circle_content;
    }

    @Override
    public void initUI() {
        super.initUI();

        if (getArguments() != null) {
            socialInfo = (SocialInfo) getArguments().getSerializable(EXTRA_SOCIAL_INFO_KEY);
        }
        if (socialInfo == null) {
            return;
        }

        initRecyclerView();
        initPublishCommentView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(XXX messageEvent) {
        requestGetArticle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_GET_ARTICLE_TASK_ID:   //社团帖子列表
                if (msg.getIsSuccess()) {
                    ArticleResponse articleResponse = (ArticleResponse) msg.getObj();
                    if (articleResponse != null) {
                        if (recyclerView.isPullDownToRefresh()) {
                            socialCircleContentListRecyclerAdapter.getDataList().clear();
                            if (articleResponse.getArticleList() != null && articleResponse.getArticleList().size() != 0) {
                                socialCircleContentListRecyclerAdapter.getDataList().addAll(articleResponse.getArticleList());
                            }
                            socialCircleContentListRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            if (articleResponse.getArticleList() != null && articleResponse.getArticleList().size() != 0) {
                                socialCircleContentListRecyclerAdapter.getDataList().addAll(articleResponse.getArticleList());
                                socialCircleContentListRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    if (socialInfo.getAppType() == 1) {   //乡镇圈子
                        if (socialCircleContentListRecyclerAdapter.getItemCount() == 0) {
                            emptyTxtView.setVisibility(View.VISIBLE);
                            emptyTxtView.setText("该圈子暂无内容");
                        } else {
                            emptyTxtView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (socialInfo.getAppType() == 1) {   //乡镇圈子
                        if ("5".equals(msg.getCode())) {  //非本乡镇成员不能浏览帖子
                            if (getParentFragment() instanceof TownFragment) {
                                ((TownFragment) getParentFragment()).hiddenPublishView();
                            }
                            emptyTxtView.setVisibility(View.VISIBLE);
                            emptyTxtView.setText("只能访问注册本地圈子");
                        } else {
                            emptyTxtView.setVisibility(View.GONE);
                        }
                    } else {  //社团圈子
                        ToastUtil.showCenter(getContext(), msg.getMsg());
                    }
                }
                recyclerView.onRefreshComplete();
                break;
            case REQUEST_PRAISE_TASK_ID:    //点赞/取消
                if (msg.getIsSuccess()) {
                    ArticleInfo articleInfo = (ArticleInfo) msg.getTargetObj();
                    if (articleInfo != null) {
                        articleInfo.setPraised(!articleInfo.isPraised());
                        if (articleInfo.isPraised()) {
                            articleInfo.setPraiseNum(String.valueOf(articleInfo.getPraiseNumLong() + 1));
                        } else {
                            articleInfo.setPraiseNum(String.valueOf(articleInfo.getPraiseNumLong() - 1));
                        }
                        int count = socialCircleContentListRecyclerAdapter.getItemCount();
                        for (int pos = 0; pos < count; pos++) {
                            ArticleInfo articleInfoData = socialCircleContentListRecyclerAdapter.getItem(pos);
                            if (articleInfoData == articleInfo) {
                                socialCircleContentListRecyclerAdapter.notifyItemChanged(pos);
                                break;
                            }
                        }
                    }
                }
                dismissProgressDialog();
                break;
            case REQUEST_ARTICLE_COMMENT_TASK_ID: //发表帖子评论
                if (msg.getIsSuccess()) {
                    Comment resultComment = (Comment) msg.getObj();
                    if (commentArticleInfo != null) {
                        List<Comment> commentList = commentArticleInfo.getCommentList();
                        if (commentList == null) {
                            commentList = new ArrayList<>();
                            commentArticleInfo.setCommentList(commentList);
                        }
                        Comment comment = new Comment();
                        comment.setNickName(UserInfoUtil.getInstance().getUserInfo().getNickName());
                        comment.setCommentId(resultComment.getCommentId());
                        comment.setComment(publishedCommentView.getText());
                        commentList.add(0, comment);

                        socialCircleContentListRecyclerAdapter.notifyDataSetChanged();
                    }
                    publishedCommentView.hidden();
                    publishedCommentView.setText("");

                    commentArticleInfo = null;
                    replyComment = null;
                }
                dismissProgressDialog();
                break;
            case REQUEST_SENT_ARTICLE_REPLY_TASK_ID: //发表帖子回复
                if (msg.getIsSuccess()) {
                    Comment resultReplyComment = (Comment) msg.getObj();
                    if (replyComment != null) {
                        List<Comment> replyList = replyComment.getReplyList();
                        if (replyList == null) {
                            replyList = new ArrayList<>();
                            replyComment.setReplyList(replyList);
                        }
                        Comment replyComment = new Comment();
                        replyComment.setNickName(UserInfoUtil.getInstance().getUserInfo().getNickName());
                        replyComment.setReplyId(resultReplyComment.getReplyId());
                        replyComment.setComment(publishedCommentView.getText());
                        replyList.add(0, replyComment);

                        socialCircleContentListRecyclerAdapter.notifyDataSetChanged();
                    }

                    publishedCommentView.hidden();
                    publishedCommentView.setText("");

                    commentArticleInfo = null;
                    replyComment = null;
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化 发表评论、回复
     */
    private void initPublishCommentView() {
        publishedCommentView.setOnPublishedOnClickListener(new PublishedCommentView.SimplePublishedClickListener() {
            @Override
            public void onOk() {
                if (TextUtils.isEmpty(publishedCommentView.getText().trim())) {
                    ToastUtil.showCenter(getContext(), "请输入评论");
                    return;
                }
                publishedCommentView.hiddenSoftInput();
                if (replyComment != null) { //回复
                    requestPublishedCommentReply();
                } else {  //评论
                    requestPublishedComment();
                }
            }
        });
    }

    /**
     * 初始化 RecyclerView
     */
    private void initRecyclerView() {
        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.getRefreshableView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        socialCircleContentListRecyclerAdapter = new SocialCircleContentListV2RecyclerAdapter(this);

        recyclerView.getRefreshableView().setAdapter(socialCircleContentListRecyclerAdapter);
       /* //
        recyclerView.setOnReadyForPullListener(new PullToRefreshRecycleView.OnReadyForPullListener() {
            @Override
            public boolean isDisablePullStart() {
                if (getActivity() instanceof SocialCircleContentV2Activity) {
                    return !((SocialCircleContentV2Activity) getActivity()).isMDScrollBottom();
                }
                return false;
            }

            @Override
            public boolean isDisablePullEnd() {
                if (getActivity() instanceof SocialCircleContentV2Activity) {
                    return !((SocialCircleContentV2Activity) getActivity()).isMDScrollTop();
                }
                return false;
            }
        });*/
        //
        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page = 1;
                requestGetArticle();
            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                requestGetArticle();
            }
        });

        socialCircleContentListRecyclerAdapter.setOnItemClickListener(new CustomRecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position, long id) {
                ArticleInfo articleInfo = socialCircleContentListRecyclerAdapter.getDataList().get(position);
                if (articleInfo.getType()==1){
                    Intent intent = new Intent(getActivity(),WebViewActivity.class);
                    intent.putExtra("url",articleInfo.getUrl());
                    intent.putExtra("title","新闻分享");
                    startActivity(intent);
                }else{
                    articleInfo.setAppType(socialInfo.getAppType());
                    SocialCommentDetailActivity.startActivityForResult(getActivity(), articleInfo, 1);
                }
                }

        });
        recyclerView.setTopRefreshing();
        //解决上下拉刷新冲突问题
        if (null!=TownFragment.appBarLayout){
            TownFragment.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (null != recyclerView) {
                        if (verticalOffset >= 0) {
                            recyclerView.setPullToRefreshEnabled(true);
                        } else {
                            recyclerView.setPullToRefreshEnabled(false);
                        }
                    }
                }
            });
        }
    }

    /**
     * 显示回复view
     */
    public void showReplyView(ArticleInfo articleInfo, Comment comment) {
        commentArticleInfo = articleInfo;
        replyComment = comment;
        publishedCommentView.show();
    }

    /**
     * 隐藏回复view
     */
    public void hiddenReplyView() {
        commentArticleInfo = null;
        replyComment = null;
        publishedCommentView.hidden();
    }

    /**
     * 发表评论
     */
    private void requestPublishedComment() {
        if (commentArticleInfo != null) {
            showProgressDialog();
            socialRestUsage.sentArticleComment(REQUEST_ARTICLE_COMMENT_TASK_ID, commentArticleInfo.getArticleId(), publishedCommentView.getText());
        }
    }

    /**
     * 发表回复
     */
    private void requestPublishedCommentReply() {
        if (replyComment != null) {
            showProgressDialog();
            socialRestUsage.sentArticleReply(REQUEST_SENT_ARTICLE_REPLY_TASK_ID, replyComment.getCommentId(), publishedCommentView.getText());
        }
    }

    /**
     * 请求社团帖子列表
     */
    private void requestGetArticle() {
        socialRestUsage.getArticle(REQUEST_GET_ARTICLE_TASK_ID, socialInfo.getCommunityId(), page);
    }

    /**
     * 点赞
     */
    public void requestPraise(ArticleInfo articleInfo) {
        if (articleInfo != null) {
            showProgressDialog();
            socialRestUsage.praise(REQUEST_PRAISE_TASK_ID, articleInfo.getArticleId(), !articleInfo.isPraised(), articleInfo);
        }
    }

    /**
     * 发布社区
     */
    public void startPublishedActivity() {
        PublishedActivity.startActivityForResult(this, REQUEST_CODE_PUBLISHED, socialInfo);
    }

}
