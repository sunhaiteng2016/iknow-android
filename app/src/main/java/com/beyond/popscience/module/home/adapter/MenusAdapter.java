package com.beyond.popscience.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond.popscience.R;
import com.beyond.popscience.module.home.entity.MyMenu;
import com.bumptech.glide.Glide;

import java.util.List;

public class MenusAdapter extends BaseAdapter {


    public List<MyMenu> list;
    public Context context;

    public MenusAdapter(List<MyMenu> list, Context context) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menus, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }

    public class ViewHolder {
        public TextView name;
        public ImageView img;
        public RelativeLayout item_menu;
        public LinearLayout ll_dz;
        public ViewHolder(View view) {
            img = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            item_menu = (RelativeLayout) view.findViewById(R.id.item_menu);
            ll_dz = (LinearLayout) view.findViewById(R.id.ll_dz);
        }
    }
}
