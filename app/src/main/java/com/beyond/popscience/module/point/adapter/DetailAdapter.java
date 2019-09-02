package com.beyond.popscience.module.point.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.PointDetailResult;
import com.beyond.popscience.frame.util.TimeUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class DetailAdapter extends CustomBaseAdapter<PointDetailResult.Details> {

    private Context mContext;

    public DetailAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_point_detail_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setData(getDataList().get(position));
        return convertView;
    }

    class ViewHolder {

        TextView tvMessage;
        TextView tvCreateTime;
        TextView tvScore;

        public ViewHolder(View itemView) {
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
        }

        public void setData(PointDetailResult.Details info) {
            if (info != null) {
                tvMessage.setText(info.getMessage());
                tvCreateTime.setText(TimeUtils.getStrTime(info.getCreatetime()));
                if (info.getScore().contains("-"))
                    tvScore.setText(info.getScore());
                else
                    tvScore.setText("+" + info.getScore());
            }
        }
    }
}
