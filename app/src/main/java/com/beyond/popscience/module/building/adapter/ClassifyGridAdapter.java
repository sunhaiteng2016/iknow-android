package com.beyond.popscience.module.building.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.SelectStatusInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danxiang.feng on 2017/10/14.
 */

public class ClassifyGridAdapter extends CustomBaseAdapter<SelectStatusInfo> {

    public ClassifyGridAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassifyGridAdapter.ViewHolder holder= null;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_classify_building_item, parent, false);
            holder =new ClassifyGridAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ClassifyGridAdapter.ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position), position);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.textView)
        TextView textView;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setData(SelectStatusInfo info, int position){
            if (info!= null) {
                textView.setText(info.getTitle());
                if(info.isSelected()){
                    textView.setTextColor(context.getResources().getColor(R.color.blue2));
                } else {
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
        }
    }
}
