package com.beyond.popscience.module.home.fragment.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager  extends ViewPager {

    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new XYScrollDetector());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event)&&mGestureDetector.onTouchEvent(event);

    }
    class XYScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(distanceY!=0&&distanceX!=0){
            }
            if(Math.abs(distanceY) >= Math.abs(distanceX)) {
                return false;
            }
            return true;
        }
    }
}
