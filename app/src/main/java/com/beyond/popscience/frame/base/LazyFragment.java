package com.beyond.popscience.frame.base;

import android.util.Log;

/**
 * 懒加载fragment
 *
 * Created by yao.cui on 2017/6/30.
 */

public class LazyFragment extends BaseFragment {

    protected boolean isVisible;
    protected boolean isPrepared;

    @Override
    public void initUI() {
        super.initUI();
        isPrepared = true;
        if(isPrepared &&isVisible) {
            lazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("uservisiable","------" +isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    protected int getLayoutResID() {
        return 0;
    }

    protected void onVisible(){
        if(isPrepared &&isVisible) {
            lazyLoad();
        }

    }

    /**
     * 填充各控件的数据
     */
    protected void lazyLoad(){


    };

    protected void onInvisible(){}
}
