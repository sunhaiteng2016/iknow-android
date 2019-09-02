package com.beyond.popscience.module.news.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.net.VideoRestUsage;
import com.beyond.popscience.frame.pojo.NewsDetailContent;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.util.VideoDownloader;
import com.beyond.popscience.module.home.entity.TownUser;
import com.beyond.popscience.module.news.ShowPhotoActivity;
import com.beyond.popscience.module.news.VideoPlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class NewsUserListAdapter extends CustomBaseAdapter<TownUser> {


    public NewsUserListAdapter(Fragment fragment) {
        super(fragment);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView = inflater.inflate(R.layout.item_user, parent, false);
            viewHolder.contextTxtView = (TextView) convertView.findViewById(R.id.contextTxtViewss);
            viewHolder.picImgView = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TownUser newsDetailContent = dataList.get(position);
        viewHolder.contextTxtView.setText(newsDetailContent.getNickname());
        Glide.with(context).load(newsDetailContent.getAvatar()).into(viewHolder.picImgView);
        return convertView;
    }

    class ViewHolder {
        TextView contextTxtView;
        ImageView picImgView;
    }

}
