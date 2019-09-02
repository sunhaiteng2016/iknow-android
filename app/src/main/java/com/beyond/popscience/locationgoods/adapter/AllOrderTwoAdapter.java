package com.beyond.popscience.locationgoods.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.locationgoods.ShopOrderDetailActivity;
import com.beyond.popscience.locationgoods.bean.OrderLsitTwoBean;
import com.bumptech.glide.Glide;

import java.util.List;

public class AllOrderTwoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int tabPosition;
    public List<OrderLsitTwoBean> list;
    public Context context;
    private BottomButtonClickLinster linster;
    private ItemOnClickLinster itemLinster;


    public AllOrderTwoAdapter(List<OrderLsitTwoBean> list, Context context, int position) {
        this.list = list;
        this.tabPosition = position;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view = LayoutInflater.from(context).inflate(R.layout.item_one_view_holder_two, null);
                return new OneViewHolder(view);
            case 2:
                View view1 = LayoutInflater.from(context).inflate(R.layout.item_two_view_holder, null);
                return new TwoViewHolder(view1);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_one_view_holder, null);
        return new OneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OneViewHolder) {
            ((OneViewHolder) holder).ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemLinster.onClick(position);
                }
            });
            //设置数据
            ((OneViewHolder) holder).tvOrderNo.setText("订单编号：" + list.get(position).getOrderSn());
            ((OneViewHolder) holder).tvName.setText(list.get(position).getReceiverName());
            ((OneViewHolder) holder).tvPhone.setText(list.get(position).getReceiverPhone());
            ((OneViewHolder) holder).tvAddress.setText(list.get(position).getReceiverProvince() +
                    list.get(position).getReceiverCity() +
                    list.get(position).getReceiverRegion() +
                    list.get(position).getReceiverDetailAddress());
            Glide.with(context).load(list.get(position).getItemList().get(0).getProductPic()).into(((OneViewHolder) holder).ivImg);
            ((OneViewHolder) holder).tvShopName.setText(list.get(position).getItemList().get(0).getProductName());
            ((OneViewHolder) holder).tvGoodAttr.setText(list.get(position).getItemList().get(0).getSp1());
            ((OneViewHolder) holder).tvTime.setText(list.get(position).getModifyTime());
            ((OneViewHolder) holder).tvPirce.setText("应付：￥" + list.get(position).getPayAmount() + "");
            ((OneViewHolder) holder).tv_price1.setText("价格：" + list.get(position).getItemList().get(0).getProductPrice());
            ((OneViewHolder) holder).number1.setText("数量：" + list.get(position).getItemList().get(0).getProductQuantity());

            if (list.get(position).getOrderType() == 1) {
                ((OneViewHolder) holder).ivPt.setVisibility(View.VISIBLE);
            } else {
                ((OneViewHolder) holder).ivPt.setVisibility(View.GONE);
            }
            if (tabPosition == 0) {
                //这个是未付款
                ((OneViewHolder) holder).tvBottomBtn.setVisibility(View.VISIBLE);
                ((OneViewHolder) holder).tvBtn1.setVisibility(View.GONE);
                ((OneViewHolder) holder).tvBtn2.setText("立即付款");
                ((OneViewHolder) holder).tvBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (list.get(position).getOrderType() == 1) {
                            linster.onClick(1, list.get(position).getItemList().get(0).getProductName() + "/" +list.get(position).getPayAmount()+ "/" + list.get(position).getOrderSn());
                        } else {
                            linster.onClick(1, list.get(position).getItemList().get(0).getProductName() + "/" + list.get(position).getPayAmount() + "/" + list.get(position).getOrderSn());
                        }
                    }
                });
                ((OneViewHolder) holder).tvOrderStatus.setText("未付款");
            }

            if (tabPosition == 1) {
                ((OneViewHolder) holder).tvBottomBtn.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ((OneViewHolder) holder).tvBtn1.setLayoutParams(layoutParams);
                ((OneViewHolder) holder).tvBtn1.setText("取消订单");
                ((OneViewHolder) holder).tvBtn2.setVisibility(View.GONE);
                ((OneViewHolder) holder).tvOrderStatus.setText("正在拼团");
                ((OneViewHolder) holder).tvBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linster.onClick(6, list.get(position).getOrderSn());
                    }
                });
                ((OneViewHolder) holder).tvOrderStatus.setText("正在拼团");
            }
            if (tabPosition == 2) {
                ((OneViewHolder) holder).tvBottomBtn.setVisibility(View.GONE);
                ((OneViewHolder) holder).tvBtn2.setText("立即发货");
                ((OneViewHolder) holder).tvOrderStatus.setText("待发货");
            }
            if (tabPosition == 3) {
                ((OneViewHolder) holder).tvBottomBtn.setVisibility(View.VISIBLE);
                ((OneViewHolder) holder).tvBtn1.setText("查看物流");
                ((OneViewHolder) holder).tvBtn2.setText("确认收货");
                ((OneViewHolder) holder).tvBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linster.onClick(2, list.get(position).getDeliveryCompany() + "/" + list.get(position).getDeliverySn() + "/" + list.get(position).getOrderSn());
                    }
                });
                ((OneViewHolder) holder).tvBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linster.onClick(3, list.get(position).getDeliverySn());
                    }
                });
                ((OneViewHolder) holder).tvOrderStatus.setText("已发货");
            }
            if (tabPosition == 4) {
                ((OneViewHolder) holder).tvBottomBtn.setVisibility(View.VISIBLE);
                ((OneViewHolder) holder).tvBtn1.setVisibility(View.GONE);
                int pinglunStatus = list.get(position).getPinglunStatus();
                if (pinglunStatus==1){
                    ((OneViewHolder) holder).tvBtn2.setText("已评价");
                    ((OneViewHolder) holder).tvBtn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linster.onClick(4, list.get(position).getOrderSn() + "&" + list.get(position).getItemList().get(0).getProductPic());
                        }
                    });
                }else{
                    ((OneViewHolder) holder).tvBtn2.setText("去评价");
                    ((OneViewHolder) holder).tvBtn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linster.onClick(4, list.get(position).getOrderSn() + "&" + list.get(position).getItemList().get(0).getProductPic());
                        }
                    });
                }

                ((OneViewHolder) holder).tvOrderStatus.setText("已完成");
            }
            if (tabPosition == 5) {
                ((OneViewHolder) holder).tvBottomBtn.setVisibility(View.GONE);
                ((OneViewHolder) holder).tvOrderStatus.setText("已取消");
            }
        }
        if (holder instanceof TwoViewHolder) {
            ((TwoViewHolder) holder).ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShopOrderDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public class OneViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBtn1, tvBtn2, tvShopName, tvOrderNo, tvOrderStatus, tvName, tvPhone, tvAddress, tvGoodAttr, tvTime, tvPirce, tv_price1, number1;
        public RelativeLayout ll;
        public RelativeLayout tvBottomBtn;
        public ImageView ivImg, ivPt;

        public OneViewHolder(View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll);
            tvBottomBtn = itemView.findViewById(R.id.tv_bottom_btn);
            tvBtn1 = itemView.findViewById(R.id.tv_btn1);
            tvBtn2 = itemView.findViewById(R.id.tv_btn2);
            tv_price1 = itemView.findViewById(R.id.tv_price1);
            number1 = itemView.findViewById(R.id.number1);
            tvBtn2 = itemView.findViewById(R.id.tv_btn2);

            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvAddress = itemView.findViewById(R.id.tv_address);
            ivImg = itemView.findViewById(R.id.iv_img);
            ivPt = itemView.findViewById(R.id.iv_pinT);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            tvGoodAttr = itemView.findViewById(R.id.tv_good_attr);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPirce = itemView.findViewById(R.id.tv_price);
        }
    }

    public class TwoViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll;

        public TwoViewHolder(View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll);
        }
    }

    public interface BottomButtonClickLinster {
        void onClick(int type, String orderSign);
    }

    public void setBottomButtonClick(BottomButtonClickLinster linster) {
        this.linster = linster;
    }

    public interface ItemOnClickLinster {
        void onClick(int itemPosition);
    }

    public void setOnClickLinster(ItemOnClickLinster linster) {
        this.itemLinster = linster;
    }
}
