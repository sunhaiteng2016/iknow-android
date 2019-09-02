package com.beyond.popscience.frame.view.imageloader;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by linjinfa on 2017/11/27.
 * email 331710168@qq.com
 */
public class ImageLoaderView extends SimpleDraweeView {

    public ImageLoaderView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public ImageLoaderView(Context context) {
        super(context);
    }

    public ImageLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageLoaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImageLoaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
