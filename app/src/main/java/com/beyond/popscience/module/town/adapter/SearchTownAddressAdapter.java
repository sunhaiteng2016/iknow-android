package com.beyond.popscience.module.town.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

public class SearchTownAddressAdapter extends CustomBaseAdapter<Address> {

    public SearchTownAddressAdapter(Activity context) {
        super(context);
    }

    public SearchTownAddressAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= null;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_search_town_address_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Address address = dataList.get(position);
        holder.setData(address);

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.addressNameTxtView)
        TextView addressNameTxtView;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setData(Address address){
            String name = "";
            Address parentAddress = address.getParentAddress();
            if(parentAddress!=null && !TextUtils.isEmpty(parentAddress.getName())){
                name +=parentAddress.getName()+"•";
            }
            name +=address.getName();
            addressNameTxtView.setText(name);
        }
    }
}
