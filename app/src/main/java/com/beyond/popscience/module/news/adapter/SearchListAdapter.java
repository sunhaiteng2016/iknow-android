package com.beyond.popscience.module.news.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.SearchObj;

/**
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class SearchListAdapter extends CustomRecyclerBaseAdapter<SearchObj> {

    public SearchListAdapter(Activity context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchItemViewHolder(inflater.inflate(R.layout.adapter_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class SearchItemViewHolder extends RecyclerView.ViewHolder{

        public SearchItemViewHolder(View itemView) {
            super(itemView);
        }
    }

}
