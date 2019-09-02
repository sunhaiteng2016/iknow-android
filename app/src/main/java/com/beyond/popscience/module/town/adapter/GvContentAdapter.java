package com.beyond.popscience.module.town.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.home.entity.Address;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/6/23.
 */

public class GvContentAdapter extends CustomBaseAdapter<Address> {
    public GvContentAdapter(Activity context) {
        super(context);
    }

    public GvContentAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= null;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_category_content, parent, false);
            holder =new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position), position);
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.circleAddressNameTxtView)
        TextView circleAddressNameTxtView;

        @BindView(R.id.cvHeader)
        ImageView cvHeaderImgView;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setData(Address address, int position){
            if(position == 0){
                cvHeaderImgView.setVisibility(View.GONE);
                tvTitle.setVisibility(View.INVISIBLE);
                circleAddressNameTxtView.setVisibility(View.VISIBLE);

                circleAddressNameTxtView.setText(address.getName());
            }else{
                cvHeaderImgView.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                circleAddressNameTxtView.setVisibility(View.GONE);

                tvTitle.setText(address.getName());
                ImageLoaderUtil.displayImage(context, address.getPic(), cvHeaderImgView);
            }
        }
    }
}
