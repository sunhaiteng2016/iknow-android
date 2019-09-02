package com.beyond.popscience.module.building;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DateUtil;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.view.DragLinearView;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.mservice.CheckAuthTool;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.beyond.popscience.module.square.ClassifyActivity;
import com.beyond.popscience.widget.BasePopupWindow.utils.ToastUtils;
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
 * Created by danxiang.feng on 2017/10/14.
 */

public class PublishBuildingActivity extends BaseActivity {

    private final int REQ_PUBLISH_ID = 1201;

    private final int EXTRA_CLASSIFY = 101;
    private final int EXTRA_CLASSIFY_DECORATE = 102;
    private final int EXTRA_CLASSIFY_CONFIG = 103;
    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 1702;

    private static final String BUILDING_DETAIL = "buildingDetail";

    private final String RMB_SYMBOL = "¥";
    private final String SIZE_SYMBOL = "㎡";
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
    @BindView(R.id.xiangxidizhiaddress)
    protected TextView xiangxidizhiaddress;

    @BindView(R.id.tradeLeftView)
    protected TextView tradeLeftView;
    @BindView(R.id.tradeRightView)
    protected TextView tradeRightView;
    @BindView(R.id.houseHolderView)
    protected TextView houseHolderView;
    @BindView(R.id.intermediaryView)
    protected TextView intermediaryView;
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
    @BindView(R.id.tingEditView)
    protected EditText tingEditView;
    @BindView(R.id.shiEditView)
    protected EditText shiEditView;
    @BindView(R.id.weiEditView)
    protected EditText weiEditView;
    @BindView(R.id.sizeEditView)
    protected EditText sizeEditView;
    @BindView(R.id.priceEditTxt)
    protected EditText priceEditTxt;
    @BindView(R.id.classifyTxtView)
    protected TextView classifyTxtView;
    @BindView(R.id.decorateTxtView)
    protected TextView decorateTxtView;
    @BindView(R.id.configTxtView)
    protected TextView configTxtView;

    @BindView(R.id.coverPicDragLinearView)
    protected DragLinearView coverPicDragLinearView;
    @BindView(R.id.dragLinearView)
    protected DragLinearView dragLinearView;
    @BindView(R.id.rl_ss)
    LinearLayout rlSs;
    @BindView(R.id.priceTipTxtView)
    TextView priceTipTxtView;
    @BindView(R.id.mianyi_iv)
    ImageView mianyiIv;

    @Request
    private BuildingRestUsage mRestUsage;

    private BuildingDetail request = new BuildingDetail();
    private BuildingDetail buildingDetail;

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


    public static void startActivity(Context context, BuildingDetail buildingDetail) {
        Intent intent = new Intent(context, PublishBuildingActivity.class);
        intent.putExtra(BUILDING_DETAIL, buildingDetail);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_building_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        buildingDetail = (BuildingDetail) getIntent().getSerializableExtra(BUILDING_DETAIL);
        mAddress = BeyondApplication.getInstance().getLocation().city + "-" + BeyondApplication.getInstance().getLocation().district;
        tv_title.setText("出租出售");
        initEditTxt();
        iniEditStatusView();
        initCoverPicDragView();
        initDragView();
        xiangxidizhiaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressPickTask task = new AddressPickTask(PublishBuildingActivity.this);
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
                        } else {
                            mAddress = city.getAreaName() + "-" + county.getAreaName();
                            //重新的刷新  列表的數據
                            xiangxidizhiaddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                        }
                    }
                });

                String detailedArea = SPUtils.get(PublishBuildingActivity.this, "detailedArea", "") + "";
                String[] detailedAreas = detailedArea.split("-");
                if (detailedAreas.length > 1) {
                    task.execute(detailedAreas[0], detailedAreas[1], detailedAreas[2]);
                } else {
                    task.execute("浙江省", "杭州市", "仙居县");
                }

            }
        });

    }

    private void iniEditStatusView() {
        if (buildingDetail != null) {
            switchSellType(buildingDetail.getSellTypeInt());
            switchTradeType(buildingDetail.getTradeInt());
            titleEditTxt.setText(buildingDetail.title);
            descriptEditTxt.setText(buildingDetail.introduce);
            addressEditTxt.setText(buildingDetail.address);
            userNameEditTxt.setText(buildingDetail.realName);
            phoneEditTxt.setText(buildingDetail.mobile);
            priceEditTxt.setText(buildingDetail.price);
            classifyTxtView.setText(buildingDetail.classifyName);
            decorateTxtView.setText(buildingDetail.decorate);
            configTxtView.setText(buildingDetail.config);
            sizeEditView.setText(buildingDetail.size);
            if (!TextUtils.isEmpty(buildingDetail.houseType)) {
                String houseTypes[] = buildingDetail.houseType.split(",");
                if (houseTypes.length > 0) {
                    tingEditView.setText(houseTypes[0]);
                }
                if (houseTypes.length > 1) {
                    shiEditView.setText(houseTypes[1]);
                }
                if (houseTypes.length > 2) {
                    weiEditView.setText(houseTypes[2]);
                }
            }
        } else {
            switchSellType(2);
            switchTradeType(1);
            if (BeyondApplication.getInstance().getLocation() != null) {
                xiangxidizhiaddress.setText(BeyondApplication.getInstance().getLocation().province + BeyondApplication.getInstance().getLocation().city + BeyondApplication.getInstance().getLocation().district);
                mAddress = BeyondApplication.getInstance().getLocation().city + "-" + BeyondApplication.getInstance().getLocation().district;
            }
        }
    }

    private void switchSellType(int sellType) {
        if (sellType == 1) {   //1：中介 2：房东
            intermediaryView.setTextColor(getResources().getColor(R.color.white));
            intermediaryView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            houseHolderView.setTextColor(getResources().getColor(R.color.blue2));
            houseHolderView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            request.sellType = "1";
        } else if (sellType == 2) {  //
            houseHolderView.setTextColor(getResources().getColor(R.color.white));
            houseHolderView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            intermediaryView.setTextColor(getResources().getColor(R.color.blue2));
            intermediaryView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            request.sellType = "2";
        }
    }

    private void switchTradeType(int tradeType) {
        if (tradeType == 1) {  //交易方式	1:出租 2:出售
            tradeLeftView.setTextColor(getResources().getColor(R.color.white));
            tradeLeftView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            tradeRightView.setTextColor(getResources().getColor(R.color.blue2));
            tradeRightView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            request.trade = "1";
        } else if (tradeType == 2) {
            tradeRightView.setTextColor(getResources().getColor(R.color.white));
            tradeRightView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round1));
            tradeLeftView.setTextColor(getResources().getColor(R.color.blue2));
            tradeLeftView.setBackground(getResources().getDrawable(R.drawable.bg_blue_round3));
            request.trade = "2";
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
                /*if (!priceEditTxt.getText().toString().startsWith(RMB_SYMBOL)) {
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
               /* if (s.toString().contains("面议")) {
                    priceEditTxt.setText(s);
                } else {
                    priceEditTxt.setText(RMB_SYMBOL + s);
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
                    ImageLoaderUtil.displayImage(PublishBuildingActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if (buildingDetail != null) {
            if (!TextUtils.isEmpty(buildingDetail.coverPic)) {
                coverPicDragLinearView.addMutilItemView(new LinkedList<DragLinearView.ImageTagElement>(Arrays.asList(new DragLinearView.ImageTagElement(null, buildingDetail.coverPic))), false);
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
                    ImageLoaderUtil.displayImage(PublishBuildingActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if (buildingDetail != null) {
            List<String> goodsDescImgList = buildingDetail.getDetailPicList();
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
                        ToastUtil.showCenter(PublishBuildingActivity.this, "详情图片上传失败");
                        requestPublishProduct();
                    }
                });
            }

        });
    }

    @OnClick(R.id.intermediaryView)
    void intermediaryView() {  //中介
        switchSellType(1);
    }

    @OnClick(R.id.houseHolderView)
    void houseHolderView() {   //房东
        switchSellType(2);
    }

    @OnClick(R.id.tradeLeftView)
    void tradeLeftView() {  //中介
        switchTradeType(1);
    }

    @OnClick(R.id.tradeRightView)
    void tradeRightView() {   //房东
        switchTradeType(2);
    }


    @OnClick(R.id.classifyLayout)
    void classifyLayout() {  //分类
        ClassifyActivity.startActivityForResult(this, ClassifyActivity.TYPE_SELECT_BUILDING, classifyTxtView.getText().toString(), EXTRA_CLASSIFY);
    }

    @OnClick(R.id.decorateLayout)
    void decorateLayout() {  //装修
        ClassifyBuildingActivity.startActivityForResult(this, ClassifyBuildingActivity.TYPE_DECORATE, decorateTxtView.getText().toString(), false, EXTRA_CLASSIFY_DECORATE);
    }

    @OnClick(R.id.configLayout)
    void configLayout() {  //配置
        ClassifyBuildingActivity.startActivityForResult(this, ClassifyBuildingActivity.TYPE_CONFIG, configTxtView.getText().toString(), true, EXTRA_CLASSIFY_CONFIG);
    }


    @OnClick(R.id.publishView)
    void publishView() {  //发布
        long currClickTime = DateUtil.getCurrentTime();
        if (currClickTime - lastClickTime < 1000) {
            return;
        }
        lastClickTime = currClickTime;


        if (TextUtils.isEmpty(request.trade)) {
            ToastUtil.showCenter(this, "请选择交易方式");
            return;
        }
        if (TextUtils.isEmpty(request.sellType)) {
            ToastUtil.showCenter(this, "请选择房源类型");
            return;
        }
        if (TextUtils.isEmpty(titleEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入房源标题");
            return;
        }
        if (TextUtils.isEmpty(descriptEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请详细描述您的房源");
            return;
        }
        if (coverPicDragLinearView.getItemCount() <= 0) {
            ToastUtil.showCenter(this, "请添加封面");
            return;
        }
        if (dragLinearView.getItemCount() <= 0) {
            ToastUtil.showCenter(this, "请添加图片");
            return;
        }
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
        if (TextUtils.isEmpty(tingEditView.getText().toString())
                || TextUtils.isEmpty(shiEditView.getText().toString())
                || TextUtils.isEmpty(weiEditView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入户型");
            return;
        }
        if (TextUtils.isEmpty(sizeEditView.getText().toString())) {
            ToastUtil.showCenter(this, "请输入面积");
            return;
        }
        if (TextUtils.isEmpty(getPriceTxt())) {
            ToastUtil.showCenter(this, "请输入房源价格");
            return;
        }
        if (TextUtils.isEmpty(classifyTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择分类");
            return;
        }
        if (TextUtils.isEmpty(decorateTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择装修");
            return;
        }
        if (TextUtils.isEmpty(configTxtView.getText().toString())) {
            ToastUtil.showCenter(this, "请选择配置");
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
        request.houseType = tingEditView.getText().toString() + "," + shiEditView.getText().toString() + "," + weiEditView.getText().toString();
        request.size = sizeEditView.getText().toString();
        request.price = getPriceTxt();
        request.classifyName = classifyTxtView.getText().toString();
        request.decorate = decorateTxtView.getText().toString();
        request.config = configTxtView.getText().toString();
        request.areaName = mAddress;
        if (CheckAuthTool.result.getCompany().equals("0")){
            request.authorityType="2";
        }else{
            request.authorityType="1";
        }

        getLatlon(xiangxidizhiaddress.getText().toString() + addressEditTxt.getText().toString());
        /*if (BeyondApplication.getInstance().getLocation() != null) {
            request.lon = String.valueOf(BeyondApplication.getInstance().getLocation().longitude);
            request.lat = String.valueOf(BeyondApplication.getInstance().getLocation().latitude);
        }*/

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
                    if (buildingDetail != null) {
                        ToastUtil.showCenter(this, "修改成功");
                    } else {
                        ToastUtil.showCenter(this, "发布成功");
                        BuildingDetail buildingDetail = (BuildingDetail) msg.getObj();
                        if (buildingDetail != null && !TextUtils.isEmpty(buildingDetail.buildingId)) {
                            finish();
                        }
                    }
                    finish();
                } else {
                    ToastUtil.showCenter(this, "修改成功");
                    BuildingActivity.startActivity(PublishBuildingActivity.this);
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
            case EXTRA_CLASSIFY_DECORATE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String decorate = data.getStringExtra(ClassifyBuildingActivity.EXTRA_CLASSIFY_RESULT_KEY);
                    decorateTxtView.setText(decorate);
                }
                break;
            case EXTRA_CLASSIFY_CONFIG:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String config = data.getStringExtra(ClassifyBuildingActivity.EXTRA_CLASSIFY_RESULT_KEY);
                    configTxtView.setText(config);
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

    private void getLatlon(String cityName) {

        GeocodeSearch geocodeSearch = new GeocodeSearch(PublishBuildingActivity.this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                if (i == 1000) {
                    if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null &&
                            geocodeResult.getGeocodeAddressList().size() > 0) {

                        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                        double latitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
                        double longititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
                        String adcode = geocodeAddress.getAdcode();//区域编码
                        request.lat = latitude + "";
                        request.lon = longititude + "";
                        Log.e("地理编码", geocodeAddress.getAdcode() + "");
                        Log.e("纬度latitude", latitude + "");
                        Log.e("经度longititude", longititude + "");
                        String buildingId = buildingDetail != null ? buildingDetail.buildingId : null;
                        mRestUsage.publishBuilding(REQ_PUBLISH_ID, buildingId, request);
                    } else {
                        ToastUtil.show(PublishBuildingActivity.this, "请填写正确的地址");
                    }
                }
            }
        });

        GeocodeQuery geocodeQuery = new GeocodeQuery(cityName.trim(), "29");
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
    }

}
