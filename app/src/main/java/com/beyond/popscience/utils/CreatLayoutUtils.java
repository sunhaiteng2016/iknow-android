package com.beyond.popscience.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

public class CreatLayoutUtils {

    public static void creatLinearLayout(Context mContext, RecyclerView rlv) {
        rlv.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        //设置布局管理器
        rlv.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rlv.setHasFixedSize(true);
        rlv.setNestedScrollingEnabled(false);
    }

    public static void creatLinearLayoutHorizontal(Context mContext, RecyclerView rlv) {
        rlv.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        rlv.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
    }

    public static void cretGridViewLayout(Context mContext, RecyclerView rlv, int count) {
        GridLayoutManager layoutManage = new GridLayoutManager(mContext, count);
        rlv.setLayoutManager(layoutManage);
    }
}
