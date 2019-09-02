package com.beyond.popscience.module.news.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beyond.library.util.ClipBoardUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.frame.view.ViewUtils;
import com.beyond.popscience.module.news.CommentDetailActivity;
import com.beyond.popscience.module.news.CommentListActivity;

/**
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class CommentListAdapter extends CustomBaseAdapter<Comment> {

    /**
     * 是否回复列表  回复界面使用
     */
    private boolean isReply = false;
    /**
     * 更多操作
     */
    private PopupWindow morePopWindow;

    public CommentListAdapter(Activity context) {
        super(context);
    }

    /**
     *
     * @param reply
     * @return
     */
    public CommentListAdapter setReply(boolean reply) {
        isReply = reply;
        return this;
    }

    /**
     * 释放相关资源
     */
    public void release(){
        if(morePopWindow!=null){
            morePopWindow.dismiss();
            morePopWindow = null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        MoreClick moreClick = null;
        PraiseClick praiseClick = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            moreClick = new MoreClick();
            praiseClick = new PraiseClick();

            convertView = inflater.inflate(R.layout.adapter_comment_item, parent, false);
            viewHolder.praiseLinLay = (LinearLayout) convertView.findViewById(R.id.praiseLinLay);
            viewHolder.lookReplyTxtView = (TextView) convertView.findViewById(R.id.lookReplyTxtView);
            viewHolder.timeStampTxtView = (TextView) convertView.findViewById(R.id.timeStampTxtView);
            viewHolder.authorTxtView = (TextView) convertView.findViewById(R.id.authorTxtView);
            viewHolder.praiseNumTxtView = (TextView) convertView.findViewById(R.id.praiseNumTxtView);
            viewHolder.commentContentTxtView = (TextView) convertView.findViewById(R.id.commentContentTxtView);
            viewHolder.allReplyLinLay = (LinearLayout) convertView.findViewById(R.id.allReplyLinLay);
            viewHolder.lineView = convertView.findViewById(R.id.lineView);
            viewHolder.praiseImgView = (ImageView) convertView.findViewById(R.id.praiseImgView);
            viewHolder.avatarImgView = (ImageView) convertView.findViewById(R.id.avatarImgView);
            viewHolder.moreImgView = (ImageView) convertView.findViewById(R.id.moreImgView);

            viewHolder.moreImgView.setOnClickListener(moreClick);
            viewHolder.praiseLinLay.setOnClickListener(praiseClick);

            convertView.setTag(viewHolder);
            convertView.setTag(viewHolder.moreImgView.getId(), moreClick);
            convertView.setTag(viewHolder.praiseLinLay.getId(), praiseClick);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            moreClick = (MoreClick) convertView.getTag(viewHolder.moreImgView.getId());
            praiseClick = (PraiseClick) convertView.getTag(viewHolder.praiseLinLay.getId());
        }

        Comment comment = dataList.get(position);

        moreClick.set(comment, position);
        praiseClick.set(comment, viewHolder);

        viewHolder.praiseImgView.setImageResource(comment.isPraised() ? R.drawable.icon_like_red : R.drawable.icon_like);

        ImageLoaderUtil.displayImage(context, comment.getAvatar(), viewHolder.avatarImgView, getAvatarDisplayImageOptions());
        viewHolder.authorTxtView.setText(comment.getNickName());
        viewHolder.praiseNumTxtView.setText(comment.getPraiseNum());
        viewHolder.timeStampTxtView.setText(Util.getDisplayDateTime(BeyondApplication.getInstance().getCurrSystemTime(), comment.getCreateTime()));
        viewHolder.commentContentTxtView.setText(comment.getComment());

        if(isReply){    //回复列表
            viewHolder.lookReplyTxtView.setVisibility(View.GONE);
            viewHolder.moreImgView.setVisibility(View.GONE);
            if(position == 0){
                viewHolder.lineView.setVisibility(View.GONE);
                viewHolder.allReplyLinLay.setVisibility(View.VISIBLE);
            }else{
                viewHolder.lineView.setVisibility(View.VISIBLE);
                viewHolder.allReplyLinLay.setVisibility(View.GONE);

//                String replayNickNameTxt = "回复一杯清水: ";
//                SpannableString spannableString = new SpannableString(replayNickNameTxt+"第七届很快我就回去额客户全文全文近期考核为轻微第七届很快我就回去额客户全文全文近期考核为轻微");
//                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#9B9B9B")), 0, replayNickNameTxt.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//                viewHolder.commentContentTxtView.setText(spannableString);
            }
        }else{
            viewHolder.allReplyLinLay.setVisibility(View.GONE);

            if(comment.getReplyNumLong()>0){
                viewHolder.lookReplyTxtView.setVisibility(View.VISIBLE);
                viewHolder.lookReplyTxtView.setText("查看全部"+comment.getReplyNum()+"条回复");
            }else{
                viewHolder.lookReplyTxtView.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    class ViewHolder{
        LinearLayout praiseLinLay;
        TextView lookReplyTxtView;
        TextView timeStampTxtView;
        TextView authorTxtView;
        TextView praiseNumTxtView;
        TextView commentContentTxtView;
        ImageView praiseImgView;
        ImageView avatarImgView;
        ImageView moreImgView;
        LinearLayout allReplyLinLay;
        View lineView;
    }

    /**
     * 点赞
     */
    class PraiseClick implements View.OnClickListener{

        private Comment comment;
        private ViewHolder viewHolder;

        /**
         *
         * @param comment
         */
        public void set(Comment comment, ViewHolder viewHolder){
            this.comment = comment;
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            if(comment.isPraised()){
                return ;
            }

//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.playTogether(ObjectAnimator.ofFloat(viewHolder.praiseImgView, "scaleX", 1f, 1.5f, 1f), ObjectAnimator.ofFloat(viewHolder.praiseImgView, "scaleY", 1f, 1.5f, 1f));
//            animatorSet.setDuration(300);
//            animatorSet.start();

            if(context instanceof CommentListActivity){
                ((CommentListActivity)context).requestPraise(comment);
            }else if(context instanceof CommentDetailActivity){
                ((CommentDetailActivity)context).requestPraise(comment);
            }
        }

    }

    /**
     * 更多
     */
    class MoreClick implements View.OnClickListener{

        private Comment comment;
        private int position;

        /**
         *
         * @param comment
         */
        public void set(Comment comment, int position){
            this.comment = comment;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(morePopWindow == null){
                morePopWindow = new PopupWindow();
                morePopWindow.setContentView(inflater.inflate(R.layout.menu_comment_more, null));
                morePopWindow.setOutsideTouchable(true);
                morePopWindow.setTouchable(true);
                morePopWindow.setFocusable(true);

                morePopWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

                if(morePopWindow.getContentView().getMeasuredWidth() == 0){
                    ViewUtils.measureView(morePopWindow.getContentView());
                }

                morePopWindow.setWidth(morePopWindow.getContentView().getMeasuredWidth());
                morePopWindow.setHeight(morePopWindow.getContentView().getMeasuredHeight());

                TextView replyTxtView = (TextView) morePopWindow.getContentView().findViewById(R.id.replyTxtView);
                TextView copyTxtView = (TextView) morePopWindow.getContentView().findViewById(R.id.copyTxtView);
                TextView reportTxtView = (TextView) morePopWindow.getContentView().findViewById(R.id.reportTxtView);
                replyTxtView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(context instanceof CommentListActivity){
                            ((CommentListActivity)context).showReplyPublishedComment(comment, position);
                        }
                        morePopWindow.dismiss();
                    }
                });
                reportTxtView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(context instanceof CommentListActivity){
                            ((CommentListActivity)context).requestReport(comment);
                        }
                        morePopWindow.dismiss();
                    }
                });
                copyTxtView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipBoardUtil.copy(comment.getComment(), context);
                        ToastUtil.showCenter(context, "复制成功");

                        morePopWindow.dismiss();
                    }
                });
            }
            morePopWindow.showAsDropDown(v, (int) (-(morePopWindow.getWidth() - v.getMeasuredWidth()) + v.getMeasuredWidth() / 3.4), -v.getMeasuredHeight()/3);
        }

    }

}
