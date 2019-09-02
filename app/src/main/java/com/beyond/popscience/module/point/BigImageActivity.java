package com.beyond.popscience.module.point;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.beyond.popscience.R;
import com.beyond.popscience.view.preview.ImageInfo;
import com.beyond.popscience.view.preview.NineGridView;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.view.preview.NineGridViewClickAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class BigImageActivity extends BaseActivity {

    /* @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.activity_big_image);
         ImageView iv_big= (ImageView) findViewById(R.id.iv_big);
         Glide.with(this).load(getIntent().getStringExtra("image")).into(iv_big);
     }*/
    public NineGridView nineGrid;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_big_image;
    }

    @Override
    public void initUI() {
        super.initUI();
      /*  NineGridView.setImageLoader(new GlideImageLoader());
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        ImageInfo info = new ImageInfo();
        info.setThumbnailUrl(getIntent().getStringExtra("image"));
        info.setBigImageUrl(getIntent().getStringExtra("image"));
        imageInfo.add(info);
        nineGrid = (NineGridView) findViewById(R.id.nineGrid);
        nineGrid.setAdapter(new NineGridViewClickAdapter(this, imageInfo));*/

        ImageView iv_ss = (ImageView) findViewById(R.id.iv_ss);
        Glide.with(this).load(getIntent().getStringExtra("image")).into(iv_ss);
    }
    /** Glide 加载 */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

}

