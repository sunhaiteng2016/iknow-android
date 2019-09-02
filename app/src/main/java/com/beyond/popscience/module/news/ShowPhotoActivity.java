package com.beyond.popscience.module.news;

import android.content.Context;
import android.content.Intent;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.view.photoview.HackyViewPager;
import com.beyond.popscience.module.news.adapter.PhotoPagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 预览图片
 * Created by linjinfa 331710168@qq.com on 2015/1/29.
 */
public class ShowPhotoActivity extends BaseActivity implements PhotoPagerAdapter.ViewTapClick {

    private final static String EXTRA_IMG_URL_LIST_KEY = "imgUrlList";
    private final static String EXTRA_POSITION_KEY = "position";

    HackyViewPager hackyViewPager;
    List<String> imgUrlList;
    int position;

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, String imgUrl){
        startActivity(context, Arrays.asList(imgUrl), 0);
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, List<String> imgUrlList, int position){
        context.startActivity(new Intent(context,ShowPhotoActivity.class).putExtra(EXTRA_IMG_URL_LIST_KEY, (Serializable)imgUrlList).putExtra(EXTRA_POSITION_KEY, position));
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, ArrayList<String> imgUrlList, int position){
        context.startActivity(new Intent(context,ShowPhotoActivity.class).putExtra(EXTRA_IMG_URL_LIST_KEY, imgUrlList).putExtra(EXTRA_POSITION_KEY,position));
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_show_photo;
    }

    public void initUI() {
        initViewAndData();

        PhotoPagerAdapter adapter = new PhotoPagerAdapter(this);
        adapter.getDataList().addAll(imgUrlList);
        hackyViewPager.setAdapter(adapter);
        hackyViewPager.setCurrentItem(position);
    }

    private void initViewAndData() {
        hackyViewPager = (HackyViewPager) findViewById(R.id.hackyViewPager);

        imgUrlList = (List<String>) getIntent().getSerializableExtra(EXTRA_IMG_URL_LIST_KEY);
        position = getIntent().getIntExtra(EXTRA_POSITION_KEY,0);
    }

    @Override
    public void itemTapClick() {
        this.finish();
    }
}
