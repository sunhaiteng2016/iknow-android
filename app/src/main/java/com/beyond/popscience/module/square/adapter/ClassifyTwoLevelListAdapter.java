package com.beyond.popscience.module.square.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.ClassifyMenu;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class ClassifyTwoLevelListAdapter extends CustomBaseAdapter<ClassifyMenu> {

    private String selectStr;

    public ClassifyTwoLevelListAdapter(Activity context) {
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
        ClassifyTwoLevelListAdapter.ViewHolder holder= null;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_classify_two_level_item, parent, false);
            holder =new ClassifyTwoLevelListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ClassifyTwoLevelListAdapter.ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position), position);
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.textView)
        TextView textView;


        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setData(ClassifyMenu info, int position){
            textView.setText(info.getMenuName());
            if(!TextUtils.isEmpty(selectStr) && selectStr.equals(info.getMenuName())){
                textView.setTextColor(context.getResources().getColor(R.color.blue2));
                imageView.setVisibility(View.VISIBLE);
            } else {
                textView.setTextColor(context.getResources().getColor(R.color.grey17));
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
