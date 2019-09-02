package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.TeachItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.home.entity.TownUser;
import com.bumptech.glide.Glide;

/**
 *
 *
 * Created by yao.cui on 2017/7/17.
 */

public class ChengyuanListAdapter extends CustomRecyclerBaseAdapter<TownUser> {
    public ChengyuanListAdapter(Activity context) {
        super(context);
    }

    public ChengyuanListAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder)holder).setData(getDataList().get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_user,parent,false));
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView ivCover;

        public MyViewHolder(View view){
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.contextTxtViewss);
            ivCover = (ImageView) view.findViewById(R.id.img);
        }

        public void setData(TownUser detail){
            tvTitle.setText(detail.getNickname());
            Glide.with(context).load(detail.getAvatar()).into(ivCover);
        }

    }
}
