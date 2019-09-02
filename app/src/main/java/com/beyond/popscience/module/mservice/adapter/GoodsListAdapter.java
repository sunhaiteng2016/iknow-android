package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.mservice.GoodsDetailV2Activity;
import com.beyond.popscience.module.mservice.GoodsNewDetailActivity;

/**
 * Created by lenovo on 2017/6/30.
 */

public class GoodsListAdapter extends CustomRecyclerBaseAdapter<ServiceGoodsItem> {

    private ServiceCategory serviceCategory;

    public GoodsListAdapter(Activity context) {
        super(context);
    }

    public GoodsListAdapter(Fragment fragment) {
        super(fragment);
    }

    public void setServiceCategory(ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
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

                item.setAppGoodsType(serviceCategory != null ? Util.getGoodsTypeByTabId(serviceCategory.getTabId()) : "");
//                GoodsDetailV2Activity.startActivity(context, item);
                GoodsNewDetailActivity.startActivity(context, item.getProductId());
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_service_goods_list, parent, false);
        GoodsViewHolder holder = new GoodsViewHolder(view);

        return holder;
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivGoods;
        TextView tvDes;
        TextView tvMoney;
        TextView tvAddress;
        TextView nameTxtView;
        TextView distanceTxtView;
        TextView verticalLineView;
        LinearLayout llGoods;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            nameTxtView = (TextView) itemView.findViewById(R.id.nameTxtView);
            distanceTxtView = (TextView) itemView.findViewById(R.id.distanceTxtView);
            verticalLineView = (TextView) itemView.findViewById(R.id.verticalLineView);
            ivGoods = (ImageView) itemView.findViewById(R.id.ivGoods);
            tvDes = (TextView) itemView.findViewById(R.id.tvDes);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            llGoods = (LinearLayout) itemView.findViewById(R.id.llGoods);
        }

        public void setData(ServiceGoodsItem item) {
            ImageLoaderUtil.displayImage(context, item.getCoverPic(), ivGoods, getDisplayImageOptions());
            nameTxtView.setText(item.getRealName());
            nameTxtView.setVisibility(TextUtils.isEmpty(item.getRealName()) ? View.GONE : View.VISIBLE);

            if(TextUtils.isEmpty(item.getDistance())){
                distanceTxtView.setVisibility(View.GONE);
                verticalLineView.setVisibility(View.GONE);
            }else{
                verticalLineView.setVisibility(View.VISIBLE);
                distanceTxtView.setVisibility(View.VISIBLE);
                distanceTxtView.setText(item.getDistance()+"km");
            }

            tvTitle.setText(item.getTitle());
            tvDes.setText("发布于"+Util.getDisplayDateTimeV2(BeyondApplication.getInstance().getCurrSystemTime(), item.getCreateTime()));
            tvMoney.setText("¥" + item.getPrice());
            tvAddress.setText(item.getAddress());
        }
    }
}
