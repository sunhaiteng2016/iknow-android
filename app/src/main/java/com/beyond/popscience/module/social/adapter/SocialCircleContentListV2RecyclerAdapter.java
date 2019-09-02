package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.ArticleInfo;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.home.ShareShuoShuoActivity;
import com.beyond.popscience.module.home.ShareShuoShuoTwoActivity;
import com.beyond.popscience.module.home.ShareTxLActivity;
import com.beyond.popscience.module.home.ShareTxLTwoActivity;
import com.beyond.popscience.module.home.fragment.view.ContactBean;
import com.beyond.popscience.module.home.fragment.view.LocationAddressList;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.beyond.popscience.module.social.fragment.SocialCircleContentFragment;
import com.beyond.popscience.widget.SharePopWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialCircleContentListV2RecyclerAdapter extends CustomRecyclerBaseAdapter<ArticleInfo> {

    /**
     * 列数
     */
    private final int COLUMN_NUM = 3;
    /**
     * 总宽度
     */
    private int imageLayoutTotalWidth = 0;
    /**
     * 是否在详情中显示
     */
    private boolean isShowInDetail = false;

    @Request
    SocialRestUsage socialRestUsage;

    public SocialCircleContentListV2RecyclerAdapter(Activity context) {
        super(context);
    }

    public SocialCircleContentListV2RecyclerAdapter(Fragment fragment) {
        super(fragment);
    }

    public boolean isShowInDetail() {
        return isShowInDetail;
    }

    public void setShowInDetail(boolean showInDetail) {
        isShowInDetail = showInDetail;
    }


    @Override
    public int getItemViewType(int position) {
        final ArticleInfo articleInfo = dataList.get(position);
        int type = articleInfo.getType();
        if (type==1){
            //新闻
            return 1;
        }
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==1){
            return  new  NewsViewHolder(inflater.inflate(R.layout.adapter_social_circle_content_v2_item_two, parent, false));
        }
        return new SocialCircleViewHolder(inflater.inflate(R.layout.adapter_social_circle_content_v2_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder, position, getItemId(position));
                }
            }
        });
        int viewType = getItemViewType(position);
        if (viewType==1){
            final ArticleInfo articleInfo = dataList.get(position);
            ((NewsViewHolder) holder).setData(articleInfo);
            ((NewsViewHolder) holder).llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //这个地方弹窗

                    SharePopWindow sharePopWindow = new SharePopWindow(context);
                    sharePopWindow.show(holder.itemView);

                    sharePopWindow.setOnClickListener(new SharePopWindow.ShareChangeListener() {


                        @Override
                        public void onAddressChange(int flag) {
                            String articleid = articleInfo.getArticleId();
                            String acatar="";
                            if (null!=articleInfo.getDetailPicList()&&articleInfo.getDetailPicList().size()>0){
                                acatar = articleInfo.getDetailPicList().get(0);
                            }
                            String content = articleInfo.getContent();

                            if (articleInfo.getType()==1){
                                //全部都是网页分享
                                if (flag == 1) {
                                    Intent intent = new Intent(context, ShareShuoShuoTwoActivity.class);
                                    intent.putExtra("link", articleInfo.getUrl());
                                    intent.putExtra("pics", acatar);
                                    intent.putExtra("titles", content);
                                    context.startActivity(intent);
                                }
                                if (flag == 2) {
                                    Intent intent = new Intent(context, ShareTxLTwoActivity.class);
                                    intent.putExtra("link", articleInfo.getUrl());
                                    intent.putExtra("pics", acatar);
                                    intent.putExtra("titles", content);
                                    context.startActivity(intent);
                                }
                                if (flag == 3) {
                                    //发送到乡镇说说
                                    int type = articleInfo.getType();
                                    AppBaseRestUsageV2 appBaseRestUsageV1 = new AppBaseRestUsageV2();
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("type", "1");
                                    map.put("url", articleInfo.getUrl());
                                    map.put("content", content);
                                    map.put("detailPics", acatar);
                                    appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/team/publishArticle/" + UserInfoUtil.getInstance().getUserInfo().getVillageId(), map, new NewCustomResponseHandler() {

                                        @Override
                                        public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                                            super.onSuccess(httpStatusCode, headerMap, responseStr);
                                            ToastUtil.showCenter(context,"转发成功");
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                                            super.onFailure(statusCode, headerMap, responseString, throwable);
                                        }
                                    });
                                }
                            }else {
                                if (flag == 1) {
                                    Intent intent = new Intent(context, ShareShuoShuoActivity.class);
                                    intent.putExtra("articleid", articleid);
                                    intent.putExtra("acatar", acatar);
                                    intent.putExtra("content", content);
                                    context.startActivity(intent);
                                }
                                if (flag == 2) {
                                    Intent intent = new Intent(context, ShareTxLActivity.class);
                                    intent.putExtra("articleid", articleid);
                                    intent.putExtra("acatar", acatar);
                                    intent.putExtra("content", content);
                                    context.startActivity(intent);
                                }
                                if (flag == 3) {
                                    //发送到乡镇说说
                                    int type = articleInfo.getType();
                                    AppBaseRestUsageV2 appBaseRestUsageV1 = new AppBaseRestUsageV2();
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("content", content);
                                    map.put("detailPics", acatar);
                                    appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/team/publishArticle/" + UserInfoUtil.getInstance().getUserInfo().getVillageId(), map, new NewCustomResponseHandler() {

                                        @Override
                                        public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                                            super.onSuccess(httpStatusCode, headerMap, responseStr);
                                            ToastUtil.showCenter(context,"转发成功");
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                                            super.onFailure(statusCode, headerMap, responseString, throwable);
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            });
        }else{
            final ArticleInfo articleInfo = dataList.get(position);
            ((SocialCircleViewHolder) holder).setData(articleInfo);
            ((SocialCircleViewHolder) holder).llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //这个地方弹窗

                    SharePopWindow sharePopWindow = new SharePopWindow(context);
                    sharePopWindow.show(holder.itemView);

                    sharePopWindow.setOnClickListener(new SharePopWindow.ShareChangeListener() {


                        @Override
                        public void onAddressChange(int flag) {
                            String articleid = articleInfo.getArticleId();
                            String acatar="";
                            List<String> ss = articleInfo.getDetailPicList();
                            if (null!=articleInfo.getDetailPicList()&&articleInfo.getDetailPicList().size()>0){
                                acatar = articleInfo.getDetailPicList().get(0);
                            }
                            String content = articleInfo.getContent();

                            if (articleInfo.getType()==1){
                                //全部都是网页分享
                                if (flag == 1) {
                                    Intent intent = new Intent(context, ShareShuoShuoTwoActivity.class);
                                    intent.putExtra("link", articleInfo.getUrl());
                                    intent.putExtra("pics", articleInfo.getDetailPics());
                                    intent.putExtra("titles", content);
                                    context.startActivity(intent);
                                }
                                if (flag == 2) {
                                    Intent intent = new Intent(context, ShareTxLTwoActivity.class);
                                    intent.putExtra("link", articleInfo.getUrl());
                                    intent.putExtra("pics", articleInfo.getDetailPics());
                                    intent.putExtra("titles", content);
                                    context.startActivity(intent);
                                }
                                if (flag == 3) {
                                    //发送到乡镇说说
                                    int type = articleInfo.getType();
                                    AppBaseRestUsageV2 appBaseRestUsageV1 = new AppBaseRestUsageV2();
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("type", "1");
                                    map.put("url", articleInfo.getUrl());
                                    map.put("content", content);
                                    map.put("detailPics", acatar);
                                    appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/team/publishArticle/" + UserInfoUtil.getInstance().getUserInfo().getVillageId(), map, new NewCustomResponseHandler() {

                                        @Override
                                        public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                                            super.onSuccess(httpStatusCode, headerMap, responseStr);
                                            ToastUtil.showCenter(context,"转发成功");
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                                            super.onFailure(statusCode, headerMap, responseString, throwable);
                                        }
                                    });
                                }
                            }else {
                                if (flag == 1) {
                                    Intent intent = new Intent(context, ShareShuoShuoActivity.class);
                                    intent.putExtra("articleid", articleid);
                                    intent.putExtra("acatar", articleInfo.getDetailPics());
                                    intent.putExtra("content", content);
                                    context.startActivity(intent);
                                }
                                if (flag == 2) {
                                    Intent intent = new Intent(context, ShareTxLActivity.class);
                                    intent.putExtra("articleid", articleid);
                                    intent.putExtra("acatar", acatar);
                                    intent.putExtra("content", content);
                                    context.startActivity(intent);
                                }
                                if (flag == 3) {
                                    //发送到乡镇说说
                                    int type = articleInfo.getType();
                                    AppBaseRestUsageV2 appBaseRestUsageV1 = new AppBaseRestUsageV2();
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("content", content);
                                    map.put("detailPics", acatar);
                                    appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/team/publishArticle/" + UserInfoUtil.getInstance().getUserInfo().getVillageId(), map, new NewCustomResponseHandler() {

                                        @Override
                                        public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                                            super.onSuccess(httpStatusCode, headerMap, responseStr);
                                            ToastUtil.showCenter(context,"转发成功");
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                                            super.onFailure(statusCode, headerMap, responseString, throwable);
                                        }
                                    });
                                }
                            }

                        }
                    });

                }
            });
        }

    }

    class NewsViewHolder extends  RecyclerView.ViewHolder{
        private CircleImageView avatarImgView;
        private TextView authorTxtView;
        private TextView timeStampTxtView;
        private TextView praiseNumTxtView;
        private TextView commentContentTxtView;
        private TextView replyNumTxtView;
        private ImageView praiseImgView;
        private LinearLayout praiseLinLay;
        private LinearLayout replyLinLay;
        private LinearLayout normalOperLinLay;
        private LinearLayout commentLinLay;
        private TextView moreCommentTxtView;
        private ImageView iv_img;
        public LinearLayout llShare;
        public NewsViewHolder(View itemView) {
            super(itemView);
            avatarImgView = (CircleImageView) itemView.findViewById(R.id.avatarImgView);
            authorTxtView = (TextView) itemView.findViewById(R.id.authorTxtView);
            timeStampTxtView = (TextView) itemView.findViewById(R.id.timeStampTxtView);
            praiseNumTxtView = (TextView) itemView.findViewById(R.id.praiseNumTxtView);
            commentContentTxtView = (TextView) itemView.findViewById(R.id.commentContentTxtView);
            replyNumTxtView = (TextView) itemView.findViewById(R.id.replyNumTxtView);
            moreCommentTxtView = (TextView) itemView.findViewById(R.id.moreCommentTxtView);
            praiseImgView = (ImageView) itemView.findViewById(R.id.praiseImgView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            praiseLinLay = (LinearLayout) itemView.findViewById(R.id.praiseLinLay);
            replyLinLay = (LinearLayout) itemView.findViewById(R.id.replyLinLay);
            normalOperLinLay = (LinearLayout) itemView.findViewById(R.id.normalOperLinLay);
            commentLinLay = (LinearLayout) itemView.findViewById(R.id.commentLinLay);
            llShare = (LinearLayout) itemView.findViewById(R.id.ll_share);

            NewsViewHolder.PraiseClick praiseClick = new NewsViewHolder.PraiseClick();
            praiseLinLay.setOnClickListener(praiseClick);
            itemView.setTag(praiseLinLay.getId(), praiseClick);

            if (isShowInDetail) {
                normalOperLinLay.setVisibility(View.GONE);
            } else {
                normalOperLinLay.setVisibility(View.VISIBLE);
            }
        }


        /**
         * @param articleInfo
         */
        public void setData(final ArticleInfo articleInfo) {
            replyLinLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof SocialCircleContentFragment) {
                        ((SocialCircleContentFragment) fragment).showReplyView(articleInfo, null);
                    }
                }
            });


            NewsViewHolder.PraiseClick praiseClick = (NewsViewHolder.PraiseClick) itemView.getTag(praiseLinLay.getId());
            praiseClick.set(articleInfo);

            ImageLoaderUtil.displayImage(context, articleInfo.getAvatar(), avatarImgView, getAvatarDisplayImageOptions());
            authorTxtView.setText(articleInfo.getNickName());
            ImageLoaderUtil.displayImage(context, articleInfo.getDetailPics(), iv_img, getAvatarDisplayImageOptions());
            praiseImgView.setImageResource(articleInfo.isPraised() ? R.drawable.icon_like_red : R.drawable.icon_like);

            praiseNumTxtView.setText(articleInfo.getPraiseNum());
            replyNumTxtView.setText(articleInfo.getReplyNum());
            timeStampTxtView.setText(Util.getDisplayDateTime(BeyondApplication.getInstance().getCurrSystemTime(), articleInfo.getPublishTime()));

            commentContentTxtView.setText(articleInfo.getContent());
                addCommentViewLayout(articleInfo);
        }

        /**
         * 添加评论 layout
         *
         * @param articleInfo
         */
        private void addCommentViewLayout(final ArticleInfo articleInfo) {
            if (commentLinLay.getVisibility() != View.GONE) {
                commentLinLay.setVisibility(View.GONE);
            }
            moreCommentTxtView.setVisibility(View.GONE);

            List<Comment> commentList = articleInfo.getCommentList();
            if (commentList != null && commentList.size() != 0) {
                if (commentList.size() >= 10) {
                    moreCommentTxtView.setVisibility(View.VISIBLE);
                }

                List<Comment> subCommentList = commentList.subList(0, commentList.size() > 10 ? 10 : commentList.size());
                int count = subCommentList.size();
                for (int i = 0; i < count; i++) {
                    final Comment comment = subCommentList.get(i);
                    View view = commentLinLay.getChildAt(i);
                    if (view == null) {
                        view = inflater.inflate(R.layout.adapter_social_circle_content_v2_comment_item, commentLinLay, false);
                        commentLinLay.addView(view);
                    } else {
                        if (view.getVisibility() != View.VISIBLE) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }

                    NewsViewHolder.CommentViewHolder commentViewHolder = (NewsViewHolder.CommentViewHolder) view.getTag();
                    if (commentViewHolder == null) {
                        commentViewHolder = new NewsViewHolder.CommentViewHolder();

                        commentViewHolder.commentReplyLinLay = (LinearLayout) view.findViewWithTag("commentReplyLinLay");
                        commentViewHolder.commentLinLay = (LinearLayout) view.findViewWithTag("commentLinLay");
                        commentViewHolder.commentNickNameTxtView = (TextView) view.findViewWithTag("commentNickNameTxtView");
                        commentViewHolder.commentContentTxtView = (TextView) view.findViewWithTag("commentContentTxtView");
                        commentViewHolder.lineView = view.findViewWithTag("lineView");

                        view.setTag(commentViewHolder);
                    }
                    commentViewHolder.commentNickNameTxtView.setText(comment.getNickName() + ": ");
                    commentViewHolder.commentContentTxtView.setText(comment.getComment());
                    commentViewHolder.lineView.setVisibility((i == count - 1) ? View.GONE : View.VISIBLE);
                    commentViewHolder.commentLinLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (fragment instanceof SocialCircleContentFragment) {
                                ((SocialCircleContentFragment) fragment).showReplyView(null, comment);
                            }
                        }
                    });

                    //回复
                    addCommentReplyViewLayout(comment, commentViewHolder);
                }

                for (int i = count; i < commentLinLay.getChildCount(); i++) {
                    View view = commentLinLay.getChildAt(i);
                    if (view != null) {
                        view.setVisibility(View.GONE);
                    }
                }
                commentLinLay.setVisibility(View.VISIBLE);
            }
        }

        /**
         * 添加回复view layout
         *
         * @param comment
         */
        private void addCommentReplyViewLayout(Comment comment, NewsViewHolder.CommentViewHolder commentViewHolder) {
            if (commentViewHolder.commentReplyLinLay.getVisibility() != View.GONE) {
                commentViewHolder.commentReplyLinLay.setVisibility(View.GONE);
            }

            List<Comment> replyList = comment.getReplyList();
            if (replyList != null && replyList.size() != 0) {
                int replyCount = replyList.size();
                for (int replyIndex = 0; replyIndex < replyCount; replyIndex++) {
                    Comment replyComment = replyList.get(replyIndex);

                    View replyView = commentViewHolder.commentReplyLinLay.getChildAt(replyIndex);
                    if (replyView == null) {
                        replyView = inflater.inflate(R.layout.adapter_social_circle_content_v2_comment_reply_item, commentViewHolder.commentReplyLinLay, false);
                        commentViewHolder.commentReplyLinLay.addView(replyView);
                    } else {
                        if (replyView.getVisibility() != View.VISIBLE) {
                            replyView.setVisibility(View.VISIBLE);
                        }
                    }
                    NewsViewHolder.CommentReplyViewHolder commentReplyViewHolder = (NewsViewHolder.CommentReplyViewHolder) replyView.getTag();
                    if (commentReplyViewHolder == null) {
                        commentReplyViewHolder = new NewsViewHolder.CommentReplyViewHolder();

                        commentReplyViewHolder.replyNickNameTxtView = (TextView) replyView.findViewWithTag("replyNickNameTxtView");
                        commentReplyViewHolder.replyContentTxtView = (TextView) replyView.findViewWithTag("replyContentTxtView");

                        replyView.setTag(commentReplyViewHolder);
                    }

                    commentReplyViewHolder.replyNickNameTxtView.setText(replyComment.getNickName() + ": ");
                    commentReplyViewHolder.replyContentTxtView.setText(replyComment.getComment());
                }

                for (int replyViewChildIndex = replyCount; replyViewChildIndex < commentViewHolder.commentReplyLinLay.getChildCount(); replyViewChildIndex++) {
                    View replyView = commentViewHolder.commentReplyLinLay.getChildAt(replyViewChildIndex);
                    if (replyView != null) {
                        replyView.setVisibility(View.GONE);
                    }
                }
                commentViewHolder.commentReplyLinLay.setVisibility(View.VISIBLE);
            }
        }


        class PraiseClick implements View.OnClickListener {

            private ArticleInfo articleInfo;

            /**
             * @param articleInfo
             */
            public void set(ArticleInfo articleInfo) {
                this.articleInfo = articleInfo;
            }

            @Override
            public void onClick(View v) {
                if (fragment != null) {
                    ((SocialCircleContentFragment) fragment).requestPraise(articleInfo);
                }
            }
        }

        class CommentViewHolder {
            TextView commentNickNameTxtView;
            TextView commentContentTxtView;
            LinearLayout commentReplyLinLay;
            LinearLayout commentLinLay;
            View lineView;
        }

        class CommentReplyViewHolder {
            TextView replyNickNameTxtView;
            TextView replyContentTxtView;
        }
    }

    class SocialCircleViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView avatarImgView;
        private TextView authorTxtView;
        private TextView timeStampTxtView;
        private TextView praiseNumTxtView;
        private TextView commentContentTxtView;
        private TextView replyNumTxtView;
        private ImageView praiseImgView;
        private LinearLayout praiseLinLay;
        private LinearLayout replyLinLay;
        private LinearLayout picsLinLay;
        private LinearLayout normalOperLinLay;
        private LinearLayout commentLinLay;
        private TextView moreCommentTxtView;
        public LinearLayout llShare;

        public SocialCircleViewHolder(View itemView) {
            super(itemView);
            avatarImgView = (CircleImageView) itemView.findViewById(R.id.avatarImgView);
            authorTxtView = (TextView) itemView.findViewById(R.id.authorTxtView);
            timeStampTxtView = (TextView) itemView.findViewById(R.id.timeStampTxtView);
            praiseNumTxtView = (TextView) itemView.findViewById(R.id.praiseNumTxtView);
            commentContentTxtView = (TextView) itemView.findViewById(R.id.commentContentTxtView);
            replyNumTxtView = (TextView) itemView.findViewById(R.id.replyNumTxtView);
            moreCommentTxtView = (TextView) itemView.findViewById(R.id.moreCommentTxtView);
            praiseImgView = (ImageView) itemView.findViewById(R.id.praiseImgView);
            praiseLinLay = (LinearLayout) itemView.findViewById(R.id.praiseLinLay);
            replyLinLay = (LinearLayout) itemView.findViewById(R.id.replyLinLay);
            picsLinLay = (LinearLayout) itemView.findViewById(R.id.picsLinLay);
            normalOperLinLay = (LinearLayout) itemView.findViewById(R.id.normalOperLinLay);
            commentLinLay = (LinearLayout) itemView.findViewById(R.id.commentLinLay);
            llShare = (LinearLayout) itemView.findViewById(R.id.ll_share);


            PraiseClick praiseClick = new PraiseClick();
            praiseLinLay.setOnClickListener(praiseClick);
            itemView.setTag(praiseLinLay.getId(), praiseClick);

            if (isShowInDetail) {
                normalOperLinLay.setVisibility(View.GONE);
            } else {
                normalOperLinLay.setVisibility(View.VISIBLE);
            }
        }

        /**
         * @param articleInfo
         */
        public void setData(final ArticleInfo articleInfo) {
            replyLinLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof SocialCircleContentFragment) {
                        ((SocialCircleContentFragment) fragment).showReplyView(articleInfo, null);
                    }
                }
            });


            PraiseClick praiseClick = (PraiseClick) itemView.getTag(praiseLinLay.getId());
            praiseClick.set(articleInfo);

            ImageLoaderUtil.displayImage(context, articleInfo.getAvatar(), avatarImgView, getAvatarDisplayImageOptions());
            authorTxtView.setText(articleInfo.getNickName());

            praiseImgView.setImageResource(articleInfo.isPraised() ? R.drawable.icon_like_red : R.drawable.icon_like);

            praiseNumTxtView.setText(articleInfo.getPraiseNum());
            replyNumTxtView.setText(articleInfo.getReplyNum());
            timeStampTxtView.setText(Util.getDisplayDateTime(BeyondApplication.getInstance().getCurrSystemTime(), articleInfo.getPublishTime()));

            commentContentTxtView.setText(articleInfo.getContent());

            if (imageLayoutTotalWidth == 0) {
                picsLinLay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        picsLinLay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        imageLayoutTotalWidth = picsLinLay.getMeasuredWidth();

                        addImageViewLayout(articleInfo);
                        addCommentViewLayout(articleInfo);
                    }
                });
            } else {
                addImageViewLayout(articleInfo);
                addCommentViewLayout(articleInfo);
            }
        }

        /**
         * 添加评论 layout
         *
         * @param articleInfo
         */
        private void addCommentViewLayout(final ArticleInfo articleInfo) {
            if (commentLinLay.getVisibility() != View.GONE) {
                commentLinLay.setVisibility(View.GONE);
            }
            moreCommentTxtView.setVisibility(View.GONE);

            List<Comment> commentList = articleInfo.getCommentList();
            if (commentList != null && commentList.size() != 0) {
                if (commentList.size() >= 10) {
                    moreCommentTxtView.setVisibility(View.VISIBLE);
                }

                List<Comment> subCommentList = commentList.subList(0, commentList.size() > 10 ? 10 : commentList.size());
                int count = subCommentList.size();
                for (int i = 0; i < count; i++) {
                    final Comment comment = subCommentList.get(i);
                    View view = commentLinLay.getChildAt(i);
                    if (view == null) {
                        view = inflater.inflate(R.layout.adapter_social_circle_content_v2_comment_item, commentLinLay, false);
                        commentLinLay.addView(view);
                    } else {
                        if (view.getVisibility() != View.VISIBLE) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }

                    CommentViewHolder commentViewHolder = (CommentViewHolder) view.getTag();
                    if (commentViewHolder == null) {
                        commentViewHolder = new CommentViewHolder();

                        commentViewHolder.commentReplyLinLay = (LinearLayout) view.findViewWithTag("commentReplyLinLay");
                        commentViewHolder.commentLinLay = (LinearLayout) view.findViewWithTag("commentLinLay");
                        commentViewHolder.commentNickNameTxtView = (TextView) view.findViewWithTag("commentNickNameTxtView");
                        commentViewHolder.commentContentTxtView = (TextView) view.findViewWithTag("commentContentTxtView");
                        commentViewHolder.lineView = view.findViewWithTag("lineView");

                        view.setTag(commentViewHolder);
                    }
                    commentViewHolder.commentNickNameTxtView.setText(comment.getNickName() + ": ");
                    commentViewHolder.commentContentTxtView.setText(comment.getComment());
                    commentViewHolder.lineView.setVisibility((i == count - 1) ? View.GONE : View.VISIBLE);
                    commentViewHolder.commentLinLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (fragment instanceof SocialCircleContentFragment) {
                                ((SocialCircleContentFragment) fragment).showReplyView(null, comment);
                            }
                        }
                    });

                    //回复
                    addCommentReplyViewLayout(comment, commentViewHolder);
                }

                for (int i = count; i < commentLinLay.getChildCount(); i++) {
                    View view = commentLinLay.getChildAt(i);
                    if (view != null) {
                        view.setVisibility(View.GONE);
                    }
                }
                commentLinLay.setVisibility(View.VISIBLE);
            }
        }

        /**
         * 添加回复view layout
         *
         * @param comment
         */
        private void addCommentReplyViewLayout(Comment comment, CommentViewHolder commentViewHolder) {
            if (commentViewHolder.commentReplyLinLay.getVisibility() != View.GONE) {
                commentViewHolder.commentReplyLinLay.setVisibility(View.GONE);
            }

            List<Comment> replyList = comment.getReplyList();
            if (replyList != null && replyList.size() != 0) {
                int replyCount = replyList.size();
                for (int replyIndex = 0; replyIndex < replyCount; replyIndex++) {
                    Comment replyComment = replyList.get(replyIndex);

                    View replyView = commentViewHolder.commentReplyLinLay.getChildAt(replyIndex);
                    if (replyView == null) {
                        replyView = inflater.inflate(R.layout.adapter_social_circle_content_v2_comment_reply_item, commentViewHolder.commentReplyLinLay, false);
                        commentViewHolder.commentReplyLinLay.addView(replyView);
                    } else {
                        if (replyView.getVisibility() != View.VISIBLE) {
                            replyView.setVisibility(View.VISIBLE);
                        }
                    }
                    CommentReplyViewHolder commentReplyViewHolder = (CommentReplyViewHolder) replyView.getTag();
                    if (commentReplyViewHolder == null) {
                        commentReplyViewHolder = new CommentReplyViewHolder();

                        commentReplyViewHolder.replyNickNameTxtView = (TextView) replyView.findViewWithTag("replyNickNameTxtView");
                        commentReplyViewHolder.replyContentTxtView = (TextView) replyView.findViewWithTag("replyContentTxtView");

                        replyView.setTag(commentReplyViewHolder);
                    }

                    commentReplyViewHolder.replyNickNameTxtView.setText(replyComment.getNickName() + ": ");
                    commentReplyViewHolder.replyContentTxtView.setText(replyComment.getComment());
                }

                for (int replyViewChildIndex = replyCount; replyViewChildIndex < commentViewHolder.commentReplyLinLay.getChildCount(); replyViewChildIndex++) {
                    View replyView = commentViewHolder.commentReplyLinLay.getChildAt(replyViewChildIndex);
                    if (replyView != null) {
                        replyView.setVisibility(View.GONE);
                    }
                }
                commentViewHolder.commentReplyLinLay.setVisibility(View.VISIBLE);
            }
        }

        /**
         * 添加image layout
         *
         * @param articleInfo
         */
        private void addImageViewLayout(ArticleInfo articleInfo) {
//            String tests[] = {
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg"
//            };
//            List<String> picList = new ArrayList<String>(Arrays.asList(tests));

            picsLinLay.setVisibility(View.GONE);

            final List<String> picList = articleInfo.getDetailPicList();
            if (picList != null && picList.size() != 0) {
                //间距
                GradientDrawable dividerDrawable = new GradientDrawable();
                dividerDrawable.setSize(context.getResources().getDimensionPixelSize(R.dimen.dp5), 0);
                dividerDrawable.setStroke(0, Color.TRANSPARENT);

                int count = picList.size();
                int rowCount = (int) Math.ceil(count * 1f / COLUMN_NUM);
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    int fromIndex = rowIndex * COLUMN_NUM;
                    int toIndex = fromIndex + COLUMN_NUM;
                    if (fromIndex < count) {
                        if (toIndex >= count) {
                            toIndex = count;
                        }
                        List<String> rowPicList = picList.subList(fromIndex, toIndex);
                        if (rowPicList == null || rowPicList.size() == 0) {
                            continue;
                        }
                        //补全 COLUMN_NUM
                        int rowPicCount = rowPicList.size();
                        if (rowPicCount < COLUMN_NUM) {
                            for (int i = 0; i < (COLUMN_NUM - rowPicCount); i++) {
                                rowPicList.add("");
                            }
                        }

                        LinearLayout linearLayout = (LinearLayout) picsLinLay.getChildAt(rowIndex);
                        if (linearLayout == null) {
                            linearLayout = new LinearLayout(context);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.topMargin = context.getResources().getDimensionPixelSize(R.dimen.dp5);
                            picsLinLay.addView(linearLayout, layoutParams);
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                        }

                        if (linearLayout.getDividerDrawable() == null) {
                            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                            linearLayout.setDividerDrawable(dividerDrawable);
                        }

                        int imgWidthHeight = imageLayoutTotalWidth / COLUMN_NUM;
                        rowPicCount = rowPicList.size();
                        for (int picIndex = 0; picIndex < rowPicCount; picIndex++) {
                            String picUrl = rowPicList.get(picIndex);
                            ImageView imageView = (ImageView) linearLayout.getChildAt(picIndex);
                            if (imageView == null) {
                                imageView = new ImageView(context);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(0, imgWidthHeight, 1);
                                linearLayout.addView(imageView, imgLp);
                            } else {
                                LinearLayout.LayoutParams imgLp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                                if (imgLp.height != imgWidthHeight) {
                                    imgLp.height = imgWidthHeight;
                                    imageView.setLayoutParams(imgLp);
                                }
                            }

                            imageView.setImageBitmap(null);
                            imageView.setOnClickListener(null);
                            imageView.setTag(String.valueOf((rowIndex * COLUMN_NUM + picIndex)));

                            if (!TextUtils.isEmpty(picUrl)) {
                                ImageLoaderUtil.displayImage(context, picUrl, imageView, getDisplayImageOptions());

                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ArrayList<String> showPicList = new ArrayList<String>();
                                        for (String str : picList) {
                                            if (!TextUtils.isEmpty(str)) {
                                                showPicList.add(str);
                                            }
                                        }
                                        ShowPhotoActivity.startActivity(context, showPicList, Integer.parseInt(v.getTag().toString()));
                                    }
                                });
                            }
                        }
                    }
                }
                for (int childIndex = rowCount; childIndex < picsLinLay.getChildCount(); childIndex++) {
                    View childView = picsLinLay.getChildAt(childIndex);
                    if (childView != null) {
                        childView.setVisibility(View.GONE);
                    }
                }
                picsLinLay.setVisibility(View.VISIBLE);
            }
        }

        class PraiseClick implements View.OnClickListener {

            private ArticleInfo articleInfo;

            /**
             * @param articleInfo
             */
            public void set(ArticleInfo articleInfo) {
                this.articleInfo = articleInfo;
            }

            @Override
            public void onClick(View v) {
                if (fragment != null) {
                    ((SocialCircleContentFragment) fragment).requestPraise(articleInfo);
                }
            }
        }

        class CommentViewHolder {
            TextView commentNickNameTxtView;
            TextView commentContentTxtView;
            LinearLayout commentReplyLinLay;
            LinearLayout commentLinLay;
            View lineView;
        }

        class CommentReplyViewHolder {
            TextView replyNickNameTxtView;
            TextView replyContentTxtView;
        }

    }
}
