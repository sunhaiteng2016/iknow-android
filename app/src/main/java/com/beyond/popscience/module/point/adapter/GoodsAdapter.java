package com.beyond.popscience.module.point.adapter;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.frame.pojo.point.IndexGoods;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.module.point.PonitShopActivity;
import com.squareup.picasso.Picasso;

/**
 * 绿币商城首页 商品列表适配器
 */

public class GoodsAdapter extends CustomRecyclerBaseAdapter<IndexGoods> {

    private OnGoodsClickListener onGoodsClickListener;

    public GoodsAdapter(Activity context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_point_exchange_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPic;
        TextView tvTitle;
        TextView tvGreenB;
        TextView tvExchange;
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvGreenB = (TextView) itemView.findViewById(R.id.tv_green_b);
            tvExchange = (TextView) itemView.findViewById(R.id.index_exchange);
            llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
        }

        public void setData(final IndexGoods info) {
            if (info != null) {
//                ImageLoaderUtil.displayImage(context, "http://h.hiphotos.baidu.com/image/pic/item/08f790529822720e23a3d3b777cb0a46f21fab09.jpg",
//                        ivPic, getDisplayImageOptions());
                if(TextUtils.isEmpty(info.getDisplaythepicture())){
                    Picasso.with(context).load(R.drawable.ic_empty_img).placeholder(R.drawable.ic_empty_img).into(ivPic);
                } else {
                    Picasso.with(context).load(info.getDisplaythepicture()).placeholder(R.drawable.ic_empty_img).into(ivPic);
                }
                tvTitle.setText(info.getTitle());
                tvGreenB.setText(info.getIntegral() + "绿币");

                String mscore =(String) SPUtils.get(context, "score", "");
                float mscores = Float.parseFloat(mscore);
                float jifen = info.getIntegral();
                if (jifen>mscores){
                    tvExchange.setEnabled(false);
                    tvExchange.setBackgroundResource(R.drawable.bg_border_round_grey);
                    tvExchange.setTextColor(context.getResources().getColor(R.color.grey6));
                    tvExchange.setText("绿币不足，兑换不了");
                }
                llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onGoodsClickListener.onItemClick(view, info);
                    }
                });

                tvExchange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onGoodsClickListener.onItemClick(view, info);
                    }
                });
            }
        }
    }

    public OnGoodsClickListener getOnGoodsClickListener() {
        return onGoodsClickListener;
    }

    public void setOnGoodsClickListener(OnGoodsClickListener onItemClickListener) {
        this.onGoodsClickListener = onItemClickListener;
    }
    /**
     *
     */
    public interface OnGoodsClickListener {

        void onItemClick(View view, IndexGoods info);
    }
}
