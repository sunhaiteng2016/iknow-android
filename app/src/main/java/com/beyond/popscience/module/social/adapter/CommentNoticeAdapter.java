package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.Comment;

/**
 * Created by linjinfa on 2017/6/24.
 * email 331710168@qq.com
 */
public class CommentNoticeAdapter extends CustomBaseAdapter<Comment> {

    public CommentNoticeAdapter(Activity context) {
        super(context);
    }

    public CommentNoticeAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_comment_notice_item, parent, false);
        }
        return convertView;
    }

}
