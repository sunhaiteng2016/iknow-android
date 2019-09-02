package com.beyond.popscience.utils.glide;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

import java.io.InputStream;

/**
 * 说明: glide数据加载模型
 * 作者: fangkaijin on 2017/5/17.17:52
 * 邮箱: fangkaijin@gmail.com
 */

public class KJDataLoader extends BaseGlideUrlLoader<IGlideModel> {
    public KJDataLoader(Context context) {
        super(context);
    }

    public KJDataLoader(ModelLoader<GlideUrl, InputStream> urlLoader) {
        super(urlLoader, null);
    }

    @Override
    protected String getUrl(IGlideModel model, int width, int height) {
        return model.buildDataModelUrl(width, height);
    }

    /**
     */
    public static class Factory implements ModelLoaderFactory<IGlideModel, InputStream> {

        @Override
        public ModelLoader<IGlideModel, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new KJDataLoader(factories.buildModelLoader(GlideUrl.class, InputStream.class));
        }

        @Override
        public void teardown() {
        }
    }
}