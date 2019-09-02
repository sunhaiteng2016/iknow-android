package com.beyond.popscience.module.job;

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
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DateUtil;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.JobRestUsage;
import com.beyond.popscience.frame.pojo.JobDetail;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.view.DragLinearView;
import com.beyond.popscience.frame.view.SelectCultureDialog;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.beyond.popscience.module.square.ClassifyActivity;
import com.beyond.popscience.module.town.TownCategoryActivity;
import com.beyond.popscience.widget.wheelview.WheelMenuInfo;
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

/**
 * Created by danxiang.feng on 2017/10/15.
 */

public class PublishJobProvideActivity extends BaseActivity {


    private final int REQ_PUBLISH_ID = 1001;

    private final int CODE_SELECT_ADDRESS = 101;  //选择地址
    private final int CODE_SELECT_POSITION = 102; //选择职位

    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 1702;
    public static final String JOB_DETAIL = "jobDetail";

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
    @BindView(R.id.positionTxtView)
    protected TextView positionTxtView;
    @BindView(R.id.descriptEditTxt)
    protected EditText descriptEditTxt;
    @BindView(R.id.userNameEditTxt)
    protected EditText userNameEditTxt;
    @BindView(R.id.phoneEditTxt)
    protected EditText phoneEditTxt;
    @BindView(R.id.priceMinEditTxt)
    protected EditText priceMinEditTxt;
    @BindView(R.id.priceMaxEditTxt)
    protected EditText priceMaxEditTxt;

    @BindView(R.id.addressTxtView)
    protected TextView addressTxtView;
    @BindView(R.id.addressDetailEditView)
    protected EditText addressDetailEditView;
    @BindView(R.id.educationTxtView)
    protected TextView educationTxtView;
    @BindView(R.id.workExperienceTxtView)
    protected TextView workExperienceTxtView;
    @BindView(R.id.requireEditView)
    protected EditText requireEditView;
    @BindView(R.id.coverPicDragLinearView)
    protected DragLinearView coverPicDragLinearView;
    @BindView(R.id.dragLinearView)
    protected DragLinearView dragLinearView;
    @BindView(R.id.mianyi_iv)
    ImageView mianyiIv;
    @BindView(R.id.rl_ss)
    LinearLayout rlSs;

    @Request
    private JobRestUsage mRestUsage;

    private JobDetail request = new JobDetail();
    private JobDetail jobDetail;

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
    /**
     * 当前的地址
     */
    private Address currAddress;

    private long lastClickTime = 0;   //上次点击的时间戳
    /**
     * 文化程度
     */
    private SelectCultureDialog selectEducationDialog;
    /**
     * 工作经验
     */
    private SelectCultureDialog selectWorkExperienceDialog;


    public static void startActivity(Context context, JobDetail jobDetail) {
        Intent intent = new Intent(context, PublishJobProvideActivity.class);
        intent.putExtra(JOB_DETAIL, jobDetail);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_jobprovide_layout;
    }

    @Override
    public void initUI() {
        super.initUI();

        jobDetail = (JobDetail) getIntent().getSerializableExtra(JOB_DETAIL);

        tv_title.setText("发布职位");
        initEditTxt();
        iniEditStatusView();
        initCoverPicDragView();
        initDragView();
    }


    private void iniEditStatusView() {
        if (jobDetail != null) {
            positionTxtView.setText(jobDetail.title);
            request.position = jobDetail.position;
            descriptEditTxt.setText(jobDetail.introduce);
            if (jobDetail.lowPrice.equals("-1") || jobDetail.highPrice.equals("-1")) {
                priceMinEditTxt.setEnabled(false);
                priceMaxEditTxt.setEnabled(false);
                mianyiIv.setBackgroundResource(R.drawable.ok_check);
                isOnOff = false;
            } else {
                priceMinEditTxt.setText(jobDetail.lowPrice);
                priceMaxEditTxt.setText(jobDetail.highPrice);
            }

            userNameEditTxt.setText(jobDetail.realName);
            phoneEditTxt.setText(jobDetail.mobile);
            requireEditView.setText(jobDetail.require);
            addressDetailEditView.setText(jobDetail.address);
            String educationCode = SelectCultureDialog.getMenuCode(jobDetail.education, SelectCultureDialog.TYPE_EDUCATION);
            educationTxtView.setText(jobDetail.education);
            request.education = educationCode;
            workExperienceTxtView.setText(jobDetail.workExperience);
            request.workExperience = jobDetail.workExperience;
            addressTxtView.setText(jobDetail.area);
            request.area = jobDetail.areaCode;
        }
    }


    /**
     * 初始化相关 EditText
     */
    private void initEditTxt() {
        priceMinEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (!priceMinEditTxt.getText().toString().startsWith(RMB_SYMBOL)) {
                    if (priceMinEditTxt.getText().toString().contains(RMB_SYMBOL)) {
                        int selectionIndex = priceMinEditTxt.getSelectionStart();
                        priceMinEditTxt.setText(RMB_SYMBOL + priceMinEditTxt.getText().toString().replaceAll(RMB_SYMBOL, ""));
                        if (selectionIndex == 0) {
                            priceMinEditTxt.setSelection(priceMinEditTxt.getText().length());
                        }
                    } else {
                        priceMinEditTxt.setText(RMB_SYMBOL + priceMinEditTxt.getText().toString());
                        priceMinEditTxt.setSelection(priceMinEditTxt.getText().length());
                    }
                }*/
            }
        });
        priceMaxEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (!priceMaxEditTxt.getText().toString().startsWith(RMB_SYMBOL)) {
                    if (priceMaxEditTxt.getText().toString().contains(RMB_SYMBOL)) {
                        int selectionIndex = priceMaxEditTxt.getSelectionStart();
                        priceMaxEditTxt.setText(RMB_SYMBOL + priceMaxEditTxt.getText().toString().replaceAll(RMB_SYMBOL, ""));
                        if (selectionIndex == 0) {
                            priceMaxEditTxt.setSelection(priceMaxEditTxt.getText().length());
                        }
                    } else {
                        priceMaxEditTxt.setText(RMB_SYMBOL + priceMaxEditTxt.getText().toString());
                        priceMaxEditTxt.setSelection(priceMaxEditTxt.getText().length());
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
                    ImageLoaderUtil.displayImage(PublishJobProvideActivity.this, url, imageView, options);

                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if (jobDetail != null) {
            if (!TextUtils.isEmpty(jobDetail.coverPic)) {
                coverPicDragLinearView.addMutilItemView(new LinkedList<DragLinearView.ImageTagElement>(Arrays.asList(new DragLinearView.ImageTagElement(null, jobDetail.coverPic))), false);
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
                    ImageLoaderUtil.displayImage(PublishJobProvideActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if (jobDetail != null) {
            List<String> goodsDescImgList = jobDetail.getDetailPicList();
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
                        ToastUtil.showCenter(PublishJobProvideActivity.this, "详情图片上传失败");
                        requestPublishProduct();
                    }
                });
            }

        });
    }

    @OnClick(R.id.positionLayout)
    void positionLayout() {  //职位
        ClassifyActivity.startActivityForResult(this, ClassifyActivity.TYPE_SELECT_JOB_POSITION, request.position, CODE_SELECT_POSITION);
    }

    @OnClick(R.id.addressLayout)
    void addressLayout() {  //地点
        TownCategoryActivity.startActivityForResult(this, currAddress, CODE_SELECT_ADDRESS);
    }

    @OnClick(R.id.educationLayout)
    void educationLayout() {  //学历
        if (selectEducationDialog == null) {
            selectEducationDialog = new SelectCultureDialog(this, SelectCultureDialog.TYPE_DEFAULT);
            selectEducationDialog.setCyclic(false);
            selectEducationDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if (wheelMenuInfo != null) {
                        educationTxtView.setText(wheelMenuInfo.getName());
                        request.education = wheelMenuInfo.getCode();
                    }
                }
            });
        }
        selectEducationDialog.setSelectedMenuName(educationTxtView.getText().toString());
        selectEducationDialog.show();
    }

    @OnClick(R.id.workExperienceLayout)
    void workExperienceLayout() {  //工作经验
        if (selectWorkExperienceDialog == null) {
            selectWorkExperienceDialog = new SelectCultureDialog(this, SelectCultureDialog.TYPE_WORKEXPERIENCE);
            selectWorkExperienceDialog.setCyclic(false);
            selectWorkExperienceDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if (wheelMenuInfo != null) {
                        workExperienceTxtView.setText(wheelMenuInfo.getName());
                        request.workExperience = wheelMenuInfo.getName();
                    }
                }
            });
        }
        selectWorkExperienceDialog.setSelectedMenuName(workExperienceTxtView.getText().toString());
        selectWorkExperienceDialog.show();
    }


    @OnClick(R.id.publishView)
    void publishView() {  //发布
        long currClickTime = DateUtil.getCurrentTime();
        if (currClickTime - lastClickTime < 1000) {
            return;
        }
        lastClickTime = currClickTime;

        if (TextUtils.isEmpty(positionTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择职位");
            return;
        }
        if (TextUtils.isEmpty(descriptEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请详细描述职位");
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
        if (isOnOff) {
            if (TextUtils.isEmpty(getPriceTxt(priceMinEditTxt.getText().toString()))) {
                ToastUtil.showCenter(this, "请输入最低工资");
                return;
            }
            if (TextUtils.isEmpty(getPriceTxt(priceMaxEditTxt.getText().toString()))) {
                ToastUtil.showCenter(this, "请输入最高工资");
                return;
            }
        }

        if (TextUtils.isEmpty(addressTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择地点");
            return;
        }
        if (TextUtils.isEmpty(addressDetailEditView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入详细地址");
            return;
        }
        if (TextUtils.isEmpty(educationTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择学历");
            return;
        }
        if (TextUtils.isEmpty(workExperienceTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入工作经验");
            return;
        }
        if (TextUtils.isEmpty(requireEditView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入技能要求");
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
        request.introduce = descriptEditTxt.getText().toString();
        request.lowPrice = getPriceTxt(priceMinEditTxt.getText().toString());
        request.highPrice = getPriceTxt(priceMaxEditTxt.getText().toString());
        request.realName = userNameEditTxt.getText().toString();
        request.mobile = phoneEditTxt.getText().toString();
        request.address = addressDetailEditView.getText().toString();
        request.require = requireEditView.getText().toString();

        String jobId = jobDetail != null ? jobDetail.jobId : null;
        mRestUsage.publishJobProvide(REQ_PUBLISH_ID, jobId, request);
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
                    if (jobDetail != null) {
                        ToastUtil.showCenter(this, "修改成功");
                    } else {
                        ToastUtil.showCenter(this, "发布成功");
                        JobDetail job = (JobDetail) msg.getObj();
                        if (job != null && !TextUtils.isEmpty(job.jobId)) {
                            finish();
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
            case CODE_SELECT_ADDRESS:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Address address = (Address) data.getSerializableExtra("address");
                    if (address != null) {
                        currAddress = address;
                        addressTxtView.setText(address.getName());
                        request.area = String.valueOf(address.getId());
                    }
                }
                break;
            case CODE_SELECT_POSITION:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String position = data.getStringExtra(ClassifyActivity.EXTRA_CLASSIFY_KEY);
                    positionTxtView.setText(position);
                    request.title = position;
                }
                break;
        }
    }

    /**
     * 修改成功
     * /**
     * 获取商品定价
     *
     * @return
     */
    private String getPriceTxt(String price) {

        if (!TextUtils.isEmpty(price)) {
            if (price.equals("面议")) {
                return "-1";
            } else {
                return price.replaceAll(RMB_SYMBOL, "");
            }
        } else {
            return "-1";
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
            priceMinEditTxt.setText("");
            priceMaxEditTxt.setText("");
            priceMinEditTxt.setEnabled(false);
            priceMaxEditTxt.setEnabled(false);
            isOnOff = false;
            mianyiIv.setBackgroundResource(R.drawable.ok_check);
        } else {
            priceMinEditTxt.setText("");
            priceMaxEditTxt.setText("");
            priceMinEditTxt.setEnabled(true);
            priceMaxEditTxt.setEnabled(true);
            isOnOff = true;
            mianyiIv.setBackgroundResource(R.drawable.check_nor);
        }
    }
}
