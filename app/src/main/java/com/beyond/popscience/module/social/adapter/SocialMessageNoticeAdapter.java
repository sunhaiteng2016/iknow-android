package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.ArticleRemind;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 消息
 * Created by linjinfa on 2017/6/24.
 * email 331710168@qq.com
 */
public class SocialMessageNoticeAdapter extends CustomBaseAdapter<ArticleRemind> {

    public SocialMessageNoticeAdapter(Activity context) {
        super(context);
    }

    public SocialMessageNoticeAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_social_message_notice_item, parent, false);
            viewHolder.avatarImgView = (CircleImageView) convertView.findViewById(R.id.avatarImgView);
            viewHolder.picImgView = (ImageView) convertView.findViewById(R.id.picImgView);
            viewHolder.authorTxtView = (TextView) convertView.findViewById(R.id.authorTxtView);
            viewHolder.timeStampTxtView = (TextView) convertView.findViewById(R.id.timeStampTxtView);
            viewHolder.contentTxtView = (TextView) convertView.findViewById(R.id.contentTxtView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArticleRemind articleRemind = dataList.get(position);

        ImageLoaderUtil.displayImage(context, articleRemind.getAvatar(), viewHolder.avatarImgView, getAvatarDisplayImageOptions());
        ImageLoaderUtil.displayImage(context, articleRemind.getPic(), viewHolder.picImgView);
        viewHolder.authorTxtView.setText(articleRemind.getNickName());
        viewHolder.timeStampTxtView.setText(Util.getDisplayDateTime(BeyondApplication.getInstance().getCurrSystemTime(), articleRemind.getPublishTime()));
        viewHolder.contentTxtView.setText(articleRemind.getComment());

        return convertView;
    }

    class ViewHolder{
        CircleImageView avatarImgView;
        ImageView picImgView;
        TextView authorTxtView;
        TextView timeStampTxtView;
        TextView contentTxtView;
    }

}
