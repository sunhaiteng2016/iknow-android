package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.module.building.adapter.BuildingListAdapter;

/**
 * Created by linjinfa on 2017/10/27.
 * email 331710168@qq.com
 */
public class BuildingListV2Adapter extends CustomBaseAdapter<BuildingDetail> {

    private BuildingListAdapter buildingListAdapter;

    public BuildingListV2Adapter(Activity context) {
        super(context);
        buildingListAdapter = new BuildingListAdapter(context);
        buildingListAdapter.setDataList(dataList);
    }

    public BuildingListV2Adapter(Fragment fragment) {
        super(fragment);
        buildingListAdapter = new BuildingListAdapter(fragment);
        buildingListAdapter.setDataList(dataList);
    }

    /**
     * 1: 出租出售   2：求租求购
     * @param type
     */
    public void setType(int type) {
        buildingListAdapter.setType(type);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        buildingListAdapter.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = buildingListAdapter.onCreateViewHolder(parent, getItemViewType(position));
            convertView = viewHolder.itemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }

        buildingListAdapter.onBindViewHolder(viewHolder, position);
        viewHolder.itemView.setClickable(false);

        return convertView;
    }

}
