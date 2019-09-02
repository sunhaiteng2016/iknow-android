/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.beyond.library.sharesdk.theme;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.R;
import com.beyond.library.sharesdk.PlatformListFakeActivity;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;

import static com.mob.tools.utils.R.getBitmapRes;
import static com.mob.tools.utils.R.getStringRes;

public class PlatformListPage extends PlatformListFakeActivity implements View.OnClickListener {
    // page container
    private FrameLayout flPage;
    // gridview of platform list
    private PlatformGridView grid;
    //ShareContinueReLay
    private View shareContinueView;
    //ShareTipTxtView
    private TextView shareTipTxtView;
    // cancel button
    private Button btnCancel;
    // sliding up animation
    private Animation animShow;

    @Override
    protected void onShareButtonClick(View v, List<Platform> checkedPlatforms) {
        super.onShareButtonClick(v, checkedPlatforms);
    }

    // sliding down animation
    private Animation animHide;
    private boolean finishing;
    private LinearLayout llPage;

    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | localLayoutParams.flags);
        }

        finishing = false;
        initPageView();
        initAnim();
        setSilent(true);
        activity.setContentView(flPage);

        // set the data for platform gridview
        grid.setData(mixFields, silent);
        grid.setHiddenPlatforms(hiddenPlatforms);
        grid.setCustomerLogos(customerLogos);
        grid.setParent(this);
        btnCancel.setOnClickListener(this);

        // display gridviews
        llPage.clearAnimation();
        llPage.startAnimation(animShow);
    }

    private void initPageView() {
        int bgResId = getBitmapRes(getContext(), "classic_platform_corners_bg");
        int dp15 = com.mob.tools.utils.R.dipToPx(getContext(), 15);

        flPage = new FrameLayout(getContext());
        flPage.setOnClickListener(this);
        flPage.setBackgroundDrawable(new ColorDrawable(0x55000000));

        // container of the platform gridview
        llPage = new LinearLayout(getContext()) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return true;
            }
        };
        llPage.setOrientation(LinearLayout.VERTICAL);
//        llPage.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        FrameLayout.LayoutParams lpLl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lpLl.gravity = Gravity.BOTTOM;
        llPage.setLayoutParams(lpLl);
        flPage.addView(llPage);

        // gridview
        grid = new PlatformGridView(getContext());
        grid.setEditPageBackground(getBackgroundView());
        grid.setBackgroundResource(bgResId);

        LinearLayout.LayoutParams lpWg = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpWg.setMargins(dp15, 0, dp15, 0);
        grid.setLayoutParams(lpWg);
        llPage.addView(grid);

        //Special for SharedProfit
        if (shareProfitMode){
            shareContinueView = LayoutInflater.from(getContext()).inflate(R.layout.layout_share_continue, null);
            shareTipTxtView = (TextView) shareContinueView.findViewById(R.id.shareTipTxtView);
            shareTipTxtView.setText(shareTip);
            RelativeLayout.LayoutParams lpsp = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            shareContinueView.setLayoutParams(lpsp);
            llPage.addView(shareContinueView);
        }

        // cancel button
        btnCancel = new Button(getContext());
        btnCancel.setTextColor(Color.parseColor("#5c5c5c"));
        btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        btnCancel.setGravity(Gravity.CENTER);
        int resId = getStringRes(getContext(), "share_cancel");
        if (resId > 0) {
            btnCancel.setText(resId);
        }
        btnCancel.setPadding(0, 0, 0, com.mob.tools.utils.R.dipToPx(getContext(), 5));

        if (bgResId > 0) {
            btnCancel.setBackgroundResource(bgResId);
        } else {
            btnCancel.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        }

        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, com.mob.tools.utils.R.dipToPx(getContext(), 50));
        lpBtn.setMargins(dp15, dp15, dp15, dp15);
        btnCancel.setLayoutParams(lpBtn);
        llPage.addView(btnCancel);
    }

    private void initAnim() {
        animShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        animShow.setDuration(300);

        animHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1);
        animHide.setDuration(300);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (grid != null) {
            grid.onConfigurationChanged();
        }
    }

    public boolean onFinish() {
        if (finishing) {
            return super.onFinish();
        }

        if (animHide == null) {
            finishing = true;
            return false;
        }

        finishing = true;
        animHide.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                flPage.setVisibility(View.GONE);
                finish();
            }
        });
        llPage.clearAnimation();
        llPage.startAnimation(animHide);
        //中断finish操作
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(flPage) || v.equals(btnCancel)) {
            setCanceled(true);
            finish();
        }
    }

    public void onPlatformIconClick(View v, ArrayList<Platform> platforms) {
        onShareButtonClick(v, platforms);
    }
}
