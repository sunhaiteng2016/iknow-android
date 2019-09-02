package com.beyond.popscience.utils.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * 说明: glide公共参数配置
 * 作者: fangkaijin on 2017/5/17.17:50
 * 邮箱: fangkaijin@gmail.com
 */

public class SuyhAgentGlideModle implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskSize = 1024 * 1024 * 100;
        int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;  // 取1/8最大内存作为最大缓存
        // 定义缓存大小和位置
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "cache", diskSize)); //sd卡中

        // 默认内存和图片池大小
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));

        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.DEFAULT);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(IGlideModel.class, InputStream.class,
                new KJDataLoader.Factory());
    }
}