package com.beyond.popscience.module.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import com.beyond.popscience.R;
public class LkAdapter extends BaseAdapter {
    public Context  context;
    public List<String>  list;

    public LkAdapter(Context context, List<String> list) {
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
        viewHolder  holder;
        if (view==null){
            holder= new viewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.item_only_textview_two,null);
            holder.tv= (TextView) view.findViewById(R.id.tv_only_textview);
            view.setTag(holder);
        }else{
            holder= (viewHolder) view.getTag();
        }
        holder.tv.setText(list.get(i));
        return view;
    }

    class  viewHolder {
        TextView  tv;
    }
}
