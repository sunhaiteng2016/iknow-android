package com.beyond.popscience.module.square.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.mservice.GoodsDetailV2Activity;
import com.beyond.popscience.module.mservice.GoodsDetailV2Activity2;

/**
 * Created by danxiang.feng on 2017/10/11.
 */

public class SquareListAdapter extends CustomRecyclerBaseAdapter<ServiceGoodsItem> {

    // 1: 技能广场   2：任务广场
    private int type;

    public SquareListAdapter(Fragment fragment) {
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
        View view = inflater.inflate(R.layout.adapter_skill_square_item,parent,false);
        SquareListAdapter.ViewHolder holder = new SquareListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).setData(position, getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView titleTxtView;
        TextView classifyNameTxtView;
        TextView nameTxtView;
        TextView descriptTxtView;
        TextView priceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            titleTxtView = (TextView) itemView.findViewById(R.id.titleTxtView);
            classifyNameTxtView = (TextView) itemView.findViewById(R.id.classifyNameTxtView);
            nameTxtView = (TextView) itemView.findViewById(R.id.nameTxtView);
            descriptTxtView = (TextView) itemView.findViewById(R.id.descriptTxtView);
            priceTextView = (TextView) itemView.findViewById(R.id.priceTextView);
        }

        public void setData(int position, final ServiceGoodsItem info){
            if(info != null){
                ImageLoaderUtil.displayImage(context, info.getCoverPic(), imageView, getDisplayImageOptions());
                titleTxtView.setText(info.getTitle());
                if(!TextUtils.isEmpty(info.getClassifyName())) {
                    String classifyName = info.getClassifyName().replace(",", " | ");
                    classifyNameTxtView.setText(classifyName);
                }
                if(!TextUtils.isEmpty(info.getRealName())) {
                    nameTxtView.setText(info.getRealName());
                    nameTxtView.setVisibility(View.VISIBLE);
                } else {
                    nameTxtView.setVisibility(View.GONE);
                }
                String descript = "";
                if(!TextUtils.isEmpty(info.getCreateTime())){
                    String time = Util.getBetweenTime(BeyondApplication.getInstance().getCurrSystemTime(), info.getCreateTime());
                    descript += "发布于" + time + "前";
                }
                if(!TextUtils.isEmpty(info.getDistance())){
                    descript += " | "+info.getDistance()+"km";
                }
                descriptTxtView.setText(descript);
                if(!TextUtils.isEmpty(info.getPrice())) {
                    priceTextView.setText("¥" + info.getPrice());
                } else {
                    priceTextView.setText(null);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        info.setAppGoodsType(String.valueOf(type));
                        info.setProductId(info.getUid());
//                        GoodsDetailV2Activity.startActivity(context, info);
                        GoodsDetailV2Activity2.startActivity(context,info);
                    }
                });
            }
        }
    }
}
