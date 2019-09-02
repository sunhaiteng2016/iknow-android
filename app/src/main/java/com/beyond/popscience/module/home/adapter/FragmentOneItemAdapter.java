package com.beyond.popscience.module.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.pojo.ComfinOrderInfoBean;
import com.beyond.popscience.module.home.shopcart.CartMakeSureActivity;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by Administrator on 2017-07-19.
 */

public class FragmentOneItemAdapter extends BaseAdapter {
    static final String TAG = "FragmentOneItemAdapter";

    private List<ComfinOrderInfoBean.StoreListBean.GoodsInfoBean> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public static  String goodsnum;
    public FragmentOneItemAdapter(Context context, List<ComfinOrderInfoBean.StoreListBean.GoodsInfoBean> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new FragmentOneItemAdapter.ViewHolder();
            // 获取组件布局
            convertView = layoutInflater.inflate(R.layout.item_one_item_layout, null);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.img_goods);
            holder.tv_nums = (TextView) convertView.findViewById(R.id.tv_buynumber);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_danjia);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_shangpinname);
            holder.tv_guige = (TextView) convertView.findViewById(R.id.tv_guige);
            holder.item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            holder.tvadd = (TextView) convertView.findViewById(R.id.tv_add_num);
            holder.tvjian = (TextView) convertView.findViewById(R.id.jian);

            // 这里要注意，是使用的tag来存储数据的。
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(data.get(position).getGoods_name());
        if (TextUtils.isEmpty(data.get(position).getPrice())) {
            holder.tv_price.setText("¥ 0.00 元");
        } else {
            holder.tv_price.setText("¥ " + data.get(position).getPrice() + " 元");
        }
        String ss = data.get(position).getNum();
        if (TextUtils.isEmpty(data.get(position).getNum())) {
            holder.tv_nums.setText("0");
        } else {
            holder.tv_nums.setText( data.get(position).getNum());
        }
//        if (!TextUtils.isEmpty(data.get(position).getSku_info())) {
//            holder.tv_guige.setText(data.get(position).getSku_info());
//        }
        holder.tvadd.setOnClickListener(new MyAddClickListener(position,holder.tv_nums));
        holder.tvjian.setOnClickListener(new MyjianClickListener(position,holder.tv_nums));
        Glide.with(context).load(data.get(position).getLogo_pic()).into(holder.iv_img);

        return convertView;
    }
    class  MyAddClickListener implements View.OnClickListener{
        public TextView tvnum;
        public int position;
        public MyAddClickListener(int position,TextView tvnum) {
            this.tvnum = tvnum;
            this.position=position;
        }

        @Override
        public void onClick(View view) {
            int number = Integer.parseInt(tvnum.getText().toString());
            number++;
            if ( context instanceof CartMakeSureActivity){
                CartMakeSureActivity activity = (CartMakeSureActivity) context;
                activity.updateAdapter(position,number);
            }
        }
    }
    class  MyjianClickListener implements View.OnClickListener{
        public TextView tvnum;
        public int position;
        public MyjianClickListener(int position,TextView tvnum) {
            this.tvnum = tvnum;
            this.position=position;

        }

        @Override
        public void onClick(View view) {
            int number = Integer.parseInt(tvnum.getText().toString());
            if (number>1){
                number--;
                if ( context instanceof CartMakeSureActivity){
                    CartMakeSureActivity activity = (CartMakeSureActivity) context;
                    activity.updateAdapter(position,number);
                }
            }
        }
    }

    public class ViewHolder {
        public TextView tv_title;
        public TextView tv_price;
        public TextView tv_nums;
        public ImageView iv_img;
        public TextView tv_guige;
        public LinearLayout item_layout;
        public TextView tvadd;
        public TextView tvjian;
    }
}
