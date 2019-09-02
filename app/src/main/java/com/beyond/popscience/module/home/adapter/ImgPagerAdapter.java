package com.beyond.popscience.module.home.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyond.popscience.frame.base.CustomPagerAdapter;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.module.home.AnnouncementActivity;
import com.beyond.popscience.module.home.entity.News;
import com.beyond.popscience.module.mservice.X5WebViewActivity;
import com.beyond.popscience.module.news.NewsDetailActivity;
import com.beyond.popscience.widget.IRecycling;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImgPagerAdapter extends CustomPagerAdapter implements IRecycling {


    private List<Carousel> mCarousels;
    private boolean mIsLoop = false;
    private int type;

    public ImgPagerAdapter(Activity context, boolean isLoop, List<Carousel> urls) {
        super(context);
        this.mIsLoop = isLoop;
        this.mCarousels = urls;
    }

    public ImgPagerAdapter(Fragment context, boolean isLoop, List<Carousel> urls) {
        super(context);
        this.mIsLoop = isLoop;
        this.mCarousels = urls;
    }

    public void setmCarousels(List<Carousel> mCarousels) {
        if (mCarousels == null){
            mCarousels = new ArrayList<>();
        }
        this.mCarousels = mCarousels;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        if (this.mCarousels== null ||this.mCarousels.isEmpty()) return 0;
        return mIsLoop ? Integer.MAX_VALUE : this.mCarousels.size();
    }

    @Override
    public int getRealCount() {
        return this.mCarousels ==null ? 0: this.mCarousels.size();
    }

    @Override
    public int getRealPosition(int position) {
        if (mCarousels == null || mCarousels.isEmpty()) return 0;

        return position % mCarousels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Carousel carousel = mCarousels.get(getRealPosition(position));
        ImageView imgView = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgView.setLayoutParams(params);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(carousel.getPic()).into(imgView);
        container.addView(imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Carousel carousel1 = mCarousels.get(getRealPosition(position));
                if("2".equals(carousel1.type)){ //外链
                    X5WebViewActivity.startActivity(context, carousel1.link, carousel1.getTitle());
                    return ;
                }
                News news = new News();
                news.newsId = carousel1.newsId;
                news.pics = carousel1.getPic();
                news.title = carousel1.getTitle();
                news.appNewsType = type;

                if(type == News.TYPE_TOWN_ANNOUNCEMENT){   //公告
                    AnnouncementActivity.startActivity(context, news);
                }else{
                    NewsDetailActivity.startActivity(context, news);
                }
            }
        });
        return imgView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}