package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.locationgoods.bean.ProductList;
import com.bumptech.glide.Glide;
import com.idlestar.ratingstar.RatingStarView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品搜索adapter
 * Created by yao.cui on 2017/6/24.
 */

public class GoodsAdapterTwo extends BaseAdapter {
    private int layoutId = R.layout.item_list_view_goods;

    List<ProductList> productLists;
    Context context;

    public GoodsAdapterTwo(Context fragment, List<ProductList> productLists) {
        this.context=fragment;
        this.productLists = productLists;
    }


    @Override
    public int getCount() {
        return productLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (productLists.size()>0){
            holder.setData(productLists.get(position));
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        ImageView ivGoods;
        TextView tv_pt_num;
        TextView tv_group_price;
        TextView tv_price;
        RatingStarView rsv_rating;
        TextView tv_sale;
        TextView tv_address;

        public ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.tv_name);
            ivGoods = view.findViewById(R.id.iv_img);
            tv_pt_num = view.findViewById(R.id.tv_pt_num);
            tv_group_price = view.findViewById(R.id.tv_group_price);
            tv_price = view.findViewById(R.id.tv_price);
            rsv_rating = view.findViewById(R.id.rsv_rating);
            tv_sale = view.findViewById(R.id.tv_sale);
            tv_address = view.findViewById(R.id.tv_address);
        }

        public void setData(ProductList item) {
            tvTitle.setText(item.getName());
            Glide.with(context).load(item.getPic()).into(ivGoods);
            tv_pt_num.setText(item.getGroupSize() + "人拼团");
            tv_group_price.setText("拼团价:" + item.getSku().getGroupPrice());
            tv_price.setText("拼团价:" + item.getSku().getPrice());
            tv_address.setText(item.getShopAddress());
            tv_sale.setText("已售" + item.getSale() + "单");
            rsv_rating.setRating(item.getScore());
        }
    }
}
