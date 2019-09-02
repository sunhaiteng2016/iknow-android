package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.pojo.ServiceGoodsCategory;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.view.DragLinearView;
import com.beyond.popscience.frame.view.SelectDialog;
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
import butterknife.OnClick;

/**
 * 发布商品
 * Created by linjinfa on 2017/6/23.
 * email 331710168@qq.com
 */
public class PublishGoodsActivity_backup extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String KEY_GOODS_DETAIL = "goods_detail";
    /**
     *
     */
    private final String RMB_SYMBOL = "¥";

    /**
     * 最多选择10张
     */
    private final int MAX_PHOTO_NUMBER = 10;

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

    @BindView(R.id.tv_title)
    protected TextView titleTxtView;

    @BindView(R.id.goodsTitleEditTxt)
    protected EditText goodsTitleEditTxt;

    @BindView(R.id.goodsDescEditTxt)
    protected EditText goodsDescEditTxt;

    @BindView(R.id.userNameEditTxt)
    protected EditText userNameEditTxt;

    @BindView(R.id.addressEditTxt)
    protected EditText addressEditTxt;

    @BindView(R.id.phoneEditTxt)
    protected EditText phoneEditTxt;

    @BindView(R.id.priceEditTxt)
    protected EditText priceEditTxt;

    @BindView(R.id.coverPicDragLinearView)
    protected DragLinearView coverPicDragLinearView;

    @BindView(R.id.dragLinearView)
    protected DragLinearView dragLinearView;

    @BindView(R.id.classifyTxtView)
    protected TextView classifyTxtView;

    @BindView(R.id.publishedBtn)
    protected Button publishedBtn;

    @Request
    private ServiceRestUsage serviceRestUsage;

    /**
     * 选择分类
     */
    private SelectDialog selectClassifyDialog;

    /**
     * 存放已上传的封面图片
     */
    private List<String> goodsCoverImgUrlList = new ArrayList<>();

    /**
     * 存放已上传的详情图片
     */
    private List<String> goodsDescImgUrlList = new ArrayList<>();
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
     * @param context
     */
    public static void startActivity(Context context){
        Intent intent = new Intent(context, PublishGoodsActivity_backup.class);
        context.startActivity(intent);
    }

    /**
     *
     * @param fragment
     */
    public static void startActivity(Fragment fragment){
        Intent intent = new Intent(fragment.getContext(),PublishGoodsActivity_backup.class);
        fragment.startActivity(intent);
    }

    /**
     * 编辑商品
     * @param context
     * @param detail
     */
    public static void startActivityEdit(Context context, GoodsDetail detail){
        Intent intent = new Intent(context,PublishGoodsActivity_backup.class);
        intent.putExtra(KEY_GOODS_DETAIL, detail);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> imgList = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    if(imgList!=null && imgList.size()!=0){
                        showProgressDialog();
                        List<String> imgPathList = new ArrayList<>();
                        for(ImageFile imageFile : imgList){
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

//        ThirdSDKManager.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        destroyLocation();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_goods;
    }

    @Override
    public void initUI() {
        super.initUI();

        editGoodsDetail = (GoodsDetail) getIntent().getSerializableExtra(KEY_GOODS_DETAIL);
        titleTxtView.setText("发布");

        initEditTxt();
        iniEditStatusView();
        initCoverPicDragView();
        initDragView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case HANDL_PHOTO_TASK_ID:   //处理图片
                if(msg.getIsSuccess()){
                    List<String> imgPathList = (List<String>) msg.getObj();

                    addProductImg(imgPathList);
                }
                dismissProgressDialog();
                break;
            case REQUST_PRODUCT_CLASSFY_TASK_ID:    //商品分类
                if(msg.getIsSuccess()){
                    List<ServiceGoodsCategory> serviceGoodsCategoryList = (List<ServiceGoodsCategory>) msg.getObj();
                    if(serviceGoodsCategoryList!=null && serviceGoodsCategoryList.size()!=0){
                        initSelectClassifyDialog(serviceGoodsCategoryList);
                        showSelectClassifyDialog();
                    }
                }
                dismissProgressDialog();
                break;
            case REQUST_PUBLISH_PRODUCT_TASK_ID:    //发布商品
                if(msg.getIsSuccess()){
                    if(editGoodsDetail!=null){  //修改商品
                        ToastUtil.showCenter(this, "修改成功");
                    }else{
                        ToastUtil.showCenter(this, "发布成功");
                        if(msg.getObj() instanceof GoodsDetail){
                            GoodsDetail goodsDetail = (GoodsDetail) msg.getObj();
                            if(!TextUtils.isEmpty(goodsDetail.productId)){
                                GoodsDetailActivity.startActivity(this, goodsDetail.productId);
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
     * 开始定位
     */
    private void startLocation(){
        ThirdSDKManager.getInstance().startLocation(this, new ThirdSDKManager.ILocationListener() {
            @Override
            public void onSuccess(ThirdSDKManager.Location location) {
                destroyLocation();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onFailure(String errCode, String errInfo, String errDetail) {
                destroyLocation();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showCenter(PublishGoodsActivity_backup.this, "定位失败");
                    }
                });
            }
        });
    }

    /**
     * 销毁定位相关
     */
    private void destroyLocation(){
        ThirdSDKManager.getInstance().destroyLocation();
    }

    /**
     * 初始化相关 EditText
     */
    private void initEditTxt(){
        priceEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!priceEditTxt.getText().toString().startsWith(RMB_SYMBOL)){
                    if(priceEditTxt.getText().toString().contains(RMB_SYMBOL)){
                        int selectionIndex = priceEditTxt.getSelectionStart();
                        priceEditTxt.setText(RMB_SYMBOL+priceEditTxt.getText().toString().replaceAll(RMB_SYMBOL, ""));
                        if(selectionIndex == 0){
                            priceEditTxt.setSelection(priceEditTxt.getText().length());
                        }
                    }else{
                        priceEditTxt.setText(RMB_SYMBOL+priceEditTxt.getText().toString());
                        priceEditTxt.setSelection(priceEditTxt.getText().length());
                    }
                }
            }
        });
    }

    /**
     * 初始化编辑状态下的view
     */
    private void iniEditStatusView(){
        if(editGoodsDetail!=null){
            publishedBtn.setText("更改信息");

            selectedClassfyId = editGoodsDetail.classfyId;

            userNameEditTxt.setText(editGoodsDetail.realName);
            goodsTitleEditTxt.setText(editGoodsDetail.title);
            goodsDescEditTxt.setText(editGoodsDetail.introduce);
            addressEditTxt.setText(editGoodsDetail.address);
            phoneEditTxt.setText(editGoodsDetail.mobile);
            priceEditTxt.setText(editGoodsDetail.price);

            classifyTxtView.setText(editGoodsDetail.classfyName);
        }
    }

    /**
     * 初始化 封面 DragLinearView
     */
    private void initCoverPicDragView(){
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
                if(itemCount == MAX_COVER_PHOTO_NUMBER){
                    coverPicDragLinearView.setShowAddImg(false);
                }else{
                    coverPicDragLinearView.setShowAddImg(true);
                }
            }
        });
        coverPicDragLinearView.setOnItemViewListener(new DragLinearView.OnItemViewListener() {
            @Override
            public void onAddItem(ImageView imageView, Object tag) {
                if(tag!=null){
                    String url;
                    DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    if(tag.toString().toLowerCase().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || tag.toString().toLowerCase().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)){
                        builder.cacheOnDisk(true);
                        url = tag.toString();
                    }else{
                        url = VKConstants.FILE_PROTOCOL_PREFIX+(tag.toString().startsWith("/")?(tag.toString().replaceFirst("/","")):tag.toString());
                    }
                    DisplayImageOptions options = builder.displayer(new FadeInBitmapDisplayer(200, true, true, false)).showImageOnLoading(R.drawable.common_transparent).showImageForEmptyUri(R.drawable.common_transparent).build();
                    ImageLoaderUtil.displayImage(PublishGoodsActivity_backup.this, url, imageView,options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if(editGoodsDetail!=null){
            if(!TextUtils.isEmpty(editGoodsDetail.coverPic)){
                coverPicDragLinearView.addMutilItemView(new LinkedList<DragLinearView.ImageTagElement>(Arrays.asList(new DragLinearView.ImageTagElement(null, editGoodsDetail.coverPic))), false);
            }
        }

//        String tests[] = {
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg"
//        };
//        LinkedList<DragLinearView.ImageTagElement> imageTagElementList = new LinkedList<DragLinearView.ImageTagElement>();
//        for(String imagePath : tests){
//            imageTagElementList.add(new DragLinearView.ImageTagElement(null, imagePath));
//        }
//        dragLinearView.addMutilItemView(imageTagElementList, false);
    }

    /**
     * 初始化 DragLinearView
     */
    private void initDragView(){
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
                if(itemCount == MAX_PHOTO_NUMBER){
                    dragLinearView.setShowAddImg(false);
                }else{
                    dragLinearView.setShowAddImg(true);
                }
            }
        });
        dragLinearView.setOnItemViewListener(new DragLinearView.OnItemViewListener() {
            @Override
            public void onAddItem(ImageView imageView, Object tag) {
                if(tag!=null){
                    String url;
                    DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
                    if(tag.toString().toLowerCase().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || tag.toString().toLowerCase().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)){
                        builder.cacheOnDisk(true);
                        url = tag.toString();
                    }else{
                        url = VKConstants.FILE_PROTOCOL_PREFIX+(tag.toString().startsWith("/")?(tag.toString().replaceFirst("/","")):tag.toString());
                    }
                    DisplayImageOptions options = builder.displayer(new FadeInBitmapDisplayer(200, true, true, false)).showImageOnLoading(R.drawable.common_transparent).showImageForEmptyUri(R.drawable.common_transparent).build();
                    ImageLoaderUtil.displayImage(PublishGoodsActivity_backup.this, url, imageView,options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

        if(editGoodsDetail!=null){
            List<String> goodsDescImgList = editGoodsDetail.getDetailPicList();
            if(goodsDescImgList!=null && goodsDescImgList.size()>0){
                LinkedList<DragLinearView.ImageTagElement> imageTagElementList = new LinkedList<DragLinearView.ImageTagElement>();
                for(String imagePath : goodsDescImgList){
                    imageTagElementList.add(new DragLinearView.ImageTagElement(null, imagePath));
                }
                dragLinearView.addMutilItemView(imageTagElementList, false);
            }
        }

//        String tests[] = {
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202848605&di=53ff51cb7bfac60595f52ec3534a56f7&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160309%2F9-16030Z92137.jpg",
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498202894397&di=d30aa219a4325fdc5870eaeee14f0902&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201504%2F2015041508.jpg"
//        };
//        LinkedList<DragLinearView.ImageTagElement> imageTagElementList = new LinkedList<DragLinearView.ImageTagElement>();
//        for(String imagePath : tests){
//            imageTagElementList.add(new DragLinearView.ImageTagElement(null, imagePath));
//        }
//        dragLinearView.addMutilItemView(imageTagElementList, false);
    }

    /**
     * 添加产品图片
     */
    private void addProductImg(List<String> imgPathList){
        if(imgPathList == null){
            return ;
        }
        LinkedList<DragLinearView.ImageTagElement> imageTagElementList = new LinkedList<DragLinearView.ImageTagElement>();
        for(String imagePath : imgPathList){
            imageTagElementList.add(new DragLinearView.ImageTagElement(null, imagePath));
        }

        if(currDragLinerView!=null){
            currDragLinerView.addMutilItemView(imageTagElementList);
        }
    }

    /**
     * 选择图片
     */
    private void startSelectImageActivity(int maxNum){
        if(maxNum<=0){
            return ;
        }
        Intent intent1 = new Intent(PublishGoodsActivity_backup.this, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 根据DragLinearView获取要上传的图片路径
     * @param dragLinearView
     * @return
     */
    private List<String> getUploadProductImgList(DragLinearView dragLinearView){
        List<View> viewList = dragLinearView.getItemViewList();
        List<String> pathList = new ArrayList<String>();
        for(View view : viewList){
            if(view.getTag()!=null && !TextUtils.isEmpty(view.getTag().toString()) && !view.getTag().toString().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) && !view.getTag().toString().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)){
                pathList.add(view.getTag().toString());
            }
        }
        return pathList;
    }

    /**
     * 根据DragLinearView获取要Http的图片
     * @param dragLinearView
     * @return
     */
    private List<String> getHttpProductImgList(DragLinearView dragLinearView){
        List<View> viewList = dragLinearView.getItemViewList();
        List<String> pathList = new ArrayList<String>();
        for(View view : viewList){
            if(view.getTag()!=null && !TextUtils.isEmpty(view.getTag().toString()) && (view.getTag().toString().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) || view.getTag().toString().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX))){
                pathList.add(view.getTag().toString());
            }
        }
        return pathList;
    }

    /**
     * 初始化分类Dialog
     */
    private void initSelectClassifyDialog(List<ServiceGoodsCategory> serviceGoodsCategoryList){
        if(selectClassifyDialog == null){
            selectClassifyDialog = new SelectDialog(this);

            List<SelectDialog.MenuInfo> menuInfoList = new ArrayList<>();

            for(ServiceGoodsCategory serviceGoodsCategory : serviceGoodsCategoryList){
                menuInfoList.add(new SelectDialog.MenuInfo(serviceGoodsCategory.getClassfyId(), serviceGoodsCategory.getClassfyName(), serviceGoodsCategory));
            }

            selectClassifyDialog.addAllMenu(menuInfoList);

            selectClassifyDialog.setOnSelectClickListener(new SelectDialog.OnSelectClickListener() {
                @Override
                public void onOk(SelectDialog.MenuInfo menuInfo) {
                    if(menuInfo!=null){
                        selectedClassfyId = menuInfo.getMenuCode();
                        classifyTxtView.setText(menuInfo.getMenuName());
                    }
                }
            });
        }
    }

    /**
     * 选择分类
     */
    private void showSelectClassifyDialog(){
        if(selectClassifyDialog!=null){
            //设置选中的默认值
            selectClassifyDialog.setSelectMenuCode(selectedClassfyId);
            selectClassifyDialog.show();
        }
    }

    /**
     * 请求商品分类
     */
    private void requestGoodsCategory(){
        showProgressDialog();
        serviceRestUsage.getProductClassfy(REQUST_PRODUCT_CLASSFY_TASK_ID);
    }

    /**
     * 请求上传商品
     */
    private void requestPublishProduct(){
        if(!isUploadCoverSuccess || !isUploadDetailSuccess){
            dismissProgressDialog();
            return ;
        }
        String goodsTitle = goodsTitleEditTxt.getText().toString();
        String introduce = goodsDescEditTxt.getText().toString();
        String address = addressEditTxt.getText().toString();
        String phone = phoneEditTxt.getText().toString();
        //封面图片
        String coverPic = null;
        List<String> coverImgPathList = getHttpProductImgList(coverPicDragLinearView);
        coverImgPathList.addAll(goodsCoverImgUrlList);
        if(coverImgPathList!=null && coverImgPathList.size()!=0){
            coverPic = coverImgPathList.get(0);
        }
        //详情图片
        String detailPic = "";
        List<String> detailImgPathList = getHttpProductImgList(dragLinearView);
        detailImgPathList.addAll(goodsDescImgUrlList);
        for(int i=0;i<detailImgPathList.size();i++){
            detailPic +=detailImgPathList.get(i);
            if(i!=detailImgPathList.size()-1){
                detailPic +=",";
            }
        }

        //分类
        String classfyId = null;
        String classfyName = null;
        if(selectClassifyDialog!=null && selectClassifyDialog.getSelectedMenuInfo()!=null){
            SelectDialog.MenuInfo menuInfo = selectClassifyDialog.getSelectedMenuInfo();
            classfyId = menuInfo.getMenuCode();
            classfyName = menuInfo.getMenuName();
        }
        if(classfyId == null && editGoodsDetail!=null){
            classfyId = editGoodsDetail.classfyId;
            classfyName = editGoodsDetail.classfyName;
        }

        String userName = userNameEditTxt.getText().toString();
        String price = getPriceTxt();

        //是否修改商品
        String productId = editGoodsDetail != null ? editGoodsDetail.productId : null;

//        serviceRestUsage.publishProduct(REQUST_PUBLISH_PRODUCT_TASK_ID,
//                                        productId,
//                                        goodsTitle,
//                                        introduce,
//                                        address,
//                                        phone,
//                                        coverPic,
//                                        detailPic,
//                                        classfyId,
//                                        classfyName,
//                                        userName,
//                                        price);
    }

    /**
     * 上传图片
     */
    private void requestUploadImg(DragLinearView dragLinearView, List<String> pathList){
        ThirdSDKManager.getInstance().uploadImage(pathList, new ThirdSDKManager.UploadCallback(dragLinearView) {

            @Override
            public void onSuccess(Map<String, String> successMap, List<String> failureList, Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                L.v("上传成功=========> "+targetObj);
//                for(Map.Entry<String, String> entry : successMap.entrySet()){
//                    L.v(entry.getKey()+" =============> "+entry.getValue());
//                }

                if(targetObj == coverPicDragLinearView){    //封面图
                    isUploadCoverSuccess = true;
                    goodsCoverImgUrlList.addAll(new ArrayList<String>(successMap.values()));
                }else{  //详情图
                    isUploadDetailSuccess = true;
                    goodsDescImgUrlList.addAll(new ArrayList<String>(successMap.values()));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestPublishProduct();
                    }
                });
            }

            @Override
            public void onFailure(List<String> failureList, String errCode, String errMsg, Object targetObj) {
                super.onFailure(failureList, errCode, errMsg, targetObj);
                L.v("上传失败=========>"+targetObj);
                if(targetObj == coverPicDragLinearView){    //封面图
                    isUploadCoverSuccess = false;
                }else{  //详情图
                    isUploadDetailSuccess = false;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showCenter(PublishGoodsActivity_backup.this, "详情图片上传失败");
                        requestPublishProduct();
                    }
                });
            }

        });
    }

    /**
     * 获取商品定价
     * @return
     */
    private String getPriceTxt(){
        return priceEditTxt.getText().toString().replaceAll(RMB_SYMBOL, "");
    }

    /**
     * 选择分类
     * @param view
     */
    @OnClick(R.id.classifyLinLay)
    public void selectClassifyClick(View view){
        if(selectClassifyDialog!=null){
            showSelectClassifyDialog();
        }else{
            requestGoodsCategory();
        }
    }

    /**
     * 发布
     * @param view
     */
    @OnClick(R.id.publishedBtn)
    public void publishedClick(View view){
        if(TextUtils.isEmpty(goodsTitleEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "请输入商品标题");
            return ;
        }

        if(TextUtils.isEmpty(goodsDescEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "请输入商品描述");
            return ;
        }

        if(coverPicDragLinearView.getItemCount()<=0){
            ToastUtil.showCenter(this, "请添加封面");
            return ;
        }

        if(dragLinearView.getItemCount()<=0){
            ToastUtil.showCenter(this, "请添加图片");
            return ;
        }

        if(dragLinearView.getItemCount()<2){
            ToastUtil.showCenter(this, "图片至少2张哦！");
            return ;
        }

        if(TextUtils.isEmpty(addressEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "请输入地址");
            return ;
        }

        if(TextUtils.isEmpty(userNameEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "请输入姓名");
            return ;
        }

        if(TextUtils.isEmpty(phoneEditTxt.getText().toString())){
            ToastUtil.showCenter(this, "请输入联系方式");
            return ;
        }

        if(TextUtils.isEmpty(getPriceTxt())){
            ToastUtil.showCenter(this, "请输入产品定价");
            return ;
        }

        if(editGoodsDetail == null){  //发布商品
            if(selectClassifyDialog == null || selectClassifyDialog.getSelectedMenuInfo() == null){
                ToastUtil.showCenter(this, "请选择分类");
                return ;
            }
        }

        List<String> coverPathList = getUploadProductImgList(coverPicDragLinearView);
        List<String> detailPathList = getUploadProductImgList(dragLinearView);
        if(detailPathList.size() == 0 && coverPathList.size() == 0){   //不需要上传 直接提交
            isUploadCoverSuccess = true;
            isUploadDetailSuccess = true;
            showProgressDialog();
            requestPublishProduct();
        }else{  //上传图片
            showProgressDialogNoCancel();
            goodsCoverImgUrlList.clear();
            goodsDescImgUrlList.clear();

            if(coverPathList.size() > 0){   //需要上传封面图
                isUploadCoverSuccess = false;
            }else{
                isUploadCoverSuccess = true;
            }

            if(detailPathList.size() > 0){   //需要上传详情图片
                isUploadDetailSuccess = false;
            }else{
                isUploadDetailSuccess = true;
            }

            if(coverPathList.size() > 0){   //需要上传封面图
                //上传封面图
                requestUploadImg(coverPicDragLinearView, coverPathList);
            }

            if(detailPathList.size() > 0){   //需要上传详情图片
                //上传详情图片
                requestUploadImg(dragLinearView, detailPathList);
            }
        }
    }

}
