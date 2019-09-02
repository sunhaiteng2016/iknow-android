package com.beyond.popscience.module.mservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.library.view.GridViewNoScroll;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.pojo.IconInfo;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.module.mservice.adapter.IconListAdapter;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class CompanyCertificationActivity extends BaseActivity {

    private final int REQ_AUTH_ID = 1201;

    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 201;

    private final IconInfo[] ICONINFOS = {
            new IconInfo("licenceZ", null, "营业执照(正)"),
            new IconInfo("permitZ", null, "资格许可证(正)"),
    };

    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.tv_right)
    protected TextView tv_right;
    @BindView(R.id.companyNameView)
    protected EditText companyNameView;
    @BindView(R.id.companyBossNameView)
    protected EditText companyBossNameView;
    @BindView(R.id.companyCodeView)
    protected EditText companyCodeView;
    @BindView(R.id.companyAddressView)
    protected EditText companyAddressView;
    @BindView(R.id.companyDetailView)
    protected EditText companyDetailView;
    @BindView(R.id.companyPhoneView)
    protected EditText companyPhoneView;
    @BindView(R.id.gridView)
    protected GridViewNoScroll gridView;
    @BindView(R.id.ed_yq_code)
    EditText edYqCode;

    private IconListAdapter iconListAdapter;
    /**
     * 上传队列
     */
    private ConcurrentLinkedQueue<IconInfo> uploadIconQueue = new ConcurrentLinkedQueue<>();

    @Request
    private SquareRestUsage squareRestUsage;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CompanyCertificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_company_auth_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        tv_title.setText("企业认证");
        tv_right.setText("确定");
        tv_right.setVisibility(View.VISIBLE);
        initGridView();
    }

    /**
     * 初始化GridView
     */
    private void initGridView() {
        iconListAdapter = new IconListAdapter(this);
        iconListAdapter.getDataList().addAll(new ArrayList<IconInfo>(Arrays.asList(ICONINFOS)));
        gridView.setAdapter(iconListAdapter);
    }

    private void requestAuth() {
        String userName = companyNameView.getText().toString();
        String corporate = companyBossNameView.getText().toString();
        String socialCode = companyCodeView.getText().toString();
        String address = companyAddressView.getText().toString();
        String introduce = companyDetailView.getText().toString();
        String mobile = companyPhoneView.getText().toString();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", userName);
        paramMap.put("corporate", corporate);
        paramMap.put("socialCode", socialCode);
        paramMap.put("address", address);
        paramMap.put("introduce", introduce);
        paramMap.put("mobile", mobile);
        if (!TextUtils.isEmpty(edYqCode.getText().toString().trim())) {
            paramMap.put("yqCode", edYqCode.getText().toString().trim());
        }else{
            paramMap.put("yqCode", "0");
        }
        for (IconInfo iconInfo : ICONINFOS) {
            if (!TextUtils.isEmpty(iconInfo.getIconUrl())) {
                paramMap.put(iconInfo.getFieldName(), iconInfo.getIconUrl());
            }
        }
        squareRestUsage.authCompany(REQ_AUTH_ID, paramMap);
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case HANDL_PHOTO_TASK_ID:   //处理图片
                if (msg.getIsSuccess()) {
                    List<String> imgPathList = (List<String>) msg.getObj();
                    if (imgPathList != null && imgPathList.size() != 0) {
                        if (iconListAdapter.getSelectedIconInfo() != null && !TextUtils.isEmpty(imgPathList.get(0))) {
                            iconListAdapter.getSelectedIconInfo().setIconUrl(VKConstants.FILE_PROTOCOL_PREFIX + imgPathList.get(0).replaceFirst("/", ""));
                            iconListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case REQ_AUTH_ID:
                if (msg.getIsSuccess()) {
                    finish();
                }
                break;
        }
    }

    @OnClick(R.id.tv_right)
    void tv_right() {
        if (TextUtils.isEmpty(companyNameView.getText().toString())) {
            ToastUtil.showCenter(this, "请填写企业名称");
            return;
        }
        if (TextUtils.isEmpty(companyBossNameView.getText().toString())) {
            ToastUtil.showCenter(this, "请填写法人代表");
            return;
        }
        if (TextUtils.isEmpty(companyCodeView.getText().toString())) {
            ToastUtil.showCenter(this, "请填写社会统一代码号");
            return;
        }
        if (TextUtils.isEmpty(companyAddressView.getText().toString())) {
            ToastUtil.showCenter(this, "请填写营业地址");
            return;
        }
        if (TextUtils.isEmpty(companyDetailView.getText().toString())) {
            ToastUtil.showCenter(this, "请填写企业详细介绍");
            return;
        }
        if (TextUtils.isEmpty(companyPhoneView.getText().toString())) {
            ToastUtil.showCenter(this, "请填写联系电话");
            return;
        }

        for (IconInfo iconInfo : ICONINFOS) {
            if (!TextUtils.isEmpty(iconInfo.getIconUrl())) {
                if (!iconInfo.getIconUrl().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) && !iconInfo.getIconUrl().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)) {
                    uploadIconQueue.add(iconInfo);
                }
            } else {
                if ("licenceZ".equals(iconInfo.getFieldName())) {
                    ToastUtil.showCenter(this, "请上传" + iconInfo.getName());
                    return;
                }
            }
        }
        showProgressDialogNoCancel();
        requestUploadImgV2();
    }

    /**
     * 上传图片
     */
    private void requestUploadImgV2() {
        if (uploadIconQueue.isEmpty()) {
            requestAuth();
            return;
        }
        IconInfo iconInfo = uploadIconQueue.poll();
        List<String> pathList = Arrays.asList(iconInfo.getIconUrl().replace("file://", ""));
        ThirdSDKManager.getInstance().uploadImage(pathList, new ThirdSDKManager.UploadCallback(iconInfo) {

            @Override
            public void onSuccess(final Map<String, String> successMap, List<String> failureList, final Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                L.v("上传成功=========> " + targetObj);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        IconInfo iconInfo = (IconInfo) targetObj;
                        if (successMap.values().size() != 0) {
                            iconInfo.setIconUrl(new ArrayList<String>(successMap.values()).get(0));
                        }
                        requestUploadImgV2();
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
                        IconInfo iconInfo = (IconInfo) targetObj;
                        ToastUtil.showCenter(CompanyCertificationActivity.this, iconInfo.getName() + "上传失败");
                        dismissProgressDialog();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_PICK_IMAGE:  //
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
