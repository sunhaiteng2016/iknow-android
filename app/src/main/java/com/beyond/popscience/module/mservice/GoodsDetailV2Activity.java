package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.PhoneUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.CommentRestUsage;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.pojo.CommentResponse;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.Util;
import com.beyond.popscience.module.mservice.adapter.GoodsDetailSlideAdapter;
import com.beyond.popscience.module.mservice.fragment.GoodsCommentFragment;
import com.beyond.popscience.module.mservice.fragment.GoodsDetailFragment;
import com.beyond.popscience.module.square.SkillPublishActivity;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 商品详情
 *
 * Created by yao.cui on 2017/6/24.
 */

public class GoodsDetailV2Activity extends BaseActivity {

    private final String[] TABS = new String[]{
            "详情描述", "查看评论"
    };

    private static final int TASK_GET_GOODS_DETAIL = 1402;//获取轮播图
    /**
     * 请求评论列表
     */
    private final int REQUEST_COMMENT_LIST_TASK_ID = 1403;

    private static final String KEY_PRODUCT = "product";

    @BindView(R.id.headerImgView)
    protected CircleImageView headerImgView;
    @BindView(R.id.nickNameTxtView)
    protected TextView nickNameTxtView;
    @BindView(R.id.addressNameTxtView)
    protected TextView addressNameTxtView;
    @BindView(R.id.priceTxtView)
    protected TextView priceTxtView;
    @BindView(R.id.timeTxtView)
    protected TextView timeTxtView;

    @BindView(R.id.llShotMsg)
    protected LinearLayout llShotMsg;
    @BindView(R.id.llCall)
    protected LinearLayout llCall;
    @BindView(R.id.tv_title)
    protected TextView tv_title;//activity 标题
    @BindView(R.id.tv_right)
    protected TextView tv_right;

    @BindView(R.id.emptyReLay)
    protected RelativeLayout emptyReLay;

    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;

    private GoodsDetail goodsDetail;
    private ServiceGoodsItem serviceGoodsItem;

    @Request
    private ServiceRestUsage mRestUsage;

    @Request
    private SquareRestUsage squareRestUsage;
    /**
     *
     */
    @Request
    private CommentRestUsage commentRestUsage;
    /**
     *
     */
    private GoodsDetailSlideAdapter slideAdapter;
    /**
     * 是否点击了编辑
     */
    private boolean isEdit;

    public static void startActivity(Context context, ServiceGoodsItem serviceGoodsItem){
        Intent intent = new Intent(context,GoodsDetailV2Activity.class);
        intent.putExtra(KEY_PRODUCT, serviceGoodsItem);
        context.startActivity(intent);
    }

    public static void startActivityGoods(Context context, ServiceGoodsItem serviceGoodsItem){
        if(serviceGoodsItem != null){
            serviceGoodsItem.setAppGoodsType("5");
        }
        Intent intent = new Intent(context,GoodsDetailV2Activity.class);
        intent.putExtra(KEY_PRODUCT, serviceGoodsItem);
        context.startActivity(intent);
    }

    /**
     * 任务详情
     * @param context
     */
    public static void startActivityTask(Context context, String uId){
        ServiceGoodsItem serviceGoodsItem = new ServiceGoodsItem();
        serviceGoodsItem.setAppGoodsType("2");
        serviceGoodsItem.setProductId(uId);

        Intent intent = new Intent(context,GoodsDetailV2Activity.class);
        intent.putExtra(KEY_PRODUCT, serviceGoodsItem);
        context.startActivity(intent);
    }

    /**
     * 技能详情
     * @param context
     */
    public static void startActivitySkill(Context context, String uId){
        ServiceGoodsItem serviceGoodsItem = new ServiceGoodsItem();
        serviceGoodsItem.setAppGoodsType("1");
        serviceGoodsItem.setProductId(uId);

        Intent intent = new Intent(context,GoodsDetailV2Activity.class);
        intent.putExtra(KEY_PRODUCT, serviceGoodsItem);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_detail_v2;
    }

    @Override
    public void initUI() {
        super.initUI();
        serviceGoodsItem = (ServiceGoodsItem) getIntent().getSerializableExtra(KEY_PRODUCT);
        if(serviceGoodsItem == null){
            backNoAnim();
            return ;
        }

        initTitle();
        initTabBar();

        showProgressDialog();
        getGoodsDetail();
    }

    private void initTitle(){
        if("1".equals(serviceGoodsItem.getAppGoodsType())){
            tv_title.setText("技能详情");
        } else if("2".equals(serviceGoodsItem.getAppGoodsType())){
            tv_title.setText("任务详情");
        } else {
            tv_title.setText("商品详情");
        }
    }

    /**
     * 初始化TabLayout
     */
    private void initTabBar(){
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ActivityCompat.getDrawable(this, R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(20);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFragment();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for(int i=0;i<TABS.length;i++){
            tabLayout.addTab(tabLayout.newTab().setText(TABS[i]), i == 0);
        }
    }

    /**
     * 切换fragment
     */
    private void switchFragment(){
        Fragment currFragment = null;
        switch (tabLayout.getSelectedTabPosition()){
            case 0:
                currFragment = GoodsDetailFragment.newInstance(goodsDetail);
                break;
            case 1:
                currFragment = GoodsCommentFragment.newInstance(serviceGoodsItem);
                break;
        }
        if(currFragment!=null){
            replaceFragment(R.id.frameLay, currFragment, false, false);
        }
        switchBottomViewStatus();
    }

    /**
     *
     * @param count
     */
    public void setCommentCountShow(long count){
        tabLayout.getTabAt(1).setText("查看评论"+(count>0?("("+count+")"):""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isEdit){
            isEdit = false;
            getGoodsDetail();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_GET_GOODS_DETAIL:
                if (msg.getIsSuccess()){
                    goodsDetail = (GoodsDetail) msg.getObj();
                    if (goodsDetail!= null){
                        emptyReLay.setVisibility(View.GONE);

                        initDetail();
                    }else{
                        emptyReLay.setVisibility(View.VISIBLE);
                    }
                }else{
                    emptyReLay.setVisibility(View.VISIBLE);
                }
                dismissProgressDialog();
                switchFragment();
                break;
            case REQUEST_COMMENT_LIST_TASK_ID: //评论列表
                CommentResponse commentResponse = null;
                if(msg.getIsSuccess()){
                    commentResponse = (CommentResponse) msg.getObj();
                    if(commentResponse!=null){
                        setCommentCountShow(commentResponse.getTotalcount());
                    }
                }
                break;
        }
    }

    /**
     * 填充界面数据
     */
    private void initDetail(){
        goodsDetail.appGoodsType = serviceGoodsItem.getAppGoodsType();

        setCommentCountShow(goodsDetail.commentCount);

        String nickNameHtml = goodsDetail.realName;
        if("1".equals(goodsDetail.authorityType)){  //个人
            nickNameHtml +="<font color=#4f98d5>(个人)</font>";
        }else if("2".equals(goodsDetail.authorityType)){    //企业
            nickNameHtml +="<font color=#4f98d5>(企业)</font>";
        }

        nickNameTxtView.setText(Html.fromHtml(nickNameHtml == null? "" : nickNameHtml));
        addressNameTxtView.setText(goodsDetail.address);
        priceTxtView.setText("¥"+goodsDetail.price);
        timeTxtView.setText(Util.getDisplayDateTimeV2(BeyondApplication.getInstance().getCurrSystemTime(), goodsDetail.createTime));

        tv_right.setText("编辑");

        //如果是自己的商品则展示编辑按钮
        if (TextUtils.equals(goodsDetail.userId, UserInfoUtil.getInstance().getUserInfo().getUserId())){
            tv_right.setVisibility(View.VISIBLE);
        }
        ImageLoaderUtil.displayImage(this, goodsDetail.avatar, headerImgView, getAvatarDisplayImageOptions());

        switchBottomViewStatus();
    }

    /**
     * 切换右下角显示状态
     */
    private void switchBottomViewStatus(){
        if(tabLayout.getSelectedTabPosition() == 0){
            llCall.setVisibility(View.VISIBLE);
            llShotMsg.setVisibility(View.VISIBLE);
        }else{
            llCall.setVisibility(View.GONE);
            llShotMsg.setVisibility(View.GONE);
        }
        if (goodsDetail==null || TextUtils.isEmpty(goodsDetail.mobile)){
            llCall.setVisibility(View.GONE);
            llShotMsg.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.llShotMsg)
    void toShortMsg(View view){
        //打开短信
        if (goodsDetail!= null){
            PhoneUtil.sendSms(this,goodsDetail.mobile,"");
        }
    }

    @OnClick(R.id.llCall)
    void toPhoneCall(View view){
        //打电话
        if (goodsDetail!= null){
            PhoneUtil.callPhoneDial(this,goodsDetail.mobile);
        }
    }

    @OnClick(R.id.tv_right)
    void toEditGoods(View view){
        isEdit = true;
        if("1".equals(serviceGoodsItem.getAppGoodsType())){ //技能
            Bundle bundle = new Bundle();
            bundle.putInt("type",1);
            bundle.putSerializable("goodsDetail",goodsDetail);
            CheckAuthTool.startActivity(this, SkillPublishActivity.class, bundle);
        }else if("2".equals(serviceGoodsItem.getAppGoodsType())){   //任务
            Bundle bundle = new Bundle();
            bundle.putInt("type",2);
            bundle.putSerializable("goodsDetail",goodsDetail);
            CheckAuthTool.startActivity(this, SkillPublishActivity.class, bundle);
        }else{  //商品
            PublishGoodsActivity.startActivityEdit(this,goodsDetail);
        }
    }

    @OnClick(R.id.emptyReLay)
    public void reloadClick(View view){
        showProgressDialog();
        getGoodsDetail();
    }

    /**
     * 请求评论列表
     */
    private void requestComment(){
        commentRestUsage.getComment(REQUEST_COMMENT_LIST_TASK_ID, 1, serviceGoodsItem.getAppGoodsType(), serviceGoodsItem.getProductId());
    }

    /**
     * 获取商品详情
     */
    private void getGoodsDetail(){
        if("1".equals(serviceGoodsItem.getAppGoodsType())){ //技能
            squareRestUsage.getSkillDetail(TASK_GET_GOODS_DETAIL, serviceGoodsItem.getProductId());
        }else if("2".equals(serviceGoodsItem.getAppGoodsType())){   //任务
            squareRestUsage.getTaskDetail(TASK_GET_GOODS_DETAIL, serviceGoodsItem.getProductId());
        }else{  //商品
            mRestUsage.getGoodsDetail(TASK_GET_GOODS_DETAIL, serviceGoodsItem.getProductId());
        }
    }

}
