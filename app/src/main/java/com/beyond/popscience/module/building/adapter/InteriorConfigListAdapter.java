package com.beyond.popscience.module.building.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;

/**
 * Created by linjinfa on 2017/10/14.
 * email 331710168@qq.com
 */
public class InteriorConfigListAdapter extends CustomBaseAdapter<String> {

    public InteriorConfigListAdapter(Activity context) {
        super(context);
    }

    public InteriorConfigListAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_interior_config_list_item, parent, false);

            viewHolder.nameTxtView = (TextView) convertView.findViewById(R.id.nameTxtView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTxtView.setText(dataList.get(position));

        return convertView;
    }

    class ViewHolder{
        TextView nameTxtView;
    }

}
