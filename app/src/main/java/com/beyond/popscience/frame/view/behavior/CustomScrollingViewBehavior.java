package com.beyond.popscience.frame.view.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * 增加Listener
 * Created by linjinfa on 2017/6/23.
 * email 331710168@qq.com
 */
public class CustomScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {

    private IScrollChangeListener iScrollChangeListener;

    public CustomScrollingViewBehavior() {
    }

    public CustomScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if(iScrollChangeListener!=null){
            iScrollChangeListener.onDependentViewChanged(parent, child, dependency);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    public IScrollChangeListener getiScrollChangeListener() {
        return iScrollChangeListener;
    }

    public void setiScrollChangeListener(IScrollChangeListener iScrollChangeListener) {
        this.iScrollChangeListener = iScrollChangeListener;
    }

    /**
     *
     */
    public interface IScrollChangeListener{
        void onDependentViewChanged(CoordinatorLayout parent, View child, View dependency);
    }
}
