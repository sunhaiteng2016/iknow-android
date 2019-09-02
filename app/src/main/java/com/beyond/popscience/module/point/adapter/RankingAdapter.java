package com.beyond.popscience.module.point.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.RankingListResult;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.widget.CircleImageView;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class RankingAdapter extends CustomRecyclerBaseAdapter<RankingListResult.Ranking> {

    private Context mContext;

    public RankingAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_point_ranking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(position, getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNo;
        TextView tvUsername;
        TextView tvPoint;
        CircleImageView civRankingHead;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNo = (TextView) itemView.findViewById(R.id.tv_no);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvPoint = (TextView) itemView.findViewById(R.id.tv_point);
            civRankingHead = (CircleImageView) itemView.findViewById(R.id.civ_ranking_head);
        }

        public void setData(int position, RankingListResult.Ranking info) {
            if (info != null) {
                tvNo.setText(info.getRecordid());
                tvPoint.setText(info.getRecordintegral());
                tvUsername.setText(info.getRecordname());
                ImageLoaderUtil.display(mContext, info.getRecordimg(), civRankingHead);
                switch (position) {
                    case 0:
                        tvNo.setTextColor(mContext.getResources().getColor(R.color.red));
                        break;
                    case 1:
                        tvNo.setTextColor(mContext.getResources().getColor(R.color.yellow));
                        break;
                    case 2:
                        tvNo.setTextColor(mContext.getResources().getColor(R.color.green));
                        break;
                    default:
                        tvNo.setTextColor(mContext.getResources().getColor(R.color.black4));
                        break;
                }
            }
        }
    }
}
