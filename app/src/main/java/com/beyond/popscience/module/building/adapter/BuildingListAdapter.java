package com.beyond.popscience.module.building.adapter;

import android.app.Activity;
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
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.building.BuildingDetailActivity;
import com.beyond.popscience.module.building.BuildingDetailActivity2;
import com.beyond.popscience.module.building.RentDetailActivity;
import com.beyond.popscience.module.mservice.GoodsNewDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danxiang.feng on 2017/10/11.
 */

public class BuildingListAdapter extends CustomRecyclerBaseAdapter<BuildingDetail> {

    // 1: 出租出售   2：求租求购
    private int type;

    public BuildingListAdapter(Activity context) {
        super(context);
    }

    public BuildingListAdapter(Fragment fragment) {
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
        if (type == BuildingActivity.BUILDING) {
            View view = inflater.inflate(R.layout.adapter_building_list_item, parent, false);
            holder = new BuildingListAdapter.BuildingViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.adapter_rent_list_item, parent, false);
            holder = new BuildingListAdapter.RentViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (type == BuildingActivity.BUILDING) {
            ((BuildingViewHolder) holder).setData(position, getItem(position));
        } else {
            ((RentViewHolder) holder).setData(position, getItem(position));
        }
    }

    /**
     * 出租出售
     */
    class BuildingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.titleTxtView)
        TextView titleTxtView;
        @BindView(R.id.tradeTxtView)
        TextView tradeTxtView;
        @BindView(R.id.sizeTxtView)
        TextView sizeTxtView;
        @BindView(R.id.descriptTxtView)
        TextView descriptTxtView;
        @BindView(R.id.priceTextView)
        TextView priceTextView;
        @BindView(R.id.addressss)
        TextView addressss;

        public BuildingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position, final BuildingDetail info) {
            if (info != null) {
                ImageLoaderUtil.displayImage(context, info.coverPic, imageView, getDisplayImageOptions());
                titleTxtView.setText(info.title);

                String mtrade = "1".equals(info.trade) ? "出租" : "出售";
                String msellType = "1".equals(info.getSellTypeInt() + "") ? "中介" : "房东";
                tradeTxtView.setText(msellType + mtrade);
                if (!TextUtils.isEmpty(info.price)) {
                    if (mtrade.equals("出租")) {
                        if (info.price.equals("-1.00")) {
                            priceTextView.setText("面议");
                        } else {
                            priceTextView.setText("¥" + info.price + "/月");
                        }
                    } else {
                        if (info.price.equals("-1.00")) {
                            priceTextView.setText("面议");
                        } else {
                            priceTextView.setText("¥" + info.price + "元");
                        }
                    }
                } else {
                    priceTextView.setText(null);
                }

                String size = "";
                if (!TextUtils.isEmpty(info.size)) {
                    size += " | " + info.size + "m²";
                }
                if (!TextUtils.isEmpty(info.houseType)) {
                    String houseTypes[] = info.houseType.split(",");
                    if (houseTypes.length > 0) {
                        size += " | " + houseTypes[0] + "厅";
                    }
                    if (houseTypes.length > 1) {
                        size += houseTypes[1] + "室";
                    }
                    if (houseTypes.length > 2) {
                        size += houseTypes[2] + "卫";
                    }
                }
                sizeTxtView.setText(size);
                addressss.setText(info.address);
               /* String descript = "";
                if(!TextUtils.isEmpty()){
                    String time = Util.getBetweenTime(BeyondApplication.getInstance().getCurrSystemTime(), info.createTime);
                    descript += "发布于" + time + "前";
                }
                if(!TextUtils.isEmpty(info.address)){
                    descript += " | "+info.address;
                }*/
                descriptTxtView.setText(info.createTime);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BuildingDetailActivity.startActivity(context, info.uid, "1");
                    }
                });
            }
        }
    }

    /**
     * 求租求购
     */
    class RentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTxtView)
        protected TextView titleTxtView;
        @BindView(R.id.sellTypeTxtView)
        protected TextView sellTypeTxtView;
        @BindView(R.id.addressTextView)
        protected TextView addressTextView;
        @BindView(R.id.descriptTxtView)
        protected TextView descriptTxtView;
        @BindView(R.id.priceTextView)
        protected TextView priceTextView;
        @BindView(R.id.timeTxtView)
        protected TextView timeTxtView;

        public RentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position, final BuildingDetail info) {
            if (info != null) {
                titleTxtView.setText(info.title);
                String sellType = "";
                if ("1".equals(info.sellType)) {
                    sellType = "中介";
                } else if ("2".equals(info.sellType)) {
                    sellType = "客户";
                }
                sellTypeTxtView.setText(sellType);
                addressTextView.setText(info.locationRequire);
                String trade = "";
                if ("1".equals(info.trade)) {
                    trade = "求租";
                } else if ("2".equals(info.trade)) {
                    trade = "求购";
                }
                descriptTxtView.setText(trade);
                if (info.startPrice.equals("-1.00") || info.endPrice.equals("-1.00")) {
                    priceTextView.setText("面议");
                } else {
                    priceTextView.setText(parsePrice(info.startPrice, info.endPrice, trade));
                }
                timeTxtView.setText(info.createTime);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RentDetailActivity.startActivity(context, info.rentId);
                    }
                });
            }
        }
    }

    private String parsePrice(String lowPrice, String highPrice, String trade) {
        lowPrice = Util.formatMoney(lowPrice);
        highPrice = Util.formatMoney(highPrice);
        if (TextUtils.isEmpty(lowPrice) && TextUtils.isEmpty(highPrice)) {
            return null;
        }
        if (!TextUtils.isEmpty(lowPrice) && TextUtils.isEmpty(highPrice)) {
            if (trade.equals("求租")) {
                return context.getResources().getString(R.string.price_yuan, lowPrice);
            } else {
                return context.getResources().getString(R.string.price_month, lowPrice);
            }

        }
        if (TextUtils.isEmpty(lowPrice) && !TextUtils.isEmpty(highPrice)) {
            if (trade.equals("求租")) {
                return context.getResources().getString(R.string.price_yuan, highPrice);
            } else {
                return context.getResources().getString(R.string.price_month, highPrice);
            }
        }
        String result = "";
        if (trade.equals("求租")) {
            result = context.getResources().getString(R.string.price_yuan, lowPrice) + "～" + context.getResources().getString(R.string.price_yuan, highPrice);
        } else {
            result = context.getResources().getString(R.string.price_month, lowPrice) + "～" + context.getResources().getString(R.string.price_month, highPrice);
        }
        return result;
    }
}
