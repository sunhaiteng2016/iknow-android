package com.beyond.popscience.module.point.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.RecordDetail;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.TimeUtils;
import com.beyond.popscience.module.personal.Zzjl;
import com.beyond.popscience.utils.sun.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 绿币商城首页 商品列表适配器
 */

public class RecordListAdapterTwo extends CustomRecyclerBaseAdapter<Zzjl.DataBean> {
    private Context mContext;

    public RecordListAdapterTwo(Activity context) {
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
        TextView remark;

        public ViewHolder(View itemView) {
            super(itemView);
            ivRecord = (ImageView) itemView.findViewById(R.id.iv_record);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
            remark = (TextView) itemView.findViewById(R.id.remark);
        }

        public void setData(final Zzjl.DataBean info) {
            if (info != null) {
                int type = info.getType();
                //支出
                if (type == 1) {
                    //绿币转账 或绿币提现
                    int touserid = info.getTouserid();
                    if (touserid == 0) {
                        tvName.setText(info.getTousername());
                    } else {
                        tvName.setText("我转账给" + info.getTousername());
                    }
                    tvScore.setText("-" + info.getScore()+"绿币");

                    tvScore.setTextColor(mContext.getResources().getColor(R.color.green));
                    if (0 != info.getCreatetime()) {
                        Date date = new Date(info.getCreatetime());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
                        tvCreateTime.setText(format.format(date));
                    }
                }
                //收入
                if (type == 2) {
                    //代表审核不通过
                    int userid = info.getUserid();
                    if (userid == 0) {
                       tvName.setText("审核不通过");
                    }else{
                        tvName.setText(info.getUsername()+"给我转账");
                    }
                    if (0 != info.getCreatetime()) {
                        Date date = new Date(info.getCreatetime());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
                        tvCreateTime.setText(format.format(date));
                    }
                    tvScore.setText("+"+info.getScore()+"绿币");
                    tvScore.setTextColor(mContext.getResources().getColor(R.color.red));
                }

                tvName.setTextSize(16);
                tvCreateTime.setTextSize(14);
                tvCreateTime.setTextColor(mContext.getResources().getColor(R.color.f6f6f6f));
                tvScore.setTextSize(15);
                ivRecord.setVisibility(View.GONE);
                if (null!=info.getRemarks()&&!TextUtils.isEmpty(info.getRemarks())){
                    remark.setText("备注："+info.getRemarks());
                    remark.setVisibility(View.VISIBLE);
                }else{
                    remark.setVisibility(View.GONE);
                }

            }
        }
    }
}
