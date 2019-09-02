package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.home.adapter.ServiceCategoryAdapter;
import com.beyond.popscience.module.mservice.GoodsDetailActivity;

import butterknife.BindView;

/**
 * Created by lenovo on 2017/6/30.
 */

public class GoodsRecycleAdapter extends CustomRecyclerBaseAdapter<ServiceGoodsItem> {
    public GoodsRecycleAdapter(Activity context) {
        super(context);
    }

    public GoodsRecycleAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ServiceGoodsItem item = getDataList().get(position);
        GoodsViewHolder goodsHolder = (GoodsViewHolder) holder;
        goodsHolder.setData(item);
        goodsHolder.llGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDetailActivity.startActivity(context,item.getProductId());
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_service_goods,parent,false);
        GoodsViewHolder holder = new GoodsViewHolder(view);

        return holder;
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        ImageView ivGoods;
        TextView tvDes;
        TextView tvMoney;
        TextView tvAddress;
        LinearLayout llGoods;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivGoods = (ImageView) itemView.findViewById(R.id.ivGoods);
            tvDes = (TextView) itemView.findViewById(R.id.tvDes);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            llGoods = (LinearLayout) itemView.findViewById(R.id.llGoods);
        }

        public void setData(ServiceGoodsItem item){
            ImageLoaderUtil.displayImage(context,item.getCoverPic(),ivGoods,getDisplayImageOptions());
            tvTitle.setText(item.getTitle());
            tvDes.setText(item.getCreateTime());
            tvMoney.setText("¥"+item.getPrice());
            tvAddress.setText(item.getAddress());
        }
    }
}
