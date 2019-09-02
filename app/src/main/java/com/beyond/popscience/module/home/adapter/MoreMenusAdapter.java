package com.beyond.popscience.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.ImageUtils;
import com.beyond.popscience.module.home.entity.Menus;
import com.bumptech.glide.Glide;

import java.util.List;

public class MoreMenusAdapter extends BaseAdapter {

    public List<Menus.UserAllTabBean> list;
    public Context context;
    private OnAddClickLinenter click;


    public MoreMenusAdapter(List<Menus.UserAllTabBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menus, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(list.get(i).getTabpic()).into(holder.img);
        //这个地方是减
        Glide.with(context).load(R.drawable.jian1).into(holder.img_iv);
        holder.name.setText(list.get(i).getTabname());
        holder.ll_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onClicks(view,i);
            }
        });
        return view;
    }

    public class ViewHolder {
        public TextView name;
        public ImageView img;
        public LinearLayout ll_dz;
        public ImageView img_iv;

        public ViewHolder(View view) {
            img = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            ll_dz = (LinearLayout) view.findViewById(R.id.ll_dz);
            img_iv = (ImageView) view.findViewById(R.id.img_iv);
        }
    }

    public  interface  OnAddClickLinenter{
        void onClicks(View view,int position);
    }
    public void setOnAddClick(OnAddClickLinenter click){
        this.click=click;
    }
}
