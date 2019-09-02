package com.beyond.popscience.module.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.sharesdk.ShareUtil;
import com.beyond.library.sharesdk.WebViewShare;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.pojo.SocialIntro;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.Util;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圈子详情
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialDetailActivity extends BaseActivity {

    private final static String EXTRA_SOCIAL_INFO_KEY = "socialInfo";

    private static final int TASK_REQUEST_JOIN=1511;//申请加入普通社团
    private static final int TASK_GET_INTRO = 1512;//查看简介

    @BindView(R.id.topReLay)
    protected RelativeLayout topReLay;
    @BindView(R.id.rightImgView)
    protected ImageView rightImgView;
    @BindView(R.id.picImgView)
    protected ImageView picImgView;
    @BindView(R.id.shadowView)
    protected View shadowView;
    @BindView(R.id.contentTxtView)
    protected TextView contentTxtView;
    @BindView(R.id.bottomBtn)
    protected TextView bottomBtn;
    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    private SocialInfo socialInfo;
    private SocialIntro socialIntro;//简介

    @Request
    private SocialRestUsage restUsage;

    /**
     *
     * @param context
     * @param socialInfo
     */
    public static void startActivity(Context context, SocialInfo socialInfo){
        Intent intent = new Intent(context, SocialDetailActivity.class);
        intent.putExtra(EXTRA_SOCIAL_INFO_KEY, socialInfo);
        context.startActivity(intent);
    }

    /**
     *
     * @param socialInfo
     */
    public static void startActivityForResult(Activity activity, int requestCode, SocialInfo socialInfo){
        Intent intent = new Intent(activity, SocialDetailActivity.class);
        intent.putExtra(EXTRA_SOCIAL_INFO_KEY, socialInfo);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_social_detail;
    }

    @Override
    public void initUI() {

        ToastUtil.showCenter(this,"恭喜您, + 1 科普绿币!");
        socialInfo = (SocialInfo) getIntent().getSerializableExtra(EXTRA_SOCIAL_INFO_KEY);
        if(socialInfo == null){
            backNoAnim();
            return ;
        }
        shadowView.setVisibility(View.GONE);
        topReLay.setBackgroundResource(R.drawable.shadow_bottom_grey3);
        rightImgView.setImageResource(R.drawable.icon_dots);
        rightImgView.setVisibility(View.VISIBLE);

        picImgView.getLayoutParams().height = Util.getImageHeight(DensityUtil.getScreenWidth(this));

        contentTxtView.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvTitle.setText(socialInfo.getName());

        switchBottomBtn();
        getIntro();
    }

    /**
     * 切换底部按钮
     */
    private void switchBottomBtn(){
        if(socialInfo.getState() == 0){  //0:未加入 1:申请中 2：已加入
            bottomBtn.setText("申请加入");
            bottomBtn.setEnabled(true);
            bottomBtn.setBackgroundResource(R.drawable.bg_red_round);
        }else if(socialInfo.getState() == 1){
            bottomBtn.setText("正在审核");
            bottomBtn.setEnabled(false);
            bottomBtn.setBackgroundResource(R.drawable.bg_green_round);
        }else{
            bottomBtn.setText("已加入圈子");
            bottomBtn.setEnabled(false);
            bottomBtn.setBackgroundResource(R.drawable.bg_red_round);
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);

        if (taskId == TASK_REQUEST_JOIN && msg.getIsSuccess()){
            ToastUtil.showCenter(this,"申请成功");

            socialInfo.setState(2);

            switchBottomBtn();

        } else if (taskId == TASK_GET_INTRO && msg.getIsSuccess()){
            socialIntro = (SocialIntro) msg.getObj();
            fillView();
        }

        setResult(RESULT_OK);

        dismissProgressDialog();
    }

    @OnClick(R.id.bottomBtn)
    public void requestJoin(View view){
        if (socialInfo.isNormal()){//普通社团
            showProgressDialog();
            restUsage.postAuditNormal(TASK_REQUEST_JOIN,socialInfo.getCommunityId());
        } else{
            PostAuditActivity.startActivity(this,socialInfo.getCommunityId());//跳转到提交审核
        }
    }

    /**
     * 分享
     * @param view
     */
    @OnClick(R.id.rightImgView)
    public void shareClick(View view){
        if(socialIntro!=null){
            WebViewShare webViewShare = new WebViewShare();
            webViewShare.setTitle(socialIntro.getName());
            webViewShare.setDesc(socialIntro.getIntroduce());
            webViewShare.setImgUrl(socialIntro.getDisplay());
            webViewShare.setLink("http://www.appwzd.cn/app.jsp");
            ShareUtil.directShare(this, webViewShare);
        }
    }

    /**
     * 获取社团简介
     */
    private void getIntro(){
        restUsage.getIntro(TASK_GET_INTRO,socialInfo.getCommunityId(), null);
    }

    private void fillView(){
        ImageLoaderUtil.displayImage(this,socialIntro.getDisplay(),picImgView);
        contentTxtView.setText(socialIntro.getIntroduce());
    }


}
