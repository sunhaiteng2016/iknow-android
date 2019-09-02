package com.beyond.popscience.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.module.home.entity.MyMenu;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuGridviewAdapter extends BaseAdapter {
    public Context context;
    public List<MyMenu> list;

    public MenuGridviewAdapter(Context context, List<MyMenu> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view==null){
            view=LayoutInflater.from(context).inflate(R.layout.item_menus_two,null);
            holder= new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        Glide.with(context).load(list.get(i).getTabpic()).error(R.drawable.more).into(holder.img);
        holder.name.setText(list.get(i).getTabname());
        return view;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.item_menu)
        RelativeLayout itemMenu;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
