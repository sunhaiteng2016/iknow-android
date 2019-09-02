package com.beyond.popscience.window;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.module.home.shopcart.CartMakeSureActivity;
import com.flyco.dialog.widget.popup.base.BasePopup;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe：
 * Date：2018/3/9
 * Time: 16:02
 * Author: Bin.Peng
 */

public class GoodsSelectColorWindow extends BasePopup<GoodsSelectColorWindow> implements View.OnClickListener {
    private View view;
    private RecyclerView rv_pic;//图片
    private TextView tv_price;//价格
    private TextView tv_kucun;//库存
    private GridView gv_color;//颜色列表
    private TextView tv_jian;//减商品
    private TextView tv_num;//商品数量
    private TextView tv_add;//加商品
    private TextView tv_queren;//确认

    private int num = 1;
    private int kunCun = 1;

    //图片列表
    private List<String> images = new ArrayList<>();
    //颜色列表
    private List<String> colors = new ArrayList<>();
    private Context mContext;

    public GoodsSelectColorWindow(Context context) {
        super(context);
        mContext = context;
    }

    public GoodsSelectColorWindow(Context context, List<String> images0) {
        super(context);
        this.images = images0;
    }

    @Override
    public View onCreatePopupView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.pop_goods_shop, null);
        rv_pic = (RecyclerView) view.findViewById(R.id.rv_pic);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_kucun = (TextView) view.findViewById(R.id.tv_kucun);
        gv_color = (GridView) view.findViewById(R.id.gv_color);
        tv_jian = (TextView) view.findViewById(R.id.tv_jian);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_queren = (TextView) view.findViewById(R.id.tv_queren);
        return view;
    }

    @Override
    public void setUiBeforShow() {

        colors.clear();

//        images.add("http://h.hiphotos.baidu.com/image/h%3D300/sign=ff62800b073b5bb5a1d726fe06d2d523/a6efce1b9d16fdfa7807474eb08f8c5494ee7b23.jpg");
//        images.add("http://g.hiphotos.baidu.com/image/h%3D300/sign=0a9ac84f89b1cb1321693a13ed5556da/1ad5ad6eddc451dabff9af4bb2fd5266d0163206.jpg");
//        images.add("http://a.hiphotos.baidu.com/image/h%3D300/sign=61660ec2207f9e2f6f351b082f31e962/500fd9f9d72a6059e5c05d3e2f34349b023bbac6.jpg");
//        images.add("http://c.hiphotos.baidu.com/image/h%3D300/sign=f840688728738bd4db21b431918a876c/f7246b600c338744c90c3826570fd9f9d62aa09a.jpg");


        for (int i = 0; i < 5; i++) {
            colors.add("假数据");
        }

        tv_price.setText("价格假数据");
        tv_kucun.setText("库存假数据");
        kunCun = 9000;

        rv_pic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_pic.setAdapter(new ImageAdapter());


        gv_color.setAdapter(new ColorAdapter());

        tv_jian.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        tv_queren.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jian:
                if (num > 1) {
                    num--;
                    tv_num.setText(num + "");
                } else {
                    ToastUtil.show(getContext(), "不能再减了");
                }
                break;
            case R.id.tv_add:
                if (num < kunCun) {
                    num++;
                    tv_num.setText(num + "");
                } else {
                    ToastUtil.show(getContext(), "库存不足");
                }
                break;
            case R.id.tv_queren:
                if (null != myChooseNum){
                    myChooseNum.getNum(num,colors.get(0));
                }

                dismiss();
                break;
        }
    }

    public GetChooseNum myChooseNum;

    public void setGetChooseNum(GetChooseNum getChooseNum){
        myChooseNum = getChooseNum;
    }
    public interface GetChooseNum{
        void getNum(int num,String chooseColor);
    }

    //图片列表
    class ImageAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_selete_color_pic, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ImageHolder) holder).setData(images.get(position));
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        class ImageHolder extends RecyclerView.ViewHolder {
            ImageView tv_pic;

            public ImageHolder(View itemView) {
                super(itemView);
                tv_pic = (ImageView) itemView.findViewById(R.id.tv_pic);
            }

            public void setData(String img) {
                ImageLoaderUtil.displayImage(getContext(), img, tv_pic);
            }
        }
    }

    //颜色列表
    class ColorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return colors.size();
        }

        @Override
        public Object getItem(int position) {
            return colors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_selete_color_text, null);
                holder.tv_color = (TextView) convertView.findViewById(R.id.tv_color);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_color.setText(colors.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tv_color;
        }
    }
}
