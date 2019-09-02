package com.beyond.popscience.frame.view.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Created by linjinfa on 16/9/28.
 * email 331710168@qq.com
 */
public class MDImageViewAware extends ImageViewAware {

    public MDImageViewAware(ImageView imageView) {
        super(imageView);
    }

    public MDImageViewAware(ImageView imageView, boolean checkActualViewSize) {
        super(imageView, checkActualViewSize);
    }

    @Override
    protected void setImageDrawableInto(Drawable drawable, View view) {
        if(drawable instanceof NinePatchDrawable){
            ((ImageView) view).setBackgroundDrawable(drawable);
            super.setImageDrawableInto(null, view);
        }else{
            ((ImageView) view).setBackgroundDrawable(null);
            super.setImageDrawableInto(drawable, view);
        }
    }

    @Override
    protected void setImageBitmapInto(Bitmap bitmap, View view) {
        super.setImageBitmapInto(bitmap, view);
        ((ImageView) view).setBackgroundDrawable(null);
    }

}
