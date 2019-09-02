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
public class SocialListAdapterTwo extends CustomBaseAdapter<SocialInfo> {

    public SocialListAdapterTwo(Activity context) {
        super(context);
    }

    public SocialListAdapterTwo(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_social_list_item, parent, false);
            viewHolder.ivCheck = (TextView) convertView.findViewById(R.id.iv_check);
            viewHolder.logoImgView = (ImageView) convertView.findViewById(R.id.logoImgView);
            viewHolder.nickNameTxtView = (TextView) convertView.findViewById(R.id.nickNameTxtView);
            viewHolder.updateTipTxtView = (TextView) convertView.findViewById(R.id.updateTipTxtView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivCheck.setVisibility(View.VISIBLE);
        SocialInfo socialInfo = dataList.get(position);

        ImageLoaderUtil.displayImage(context, socialInfo.getLogo(), viewHolder.logoImgView, getAvatarDisplayImageOptions());
        viewHolder.nickNameTxtView.setText(socialInfo.getName());

        if(socialInfo.getUpdateNumLong()>0){
            viewHolder.updateTipTxtView.setText(socialInfo.getUpdateNum()+"条更新");

            viewHolder.updateTipTxtView.setVisibility(View.VISIBLE);
        }else{
            viewHolder.updateTipTxtView.setVisibility(View.GONE);
        }
        if (socialInfo.isCheck()){
            viewHolder.ivCheck.setBackgroundResource(R.drawable.chenck);
        }else{
            viewHolder.ivCheck.setBackgroundResource(R.drawable.uncheck);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView logoImgView;
        TextView ivCheck;
        TextView nickNameTxtView;
        TextView updateTipTxtView;
    }

}
