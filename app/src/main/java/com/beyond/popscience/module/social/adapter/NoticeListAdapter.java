package com.beyond.popscience.module.social.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.NoticeDetail;

/**
 *
 *
 * Created by yao.cui on 2017/7/17.
 */

public class NoticeListAdapter extends CustomRecyclerBaseAdapter<NoticeDetail> {
    public NoticeListAdapter(Activity context) {
        super(context);
    }

    public NoticeListAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder)holder).setData(dataList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder, position, getItemId(position));
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new MyViewHolder(inflater.inflate(R.layout.item_notice_list,parent,false));

    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvDate;
        TextView tvAuthor;

        public MyViewHolder(View view){
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
        }

        public void setData(NoticeDetail detail){
            tvTitle.setText(detail.getTitle());
            tvDate.setText(detail.getPublishTime());
            tvAuthor.setText(detail.getAuthor());
        }

    }
}
