package com.beyond.popscience.module.mservice.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品搜索adapter
 * Created by yao.cui on 2017/6/24.
 */

public class GoodsAdapter extends CustomBaseAdapter<ServiceGoodsItem> {
    private int layoutId = R.layout.item_goods_linear_v2;

    private boolean isNeedPadding = false;

    public GoodsAdapter(Activity context) {
        super(context);
    }

    public GoodsAdapter(Fragment fragment) {
        super(fragment);
    }

    public void setLayoutRes(int id){
        this.layoutId = id;
    }

    /**
     * 设置是否需要左右编剧
     * @param isNeedPadding
     */
    public void setNeedPadding(boolean isNeedPadding){
        this.isNeedPadding = isNeedPadding;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(layoutId,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.setData(getItem(position));

        if (isNeedPadding){
            if (position%2==0){
                convertView.findViewById(R.id.llGoods).setPadding(DensityUtil.dp2px(context,10),0,0,0);
            } else{
                convertView.findViewById(R.id.llGoods).setPadding(0,0,DensityUtil.dp2px(context,10),0);

            }
        }

        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.ivGoods)
        ImageView ivGoods;
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.tvMoney)
        TextView tvMoney;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.distanceTxtView)
        TextView distanceTxtView;
        @BindView(R.id.verticalLineView)
        TextView verticalLineView;
        @BindView(R.id.nameTxtView)
        TextView nameTxtView;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setData(ServiceGoodsItem item){
            ImageLoaderUtil.displayImage(context, Util.getUrlProportion1V1(item.getCoverPic()), ivGoods, getDisplayImageOptions());

            if(TextUtils.isEmpty(item.getDistance())){
                distanceTxtView.setVisibility(View.GONE);
                verticalLineView.setVisibility(View.GONE);
            }else{
                verticalLineView.setVisibility(View.VISIBLE);
                distanceTxtView.setVisibility(View.VISIBLE);
                distanceTxtView.setText(item.getDistance()+"km");
            }

            nameTxtView.setText(item.getRealName());
            nameTxtView.setVisibility(TextUtils.isEmpty(item.getRealName()) ? View.GONE : View.VISIBLE);
            tvTitle.setText(item.getTitle());
            tvDes.setText("发布于"+Util.getDisplayDateTimeV2(BeyondApplication.getInstance().getCurrSystemTime(), item.getCreateTime()));
            tvMoney.setText("¥"+item.getPrice());
            tvAddress.setText(item.getAddress());
        }
    }
}
