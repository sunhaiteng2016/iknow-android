package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.util.ImageLoaderUtil;

/**
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialGridAdapter extends CustomBaseAdapter<SocialInfo> {

    public SocialGridAdapter(Activity context) {
        super(context);
    }

    public SocialGridAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_social_list_grid_item, parent, false);
            viewHolder.logoImgView = (ImageView) convertView.findViewById(R.id.logoImgView);
            viewHolder.nameTxtView = (TextView) convertView.findViewById(R.id.nameTxtView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SocialInfo socialInfo = dataList.get(position);
        ImageLoaderUtil.displayImage(context, socialInfo.getLogo(), viewHolder.logoImgView, getAvatarDisplayImageOptions());
        viewHolder.nameTxtView.setText(socialInfo.getName());
        return convertView;
    }

    class ViewHolder{
        ImageView logoImgView;
        TextView nameTxtView;
    }

}
