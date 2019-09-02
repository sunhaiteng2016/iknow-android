package com.beyond.popscience.module.town.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.TopicItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yao.cui on 2017/6/23.
 */

public class TopicListAdapter extends CustomBaseAdapter<TopicItem> {

    public TopicListAdapter(Activity context) {
        super(context);
    }

    public TopicListAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_topic,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.tvDisCount)
        TextView tvDisCount;
        @BindView(R.id.tvViewCount)
        TextView tvViewCount;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setData(TopicItem topicItem){
        }

    }
}
