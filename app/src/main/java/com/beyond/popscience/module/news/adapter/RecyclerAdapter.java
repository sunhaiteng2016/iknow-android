package com.beyond.popscience.module.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;

import java.util.List;

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int BANNER_VIEW_TYPE = 0;//轮播图
    private final int CHANNEL_VIEW_TYPE = 1;//频道
    private final int GIRL_VIEW_TYPE = 2;//美女
    private final int NORMAL_VIEW_TYPE = 3;//正常布局
    private Context context;


    private List  list;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == BANNER_VIEW_TYPE) {//如果viewType是轮播图就去创建轮播图的viewHolder
            view = getView(R.layout.adapter_news_detail_item);
            BannerHolder bannerHolder = new BannerHolder(view);
            return bannerHolder;
        } else if (viewType == CHANNEL_VIEW_TYPE) {//频道的type
            view = getView(R.layout.adapter_news_detail_txt_item);
            return new ChannelHolder(view);
        } else if (viewType == GIRL_VIEW_TYPE) {//美女
            view = getView(R.layout.adapter_news_detail_item_video);
            return new GirlHolder(view);
        } else {//正常
            view = getView(R.layout.adapter_news_detail_txt_item);
            return new NormalHolder(view);
        }
    }
    /**
     * 用来引入布局的方法
     */
    private View getView(int view) {
        View view1 = View.inflate(context, view, null);
        return view1;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //判断不同的ViewHolder做不同的处理
        if (holder instanceof BannerHolder) {//轮播图
            BannerHolder bannerHolder = (BannerHolder) holder;
            //调用设置轮播图相关方法
        } else if (holder instanceof ChannelHolder) {//频道
            ChannelHolder channelHolder = (ChannelHolder) holder;
            //设置频道
        } else if (holder instanceof GirlHolder) {//美女
        } else if (holder instanceof NormalHolder) {//正常布局
            NormalHolder normalHolder = (NormalHolder) holder;
            normalHolder.textView.setText("12121212121212121212121d2s1d21asd21asd21as2d12sad12sad12s1d21sd21sad21s2");
        }
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {//第0个位置是轮播图
            return BANNER_VIEW_TYPE;
        } else if (position == 1) {//第一个是频道布局
            return CHANNEL_VIEW_TYPE;
        } else if (position == 2) {//第2个位置是美女布局
            return GIRL_VIEW_TYPE;
        } else {//其他位置返回正常的布局
            return NORMAL_VIEW_TYPE;
        }
    }

    /*****************************************下面是为不同的布局创建不同的ViewHolder*******************************************************/
    /**
     * 轮播图的ViewHolder
     */
    public static class BannerHolder extends RecyclerView.ViewHolder {
       // Banner banner;

        public BannerHolder(View itemView) {
            super(itemView);
            //banner = (Banner) itemView.findViewById(R.id.banner);

        }
    }

    /**
     * 频道列表的ViewHolder
     */
    public static class ChannelHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        public ChannelHolder(View itemView) {
            super(itemView);
           // linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_channel);

        }
    }

    /**
     * 美女的ViewHolder
     */
    public static class GirlHolder extends RecyclerView.ViewHolder {
        GridView gridView;

        public GirlHolder(View itemView) {
            super(itemView);
            gridView = (GridView) itemView.findViewById(R.id.gridview);
        }
    }

    /**
     * 正常布局的ViewHolder
     */
    public static class NormalHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public NormalHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.contextTxtView);
        }
    }
}
