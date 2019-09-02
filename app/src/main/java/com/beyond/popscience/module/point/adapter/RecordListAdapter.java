package com.beyond.popscience.module.point.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.GoodsExchangeBean;
import com.beyond.popscience.frame.pojo.RecordDetail;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.TimeUtils;

/**
 * 绿币商城首页 商品列表适配器
 */

public class RecordListAdapter extends CustomRecyclerBaseAdapter<RecordDetail> {
    private Context mContext;

    public RecordListAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_point_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRecord;
        TextView tvName;
        TextView tvCreateTime;
        TextView tvScore;

        public ViewHolder(View itemView) {
            super(itemView);
            ivRecord = (ImageView) itemView.findViewById(R.id.iv_record);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
        }

        public void setData(final RecordDetail info) {
            if (info != null) {
                tvName.setText(info.getRecordname());
                tvCreateTime.setText(TimeUtils.getStrTime(info.getRecordtime()));
                tvScore.setText("-" + info.getRecordintegral());
                ImageLoaderUtil.display(mContext, info.getRecordimg(), ivRecord);
            }
        }
    }
}
