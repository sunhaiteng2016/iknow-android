package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.news.CommentDetailActivity;
import com.beyond.popscience.module.news.CommentListActivity;
import com.beyond.popscience.module.social.SocialCommentDetailActivity;

/**
 * Created by linjinfa on 2017/6/10.
 * email 331710168@qq.com
 */
public class SocialCommentListAdapter extends CustomBaseAdapter<Comment> {


    public SocialCommentListAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        PraiseClick praiseClick = null;
        ReplyClick replyClick = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            praiseClick = new PraiseClick();
            replyClick = new ReplyClick();

            convertView = inflater.inflate(R.layout.adapter_social_comment_item, parent, false);
            viewHolder.infoLinLay = (LinearLayout) convertView.findViewById(R.id.infoLinLay);
            viewHolder.contentLinLay = (LinearLayout) convertView.findViewById(R.id.contentLinLay);
            viewHolder.picsLinLay = (LinearLayout) convertView.findViewById(R.id.picsLinLay);
            viewHolder.replyLinLay = (LinearLayout) convertView.findViewById(R.id.replyLinLay);
            viewHolder.praiseLinLay = (LinearLayout) convertView.findViewById(R.id.praiseLinLay);
            viewHolder.timeStampTxtView = (TextView) convertView.findViewById(R.id.timeStampTxtView);
            viewHolder.authorTxtView = (TextView) convertView.findViewById(R.id.authorTxtView);
            viewHolder.praiseNumTxtView = (TextView) convertView.findViewById(R.id.praiseNumTxtView);
            viewHolder.commentContentTxtView = (TextView) convertView.findViewById(R.id.commentContentTxtView);
            viewHolder.lineView = convertView.findViewById(R.id.lineView);
            viewHolder.praiseImgView = (ImageView) convertView.findViewById(R.id.praiseImgView);
            viewHolder.avatarImgView = (ImageView) convertView.findViewById(R.id.avatarImgView);

            viewHolder.praiseLinLay.setOnClickListener(praiseClick);
            viewHolder.replyLinLay.setOnClickListener(replyClick);

            convertView.setTag(viewHolder);
            convertView.setTag(viewHolder.praiseLinLay.getId(), praiseClick);
            convertView.setTag(viewHolder.replyLinLay.getId(), replyClick);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            praiseClick = (PraiseClick) convertView.getTag(viewHolder.praiseLinLay.getId());
            replyClick = (ReplyClick) convertView.getTag(viewHolder.replyLinLay.getId());
        }

        Comment comment = dataList.get(position);

        praiseClick.set(comment, viewHolder);

        replyClick.set(comment);

        viewHolder.praiseImgView.setImageResource(comment.isPraised() ? R.drawable.icon_like_red : R.drawable.icon_like);

        ImageLoaderUtil.displayImage(context, comment.getAvatar(), viewHolder.avatarImgView, getAvatarDisplayImageOptions());
        viewHolder.authorTxtView.setText(comment.getNickName());
        viewHolder.praiseNumTxtView.setText(comment.getPraiseNum());
        viewHolder.timeStampTxtView.setText(Util.getDisplayDateTime(BeyondApplication.getInstance().getCurrSystemTime(), comment.getCreateTime()));
        viewHolder.commentContentTxtView.setText(comment.getComment());


        RelativeLayout.LayoutParams avatarLayoutParams = (RelativeLayout.LayoutParams) viewHolder.avatarImgView.getLayoutParams();
        RelativeLayout.LayoutParams contentLayoutParams = (RelativeLayout.LayoutParams) viewHolder.contentLinLay.getLayoutParams();
        if(TextUtils.isEmpty(comment.getReplyId())){    //评论
            contentLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, viewHolder.infoLinLay.getId());
            avatarLayoutParams.leftMargin = 0;
            viewHolder.replyLinLay.setVisibility(View.VISIBLE);
        }else{  //回复
            contentLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, viewHolder.avatarImgView.getId());
            avatarLayoutParams.leftMargin = avatarLayoutParams.width;
            viewHolder.replyLinLay.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder{
        LinearLayout praiseLinLay;
        LinearLayout infoLinLay;
        LinearLayout contentLinLay;
        LinearLayout picsLinLay;
        LinearLayout replyLinLay;
        TextView timeStampTxtView;
        TextView authorTxtView;
        TextView praiseNumTxtView;
        TextView commentContentTxtView;
        ImageView praiseImgView;
        ImageView avatarImgView;
        View lineView;
    }

    /**
     * 回复
     */
    class ReplyClick implements View.OnClickListener{

        private Comment comment;

        /**
         *
         * @param comment
         */
        public void set(Comment comment){
            this.comment = comment;
        }

        @Override
        public void onClick(View v) {
            if(context instanceof SocialCommentDetailActivity){
                ((SocialCommentDetailActivity)context).showReplyView(comment);
            }
        }
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

            if(context instanceof CommentListActivity){
                ((CommentListActivity)context).requestPraise(comment);
            }else if(context instanceof CommentDetailActivity){
                ((CommentDetailActivity)context).requestPraise(comment);
            }
        }

    }

}
