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
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.beyond.popscience.frame.view.SelectCultureDialog;
import com.beyond.popscience.module.mservice.adapter.IconListAdapter;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.beyond.popscience.widget.wheelview.WheelMenuInfo;
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
 * 个人认证
 * Created by linjinfa on 2017/10/13.
 * email 331710168@qq.com
 */
public class PersonalCertificationActivity extends BaseActivity {

    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 101;
    /**
     *
     */
    private final int REQUEST_AUTH_PERSONAL_TASK_ID = 201;

    /**
     *
     */
    private final IconInfo[] ICONINFOS = {
            new IconInfo("IDcardZ", null, "*上传身份证正面"),
            new IconInfo("IDcardF", null, "*上传身份证反面"),
            new IconInfo("IDcardZHold", null, "*上传手持正面身份证"),
            new IconInfo("photo", null, "上传头像"),
            new IconInfo("message", null, "上传职(毕)业证信息"),
    };
    @BindView(R.id.ed_yq_code)
    EditText edYqCode;

    /**
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PersonalCertificationActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.tv_title)
    protected TextView titleView;

    @BindView(R.id.tv_right)
    protected TextView rightTxtView;

    @BindView(R.id.culturalLevelTxtView)
    protected TextView culturalLevelTxtView;

    @BindView(R.id.nameEditTxt)
    protected EditText nameEditTxt;

    @BindView(R.id.idEditTxt)
    protected EditText idEditTxt;

    @BindView(R.id.introEditTxt)
    protected EditText introEditTxt;

    @BindView(R.id.gridView)
    protected GridViewNoScroll gridView;

    private SelectCultureDialog selectCareerDialog;
    /**
     *
     */
    private IconListAdapter iconListAdapter;
    /**
     * 上传队列
     */
    private ConcurrentLinkedQueue<IconInfo> uploadIconQueue = new ConcurrentLinkedQueue<>();

    @Request
    private SquareRestUsage squareRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_personal_certification;
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
    public void initUI() {
        super.initUI();
        titleView.setText("个人认证");
        rightTxtView.setText("确定");
        rightTxtView.setVisibility(View.VISIBLE);
        initGridView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
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
                dismissProgressDialog();
                break;
            case REQUEST_AUTH_PERSONAL_TASK_ID: //个人认证
                if (msg.getIsSuccess()) {
                    finish();
                }
                dismissProgressDialog();
                break;
        }
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
        String userName = nameEditTxt.getText().toString();
        String idNo = idEditTxt.getText().toString();
        String education = selectCareerDialog.getSelectedMenu().getCode();
        String intro = introEditTxt.getText().toString();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", userName);
        paramMap.put("IDNo", idNo);
        paramMap.put("education", education);
        paramMap.put("selfIntroduction", intro);
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
        squareRestUsage.authPersonal(REQUEST_AUTH_PERSONAL_TASK_ID, paramMap);
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
            public void onSuccess(Map<String, String> successMap, List<String> failureList, Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                L.v("上传成功=========> " + targetObj);

                IconInfo iconInfo = (IconInfo) targetObj;
                if (successMap.values().size() != 0) {
                    iconInfo.setIconUrl(new ArrayList<String>(successMap.values()).get(0));
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
                final IconInfo iconInfo = (IconInfo) targetObj;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showCenter(PersonalCertificationActivity.this, iconInfo.getName() + "上传失败");
                        dismissProgressDialog();
                    }
                });
            }
        });
    }

    /**
     * 显示 showSelectCareerDialog
     */
    private void showSelectCulturalLevelDialog() {
        if (selectCareerDialog == null) {
            selectCareerDialog = new SelectCultureDialog(this, SelectCultureDialog.TYPE_DEFAULT);
            selectCareerDialog.setSelectedMenu(UserInfoUtil.getInstance().getUserInfo().getEducation());
            selectCareerDialog.setOnSelectClickListener(new SelectCultureDialog.OnSelectClickListener() {
                @Override
                public void onOk(WheelMenuInfo wheelMenuInfo) {
                    if (wheelMenuInfo != null) {
                        culturalLevelTxtView.setText(wheelMenuInfo.getName());
                    }
                }
            });
        }
        selectCareerDialog.show();
    }

    /**
     * 选择文化程度
     */
    @OnClick(R.id.culturalLevelTxtView)
    public void showCulturalLevel() {
        showSelectCulturalLevelDialog();
    }

    /**
     *
     */
    @OnClick(R.id.tv_right)
    public void okClick() {
        if (TextUtils.isEmpty(nameEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(idEditTxt.getText().toString())) {
            ToastUtil.showCenter(this, "请输入身份证号");
            return;
        }
        if (selectCareerDialog.getSelectedMenu() == null) {
            ToastUtil.showCenter(this, "请选择文化程度");
            return;
        }
        for (IconInfo iconInfo : ICONINFOS) {
            if (!TextUtils.isEmpty(iconInfo.getIconUrl())) {
                if (!iconInfo.getIconUrl().startsWith(VKConstants.HTTP_PROTOCOL_PREFIX) && !iconInfo.getIconUrl().startsWith(VKConstants.HTTPS_PROTOCOL_PREFIX)) {
                    uploadIconQueue.add(iconInfo);
                }
            } else {
                if ("IDcardZ".equals(iconInfo.getFieldName())) {
                    ToastUtil.showCenter(this, "请上传" + iconInfo.getName());
                    return;
                }
            }
        }
        showProgressDialogNoCancel();
        requestUploadImgV2();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
