package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.L;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.pojo.GoodsDescImgObj;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.view.DragLinearView;
import com.beyond.popscience.locationgoods.bean.Skubean;
import com.beyond.popscience.locationgoods.bean.SpecificationAttributeBean;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.locationgoods.shopcar.util.ToastUtil;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布商品
 */
public class RelaseShopGoodActivity extends BaseActivity {


    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.good_class)
    TextView goodClass;
    @BindView(R.id.specification_attribute)
    TextView specificationAttribute;
    @BindView(R.id.goodsDetailLinLay)
    protected LinearLayout goodsDetailLinLay;
    /**
     * 最多选择10张
     */
    private final int MAX_PHOTO_NUMBER = 1;

    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 101;
    @BindView(R.id.rlv_good_attr)
    RecyclerView rlvGoodAttr;
    @BindView(R.id.ed_good_title)
    EditText edGoodTitle;
    @BindView(R.id.ed_good_brand)
    EditText edGoodBrand;
    @BindView(R.id.ed_ptrs)
    EditText edPtrs;
    @BindView(R.id.ed_good_place)
    EditText edGoodPlace;
    @BindView(R.id.ed_good_delivery)
    EditText edGoodDelivery;
    @BindView(R.id.coverPicDragLinearView)
    DragLinearView coverPicDragLinearView;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.iv_check)
    ImageView ivCheck;

    /**
     * 当前点击的 DragLinearView
     */
    private DragLinearView currDragLinerView = null;
    private List<SpecificationAttributeBean> specificationAttributeBeansList = new ArrayList<>();

    /**
     * 需要上传图片的 GoodsDetailView
     */
    private ConcurrentLinkedQueue<GoodsDetailView> needUploadImgGoodsDetailViewQueue = new ConcurrentLinkedQueue<>();
    private List<String> goodAttrList = new ArrayList<>();
    private CommonAdapter<String> goodAttrAdapter;
    private String goodTitle;
    private String productClass;
    private String goodBrand;
    private String goodPlace;
    private String goodDelvery;
    private String deliveryStatus;

    List<HashMap<String, String>> Skumap = new ArrayList<>();
    List<Skubean> skuList = new ArrayList<>();

    @Request
    AddressRestUsage addressRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_relase_good_shop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back, R.id.good_class, R.id.specification_attribute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.good_class:
                //商品分类用户自定义
                break;
            case R.id.specification_attribute:
               //这个地方先去确定数据源，通过拿分类数据
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 10086) {
            //分类的回传
            String classfyss = data.getStringExtra("classfy");
            goodClass.setText(classfyss);
        }

        if (requestCode == Constant.REQUEST_CODE_PICK_IMAGE) {
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
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("发布商品");
        initImg();
        initCoverPicDragView();
        edGoodDelivery.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    /**
     * 封面图最多一张
     */
    private final int MAX_COVER_PHOTO_NUMBER = 1;

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
                    ImageLoaderUtil.displayImage(RelaseShopGoodActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

    }

    private List<GoodsDetailView> cacheGoodsDetailViewList = new ArrayList<>();

    private void initImg() {
        cacheGoodsDetailViewList.clear();
        goodsDetailLinLay.removeAllViews();
        for (int i = 0; i < 6; i++) {
            View view = View.inflate(this, R.layout.adapter_publish_goods_item, null);
            goodsDetailLinLay.addView(view);

            DragLinearView dragLinearView = (DragLinearView) view.findViewById(R.id.dragLinearView);
            EditText goodsDescEditTxt = (EditText) view.findViewById(R.id.goodsDescEditTxt);
            goodsDescEditTxt.setHint("详情描述" + (i + 1));

            GoodsDescImgObj goodsDescImgObj = null;
            initDragView(dragLinearView, goodsDescImgObj);

            cacheGoodsDetailViewList.add(new GoodsDetailView(i, goodsDescEditTxt, dragLinearView));
        }
        CreatLayoutUtils.creatLinearLayout(this, rlvGoodAttr);
        goodAttrAdapter = new CommonAdapter<String>(this, R.layout.item_good_attr, goodAttrList) {

            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                TextView tvAttr = (TextView) holder.getView(R.id.tv_attr);
                tvAttr.setText(Html.fromHtml(s));

                holder.setOnClickListener(R.id.iv_del, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //删除当前item
                        goodAttrList.remove(position);
                        skuList.remove(position);
                        goodAttrAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        rlvGoodAttr.setAdapter(goodAttrAdapter);
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
                    ImageLoaderUtil.displayImage(RelaseShopGoodActivity.this, url, imageView, options);
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
     * 选择图片
     */
    private void startSelectImageActivity(int maxNum) {
        if (maxNum <= 0) {
            return;
        }
        Intent intent1 = new Intent(RelaseShopGoodActivity.this, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    @OnClick({R.id.rb1, R.id.rb2, R.id.rb3})
    public void onViewClickedss(View view) {
        switch (view.getId()) {
            case R.id.rb1:
                //点击了自提
                edGoodDelivery.setEnabled(false);
                edGoodDelivery.setText("0");
                deliveryStatus = "1";
                break;
            case R.id.rb2:
                edGoodDelivery.setEnabled(true);
                deliveryStatus = "2";
                break;
            case R.id.rb3:
                edGoodDelivery.setEnabled(true);
                deliveryStatus = "3";
                break;
        }
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
            case 10088:
                dismissProgressDialog();
                if (msg.getIsSuccess()) {
                    com.beyond.library.util.ToastUtil.showCenter(RelaseShopGoodActivity.this, "发布成功");
                    finish();
                } else {
                    com.beyond.library.util.ToastUtil.showCenter(RelaseShopGoodActivity.this, msg.getMsg());
                }
                break;
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

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        //发布商品
        goodTitle = edGoodTitle.getText().toString().trim();
        productClass = goodClass.getText().toString().trim();
        goodBrand = edGoodBrand.getText().toString().trim();
        goodPlace = edGoodPlace.getText().toString().trim();
        goodDelvery = edGoodDelivery.getText().toString().trim();

        if (TextUtils.isEmpty(goodTitle)) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请输入标题");
            return;
        }
        if (TextUtils.isEmpty(productClass)) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请选择产品分类");
            return;
        }
        if (TextUtils.isEmpty(goodBrand)) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请输入品牌");
            return;
        }
        if (TextUtils.isEmpty(goodPlace)) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请输入产地");
            return;
        }
        if (TextUtils.isEmpty(goodDelvery)) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请输入配送费用");
            return;
        }
        if (coverPicDragLinearView.getItemCount() <= 0) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请添加封面");
            return;
        }
        GoodsDetailView firstGoodsDetailView = cacheGoodsDetailViewList.get(0);
        if (TextUtils.isEmpty(firstGoodsDetailView.goodsDescEditTxt.getText()) || firstGoodsDetailView.dragLinearView.getItemCount() <= 0) {
            com.beyond.library.util.ToastUtil.showCenter(this, "第一个详情描述必填哦");
            return;
        }
        if (TextUtils.isEmpty(firstGoodsDetailView.goodsDescEditTxt.getText().toString())) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请输入第一个的详情描述");
            return;
        }
        if (firstGoodsDetailView.dragLinearView.getItemCount() <= 0) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请添加第一个的详情描述图片");
            return;
        }
        //只要包含 拼团价那么就 要填写拼团价
        for (String strBean : goodAttrList) {
            if (strBean.contains("拼团价:")) {
                if (TextUtils.isEmpty(edPtrs.getText().toString().trim())) {
                    com.beyond.library.util.ToastUtil.showCenter(RelaseShopGoodActivity.this, "请输入拼团人数");
                    return;
                }

            }
        }
        if (skuList.size() == 0) {
            com.beyond.library.util.ToastUtil.showCenter(this, "请选择商品属性");
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
        }
        //所有需要上传的图片
        List<GoodsDetailView> allNeedUploadGoodsDetailViewList = new ArrayList<>();
        List<String> coverPathList = getUploadProductImgList(coverPicDragLinearView);
        if (coverPathList.size() > 0) {
            allNeedUploadGoodsDetailViewList.add(new GoodsDetailView(-1, null, coverPicDragLinearView));
        }
        allNeedUploadGoodsDetailViewList.addAll(uploadGoodsDetailViewList);

        needUploadImgGoodsDetailViewQueue.clear();

        showProgressDialogNoCancel();
        needUploadImgGoodsDetailViewQueue.addAll(allNeedUploadGoodsDetailViewList);
        requestUploadImgV2();
    }

    /**
     * 存放已上传的封面图片
     */
    private List<String> goodsCoverImgUrlList = new ArrayList<>();

    /**
     * 上传图片
     */
    private void requestUploadImgV2() {
        if (needUploadImgGoodsDetailViewQueue.isEmpty()) {
            //这边是真正的上传
            upData();
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
                            com.beyond.library.util.ToastUtil.showCenter(RelaseShopGoodActivity.this, "封面图上传失败");
                            dismissProgressDialog();
                        }
                    });
                } else {  //详情图
                    if (goodsDetailView.index == 0) { //详情1
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                com.beyond.library.util.ToastUtil.showCenter(RelaseShopGoodActivity.this, "详情图上传失败");
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

    private void upData() {
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
            goodsDescContentMap.put("text", goodsDetailView.goodsDescEditTxt.getText().toString());
            goodsDescContentMap.put("image", detailPic);
            goodsDescList.add(goodsDescContentMap);
        }

        JSONArray array = JSONArray.parseArray(JSON.toJSONString(goodsDescList));
        String goodsDescString = array.toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", goodTitle);
        //分类
        String[] prduct = productClass.split("/");
        map.put("categoryOne", prduct[0]);
        map.put("categoryTwo", prduct[1]);
        map.put("brand", goodBrand);
        map.put("expressFee", goodDelvery);
        map.put("place", goodPlace);
        map.put("deliveryStatus", deliveryStatus);
        map.put("pic", coverPic);
        map.put("lunboPics", coverPic);
        map.put("description", goodsDescString);
        map.put("subTitle", "副标题");
        map.put("groupSize", edPtrs.getText().toString().trim());

        //商品规格
        for (Skubean skubean : skuList) {
            HashMap<String, String> goodsDescContentMap = new HashMap<>();
            if (!(skubean.groupPrice.equals("")) && null != skubean.groupPrice) {
                goodsDescContentMap.put("groupPrice", skubean.groupPrice);
            }
            goodsDescContentMap.put("price", skubean.price);
            goodsDescContentMap.put("sp1", skubean.sp1.substring(0, skubean.sp1.length() - 1));
            goodsDescContentMap.put("stock", skubean.stock);
            Skumap.add(goodsDescContentMap);
        }
        map.put("skuList", Skumap);
        addressRestUsage.relaseProduct(10088, map);
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

    public boolean isCheckbd = false;

    @OnClick(R.id.rb_bdtc)
    public void onViewClickedss() {
        if (isCheckbd) {
            //取消本地特产
            ivCheck.setBackgroundResource(R.drawable.icon_oval_normal);
            isCheckbd = false;
        } else {
            isCheckbd = true;
            String goodClassFY = goodClass.getText().toString();
            String[] goodsss = goodClassFY.split("/");
            if (goodsss.length <= 1) {
                ToastUtil.makeText(this, "请先选择分类");
                return;
            }
            //是否为本地特产
            Intent intent = new Intent(RelaseShopGoodActivity.this, BdtcClassActivity.class);
            intent.putExtra("className", goodsss[1]);
            startActivityForResult(intent, 101);
        }
    }

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
