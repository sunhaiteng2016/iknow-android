package com.beyond.popscience.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.module.home.entity.OpenCity;

import java.util.List;

/**
 * Created by sht on 2018/6/17.
 */

public class OpenlistCityElv extends BaseExpandableListAdapter {
    public Context context;
    public List<OpenCity.DataBean> list;
    public LayoutInflater mInflater;

    public OpenlistCityElv(Context context, List<OpenCity.DataBean> list) {
        this.context = context;
        this.list = list;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getChild().size();
    }

    @Override
    public Object getGroup(int i) {
        return i;
    }

    @Override
    public Object getChild(int i, int i1) {
        return i1;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder1 holder1 ;
        if (view==null){
            view=mInflater.inflate(R.layout.item_only_textview,null);
            holder1= new ViewHolder1();
            holder1.parentview= (TextView) view.findViewById(R.id.tv_only_textview);
            holder1.iv= (ImageView) view.findViewById(R.id.open_iv);
            holder1.iv.setVisibility(View.VISIBLE);
            holder1.parentview.setPadding(50,8,8,8);
            holder1.parentview.setTextSize(18);
            view.setTag(holder1);
        }else{
            holder1= (ViewHolder1) view.getTag();
        }
        holder1.parentview.setText(list.get(i).getAreaName());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder2 holder2 ;
        if (view==null){
            view=mInflater.inflate(R.layout.item_only_textview,null);
            holder2= new ViewHolder2();
            holder2.childview= (TextView) view.findViewById(R.id.tv_only_textview);
            holder2.childview.setPadding(80,5,5,5);
            holder2.childview.setTextSize(18);
            holder2.childview.setTextColor(context.getResources().getColor(R.color.blue));
            view.setTag(holder2);
        }else{
            holder2= (ViewHolder2) view.getTag();
        }
        holder2.childview.setText(list.get(i).getChild().get(i1).getAreaName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class  ViewHolder1{
        TextView parentview;
        ImageView iv;
    }
    public class  ViewHolder2{
        TextView childview;
    }
}
