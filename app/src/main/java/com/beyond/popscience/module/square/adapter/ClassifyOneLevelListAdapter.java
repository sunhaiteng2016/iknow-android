package com.beyond.popscience.module.square.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.ClassifyInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class ClassifyOneLevelListAdapter extends CustomBaseAdapter<ClassifyInfo> {

    private String selectStr;

    public ClassifyOneLevelListAdapter(Activity context) {
        super(context);
    }

    public void setSelectStr(String selectStr){
        this.selectStr = selectStr;
    }

    public String getSelectStr(){
        return selectStr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassifyOneLevelListAdapter.ViewHolder holder= null;

        if (convertView== null){
            convertView= inflater.inflate(R.layout.adapter_classify_one_level_item,parent,false);
            holder =new ClassifyOneLevelListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ClassifyOneLevelListAdapter.ViewHolder) convertView.getTag();
        }

        holder.setData(dataList.get(position), position);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.rbTitle)
        TextView rbTitle;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setData(ClassifyInfo info, int position){
            rbTitle.setText(info.getName());
            if(!TextUtils.isEmpty(selectStr) && selectStr.equals(info.getName())) {
                rbTitle.setSelected(true);
            } else {
                rbTitle.setSelected(false);
            }
        }
    }
}
