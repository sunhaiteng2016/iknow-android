package com.beyond.popscience.module.social.adapter;

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
import com.beyond.popscience.module.social.SocialCircleSearchResultActivity;

/**
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialSearchListAdapter extends CustomBaseAdapter<SocialInfo> {

    public SocialSearchListAdapter(Activity context) {
        super(context);
    }

    public SocialSearchListAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.adapter_social_search_list_item, parent, false);

            viewHolder.tipTxtView = (TextView) convertView.findViewById(R.id.tipTxtView);
            viewHolder.logoImgView = (ImageView) convertView.findViewById(R.id.logoImgView);
            viewHolder.nameTxtView = (TextView) convertView.findViewById(R.id.nameTxtView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final SocialInfo socialInfo = dataList.get(position);

        ImageLoaderUtil.displayImage(context, socialInfo.getLogo(), viewHolder.logoImgView, getAvatarDisplayImageOptions());
        viewHolder.nameTxtView.setText(socialInfo.getName());

        viewHolder.tipTxtView.setOnClickListener(null);
        if(socialInfo.getState() == 0){  //0:未加入 1:申请中 2：已加入
            viewHolder.tipTxtView.setText("申请加入");
            viewHolder.tipTxtView.setBackgroundResource(R.drawable.bg_green_round);
            viewHolder.tipTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof SocialCircleSearchResultActivity){
                        ((SocialCircleSearchResultActivity)context).apply(socialInfo);
                    }
                }
            });
        }else if(socialInfo.getState() == 1){
            viewHolder.tipTxtView.setText("已发出申请");
            viewHolder.tipTxtView.setBackgroundResource(R.drawable.bg_yellow_round);
        }else{
            viewHolder.tipTxtView.setText("已加入圈子");
            viewHolder.tipTxtView.setBackgroundResource(R.drawable.bg_deep_orngn_round);
        }
        return convertView;
    }

    class ViewHolder{
        TextView nameTxtView;
        TextView tipTxtView;
        ImageView logoImgView;
    }

}
