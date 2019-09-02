package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.Comment;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by linjinfa on 2017/10/12.
 * email 331710168@qq.com
 */
public class GoodsCommentAdapter extends CustomBaseAdapter<Comment> {

    public GoodsCommentAdapter(Activity context) {
        super(context);
    }

    public GoodsCommentAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_goods_comment_item, parent, false);

            viewHolder.avatarImgView = (CircleImageView) convertView.findViewById(R.id.avatarImgView);
            viewHolder.authorTxtView = (TextView) convertView.findViewById(R.id.authorTxtView);
            viewHolder.timeStampTxtView = (TextView) convertView.findViewById(R.id.timeStampTxtView);
            viewHolder.commentContentTxtView = (TextView) convertView.findViewById(R.id.commentContentTxtView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Comment comment = dataList.get(position);

        ImageLoaderUtil.displayImage(context, comment.getAvatar(), viewHolder.avatarImgView, getAvatarDisplayImageOptions());
        viewHolder.authorTxtView.setText(comment.getNickName());
        viewHolder.timeStampTxtView.setText(Util.getDisplayDateTime(BeyondApplication.getInstance().getCurrSystemTime(), comment.getCreateTime()));
        viewHolder.commentContentTxtView.setText(comment.getComment());

        return convertView;
    }

    class ViewHolder{
        CircleImageView avatarImgView;
        TextView authorTxtView;
        TextView timeStampTxtView;
        TextView commentContentTxtView;
    }

}
