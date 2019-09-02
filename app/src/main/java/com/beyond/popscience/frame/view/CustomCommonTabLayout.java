package com.beyond.popscience.frame.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.FragmentChangeManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by linjinfa on 2017/6/25.
 * email 331710168@qq.com
 */
public class CustomCommonTabLayout extends CommonTabLayout {

    private OnTabSelectListener onTabChangeListener;

    public CustomCommonTabLayout(Context context) {
        super(context);
    }

    public CustomCommonTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTabData(ArrayList<CustomTabEntity> tabEntitys, FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
        FragmentChangeManager fragmentChangeManager = new FragmentChangeManager(fm, containerViewId, fragments);
        try {
            Field field = getClass().getSuperclass().getDeclaredField("mFragmentChangeManager");
            field.setAccessible(true);
            field.set(this, fragmentChangeManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTabData(tabEntitys);
    }

    @Override
    public void setCurrentTab(int currentTab) {
        if(onTabChangeListener!=null && onTabChangeListener instanceof OnTabChangeListener){
            boolean isIntercept = ((OnTabChangeListener)onTabChangeListener).onInterceptionSetCurrentTab(currentTab);
            if(isIntercept){
                return ;
            }
        }
        super.setCurrentTab(currentTab);
    }

    @Override
    public void setOnTabSelectListener(OnTabSelectListener listener) {
        super.setOnTabSelectListener(listener);
        this.onTabChangeListener = listener;
    }

    public interface OnTabChangeListener extends OnTabSelectListener {
        /**
         * 是否拦截 不执行选中事件
         * @param currentTab    true：拦截     false：不拦截
         * @return
         */
        boolean onInterceptionSetCurrentTab(int currentTab);
    }

}
