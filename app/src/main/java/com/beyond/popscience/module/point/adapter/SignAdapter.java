package com.beyond.popscience.module.point.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.SignBean;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class SignAdapter extends CustomRecyclerBaseAdapter<SignBean> {

    private Context mContext;

    public SignAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_point_sign_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSign;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSign = (TextView) itemView.findViewById(R.id.tv_sign);
        }

        public void setData(final SignBean info) {
            if (info != null) {
                if (info.isSelect()) {
                    if (info.isSigned()) {
                        tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.bg_circle_gray_fill));
                        tvSign.setText("已签到");
                    } else {
                        tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.bg_circle_blue));
                        tvSign.setText("签到");
                    }
                    tvSign.setTextColor(mContext.getResources().getColor(R.color.white));
                } else {
                    tvSign.setText(info.getWeekday());
                    if (info.isSigned()) {
                        tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.bg_circle_red));
                        tvSign.setTextColor(mContext.getResources().getColor(R.color.red4));
                    } else {
                        tvSign.setTextColor(mContext.getResources().getColor(R.color.gray153));
                        tvSign.setBackground(mContext.getResources().getDrawable(R.drawable.bg_circle_gray));
                    }
                }
            }
        }
    }
}
