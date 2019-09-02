package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.NavObj;

import java.util.HashMap;
import java.util.List;

/**
 * 为订阅 gridadapter
 * Created by yaoc.cui on 2017/6/11.
 */

public class UnSubAdapter extends CustomBaseAdapter<NavObj> {
    private IUnSubListener mListener;

    public UnSubAdapter(Fragment context, List<NavObj> list) {
        super(context);
        this.dataList = list;
    }

    public void changeData(List<NavObj> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void setUnSubListener(IUnSubListener listener){
        this.mListener = listener;
    }
    /**
     * 删除项
     * @param position
     */
    public void remove(int position){
        if (position<dataList.size()){
            dataList.remove(position);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加项
     * @param nav
     */
    public void add(NavObj nav){
        dataList.add(nav);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public NavObj getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 由于复用convertView导致某些item消失了，所以这里不复用item，
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_grid, null);
        TextView mTag = (TextView) convertView.findViewById(R.id.tvItem);
        Button deletebtn = (Button) convertView.findViewById(R.id.imgDelete);
        deletebtn.setVisibility(View.GONE);
        mTag.setText(dataList.get(position).getClassName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!= null){
                    mListener.onUnSubItemClick(position);
                }
            }
        });

        return convertView;
    }

    public interface IUnSubListener{
        /**
         * 点击item
         * @param position
         */
        void onUnSubItemClick(int position);
    }

}
