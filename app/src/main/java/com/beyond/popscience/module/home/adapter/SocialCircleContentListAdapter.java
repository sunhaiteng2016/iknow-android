package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.ArticleInfo;
import com.beyond.popscience.module.social.adapter.SocialCircleContentListRecyclerAdapter;

/**
 * Created by linjinfa on 2017/11/5.
 * email 331710168@qq.com
 */
public class SocialCircleContentListAdapter extends CustomBaseAdapter<ArticleInfo> {

    private SocialCircleContentListRecyclerAdapter socialCircleContentListRecyclerAdapter;

    public SocialCircleContentListAdapter(Activity context) {
        super(context);
        socialCircleContentListRecyclerAdapter = new SocialCircleContentListRecyclerAdapter(context);
        socialCircleContentListRecyclerAdapter.setDataList(dataList);
        socialCircleContentListRecyclerAdapter.setShowInMySocial(true);
    }

    public SocialCircleContentListAdapter(Fragment fragment) {
        super(fragment);
        socialCircleContentListRecyclerAdapter = new SocialCircleContentListRecyclerAdapter(fragment);
        socialCircleContentListRecyclerAdapter.setDataList(dataList);
        socialCircleContentListRecyclerAdapter.setShowInMySocial(true);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        socialCircleContentListRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = socialCircleContentListRecyclerAdapter.onCreateViewHolder(parent, getItemViewType(position));
            convertView = viewHolder.itemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }

        socialCircleContentListRecyclerAdapter.onBindViewHolder(viewHolder, position);
        viewHolder.itemView.setClickable(false);

        return convertView;
    }

}
