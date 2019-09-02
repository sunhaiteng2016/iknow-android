package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.GoodsDatesActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.view.DragLinearView;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.locationgoods.view.StarBar;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品评价
 */
public class GoodsEvaluateActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_fch)
    TextView tvFch;
    @BindView(R.id.ll11)
    LinearLayout ll11;
    @BindView(R.id.ll33)
    LinearLayout ll33;
    @BindView(R.id.ed_edit)
    EditText edEdit;
    @BindView(R.id.coverPicDragLinearView)
    DragLinearView coverPicDragLinearView;
    @BindView(R.id.startBar)
    StarBar startBar;
    private DragLinearView currDragLinerView;
    private List<String> imgPathListss;
    private List<String> upList = new ArrayList<>();
    private float curMark;
    private String edPingjia;
    private String data;
    private String orderSn;
    private String img;


    @Request
    AddressRestUsage addressRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_goods_evaluate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_back, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.submit:
                edPingjia = edEdit.getText().toString();
                requestUploadImgV2();
                break;
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        data = getIntent().getStringExtra("data");
        if (null != data) {
            String[] splits = data.split("&");
            orderSn = splits[0];
            img = splits[1];
            Glide.with(this).load(img).into(ivImg);
        }
        initSel();
        startBar.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(float mark) {
                curMark = mark;
                if (mark < 1.0) {
                    tvFch.setText("非常差");
                }
                if (mark >= 1.0 && mark < 2.0) {
                    tvFch.setText("差");
                }
                if (mark >= 2.0 && mark < 3.0) {
                    tvFch.setText("一般");
                }
                if (mark >= 3.0 && mark < 4.0) {
                    tvFch.setText("好");
                }
                if (mark >= 4.0 && mark <= 5.0) {
                    tvFch.setText("非常好");
                }
            }
        });
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
            case 1008611:
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "评价成功！");
                    finish();
                } else {
                    ToastUtil.showCenter(this, msg.getMsg());
                }
                break;
        }
    }

    /**
     * 上传图片
     */
    private void requestUploadImgV2() {
        ThirdSDKManager.getInstance().uploadImage(imgPathListss, new ThirdSDKManager.UploadCallback(upList) {

            @Override
            public void onSuccess(Map<String, String> successMap, List<String> failureList, Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : successMap.entrySet()) {
                    sb.append(entry.getValue() + ",");
                }

                //上传数据
                HashMap<String, Object> map = new HashMap<>();
                map.put("detail", edPingjia);
                map.put("imglist", sb.toString());
                map.put("orderNo", orderSn);
                map.put("score", (int) curMark);
                addressRestUsage.evaluation(1008611, map);
            }

            @Override
            public void onFailure(List<String> failureList, String errCode, String errMsg, Object targetObj) {
                super.onFailure(failureList, errCode, errMsg, targetObj);
                L.v("上传失败=========>" + targetObj);

            }
        });
    }

    /**
     * 添加产品图片
     */
    private void addProductImg(List<String> imgPathList) {
        if (imgPathList == null) {
            return;
        }
        imgPathListss = imgPathList;
        LinkedList<DragLinearView.ImageTagElement> imageTagElementList = new LinkedList<DragLinearView.ImageTagElement>();
        for (String imagePath : imgPathList) {
            imageTagElementList.add(new DragLinearView.ImageTagElement(null, imagePath));
        }

        if (currDragLinerView != null) {
            currDragLinerView.addMutilItemView(imageTagElementList);
        }
    }

    /**
     * 封面图最多一张
     */
    private final int MAX_COVER_PHOTO_NUMBER = 9;

    private void initSel() {
        coverPicDragLinearView.setAddImageRes(R.drawable.wdspk_icon_2_3);
        coverPicDragLinearView.setMaxRows(9);
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
                    ImageLoaderUtil.displayImage(GoodsEvaluateActivity.this, url, imageView, options);
                }
            }

            @Override
            public void onItemClick(View itemView, Object tag) {

            }
        });

    }

    /**
     * 选择图片
     */
    private void startSelectImageActivity(int maxNum) {
        if (maxNum <= 0) {
            return;
        }
        Intent intent1 = new Intent(GoodsEvaluateActivity.this, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


}
