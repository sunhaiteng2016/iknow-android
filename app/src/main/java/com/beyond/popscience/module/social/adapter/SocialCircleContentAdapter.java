package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.SocialInfo;

/**
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialCircleContentAdapter extends CustomBaseAdapter<SocialInfo> {

    public SocialCircleContentAdapter(Activity context) {
        super(context);
    }

    public SocialCircleContentAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_social_circle_content_item, parent, false);
        }
        return convertView;
    }

}
