package com.beyond.popscience.frame.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.beyond.library.util.DensityUtil;

import java.lang.ref.WeakReference;

/**
 * Created by yao.cui on 2017/6/19.
 */

public class AutoViewPager extends ViewPager {

    public static final int SCROLL_WHAT  = 0;

    private long mInterval = 4000;
    private MyHandler mHandler = new MyHandler(this);
    /**
     * 是否停止
     */
    private boolean isStop = false;

    public AutoViewPager(Context context) {
        super(context);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置轮播间隔 ms
     * @param interval
     */
    public void setInterval(long interval){
        if (interval>0){
            this.mInterval = 4000;
        }

    }

    /**
     * 设置自动轮播
     */
    public void startAutoScroll(){
        stopAutoScroll();
        isStop = false;
        sendScrollMessage();
    }

    /**
     * 停止滚动
     */
    public void stopAutoScroll(){
        isStop = true;
        mHandler.removeMessages(SCROLL_WHAT);
    }

    /**
     * scroll only once
     */
    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount;
        if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
            stopAutoScroll();
            return;
        }

        int nextItem =  ++currentItem;
        if (nextItem == totalCount){
            nextItem = 0;
        }
        setCurrentItem(nextItem, true);

    }

    private void sendScrollMessage(){
        mHandler.sendEmptyMessageDelayed(SCROLL_WHAT,mInterval);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
    }

    private class MyHandler extends Handler {

        private final WeakReference<AutoViewPager> autoViewPager;

        public MyHandler(AutoViewPager autoViewPager) {
            this.autoViewPager = new WeakReference<AutoViewPager>(autoViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    AutoViewPager pager = this.autoViewPager.get();
                    if (pager != null) {
                        pager.scrollOnce();
                        if(!isStop){
                            pager.sendScrollMessage();
                        }
                    }else{
                        stopAutoScroll();
                    }
                default:
                    break;
            }
        }
    }
}
