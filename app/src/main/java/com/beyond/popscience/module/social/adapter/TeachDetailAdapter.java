package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.TeachItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.news.VideoPlayerActivity;

/**
 * Created by lenovo on 2017/7/20.
 */

public class TeachDetailAdapter extends CustomBaseAdapter<TeachItem> {
    public TeachDetailAdapter(Activity context) {
        super(context);
    }

    public TeachDetailAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.teach_detail_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position));
        ViewGroup.LayoutParams params = holder.rlCover.getLayoutParams();
        params.height = Util.getImageHeight(DensityUtil.getScreenWidth(context));
        holder.rlCover.setLayoutParams(params);
        holder.ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayerActivity.startActivity(context,getItem(position).getCoverPic(),
                        getItem(position).getVedioUrl());
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvDes;
        ImageView ivCover;
        RelativeLayout rlCover;

        public ViewHolder(View view){
            tvDes = (TextView) view.findViewById(R.id.tvDes);
            ivCover = (ImageView) view.findViewById(R.id.ivCover);
            rlCover = (RelativeLayout) view.findViewById(R.id.rlCover);
        }

        public void setData(TeachItem item){
            tvDes.setText(item.getDescription());
            ImageLoaderUtil.displayImage(context,item.getCoverPic(),ivCover,getDisplayImageOptions());
        }

    }
}
