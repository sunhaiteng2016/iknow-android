package com.beyond.popscience.module.town.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.module.home.entity.Address;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yao.cui on 2017/6/23.
 */

public class TownCategoryAdapter extends CustomBaseAdapter<Address> {

    public TownCategoryAdapter(Activity context) {
        super(context);
    }

    public TownCategoryAdapter(Fragment fragment) {
        super(fragment);
    }

    private int currentChecked = 0;

    /**
     * 设置选中的位置
     * @param position
     */
    public void setCurrentChecked(int position){
        this.currentChecked = position;
    }

    /**
     *
     * @return
     */
    public Address getSelectedAddress(){
        return getItem(currentChecked);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= null;

        if (convertView== null){
            convertView= inflater.inflate(R.layout.item_town_category,parent,false);
            holder =new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(dataList.get(position), position == this.currentChecked);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.rbTitle)
        TextView rbTitle;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setData(Address address,boolean checked){
            rbTitle.setText(address.getName());
            rbTitle.setSelected(checked);
        }
    }
}
