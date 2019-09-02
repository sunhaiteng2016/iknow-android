package com.beyond.popscience.frame.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.view.imageloader.MDImageViewAware;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

/**
 *
 */
public class ImageLoaderUtil {

    /**
     * @param url
     * @param imageAware
     */
    public static void display(Context context, String url, ImageView imageAware) {
        if (TextUtils.isEmpty(url)||imageAware==null){
            return;
        }
        displayImage(context, url, imageAware, null);
    }

    /**
     * ImageLoader display
     *
     * @param context
     * @param url
     * @param imageAware
     */
    public static void displayImage(Context context, String url, ImageView imageAware) {
        displayImage(context, url, imageAware, new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build());
    }

    /**
     *
     * @param context
     * @param url
     * @param imageAware
     * @param displayImageOptions
     */
    public static void displayImage(Context context, String url, ImageView imageAware, DisplayImageOptions displayImageOptions) {
        displayImage(context, url, imageAware, displayImageOptions, null);
    }

    /**
     *
     * @param url
     */
    public static void displayImageFresco(String url, SimpleDraweeView simpleDraweeView) {
        DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(url)
                .build();
        simpleDraweeView.setController(mDraweeController);
    }

    /**
     *
     * @param context
     * @param url
     * @param imageAware
     * @param displayImageOptions
     * @param loadingListener
     */
    public static void displayImage(Context context, String url, ImageView imageAware, DisplayImageOptions displayImageOptions, ImageLoadingListener loadingListener) {
        if (!ImageLoader.getInstance().isInited()) {
            init(context);
        }
        ImageLoader.getInstance().displayImage(url, new MDImageViewAware(imageAware), displayImageOptions, loadingListener);
    }

    /**
     * ImageLoader display
     *
     * @param context
     * @param url
     * @param displayImageOptions
     */
    public static void loadImage(Context context, String url, DisplayImageOptions displayImageOptions, ImageLoadingListener listener) {
        if (!ImageLoader.getInstance().isInited()) {
            init(context);
        }
        ImageLoader.getInstance().loadImage(url, displayImageOptions, listener);
    }

    /**
     * @param context
     */
    public static void init(Context context) {
        if(!ImageLoader.getInstance().isInited()){
            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.NONE)
                    .build();
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .defaultDisplayImageOptions(displayImageOptions)
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(50 * 1024 * 1024) // 50M
                    .tasksProcessingOrder(QueueProcessingType.FIFO)
//                    .imageDownloader(new HttpHeaderImageDownloader(context))    //自定义的ImageDownloader @linjifa
                    .build();
            ImageLoader.getInstance().init(configuration);
        }
    }

    /**
     * 从sdcard缓存中获取图片地址
     *
     * @param imgUrl
     * @return
     */
    public static String getImgPathFromDiskCache(String imgUrl) {
        File file = getImgFileFromDiskCache(imgUrl);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "";
    }

    /**
     * 从sdcard缓存中获取图片
     *
     * @param imgUrl
     * @return
     */
    public static File getImgFileFromDiskCache(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return null;
        }
        if (!ImageLoader.getInstance().isInited()) {
            init(BeyondApplication.getInstance());
        }
        File imgFile = ImageLoader.getInstance().getDiskCache().get(imgUrl);
        if (imgFile != null && imgFile.exists()) {
            return imgFile;
        }
        return null;
    }

    /**
     * 暂停加载图片
     */
    public static void pauseLoadImg(){
        if (ImageLoader.getInstance().isInited()){
            ImageLoader.getInstance().pause();
        }
    }

    /**
     * 恢复加载图片
     */
    public static void resumeLoadImg(){
        if (ImageLoader.getInstance().isInited()){
            ImageLoader.getInstance().resume();
        }
    }
}
