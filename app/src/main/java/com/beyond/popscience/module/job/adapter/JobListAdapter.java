package com.beyond.popscience.module.job.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.util.PhoneUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.JobDetail;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.job.JobApplyDetailActivity;
import com.beyond.popscience.module.job.JobProvideDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danxiang.feng on 2017/10/11.
 */

public class JobListAdapter extends CustomRecyclerBaseAdapter<JobDetail> {

    // 1: 招聘   2：求职
    private int type;
    public static String myage;

    public JobListAdapter(Activity context) {
        super(context);
    }

    public JobListAdapter(Fragment fragment) {
        super(fragment);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (type == JobActivity.JOB_PROVIDE) { //招聘
            View view = inflater.inflate(R.layout.adapter_job_provide_list_item, parent, false);
            holder = new JobListAdapter.JobProvideViewHolder(view);
        } else {   //求职
            View view = inflater.inflate(R.layout.adapter_job_apply_list_item, parent, false);
            holder = new JobListAdapter.JobApplyViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (type == JobActivity.JOB_PROVIDE) {//招聘
            ((JobProvideViewHolder) holder).setData(position, getItem(position));
        } else {
            ((JobApplyViewHolder) holder).setData(position, getItem(position));
        }
    }

    //求职
    class JobApplyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.positionTxtView)
        TextView positionTxtView;
        @BindView(R.id.industryTxtView)
        TextView industryTxtView;
        @BindView(R.id.applyerTxtView)
        TextView applyerTxtView;
        @BindView(R.id.addressTxtView)
        TextView addressTxtView;
        @BindView(R.id.priceTxtView)
        TextView priceTxtView;
        @BindView(R.id.timeTxtView)
        TextView timeTxtView;
        @BindView(R.id.addressTxtViewone)
        TextView addressTxtViewone;//
        @BindView(R.id.tvqwdd)
        TextView tvqwdd;
        public JobApplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position, final JobDetail info) {
            if (info != null) {
                positionTxtView.setText(info.position);
                industryTxtView.setText(info.industry);
                applyerTxtView.setText(info.realName);
                addressTxtView.setText("期望工作地："+info.address);
                if (info.arrivalTime!=null){
                    tvqwdd.setText("到岗时间："+info.arrivalTime);
                }
                timeTxtView.setText(info.createTime);
                addressTxtViewone.setText(info.age+"岁");
                if ("-1".equals(info.lowPrice)||"-1".equals(info.highPrice)){
                    priceTxtView.setText("面议");
                } else {
//                    priceTxtView.setText(Util.parsePrice(info.lowPrice, info.highPrice));
                    priceTxtView.setText(info.lowPrice +"元 - "+ info.highPrice+"元");
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myage=info.age;
                        JobApplyDetailActivity.startActivty(context, info.applyId,"1");
                    }
                });
            }
        }
    }

    //招聘
    class JobProvideViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.titleTxtView)
        TextView titleTxtView;
        @BindView(R.id.industryTxtView)
        TextView industryTxtView;
        @BindView(R.id.priceTxtView)
        TextView priceTxtView;
        @BindView(R.id.companyTxtView)
        TextView companyTxtView;
        @BindView(R.id.addressTextView)
        TextView addressTextView;
        @BindView(R.id.llCall)
        LinearLayout llCall;
        @BindView(R.id.timeTxtView)
        TextView timeTxtView;


        public JobProvideViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position, final JobDetail info) {
            if (info != null) {
                ImageLoaderUtil.displayImage(context, info.coverPic, imageView, getDisplayImageOptions());
                titleTxtView.setText(info.title);
                companyTxtView.setText(info.company);
                industryTxtView.setText(info.industry);
                addressTextView.setText(info.address);
//                priceTxtView.setText(Util.parsePrice(info.lowPrice, info.highPrice));
                if ("-1".equals(info.lowPrice)||"-1".equals(info.highPrice)){
                    priceTxtView.setText("面议");
                } else {
//                    priceTxtView.setText(Util.parsePrice(info.lowPrice, info.highPrice));
                    priceTxtView.setText(info.lowPrice +"元 - "+ info.highPrice+"元");
                }
                //priceTxtView.setText(info.lowPrice +"元 - "+ info.highPrice+"元");
                timeTxtView.setText(info.createTime);

                llCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneUtil.callPhoneDial(context, info.mobile);
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JobProvideDetailActivity.startActivity(context, info.jobId,"1");
                    }
                });
            }
        }
    }
}
