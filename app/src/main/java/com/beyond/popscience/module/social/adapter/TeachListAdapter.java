package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.NoticeDetail;
import com.beyond.popscience.frame.pojo.TeachItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;

/**
 *
 *
 * Created by yao.cui on 2017/7/17.
 */

public class TeachListAdapter extends CustomRecyclerBaseAdapter<TeachItem> {
    public TeachListAdapter(Activity context) {
        super(context);
    }

    public TeachListAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder)holder).setData(getDataList().get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder, position, getItemId(position));
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_teach_list,parent,false));
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvDate;
        TextView tvDes;
        ImageView ivCover;

        public MyViewHolder(View view){
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvDes = (TextView) view.findViewById(R.id.tvDes);
            ivCover = (ImageView) view.findViewById(R.id.ivCover);
        }

        public void setData(TeachItem detail){
            tvTitle.setText(detail.getTitle());
            tvDate.setText(detail.getPublishTime());
            tvDes.setText(detail.getDescription());
            ImageLoaderUtil.displayImage(context,detail.getCoverPic(),ivCover,getDisplayImageOptions());
        }

    }
}
