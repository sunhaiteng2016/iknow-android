package com.beyond.popscience.module.home.adapter;

import com.beyond.popscience.frame.base.CustomRecyclerBaseAdapter;
import com.beyond.popscience.module.home.entity.News;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class newListRecyViewAdapter extends CustomRecyclerBaseAdapter<News> {

    /**
     * 是否需要编辑状态
     */
    private boolean isNeedEditStatus = true;

    //1 单小 2单大 3三图  4无图
    private static final int TYPE_LEFT_IMG = 1;
    private static final int TYPE_BIG_IMG = 2;
    private static final int TYPE_THREE_IMG = 3;
    private static final int TYPE_NONE_IMG = 4;
    private static final int TYPE_NONE_AD = 5;

    /**
     * 被选中的news
     */
    private List<News> selectedNewsList = new ArrayList<>();
    private List<String> readedIdList = new ArrayList<>();

    private Context mContext;

    public newListRecyViewAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }

    public newListRecyViewAdapter(Fragment fragment) {
        super(fragment);
        this.mContext = fragment.getContext();
    }

    public void setReaded(List<String> ids) {
        readedIdList = ids;

    }


    /**
     * 切换选中状态
     *
     * @param news
     */
    public void toggle(News news) {
        if (news != null) {
            if (selectedNewsList.contains(news)) {
                selectedNewsList.remove(news);
            } else {
                selectedNewsList.add(news);
            }
        }
    }

    /**
     * 清空被选中的数据
     */
    public void clearSelectedNews() {
        selectedNewsList.clear();
    }

    /**
     * 获取被选中的数据
     *
     * @return
     */
    public List<News> getSelectedNewsList() {
        return selectedNewsList;
    }

    /**
     * 处理成需要编辑状态的view
     *
     * @param view
     * @return
     */
    private View dealEditView(View view) {
        if (isNeedEditStatus) {
            View editView = inflater.inflate(R.layout.adapter_collection_item, null);
            if (view.getLayoutParams() != null) {
                editView.setLayoutParams(view.getLayoutParams());
            }
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayout contentReLay = (RelativeLayout) editView.findViewById(R.id.contentReLay);
            ImageView okImgView = (ImageView) editView.findViewById(R.id.okImgView);
            contentReLay.addView(view);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) okImgView.getLayoutParams();
            layoutParams.setMargins(DensityUtil.dp2px(context, 8), 0, DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 8));
            okImgView.setLayoutParams(layoutParams);
            return editView;
        }
        return view;
    }

    private    itemOnClick  nClick;
     @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView;
        if (TYPE_BIG_IMG == viewType) {//大图新闻
            convertView = inflater.inflate(R.layout.item_big_img, parent, false);
            BigImgHolder holder = new BigImgHolder(convertView);
            return holder;
        } else if (TYPE_LEFT_IMG == viewType) {//左侧图片新闻
            convertView = inflater.inflate(R.layout.item_left_img, parent, false);
            LeftImgHolder holder = new LeftImgHolder(convertView);
            return holder;
        } else if (TYPE_THREE_IMG == viewType) {//三张图片新闻
            convertView = inflater.inflate(R.layout.item_three_img, parent, false);
            ThreeImgHolder holder = new ThreeImgHolder(convertView);
            return holder;
        } else if (TYPE_NONE_AD == viewType) {//广告
            convertView = inflater.inflate(R.layout.item_ad_image, parent, false);
            BigImgHolder holder = new BigImgHolder(convertView);
            return holder;
        } else { //无图
            convertView = inflater.inflate(R.layout.item_big_img, parent, false);
            NoneImgHolder holder = new NoneImgHolder(convertView);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BigImgHolder) {
            Glide.with(mContext).load(getDataList().get(position).getFirstPic()).into(((BigImgHolder) holder).ivBig);
            ((BigImgHolder) holder).tvdata.setText(getDataList().get(position).publishTime);
            ((BigImgHolder) holder).tvTitle.setText(getDataList().get(position).title);
            ((BigImgHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nClick.onMyClick(position,((BigImgHolder) holder).tvTitle);
                }
            });
        }
        if (holder instanceof LeftImgHolder) {
            Glide.with(mContext).load(getDataList().get(position).getFirstPic()).into(((LeftImgHolder) holder).ivLeft);
            ((LeftImgHolder) holder).tvdata.setText(getDataList().get(position).publishTime);
            ((LeftImgHolder) holder).tvTitle.setText(getDataList().get(position).title);
            ((LeftImgHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nClick.onMyClick(position,((LeftImgHolder) holder).tvTitle);
                }
            });
        }
        if (holder instanceof ThreeImgHolder) {
            String[] imgs = getDataList().get(position).getPicArray();
            int count = 3;
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    Glide.with(mContext).load(imgs[i]).into(((ThreeImgHolder) holder).view1);
                }
                if (i == 1) {
                    Glide.with(mContext).load(imgs[i]).into(((ThreeImgHolder) holder).view2);
                }
                if (i == 2) {
                    Glide.with(mContext).load(imgs[i]).into(((ThreeImgHolder) holder).view3);
                }
            }
            ((ThreeImgHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nClick.onMyClick(position,((ThreeImgHolder) holder).item);
                }
            });
        }
        if (holder instanceof NoneImgHolder) {
        }
    }


    public  interface  itemOnClick{
        void onMyClick(int position,View  view);
    }
    public  void setItemOnClick(itemOnClick onClick){
            this.nClick=onClick;
    }
    @Override
    public int getItemViewType(int position) {
        News news = getItem(position);
        if (news == null || news.newsStyle == 0) {
            return 0;
        }
        return news.newsStyle;
    }


    /**
     * 处理选中状态   我的收藏
     */
    class BaseHolder {

        @BindView(R.id.okImgView)
        ImageView okImgView;

        //新闻标题 时间 查看数量
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvViewCount)
        TextView tvViewCount;
        @BindView(R.id.tvTip)
        TextView tvTip;
        @BindView(R.id.tvTop)
        TextView tvTop;

        public BaseHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        public void setData(News news, boolean isReaded) {
            tvTitle.setText(news.title);
            tvDate.setText(news.publishTime);
            tvViewCount.setText("查看次数:" + news.view);

            tvTip.setVisibility(View.GONE);

            if (isReaded) {
                tvTitle.setTextColor(Color.parseColor("#6F6F6F"));
            } else {
                tvTitle.setTextColor(ContextCompat.getColor(context, R.color.grey17));
            }
        }

        /**
         * 切换选中状态
         */
        public void switchSelectedStatus(News news) {
            if (isNeedEditStatus && okImgView != null) {
                if (selectedNewsList.contains(news)) {    //选中
                    okImgView.setVisibility(View.VISIBLE);
                } else {
                    okImgView.setVisibility(View.GONE);
                }
            }
        }

    }

    class NoneImgHolder extends RecyclerView.ViewHolder {

        public NoneImgHolder(View view) {
            super(view);
            view.findViewById(R.id.img).setVisibility(View.GONE);
        }

    }

    class BigImgHolder extends RecyclerView.ViewHolder {
        public ImageView ivBig;
        public TextView tvdata;
        public TextView tvTitle;
        public LinearLayout item;

        public BigImgHolder(View view) {
            super(view);
            //setData();
            ivBig = (ImageView) view.findViewById(R.id.img);
            tvdata = (TextView) view.findViewById(R.id.tvDate);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            item = (LinearLayout) view.findViewById(R.id.item);

        }
    }

    class LeftImgHolder extends RecyclerView.ViewHolder {

        public ImageView ivLeft;
        public TextView tvdata;
        public TextView tvTitle;
        public LinearLayout item;

        public LeftImgHolder(View view) {
            super(view);
            ivLeft = (ImageView) view.findViewById(R.id.img);
            tvdata = (TextView) view.findViewById(R.id.tvDate);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            item = (LinearLayout) view.findViewById(R.id.item);
        }
    }

    class ThreeImgHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public ImageView view1;
        public ImageView view2;
        public ImageView view3;
        public LinearLayout item;

        public ThreeImgHolder(View view) {
            super(view);
            view1 = (ImageView) view.findViewById(R.id.img1);
            view2 = (ImageView) view.findViewById(R.id.img2);
            view3 = (ImageView) view.findViewById(R.id.img3);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            item = (LinearLayout) view.findViewById(R.id.item);
        }
    }

}
