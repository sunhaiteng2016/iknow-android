package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.JobDetail;
import com.beyond.popscience.module.job.adapter.JobListAdapter;

/**
 * Created by linjinfa on 2017/10/27.
 * email 331710168@qq.com
 */
public class JobListV2Adapter extends CustomBaseAdapter<JobDetail> {

    private JobListAdapter jobListAdapter;

    public JobListV2Adapter(Activity context) {
        super(context);
        jobListAdapter = new JobListAdapter(context);
        jobListAdapter.setDataList(dataList);
    }

    public JobListV2Adapter(Fragment fragment) {
        super(fragment);
        jobListAdapter = new JobListAdapter(fragment);
        jobListAdapter.setDataList(dataList);
    }

    /**
     * 1: 招聘   2：求职
     * @param type
     */
    public void setType(int type) {
        jobListAdapter.setType(type);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        jobListAdapter.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = jobListAdapter.onCreateViewHolder(parent, getItemViewType(position));
            convertView = viewHolder.itemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }

        jobListAdapter.onBindViewHolder(viewHolder, position);
        viewHolder.itemView.setClickable(false);

        return convertView;
    }

}
