package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.pojo.SocialIntro;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.social.adapter.SocialCircleContentFragmentPagerAdapter;
import com.beyond.popscience.module.social.fragment.SociaMemberFragment;
import com.beyond.popscience.module.social.fragment.SocialCircleContentFragment;
import com.beyond.popscience.module.social.fragment.SocialNoticeFragment;
import com.beyond.popscience.module.social.fragment.SocialTeachFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圈子内容
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialCircleContentV2Activity extends BaseActivity {

    /**
     *
     */
    private final String KEY_COMMUNITY_DETAIL_LAST_TIMESTAMP = "communityDetail_%s_%s";
    /**
     * 查看简介
     */
    private final int REQUEST_GET_INTRO_TASK_ID = 1001;

    private final String[] TABS = new String[]{
            "说说", "通知", "教学","成员"
    };
    private final static String EXTRA_SOCIAL_INFO_KEY = "socialInfo";

    /**
     *
     */
    @BindView(R.id.topReLay)
    protected RelativeLayout topReLay;

    /**
     *
     */
    @BindView(R.id.titleTxtView)
    protected TextView titleTxtView;

    @BindView(R.id.contentTxtView)
    protected TextView contentTxtView;

    @BindView(R.id.infoTipTxtView)
    protected TextView infoTipTxtView;

    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;

    @BindView(R.id.shadowView)
    protected View shadowView;

    @BindView(R.id.viewpager)
    protected ViewPager viewpager;

    @BindView(R.id.headerPicImgView)
    protected ImageView headerPicImgView;

    @BindView(R.id.publishedLinLay)
    protected LinearLayout publishedLinLay;

    @BindView(R.id.publishedTxtView)
    protected TextView publishedTxtView;

    @BindView(R.id.toolBar)
    protected Toolbar toolBar;
    /**
     *
     */
    @BindView(R.id.statusBarTmpView)
    protected View statusBarTmpView;

    private SocialInfo socialInfo;
    private SocialIntro socialIntro;//简介
    @Request
    private SocialRestUsage restUsage;
    private SocialCircleContentFragmentPagerAdapter socialCircleContentFragmentPagerAdapter;
    private SocialCircleContentFragment socialCircleContentFragment;
    public static AppBarLayout appBarLayout;

    /**
     * @param context
     * @param socialInfo
     */
    public static void startActivity(Context context, SocialInfo socialInfo) {
        Intent intent = new Intent(context, SocialCircleContentV2Activity.class);
        intent.putExtra(EXTRA_SOCIAL_INFO_KEY, socialInfo);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_social_circle_content_v2;
    }

    @Override
    public void initUI() {
        appBarLayout=(AppBarLayout)findViewById(R.id.appBarLayout);

        socialInfo = (SocialInfo) getIntent().getSerializableExtra(EXTRA_SOCIAL_INFO_KEY);
        if (socialInfo == null) {
            backNoAnim();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | localLayoutParams.flags);
            statusBarTmpView.getLayoutParams().height = DensityUtil.getStatusBarHeight(this);
        }
        initView();
        SocialIntro socialIntro = new SocialIntro();
        socialIntro.setName(socialInfo.getName());
        socialIntro.setDisplay(socialInfo.getLogo());

        initDataView(socialIntro);

        requestGetIntro();
    }


    /**
     * 获取社团简介
     */
    private void requestGetIntro() {
        showProgressDialog();
        String timeStamp = (String) SPUtils.get(this, getCommunityDetailLastTimeStampKey(), "");

        restUsage.getIntro(REQUEST_GET_INTRO_TASK_ID, socialInfo.getCommunityId(), timeStamp);
    }

    /**
     * @return
     */
    private String getCommunityDetailLastTimeStampKey() {
        String userId = UserInfoUtil.getInstance().getUserInfo().getUserId();
        return String.format(KEY_COMMUNITY_DETAIL_LAST_TIMESTAMP, userId == null ? "" : userId, socialInfo.getCommunityId() == null ? "" : socialInfo.getCommunityId());
    }

    /**
     * 初始化view
     */
    private void initView() {
        int imgHeight = Util.getImageHeight(DensityUtil.getScreenWidth(this));
        headerPicImgView.getLayoutParams().height = imgHeight;

//        tabLayout
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ActivityCompat.getDrawable(this, R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(20);

        List<Fragment> fragmentList = new ArrayList<>();
        socialCircleContentFragment = SocialCircleContentFragment.newInstance(socialInfo);
        fragmentList.add(socialCircleContentFragment);
        fragmentList.add(SocialNoticeFragment.newInstance(socialInfo.getCommunityId()));
        fragmentList.add(SocialTeachFragment.newInstance(socialInfo.getCommunityId()));
        fragmentList.add(SociaMemberFragment.getInstance(socialInfo.getCommunityId()));

        socialCircleContentFragmentPagerAdapter = new SocialCircleContentFragmentPagerAdapter(getSupportFragmentManager(), Arrays.asList(TABS), fragmentList, viewpager);
        viewpager.setAdapter(socialCircleContentFragmentPagerAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switchPublishView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout.setupWithViewPager(viewpager);
    }

    /**
     * 初始化
     */
    private void initDataView(SocialIntro socialIntro) {
        if (socialIntro != null) {
            ImageLoaderUtil.displayImage(this, socialIntro.getDisplay(), headerPicImgView, getDisplayImageOptions());
            titleTxtView.setText(socialIntro.getName());
            contentTxtView.setText(socialIntro.getIntroduce());


            if (socialIntro.getRemindNum() > 0) {
                infoTipTxtView.setText(socialIntro.getRemindNum() + "条信息");
                infoTipTxtView.setVisibility(View.VISIBLE);
            } else {
                infoTipTxtView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 切换底部发布view
     */
    private void switchPublishView() {
        int pos = viewpager.getCurrentItem();

        publishedLinLay.setVisibility(View.GONE);
        publishedLinLay.setOnClickListener(null);

        if (pos == 0) {   //圈子
            publishedTxtView.setText("发布圈子");
            publishedLinLay.setVisibility(View.VISIBLE);
            publishedLinLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    socialCircleContentFragment.startPublishedActivity();
                }
            });
        } else if (pos == 1) { //公告
            if (socialIntro != null) {
                if (socialIntro.isAdmin()) {  //管理员
                    publishedTxtView.setText("发布公告");
                    publishedLinLay.setVisibility(View.VISIBLE);
                    publishedLinLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublishNoticeActivity.startActivity(SocialCircleContentV2Activity.this, socialInfo.getCommunityId());
                        }
                    });
                }
            }
        } else {  //教学
            if (socialIntro != null) {
                if (socialIntro.isAdmin()) {  //管理员
                    publishedTxtView.setText("发布教学");
                    publishedLinLay.setVisibility(View.VISIBLE);
                    publishedLinLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublishTeachActivity.startActivity(SocialCircleContentV2Activity.this, socialInfo.getCommunityId());
                        }
                    });
                }
            }
        }
    }

    /**
     * 是否已经滚动到顶部
     *
     * @return
     */
    public boolean isMDScrollTop() {
        return viewpager.getTop() <= (toolBar.getMeasuredHeight() + tabLayout.getMeasuredHeight() + shadowView.getMeasuredHeight());
    }

    /**
     * 是否已经滚动到最底部
     *
     * @return
     */
    public boolean isMDScrollBottom() {
        return appBarLayout.getTop() >= 0;
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_GET_INTRO_TASK_ID: //查看简介
                if (msg.getIsSuccess()) {
                    socialIntro = (SocialIntro) msg.getObj();
                    initDataView(socialIntro);
                    switchPublishView();
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 消息通知
     *
     * @param view
     */
    @OnClick(R.id.infoTipTxtView)
    public void messageTipClick(View view) {
        SocialMessageNoticeActivity.startActivity(this, socialInfo);
    }

}
