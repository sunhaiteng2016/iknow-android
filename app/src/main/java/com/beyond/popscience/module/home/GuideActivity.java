package com.beyond.popscience.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.beyond.popscience.LoginActivity;
import com.beyond.popscience.PopularMainActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.base.CustomFragmentPagerAdapter;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.fragment.GuideFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * Created by yao.cui on 2017/6/8.
 */

public class GuideActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    private CirclePageIndicator mCirclePageIndicator;
    private ViewPager mVpGuide;

    private CustomFragmentPagerAdapter mPagerAdapter;
    private int mStartX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragments();
    }


    @Override
    public void initUI() {
        super.initUI();
        mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);
        mVpGuide = (ViewPager) findViewById(R.id.vpGuide);
    }

    private void initFragments() {
        GuideFragment guide1Fragment = new GuideFragment();
        guide1Fragment.setImgResId(R.drawable.guide_1);
        GuideFragment guide2Fragment = new GuideFragment();
        guide2Fragment.setImgResId(R.drawable.guide_2);
        GuideFragment guide3Fragment = new GuideFragment();
        guide3Fragment.setImgResId(R.drawable.guide_3);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(guide1Fragment);
        fragments.add(guide2Fragment);
        fragments.add(guide3Fragment);

        mPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments, mVpGuide);
        mVpGuide.setAdapter(mPagerAdapter);
        mCirclePageIndicator.setViewPager(mVpGuide);

        mVpGuide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mVpGuide.getCurrentItem() != mPagerAdapter.getCount() - 1) {//如果是最后一页
                    return false;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        int finishX = (int) event.getRawX();
                        if ((mStartX - finishX) > 100) {
                            jump();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void jump() {
        //已经登录进入到主页 否则 进入登录
        if (UserInfoUtil.getInstance().isHasLogin()) {
            String address = (String) SPUtils.get(GuideActivity.this, "detailedArea", "");
            String[] mAddress = address.split("-");
            if (mAddress.length > 0) {
                WelcomeActivity.seletedAdress = mAddress[1] + "-" + mAddress[2];
                WelcomeActivity.seletedAdress_two = mAddress[0] + "-" + mAddress[1] + "-" + mAddress[2];
            }
            startActivity(PopularMainActivity.class);
        } else {
            LoginActivity.startActivity(this);
        }
        finish();
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_guide;
    }
}
