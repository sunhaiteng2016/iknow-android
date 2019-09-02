package com.beyond.popscience.module.square;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DateUtil;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.pojo.AuthResult;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.view.DragLinearView;
import com.beyond.popscience.frame.view.SelectDateDialog;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.mservice.CheckAuthTool;
import com.beyond.popscience.module.mservice.CompanyCertificationActivity;
import com.beyond.popscience.module.mservice.PersonalCertificationActivity;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class SkillPublishActivity extends BaseActivity {

    private final int REQ_PUBLISH_ID = 1701;

    private final int EXTRA_CLASSIFY = 1001;
    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 1702;

    private final String RMB_SYMBOL = "¥";
    /**
     * 最多选择10张
     */
    private final int MAX_PHOTO_NUMBER = 10;

    /**
     * 封面图最多一张
     */
    private final int MAX_COVER_PHOTO_NUMBER = 1;

    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.addressssq)
    protected TextView addressssq;
    @BindView(R.id.personalAuthView)
    protected TextView personalAuthView;
    @BindView(R.id.businessAuthView)
    protected TextView businessAuthView;
    @BindView(R.id.titleEditTxt)
    protected EditText titleEditTxt;
    @BindView(R.id.descriptEditTxt)
    protected EditText descriptEditTxt;
    @BindView(R.id.addressEditTxt)
    protected EditText addressEditTxt;
    @BindView(R.id.userNameEditTxt)
    protected EditText userNameEditTxt;
    @BindView(R.id.phoneEditTxt)
    protected EditText phoneEditTxt;
    @BindView(R.id.priceEditTxt)
    protected EditText priceEditTxt;
    @BindView(R.id.classifyTxtView)
    protected TextView classifyTxtView;
    @BindView(R.id.timeTxtView)
    protected TextView timeTxtView;
    @BindView(R.id.timeLineView)
    protected View timeLineView;
    @BindView(R.id.timeLayout)
    protected RelativeLayout timeLayout;
    @BindView(R.id.coverPicDragLinearView)
    protected DragLinearView coverPicDragLinearView;
    @BindView(R.id.dragLinearView)
    protected DragLinearView dragLinearView;
    @BindView(R.id.mianyi_iv)
    ImageView mianyiIv;
    @BindView(R.id.rl_ss)
    LinearLayout rlSs;
    @BindView(R.id.ed_yq_code)
    EditText edYqCode;
    @BindView(R.id.publishView)
    TextView publishView;

    @Request
    private SquareRestUsage mRestUsage;

    private int type;   //发布类型
    private AuthResult authResult;  //认证结果
    private GoodsDetail request = new GoodsDetail();
    private GoodsDetail goodsDetail;

    private SelectDateDialog selectDateDialog;
    /**
     * 存放已上传的封面图片
     */
    private List<String> goodsCoverImgUrlList = new ArrayList<>();

    /**
     * 存放已上传的详情图片
     */
    private List<String> goodsDescImgUrlList = new ArrayList<>();

    /**
     * 当前点击的 DragLinearView
     */
    private DragLinearView currDragLinerView = null;
    /**
     * 是否封面图上传成功
     */
    private boolean isUploadCoverSuccess = false;
    /**
     * 是否详情图上传成功
     */
    private boolean isUploadDetailSuccess = false;

    private long lastClickTime = 0;   //上次点击的时间戳
    private String mAddress;

    public static void startActivity(Context context, int type, AuthResult authResult) {
        Intent intent = new Intent(context, SkillPublishActivity.class);
        intent.putExtra("type", type);
        intent.putExtra(CheckAuthTool.AUTH_RESULT, authResult);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_skill_publish_layout;
    }

    @Override
    public void initUI() {
        super.initUI();

        type = getIntent().getIntExtra("type", SquareActivity.SKILL_TYPE);
        authResult = (AuthResult) getIntent().getSerializableExtra(CheckAuthTool.AUTH_RESULT);
        if (authResult == null) {
            authResult = new AuthResult();
        }
        goodsDetail = (GoodsDetail) getIntent().getSerializableExtra("goodsDetail");
        mAddress = WelcomeActivity.seletedAdress;
        initView();
        initEditTxt();
        iniEditStatusView();
        initCoverPicDragView();
        initDragView();
        addressssq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择城市
                AddressPickTask task = new AddressPickTask(SkillPublishActivity.this);
                task.setHideProvince(false);
                task.setHideCounty(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        //showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                            //showToast(province.getAreaName() + city.getAreaName());
                        } else {
                            //showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
                            mAddress = city.getAreaName() + "-" + county.getAreaName();
                            addressssq.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                        }
                    }
                });
                task.execute(BeyondApplication.getInstance().getLocation().provider, BeyondApplication.getInstance().getLocation().city, BeyondApplication.getInstance().getLocation().district);
            }
        });
    }

    private void initView() {
        if (type == SquareActivity.SKILL_TYPE) {
            tv_title.setText("发布技能");
            titleEditTxt.setHint("在此输入技能标题");
            descriptEditTxt.setHint("详细描述您的技能");
            timeLayout.setVisibility(View.GONE);
            timeLineView.setVisibility(View.GONE);
        } else {
            tv_title.setText("发布任务");
            titleEditTxt.setHint("在此输入任务标题");
            descriptEditTxt.setHint("详细描述您的任务");
            timeLayout.setVisibility(View.VISIBLE);
            timeLineView.setVisibility(View.VISIBLE);
            String nowTime = DateUtil.getNowString(DateUtil.DATE);
            timeTxtView.setText(nowTime);
        }
    }

    private void iniEditStatusView() {
        if (goodsDetail != null) {
            switchAuthType(goodsDetail.getAuthTypeInt());
            titleEditTxt.setText(goodsDetail.title);
            descriptEditTxt.setText(goodsDetail.introduce);
            addressEditTxt.setText(goodsDetail.address);
            userNameEditTxt.setText(goodsDetail.realName);
            phoneEditTxt.setText(goodsDetail.mobile);
            priceEditTxt.setText(goodsDetail.price);
            classifyTxtView.setText(goodsDetail.classifyName);
            if (timeLayout.getVisibility() == View.VISIBLE) {
                timeTxtView.setText(goodsDetail.startTime);
            }
        } else {
            if ("1".equals(authResult.getPeople())) {
                switchAuthType(1);
            } else if ("1".equals(authResult.getCompany())) {
                switchAuthType(2);
            }
            if (BeyondApplication.getInstance().getLocation() != null) {
                addressssq.setText(BeyondApplication.getInstance().getLocation().province + BeyondApplication.getInstance().getLocation().city + BeyondApplication.getInstance().getLocation().district);
                addressEditTxt.setText(BeyondApplication.getInstance().getLocation().street);
                mAddress = WelcomeActivity.seletedAdress;
            }
        }
    }

    private void switchAuthType(int authType) {
        if (authType == 1) {   //已经个人认证
            businessAuthView.setTextColor(getResources().getColor(R.color.blue2));
            businessAuthView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            personalAuthView.setTextColor(getResources().getColor(R.color.white));
            personalAuthView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            request.authorityType = "1";
        } else if (authType == 2) {  //已经企业认证
            personalAuthView.setTextColor(getResources().getColor(R.color.blue2));
            personalAuthView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            businessAuthView.setTextColor(getResources().getColor(R.color.white));
            businessAuthView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            request.authorityType = "2";
        }
    }

    /**
     * 初始化相关 EditText
     */
    private void initEditTxt() {
        priceEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (!priceEditTxt.getText().toString().startsWith(RMB_SYMBOL)) {
                    if (priceEditTxt.getText().toString().contains(RMB_SYMBOL)) {
                        int selectionIndex = priceEditTxt.getSelectionStart();
                        priceEditTxt.setText(RMB_SYMBOL + priceEditTxt.getText().toString().replaceAll(RMB_SYMBOL, ""));
                        if (selectionIndex == 0) {
                            priceEditTxt.setSelection(priceEditTxt.getText().length());
                        }
                    } else {
                        priceEditTxt.setText(RMB_SYMBOL + priceEditTxt.getText().toString());
                        priceEditTxt.setSelection(priceEditTxt.getText().length());
                    }
                }*/
            }
        });
    }

    /**
     * 初始化 封面 DragLinearView
     */
    private void initCoverPicDragView() {
        coverPicDragLinearView.setAddImageRes(R.drawable.wdspk_icon_2_3);
        coverPicDragLinearView.setMaxRows(1);
        coverPicDragLinearView.setMaxRowsItemCount(4);
        coverPicDragLinearView.setDisableDrag(true);
        coverPicDragLinearView.setOnAddClickListener(new DragLinearView.OnAddClickListener() {
            @Override
            public void onAddClick() {
                currDragLinerView = coverPicDragLinearView;
                int maxNum = MAX_COVER_PHOTO_NUMBER - coverPicDragLinearView.getItemCount();
                startSelectImageActivity(maxNum);
            }
        });
        coverPicDragLinearView.setOnItemChangeListener(new DragLinearView.OnItemChangeListener() {
            @Override
            public void onChange(int itemCount) {
                if (itemCount == MAX_COVER_PHOTO_NUMBER) {
                    coverPicDragLinearView.setShowAddImg(false);
                } else {
                    coverPicDragLinearView.setShowAddImg(true);
                }
            }
        });
        coverPicDragLinearView.setOnItemViewListener(new DragLinearView.OnItemViewListener() {
            @Override
            public void onAddItem(ImageView imageView, Object tag) {
                if (tag != null) {
                    String url;
                    DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    if (tag.toString().toLowerCase().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || tag.toString().toLowerCase().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)) {
                        builder.cacheOnDisk(true);
                        url = tag.toString();
                    } else {
                        url = VKConstants.FILE_PROTOCOL_PREFIX + (tag.toString().startsWith("/") ? (tag.toString().replaceFirst("/", "")) : tag.toString());
                    }
                    DisplayImageOptions options = builder.displayer(new FadeInBitmapDisplayer(200, true, true, false)).showImageOnLoading(R.drawable.common_transparent).showImageForEmptyUri(R.drawable.common_transparent).build();
                    ImageLoaderUtil.displayImage(SkillPublishActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if (goodsDetail != null) {
            if (!TextUtils.isEmpty(goodsDetail.coverPic)) {
                coverPicDragLinearView.addMutilItemView(new LinkedList<DragLinearView.ImageTagElement>(Arrays.asList(new DragLinearView.ImageTagElement(null, goodsDetail.coverPic))), false);
            }
        }
    }

    /**
     * 初始化 DragLinearView
     */
    private void initDragView() {
        dragLinearView.setMaxRows(3);
        dragLinearView.setMaxRowsItemCount(4);
        dragLinearView.setDisableDrag(true);
        dragLinearView.setOnAddClickListener(new DragLinearView.OnAddClickListener() {
            @Override
            public void onAddClick() {
                currDragLinerView = dragLinearView;
                int maxNum = MAX_PHOTO_NUMBER - dragLinearView.getItemCount();
                startSelectImageActivity(maxNum);
            }
        });
        dragLinearView.setOnItemChangeListener(new DragLinearView.OnItemChangeListener() {
            @Override
            public void onChange(int itemCount) {
                if (itemCount == MAX_PHOTO_NUMBER) {
                    dragLinearView.setShowAddImg(false);
                } else {
                    dragLinearView.setShowAddImg(true);
                }
            }
        });
        dragLinearView.setOnItemViewListener(new DragLinearView.OnItemViewListener() {
            @Override
            public void onAddItem(ImageView imageView, Object tag) {
                if (tag != null) {
                    String url;
                    DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    if (tag.toString().toLowerCase().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || tag.toString().toLowerCase().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)) {
                        builder.cacheOnDisk(true);
                        url = tag.toString();
                    } else {
                        url = VKConstants.FILE_PROTOCOL_PREFIX + (tag.toString().startsWith("/") ? (tag.toString().replaceFirst("/", "")) : tag.toString());
                    }
                    DisplayImageOptions options = builder.displayer(new FadeInBitmapDisplayer(200, true, true, false)).showImageOnLoading(R.drawable.common_transparent).showImageForEmptyUri(R.drawable.common_transparent).build();
                    ImageLoaderUtil.displayImage(SkillPublishActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if (goodsDetail != null) {
            List<String> goodsDescImgList = goodsDetail.getDetailPicList();
            if (goodsDescImgList != null && goodsDescImgList.size() > 0) {
                LinkedList<DragLinearView.ImageTagElement> imageTagElementList = new LinkedList<DragLinearView.ImageTagElement>();
                for (String imagePath : goodsDescImgList) {
                    imageTagElementList.add(new DragLinearView.ImageTagElement(null, imagePath));
                }
                dragLinearView.addMutilItemView(imageTagElementList, false);
            }
        }
    }

    /**
     * 选择图片
     */
    private void startSelectImageActivity(int maxNum) {
        if (maxNum <= 0) {
            return;
        }
        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 添加产品图片
     */
    private void addProductImg(List<String> imgPathList) {
        if (imgPathList == null) {
            return;
        }
        LinkedList<DragLinearView.ImageTagElement> imageTagElementList = new LinkedList<DragLinearView.ImageTagElement>();
        for (String imagePath : imgPathList) {
            imageTagElementList.add(new DragLinearView.ImageTagElement(null, imagePath));
        }

        if (currDragLinerView != null) {
            currDragLinerView.addMutilItemView(imageTagElementList);
        }
    }

    /**
     * 根据DragLinearView获取要上传的图片路径
     *
     * @param dragLinearView
     * @return
     */
    private List<String> getUploadProductImgList(DragLinearView dragLinearView) {
        List<View> viewList = dragLinearView.getItemViewList();
        List<String> pathList = new ArrayList<String>();
        for (View view : viewList) {
            if (view.getTag() != null && !TextUtils.isEmpty(view.getTag().toString()) && !view.getTag().toString().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) && !view.getTag().toString().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)) {
                pathList.add(view.getTag().toString());
            }
        }
        return pathList;
    }

    /**
     * 根据DragLinearView获取要Http的图片
     *
     * @param dragLinearView
     * @return
     */
    private List<String> getHttpProductImgList(DragLinearView dragLinearView) {
        List<View> viewList = dragLinearView.getItemViewList();
        List<String> pathList = new ArrayList<String>();
        for (View view : viewList) {
            if (view.getTag() != null && !TextUtils.isEmpty(view.getTag().toString()) && (view.getTag().toString().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || view.getTag().toString().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX))) {
                pathList.add(view.getTag().toString());
            }
        }
        return pathList;
    }

    /**
     * 上传图片
     */
    private void requestUploadImg(DragLinearView dragLinearView, List<String> pathList) {
        ThirdSDKManager.getInstance().uploadImage(pathList, new ThirdSDKManager.UploadCallback(dragLinearView) {

            @Override
            public void onSuccess(final Map<String, String> successMap, List<String> failureList, final Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                L.v("上传成功=========> " + targetObj);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (targetObj == coverPicDragLinearView) {    //封面图
                            isUploadCoverSuccess = true;
                            goodsCoverImgUrlList.addAll(new ArrayList<String>(successMap.values()));
                        } else {  //详情图
                            isUploadDetailSuccess = true;
                            goodsDescImgUrlList.addAll(new ArrayList<String>(successMap.values()));
                        }
                        requestPublishProduct();
                    }
                });
            }

            @Override
            public void onFailure(List<String> failureList, String errCode, String errMsg, final Object targetObj) {
                super.onFailure(failureList, errCode, errMsg, targetObj);
                L.v("上传失败=========>" + targetObj);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (targetObj == coverPicDragLinearView) {    //封面图
                            isUploadCoverSuccess = false;
                        } else {  //详情图
                            isUploadDetailSuccess = false;
                        }
                        ToastUtil.showCenter(SkillPublishActivity.this, "详情图片上传失败");
                        requestPublishProduct();
                    }
                });
            }

        });
    }

    /**
     * 显示 showSelectDateDialog
     */
    private void showSelectDateDialog() {
        if (selectDateDialog == null) {
            selectDateDialog = new SelectDateDialog(this);
            String currTime = timeTxtView.getText().toString();
            if (!TextUtils.isEmpty(currTime)) {
                String time[] = currTime.split("-");
                if (time.length > 0) {
                    String year = time[0];
                    selectDateDialog.setYear(year);
                }
                if (time.length > 1) {
                    try {
                        String month = String.valueOf(Integer.parseInt(time[1]));
                        selectDateDialog.setMonth(month);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (time.length > 2) {
                    try {
                        String day = String.valueOf(Integer.parseInt(time[2]));
                        selectDateDialog.setDay(day);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            selectDateDialog.setOnSelectClickListener(new SelectDateDialog.OnSelectClickListener() {
                @Override
                public void onOk(String year, String month, String day, String format) {
                    timeTxtView.setText(format);
                }

                @Override
                public void onCancel() {

                }
            });
        }
        selectDateDialog.show();
    }

    @OnClick(R.id.businessAuthView)
    void businessAuth() {
        if ("1".equals(authResult.getCompany())) {
            switchAuthType(2);
        } else if ("2".equals(authResult.getCompany())) {
            ToastUtil.showCenter(this, "认证审核中");
        } else {
            CompanyCertificationActivity.startActivity(this);
        }
    }

    @OnClick(R.id.personalAuthView)
    void personalAuth() {
        if ("1".equals(authResult.getPeople())) {
            switchAuthType(1);
        } else if ("2".equals(authResult.getPeople())) {
            ToastUtil.showCenter(this, "认证审核中");
        } else {
            PersonalCertificationActivity.startActivity(this);
        }
    }

    @OnClick(R.id.classifyLayout)
    void classifyLayout() {  //分类
        ClassifyActivity.startActivityForResult(this, ClassifyActivity.TYPE_SELECT_SKILL, classifyTxtView.getText().toString(), EXTRA_CLASSIFY);
    }

    @OnClick(R.id.timeLayout)
    void timeLayout() {  //时间
        showSelectDateDialog();
    }


    @OnClick(R.id.publishView)
    void publishView() {  //发布
        long currClickTime = DateUtil.getCurrentTime();
        if (currClickTime - lastClickTime < 1000) {
            return;
        }
        lastClickTime = currClickTime;

        if (TextUtils.isEmpty(titleEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入任务标题");
            return;
        }
        if (TextUtils.isEmpty(descriptEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请详细描述您的任务");
            return;
        }
//        if (coverPicDragLinearView.getItemCount() <= 0) {
//            ToastUtil.showCenter(this, "请添加封面");
//            return;
//        }
//        if (dragLinearView.getItemCount() <= 0) {
//            ToastUtil.showCenter(this, "请添加图片");
//            return;
//        }
        if (TextUtils.isEmpty(addressEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入地址");
            return;
        }
        if (TextUtils.isEmpty(userNameEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(phoneEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入联系方式");
            return;
        }
        if (TextUtils.isEmpty(getPriceTxt())) {
            ToastUtil.showCenter(this, "请输入任务赏金");
            return;
        }
        if (TextUtils.isEmpty(classifyTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择分类");
            return;
        }
        if (timeLayout.getVisibility() == View.VISIBLE && TextUtils.isEmpty(timeTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择任务开启时间");
            return;
        }
        showProgressDialogNoCancel();
        List<String> coverPathList = getUploadProductImgList(coverPicDragLinearView);
        List<String> detailPathList = getUploadProductImgList(dragLinearView);
        if (detailPathList.size() == 0 && coverPathList.size() == 0) {   //不需要上传 直接提交
            isUploadCoverSuccess = true;
            isUploadDetailSuccess = true;
            requestPublishProduct();
        } else {  //上传图片
            goodsCoverImgUrlList.clear();
            goodsDescImgUrlList.clear();

            if (coverPathList.size() > 0) {   //需要上传封面图
                isUploadCoverSuccess = false;
            } else {
                isUploadCoverSuccess = true;
            }

            if (detailPathList.size() > 0) {   //需要上传详情图片
                isUploadDetailSuccess = false;
            } else {
                isUploadDetailSuccess = true;
            }

            if (coverPathList.size() > 0) {   //需要上传封面图
                //上传封面图
                requestUploadImg(coverPicDragLinearView, coverPathList);
            }

            if (detailPathList.size() > 0) {   //需要上传详情图片
                //上传详情图片
                requestUploadImg(dragLinearView, detailPathList);
            }
        }
    }

    private void requestPublishProduct() {
        if (!isUploadCoverSuccess || !isUploadDetailSuccess) {
            dismissProgressDialog();
            return;
        }

        //封面图片
        String coverPic = null;
        List<String> coverImgPathList = getHttpProductImgList(coverPicDragLinearView);
        coverImgPathList.addAll(goodsCoverImgUrlList);
        if (coverImgPathList != null && coverImgPathList.size() != 0) {
            coverPic = coverImgPathList.get(0);
        }
        //详情图片
        String detailPics = "";
        List<String> detailImgPathList = getHttpProductImgList(dragLinearView);
        detailImgPathList.addAll(goodsDescImgUrlList);
        for (int i = 0; i < detailImgPathList.size(); i++) {
            detailPics += detailImgPathList.get(i);
            if (i != detailImgPathList.size() - 1) {
                detailPics += ",";
            }
        }
        request.coverPic = coverPic;
        request.detailPics = detailPics;
        request.title = titleEditTxt.getText().toString();
        request.introduce = descriptEditTxt.getText().toString();
        request.address = addressEditTxt.getText().toString();
        request.realName = userNameEditTxt.getText().toString();
        request.mobile = phoneEditTxt.getText().toString();
        request.price = getPriceTxt();
        request.classifyName = classifyTxtView.getText().toString();
        request.areaName = mAddress;
        if (timeLayout.getVisibility() == View.VISIBLE) {
            request.startTime = timeTxtView.getText().toString();
        }

        if (BeyondApplication.getInstance().getLocation() != null) {
            request.lon = String.valueOf(BeyondApplication.getInstance().getLocation().longitude);
            request.lat = String.valueOf(BeyondApplication.getInstance().getLocation().latitude);
        }

        if (type == SquareActivity.SKILL_TYPE) {
            String skillId = goodsDetail != null ? goodsDetail.skillId : null;
            mRestUsage.publishSkill(REQ_PUBLISH_ID, skillId, request);
        } else {
            String taskId = goodsDetail != null ? goodsDetail.taskId : null;
            mRestUsage.publishTask(REQ_PUBLISH_ID, taskId, request);
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case HANDL_PHOTO_TASK_ID:   //处理图片
                if (msg.getIsSuccess()) {
                    List<String> imgPathList = (List<String>) msg.getObj();

                    addProductImg(imgPathList);
                }
                break;
            case REQ_PUBLISH_ID:
                if (msg.getIsSuccess()) {
                    if (goodsDetail != null) {
                        ToastUtil.showCenter(this, "修改成功");
                    } else {
                        ToastUtil.showCenter(this, "发布成功");
                        GoodsDetail goodsDetail = (GoodsDetail) msg.getObj();
                        if (goodsDetail != null) {
                            if (type == SquareActivity.SKILL_TYPE) {
//                                GoodsDetailV2Activity.startActivitySkill(this, goodsDetail.skillId);
                                //GoodsDetailV2Activity2.startActivitySkill(this, goodsDetail.skillId);
                                finish();
                            } else {
//                                GoodsDetailV2Activity.startActivityTask(this, goodsDetail.taskId);
                                // GoodsDetailV2Activity2.startActivityTask(this, goodsDetail.taskId);
                                finish();
                            }
                        }
                    }
                    finish();
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EXTRA_CLASSIFY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String classify = data.getStringExtra(ClassifyActivity.EXTRA_CLASSIFY_KEY);
                    classifyTxtView.setText(classify);
                }
                break;
            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> imgList = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    if (imgList != null && imgList.size() != 0) {
                        showProgressDialog();
                        List<String> imgPathList = new ArrayList<>();
                        for (ImageFile imageFile : imgList) {
                            imgPathList.add(imageFile.getPath());
                        }
                        execuTask(new HandlPhotoTask(HANDL_PHOTO_TASK_ID, imgPathList));
                    }

                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     * 获取商品定价
     *
     * @return
     */
    private String getPriceTxt() {
        String price = priceEditTxt.getText().toString();
        if (price.equals("面议")) {
            return "-1";
        } else {
            return priceEditTxt.getText().toString().replaceAll(RMB_SYMBOL, "");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    //控制面议的开关
    public boolean isOnOff = true;

    @OnClick(R.id.rl_ss)
    public void onViewClicked() {
        if (isOnOff) {
            priceEditTxt.setText("面议");
            priceEditTxt.setEnabled(false);
            isOnOff = false;
            mianyiIv.setBackgroundResource(R.drawable.ok_check);
        } else {
            priceEditTxt.setText("");
            priceEditTxt.setEnabled(true);
            isOnOff = true;
            mianyiIv.setBackgroundResource(R.drawable.check_nor);
        }
    }
}
