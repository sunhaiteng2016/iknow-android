package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.utils.sun.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by yao.cui on 2017/6/9.
 */

public class NewsListAdapter extends CustomBaseAdapter<News> {

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

    public NewsListAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }

    public NewsListAdapter(Fragment fragment) {
        super(fragment);
        this.mContext = fragment.getContext();
    }

    public void setReaded(List<String> ids) {
        readedIdList = ids;

    }

    /**
     * 设置是否需要编辑状态  目前只在 我的收藏界面需要
     *
     * @param needEditStatus
     */
    public NewsListAdapter setNeedEditStatus(boolean needEditStatus) {
        isNeedEditStatus = needEditStatus;
        return this;
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

    @Override
    public int getItemViewType(int position) {
        News news = getItem(position);
        if (news == null || news.newsStyle == 0) {
            return 0;
        }
        return news.newsStyle;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (convertView == null) {
            if (TYPE_BIG_IMG == viewType) {//大图新闻
                convertView = dealEditView(inflater.inflate(R.layout.item_big_img, parent, false));
                BigImgHolder holder = new BigImgHolder(convertView);
                convertView.setTag(holder);
            } else if (TYPE_LEFT_IMG == viewType) {//左侧图片新闻
                convertView = dealEditView(inflater.inflate(R.layout.item_left_img, parent, false));
                LeftImgHolder holder = new LeftImgHolder(convertView);
                convertView.setTag(holder);
            } else if (TYPE_THREE_IMG == viewType) {//三张图片新闻
                convertView = dealEditView(inflater.inflate(R.layout.item_three_img, parent, false));
                ThreeImgHolder holder = new ThreeImgHolder(convertView);
                convertView.setTag(holder);
            } else if (TYPE_NONE_AD == viewType) {//广告
                convertView = dealEditView(inflater.inflate(R.layout.item_ad_image, parent, false));
                BigImgHolder holder = new BigImgHolder(convertView);
                convertView.setTag(holder);
            } else { //无图
                convertView = dealEditView(inflater.inflate(R.layout.item_big_img, parent, false));
                NoneImgHolder holder = new NoneImgHolder(convertView);
                convertView.setTag(holder);
            }
        }

        News news = dataList.get(position);

        BaseHolder baseHolder = (BaseHolder) convertView.getTag();
        baseHolder.switchSelectedStatus(news);

        boolean isReaded = readedIdList != null && readedIdList.contains(getItem(position).newsId);

        if (baseHolder instanceof LeftImgHolder) {
            ((LeftImgHolder) baseHolder).setData(news, isReaded);
        } else if (baseHolder instanceof BigImgHolder) {
            ((BigImgHolder) baseHolder).setData(news, isReaded);
        } else if (baseHolder instanceof ThreeImgHolder) {
            ((ThreeImgHolder) baseHolder).setData(news, isReaded);
        } else {
            ((NoneImgHolder) baseHolder).setData(news, isReaded);
        }
        baseHolder.tvTop.setVisibility(news.isStick() ? View.VISIBLE : View.GONE);
        return convertView;
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

    class NoneImgHolder extends BaseHolder {
        @BindView(R.id.img)
        ImageView ivBig;

        public NoneImgHolder(View view) {
            super(view);
            ivBig.setVisibility(View.GONE);
        }

        @Override
        public void setData(News news, boolean isReaded) {
            super.setData(news, isReaded);
        }
    }

    class BigImgHolder extends BaseHolder {
        @BindView(R.id.img)
        ImageView ivBig;
        @BindView(R.id.tvDate)
        TextView tvdata;

        public BigImgHolder(View view) {
            super(view);

        }

        @Override
        public void setData(News news, boolean isReaded) {
            super.setData(news, isReaded);
            int sreenWidth = Util.getScreenWidth(context);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.height = sreenWidth / 16 * 9;
            ivBig.setLayoutParams(params);
            ImageLoaderUtil.displayImage(mContext, news.getFirstPic(), ivBig, getDisplayImageOptions());
            tvdata.setText(news.publishTime);
        }
    }

    class LeftImgHolder extends BaseHolder {
        @BindView(R.id.img)
        ImageView ivLeft;
        public LeftImgHolder(View view) {
            super(view);
        }

        @Override
        public void setData(News news, boolean isReaded) {
            super.setData(news, isReaded);
            ImageLoaderUtil.displayImage(mContext, news.getFirstPic(), ivLeft, getDisplayImageOptions());
        }
    }

    class ThreeImgHolder extends BaseHolder {
        @BindViews({R.id.img1, R.id.img2, R.id.img3})
        List<ImageView> ivs;

        public ThreeImgHolder(View view) {
            super(view);
        }

        @Override
        public void setData(News news, boolean isReaded) {
            super.setData(news, isReaded);
            String[] imgs = news.getPicArray();
            int count = Math.min(news.getPicArray().length, ivs.size());
            for (int i = 0; i < count; i++) {
                ImageLoaderUtil.displayImage(mContext, imgs[i], ivs.get(i), getDisplayImageOptions());
            }
        }
    }


}
