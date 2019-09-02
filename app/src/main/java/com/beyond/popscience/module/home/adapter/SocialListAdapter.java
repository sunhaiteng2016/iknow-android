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
public class SocialListAdapter extends CustomBaseAdapter<SocialInfo> {

    public SocialListAdapter(Activity context) {
        super(context);
    }

    public SocialListAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_social_list_item, parent, false);

            viewHolder.logoImgView = (ImageView) convertView.findViewById(R.id.logoImgView);
            viewHolder.nickNameTxtView = (TextView) convertView.findViewById(R.id.nickNameTxtView);
            viewHolder.updateTipTxtView = (TextView) convertView.findViewById(R.id.updateTipTxtView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SocialInfo socialInfo = dataList.get(position);


        ImageLoaderUtil.displayImage(context, socialInfo.getLogo(), viewHolder.logoImgView, getAvatarDisplayImageOptions());
        viewHolder.nickNameTxtView.setText(socialInfo.getName());

        if(socialInfo.getUpdateNumLong()>0){
            viewHolder.updateTipTxtView.setText(socialInfo.getUpdateNum()+"条更新");

            viewHolder.updateTipTxtView.setVisibility(View.VISIBLE);
        }else{
            viewHolder.updateTipTxtView.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder{
        ImageView logoImgView;
        TextView nickNameTxtView;
        TextView updateTipTxtView;
    }

}
