package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.mservice.GoodsNewDetailActivity;

/**
 * Describe： 新的 服务适配
 * Date：2018/3/7
 * Time: 16:07
 * Author: Bin.Peng
 */

public class ServiceAdapter extends CustomRecyclerBaseAdapter<ServiceGoodsItem> {

    public ServiceAdapter(Activity context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ServiceHolder(inflater.inflate(R.layout.item_new_service_list, parent, false));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ServiceHolder) holder).setData(getDataList().get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsNewDetailActivity.startActivity(context, getDataList().get(position).getProductId());
            }
        });
    }

    private class ServiceHolder extends RecyclerView.ViewHolder {
        ImageView iv_logo;
        TextView tv_name;
        TextView tv_price;
        LinearLayout ll_add_shop_car;

        public ServiceHolder(View itemView) {
            super(itemView);
            iv_logo = (ImageView) itemView.findViewById(R.id.iv_logo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            ll_add_shop_car = (LinearLayout) itemView.findViewById(R.id.ll_add_shop_car);
        }

        public void setData(final ServiceGoodsItem item) {
            ImageLoaderUtil.displayImage(context, item.getCoverPic(), iv_logo, getDisplayImageOptions());
            tv_name.setText(item.getTitle());
            tv_price.setText(item.getPrice());
            ll_add_shop_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsNewDetailActivity.startActivity(context, item.getProductId());
                }
            });
        }
    }
}
