package com.beyond.popscience.frame.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linjinfa 331710168@qq.com
 * @date 2014年6月6日
 */
public abstract class CustomPagerAdapter<T> extends PagerAdapter {

    protected List<T> dataList = new ArrayList<T>();
    protected Map<Integer,View> viewMap = new HashMap<Integer, View>();
    protected LinkedList<View> mCachedItemViews = new LinkedList<View>();
    protected Context context;
    protected Fragment fragment;
    protected LayoutInflater inflater;
    protected ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    protected ViewGroup container;

    public CustomPagerAdapter(Fragment fragment) {
        this.fragment = fragment;
        context = this.fragment.getActivity();
        init();
    }

    public CustomPagerAdapter(Context context) {
        this.context = context;
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        if(context==null){
            return ;
        }
        this.inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        if(!ImageLoader.getInstance().isInited()){
            ImageLoaderUtil.init(context);
        }
    }

    /**
     * 获取一个可以复用的Item View
     *
     * @return
     */
    protected View getCachedView() {
        if (mCachedItemViews.size() != 0) {
            return mCachedItemViews.removeFirst();
        }
        return null;
    }

    /**
     *
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    protected DisplayImageOptions getDisplayImageOptions(int targetWidth, int targetHeight){
        return getDisplayImageOptions(targetWidth,targetHeight,-1);
    }

    /**
     *
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    protected DisplayImageOptions getDisplayImageOptions(int targetWidth, int targetHeight, int radiusPixels){
        if(displayImageOptions==null || displayImageOptions.getPreProcessor()==null){
            displayImageOptions = getBaseDisplayImageOptions().build();
        }
        return displayImageOptions;
    }

    /**
     *
     * @return
     */
    protected DisplayImageOptions getDisplayImageOptions(){
        if(displayImageOptions==null || displayImageOptions.getPreProcessor()!=null){
            displayImageOptions = getBaseDisplayImageOptions().build();
        }
        return displayImageOptions;
    }

    /**
     *
     * @return
     */
    protected DisplayImageOptions.Builder getBaseDisplayImageOptions(){
        return new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(200, true, true, false)).considerExifParams(true).cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(R.drawable.default_bg_img_loading).showImageOnFail(R.drawable.default_bg_img_loading).showImageForEmptyUri(R.drawable.default_bg_img_loading);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
        this.container = container;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewMap.get(position));
    }

    /**
     * 获取Item数据
     * @param position
     * @return
     */
    public T getItem(int position) {
        if(position<0 || position>dataList.size()-1){
            return null;
        }
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public List<T> getDataList() {
        return dataList;
    }

    public Map<Integer, View> getViewMap() {
        return viewMap;
    }

    /**
     * 清理所有数据
     */
    public void clear(){
        dataList.clear();
        viewMap.clear();
        if(container!=null){
            container.removeAllViews();
        }
    }
}
