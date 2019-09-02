package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.pojo.AuthResult;
import com.beyond.popscience.frame.pojo.GoodsDescImgObj;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.pojo.ServiceGoodsItem;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.view.DragLinearView;
import com.beyond.popscience.module.home.AddressPickTask;
import com.beyond.popscience.module.home.WelcomeActivity;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * 发布商品
 * Created by linjinfa on 2017/6/23.
 * email 331710168@qq.com
 */
public class PublishGoodsActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String KEY_GOODS_DETAIL = "goods_detail";
    /**
     *
     */
    private final String RMB_SYMBOL = "¥";

    /**
     * 最多选择10张
     */
    private final int MAX_PHOTO_NUMBER = 1;

    /**
     * 封面图最多一张
     */
    private final int MAX_COVER_PHOTO_NUMBER = 1;
    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 101;
    /**
     * 获取商品分类
     */
    private final int REQUST_PRODUCT_CLASSFY_TASK_ID = 102;
    /**
     * 上传商品接口
     */
    private final int REQUST_PUBLISH_PRODUCT_TASK_ID = 103;
    private final int REQ_AUTH_STATUS = 2001;
    @BindView(R.id.tv_title)
    protected TextView titleTxtView;
    @BindView(R.id.xiangdidizhiaddress)
    protected TextView xiangdidizhiaddress;
    @BindView(R.id.tv_right)
    protected TextView rightTxtView;

    @BindView(R.id.goodsTitleEditTxt)
    protected EditText goodsTitleEditTxt;

    @BindView(R.id.userNameEditTxt)
    protected EditText userNameEditTxt;

    @BindView(R.id.phoneEditTxt)
    protected EditText phoneEditTxt;

    @BindView(R.id.priceEditTxt)
    protected EditText priceEditTxt;

    @BindView(R.id.addressEditTxt)
    protected EditText addressEditTxt;

    @BindView(R.id.coverPicDragLinearView)
    protected DragLinearView coverPicDragLinearView;

    @BindView(R.id.goodsDetailLinLay)
    protected LinearLayout goodsDetailLinLay;

    @Request
    private ServiceRestUsage serviceRestUsage;

    @Request
    private SquareRestUsage squareRestUsage;

    /**
     * 存放已上传的封面图片
     */
    private List<String> goodsCoverImgUrlList = new ArrayList<>();
    /**
     * 编辑的商品
     */
    private GoodsDetail editGoodsDetail;
    /**
     * 选中的分类id
     */
    private String selectedClassfyId = null;
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
     *
     */
    private List<GoodsDetailView> cacheGoodsDetailViewList = new ArrayList<>();
    /**
     * 需要上传图片的 GoodsDetailView
     */
    private ConcurrentLinkedQueue<GoodsDetailView> needUploadImgGoodsDetailViewQueue = new ConcurrentLinkedQueue<>();
    private String mAdress;
    private AuthResult authResult;


    /**
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PublishGoodsActivity.class);
        context.startActivity(intent);
    }

    /**
     * @param fragment
     */
    public static void startActivity(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), PublishGoodsActivity.class);
        fragment.startActivity(intent);
    }

    /**
     * 编辑商品
     *
     * @param context
     * @param detail
     */
    public static void startActivityEdit(Context context, GoodsDetail detail) {
        Intent intent = new Intent(context, PublishGoodsActivity.class);
        intent.putExtra(KEY_GOODS_DETAIL, detail);
        context.startActivity(intent);
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
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_goods;
    }

    @Override
    public void initUI() {
        super.initUI();

        editGoodsDetail = (GoodsDetail) getIntent().getSerializableExtra(KEY_GOODS_DETAIL);
        rightTxtView.setVisibility(View.VISIBLE);
        rightTxtView.setText("确认");
        mAdress = WelcomeActivity.seletedAdress;
        initEditTxt();
        iniEditStatusView();
        initCoverPicDragView();
        initGoodsDetailView();
        xiangdidizhiaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressPickTask task = new AddressPickTask(PublishGoodsActivity.this);
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
                            mAdress = city.getAreaName() + "-" + county.getAreaName();
                            xiangdidizhiaddress.setText(mAdress);
                        }
                    }
                });

                task.execute(BeyondApplication.getInstance().getLocation().provider, BeyondApplication.getInstance().getLocation().city, BeyondApplication.getInstance().getLocation().district);
            }
        });

        squareRestUsage.getAuthStatus(REQ_AUTH_STATUS);
    }


    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case HANDL_PHOTO_TASK_ID:   //处理图片
                if (msg.getIsSuccess()) {
                    List<String> imgPathList = (List<String>) msg.getObj();
                    addProductImg(imgPathList);
                }
                dismissProgressDialog();
                break;
            case REQ_AUTH_STATUS:
                if (msg.getIsSuccess()) {
                    authResult = (AuthResult) msg.getObj();
                }
                break;
            case REQUST_PRODUCT_CLASSFY_TASK_ID:    //商品分类
                if (msg.getIsSuccess()) {

                }
                dismissProgressDialog();
                break;
            case REQUST_PUBLISH_PRODUCT_TASK_ID:    //发布商品
                if (msg.getIsSuccess()) {
                    if (editGoodsDetail != null) {  //修改商品
                        ToastUtil.showCenter(this, "修改成功");
                    } else {
                        ToastUtil.showCenter(this, "发布成功");
                        if (msg.getObj() instanceof GoodsDetail) {
                            GoodsDetail goodsDetail = (GoodsDetail) msg.getObj();
                            if (!TextUtils.isEmpty(goodsDetail.productId)) {
                                ServiceGoodsItem serviceGoodsItem = new ServiceGoodsItem();
                                serviceGoodsItem.setProductId(goodsDetail.productId);
//                                GoodsDetailV2Activity.startActivityGoods(this, serviceGoodsItem);
                                // GoodsDetailV2Activity2.startActivityGoods(this, serviceGoodsItem);
                                finish();
                            }
                        }
                    }
                    finish();
                }
                dismissProgressDialog();
                break;
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
                if (!priceEditTxt.getText().toString().startsWith(RMB_SYMBOL)) {
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
                }
            }
        });
    }

    /**
     * 初始化编辑状态下的view
     */
    private void iniEditStatusView() {
        if (editGoodsDetail != null) {
            titleTxtView.setText("修改商品");

            selectedClassfyId = editGoodsDetail.classfyId;

            addressEditTxt.setText(editGoodsDetail.address);
            userNameEditTxt.setText(editGoodsDetail.realName);
            goodsTitleEditTxt.setText(editGoodsDetail.title);
            phoneEditTxt.setText(editGoodsDetail.mobile);
            priceEditTxt.setText(editGoodsDetail.price);
        } else {
            titleTxtView.setText("发布商品");
            if (BeyondApplication.getInstance().getLocation() != null) {
                xiangdidizhiaddress.setText(BeyondApplication.getInstance().getLocation().province + BeyondApplication.getInstance().getLocation().city + BeyondApplication.getInstance().getLocation().district);
                addressEditTxt.setText(BeyondApplication.getInstance().getLocation().street);
                mAdress = BeyondApplication.getInstance().getLocation().city + "-" + BeyondApplication.getInstance().getLocation().district;
            }
        }
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
                    ImageLoaderUtil.displayImage(PublishGoodsActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if (editGoodsDetail != null) {
            if (!TextUtils.isEmpty(editGoodsDetail.coverPic)) {
                coverPicDragLinearView.addMutilItemView(new LinkedList<DragLinearView.ImageTagElement>(Arrays.asList(new DragLinearView.ImageTagElement(null, editGoodsDetail.coverPic))), false);
            }
        }
    }

    /**
     * 初始化详情item
     */
    private void initGoodsDetailView() {
        cacheGoodsDetailViewList.clear();
        goodsDetailLinLay.removeAllViews();
        for (int i = 0; i < 6; i++) {
            View view = View.inflate(this, R.layout.adapter_publish_goods_item, null);
            goodsDetailLinLay.addView(view);

            DragLinearView dragLinearView = (DragLinearView) view.findViewById(R.id.dragLinearView);
            EditText goodsDescEditTxt = (EditText) view.findViewById(R.id.goodsDescEditTxt);
            goodsDescEditTxt.setHint("详情描述" + (i + 1));

            GoodsDescImgObj goodsDescImgObj = null;
            if (editGoodsDetail != null) {
                if (editGoodsDetail.goods != null && editGoodsDetail.goods.size() > 0 && i >= 0 && i < editGoodsDetail.goods.size()) {
                    goodsDescImgObj = editGoodsDetail.goods.get(i);

                    goodsDescEditTxt.setText(goodsDescImgObj.getGoodsDesc());
                }
            }
            initDragView(dragLinearView, goodsDescImgObj);

            cacheGoodsDetailViewList.add(new GoodsDetailView(i, goodsDescEditTxt, dragLinearView));
        }
    }

    /**
     * 初始化 DragLinearView
     */
    private void initDragView(final DragLinearView dragLinearView, GoodsDescImgObj goodsDescImgObj) {
        dragLinearView.setMaxRows(1);
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
                    ImageLoaderUtil.displayImage(PublishGoodsActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if (goodsDescImgObj != null) {
            LinkedList<DragLinearView.ImageTagElement> imageTagElementList = new LinkedList<DragLinearView.ImageTagElement>();
            imageTagElementList.add(new DragLinearView.ImageTagElement(null, goodsDescImgObj.getGoodsDescImg()));
            dragLinearView.addMutilItemView(imageTagElementList, false);
        }

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
     * 选择图片
     */
    private void startSelectImageActivity(int maxNum) {
        if (maxNum <= 0) {
            return;
        }
        Intent intent1 = new Intent(PublishGoodsActivity.this, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
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
     * 请求商品分类
     */
    private void requestGoodsCategory() {
        showProgressDialog();
        serviceRestUsage.getProductClassfy(REQUST_PRODUCT_CLASSFY_TASK_ID);
    }

    /**
     * 请求上传商品
     */
    private void requestPublishProduct() {

        String goodsTitle = goodsTitleEditTxt.getText().toString();
        String goodsPrice = getPriceTxt();
        String address = addressEditTxt.getText().toString();

        String userName = userNameEditTxt.getText().toString();
        String phone = phoneEditTxt.getText().toString();

        //封面图片
        String coverPic = null;
        List<String> coverImgPathList = getHttpProductImgList(coverPicDragLinearView);
        coverImgPathList.addAll(goodsCoverImgUrlList);
        if (coverImgPathList != null && coverImgPathList.size() != 0) {
            coverPic = coverImgPathList.get(0);
        }

        //详情图片
        List<HashMap<String, String>> goodsDescList = new ArrayList<>();
        for (int i = 0; i < cacheGoodsDetailViewList.size(); i++) {
            String detailPic = "";
            GoodsDetailView goodsDetailView = cacheGoodsDetailViewList.get(i);
            if (goodsDetailView.dragLinearView.getItemCount() > 0) {
                List<String> detailImgPathList = getHttpProductImgList(goodsDetailView.dragLinearView);
                detailImgPathList.addAll(goodsDetailView.successUploadImgList);
                for (int imgIndex = 0; imgIndex < detailImgPathList.size(); imgIndex++) {
                    detailPic += detailImgPathList.get(imgIndex);
                    if (imgIndex != detailImgPathList.size() - 1) {
                        detailPic += ",";
                    }
                }
            }
            if (TextUtils.isEmpty(goodsDetailView.goodsDescEditTxt.getText().toString()) && TextUtils.isEmpty(detailPic)) {
                continue;
            }
            HashMap<String, String> goodsDescContentMap = new HashMap<>();
            goodsDescContentMap.put("goodsDesc", goodsDetailView.goodsDescEditTxt.getText().toString());
            goodsDescContentMap.put("goodsDescImg", detailPic);

            goodsDescList.add(goodsDescContentMap);
        }

        //是否修改商品
        String productId = editGoodsDetail != null ? editGoodsDetail.productId : null;

        String introduce = null;
        String detailPic = null;
        String classfyId = null;
        String classfyName = null;
        String lon = null;  //经度
        String lat = null;  //纬度
        if (BeyondApplication.getInstance().getLocation() != null) {
            lon = String.valueOf(BeyondApplication.getInstance().getLocation().longitude);
            lat = String.valueOf(BeyondApplication.getInstance().getLocation().latitude);
        }
        //个人认证 跟 企业认证
        String isCompany = authResult.getCompany();
        String isPeople = authResult.getPeople();
        String rzjs = "1";
        if (isCompany.equals("0")) {
            rzjs = "2";
        }
        if (isPeople.equals("0")) {
            rzjs = "1";
        }
        serviceRestUsage.publishProductTwo(
                REQUST_PUBLISH_PRODUCT_TASK_ID,
                productId,
                goodsTitle,
                introduce,
                mAdress,
                address,
                phone,
                coverPic,
                detailPic,
                classfyId,
                classfyName,
                userName,
                goodsPrice,
                lon,
                lat,
                goodsDescList,
                rzjs
        );
    }

    /**
     * 上传图片
     */
    private void requestUploadImgV2() {
        if (needUploadImgGoodsDetailViewQueue.isEmpty()) {
            requestPublishProduct();
            return;
        }
        GoodsDetailView goodsDetailView = needUploadImgGoodsDetailViewQueue.poll();
        List<String> pathList = getUploadProductImgList(goodsDetailView.dragLinearView);
        ThirdSDKManager.getInstance().uploadImage(pathList, new ThirdSDKManager.UploadCallback(goodsDetailView) {

            @Override
            public void onSuccess(Map<String, String> successMap, List<String> failureList, Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                L.v("上传成功=========> " + targetObj);

                GoodsDetailView goodsDetailView = (GoodsDetailView) targetObj;

                if (goodsDetailView.dragLinearView == coverPicDragLinearView) {    //封面图
                    goodsCoverImgUrlList.clear();
                    goodsCoverImgUrlList.addAll(new ArrayList<String>(successMap.values()));
                } else {  //详情图
                    goodsDetailView.successUploadImgList.clear();
                    goodsDetailView.successUploadImgList.addAll(new ArrayList<String>(successMap.values()));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestUploadImgV2();
                    }
                });
            }

            @Override
            public void onFailure(List<String> failureList, String errCode, String errMsg, Object targetObj) {
                super.onFailure(failureList, errCode, errMsg, targetObj);
                L.v("上传失败=========>" + targetObj);
                GoodsDetailView goodsDetailView = (GoodsDetailView) targetObj;
                if (goodsDetailView.dragLinearView == coverPicDragLinearView) {    //封面图
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showCenter(PublishGoodsActivity.this, "封面图上传失败");
                            dismissProgressDialog();
                        }
                    });
                } else {  //详情图
                    if (goodsDetailView.index == 0) { //详情1
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showCenter(PublishGoodsActivity.this, "详情图上传失败");
                                dismissProgressDialog();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                requestUploadImgV2();
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取商品定价
     *
     * @return
     */
    private String getPriceTxt() {
        return priceEditTxt.getText().toString().replaceAll(RMB_SYMBOL, "");
    }

    /**
     * 发布
     *
     * @param view
     */
    @OnClick(R.id.tv_right)
    public void publishedClick(View view) {
        if (TextUtils.isEmpty(goodsTitleEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入商品标题");
            return;
        }

        if (TextUtils.isEmpty(getPriceTxt())) {
            ToastUtil.showCenter(this, "请输入商品价格");
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

        if (coverPicDragLinearView.getItemCount() <= 0) {
            ToastUtil.showCenter(this, "请添加封面");
            return;
        }
        GoodsDetailView firstGoodsDetailView = cacheGoodsDetailViewList.get(0);
        if (TextUtils.isEmpty(firstGoodsDetailView.goodsDescEditTxt.getText()) || firstGoodsDetailView.dragLinearView.getItemCount() <= 0) {
            ToastUtil.showCenter(this, "第一个详情描述必填哦");
            return;
        }
        if (TextUtils.isEmpty(firstGoodsDetailView.goodsDescEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入第一个的详情描述");
            return;
        }
        if (firstGoodsDetailView.dragLinearView.getItemCount() <= 0) {
            ToastUtil.showCenter(this, "请添加第一个的详情描述图片");
            return;
        }

        //需要上传的详情图片
        List<GoodsDetailView> uploadGoodsDetailViewList = new ArrayList<>();
        //可以只传图不填描述，但是不能只填描述
        for (int i = 0; i < cacheGoodsDetailViewList.size(); i++) {
            GoodsDetailView goodsDetailView = cacheGoodsDetailViewList.get(i);
            List<String> detailPathList = getUploadProductImgList(goodsDetailView.dragLinearView);
            if (detailPathList != null && detailPathList.size() != 0) {
                uploadGoodsDetailViewList.add(goodsDetailView);
            }
//            if(!TextUtils.isEmpty(goodsDetailView.goodsDescEditTxt.getText().toString()) && goodsDetailView.dragLinearView.getItemCount()<=0){
//                ToastUtil.showCenter(this, "详情"+(i+1)+" 请上传图片");
//                return ;
//            }else{
//                List<String> detailPathList = getUploadProductImgList(goodsDetailView.dragLinearView);
//                if(detailPathList!=null && detailPathList.size()!=0){
//                    uploadGoodsDetailViewList.add(goodsDetailView);
//                }
//            }
        }

        //所有需要上传的图片
        List<GoodsDetailView> allNeedUploadGoodsDetailViewList = new ArrayList<>();
        List<String> coverPathList = getUploadProductImgList(coverPicDragLinearView);
        if (coverPathList.size() > 0) {
            allNeedUploadGoodsDetailViewList.add(new GoodsDetailView(-1, null, coverPicDragLinearView));
        }
        allNeedUploadGoodsDetailViewList.addAll(uploadGoodsDetailViewList);

        needUploadImgGoodsDetailViewQueue.clear();
        if (allNeedUploadGoodsDetailViewList.size() == 0) {   //都不需要上传
            showProgressDialog();
            requestPublishProduct();
        } else {  //需要上传
            showProgressDialogNoCancel();
            needUploadImgGoodsDetailViewQueue.addAll(allNeedUploadGoodsDetailViewList);
            requestUploadImgV2();
        }
    }

    /**
     *
     */
    class GoodsDetailView {
        int index;
        EditText goodsDescEditTxt;
        DragLinearView dragLinearView;
        /**
         * 上传成功的图片路径
         */
        List<String> successUploadImgList = new ArrayList<>();

        public GoodsDetailView(int index, EditText goodsDescEditTxt, DragLinearView dragLinearView) {
            this.index = index;
            this.goodsDescEditTxt = goodsDescEditTxt;
            this.dragLinearView = dragLinearView;
        }
    }

}
