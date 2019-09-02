package com.beyond.popscience.module.social;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提交审核
 * Created by yao.cui on 2017/7/17.
 */

public class PostAuditActivity extends BaseActivity {
    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 1211;
    private final int TASK_UPLOAD_AUDIT = 1212;//上传审核信息
    private static final String KEY_ID = "id";

    @BindView(R.id.etName)
    protected EditText etName;
    @BindView(R.id.etPhone)
    protected EditText etPhone;
    @BindView(R.id.etJob)
    protected EditText etJob;

    @BindView(R.id.ivDiploma)
    protected ImageView ivDiploma;
    @BindView(R.id.ivIdCard)
    protected ImageView ivIdCard;
    @BindView(R.id.ivIdCardNav)
    protected ImageView ivIdCardNav;
    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.tv_right)
    protected TextView tvConfirm;

    private ImageView mCurrentImg;
    @Request
    private SocialRestUsage restUsage;

    private String id;//社团id

    public static void startActivity(Context context,String id){
        Intent intent = new Intent(context,PostAuditActivity.class);
        intent.putExtra(KEY_ID,id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_post_audit;
    }

    @Override
    public void initUI() {
        super.initUI();
        id = getIntent().getStringExtra(KEY_ID);
        tvTitle.setText("填写申请信息");
        tvConfirm.setText("确定");
        tvConfirm.setVisibility(View.VISIBLE);

    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId){
            case HANDL_PHOTO_TASK_ID:   //处理图片
                if(msg.getIsSuccess()){
                    List<String> imgPathList = (List<String>) msg.getObj();
                    if(imgPathList.size()==0){
                        dismissProgressDialog();
                    }else{
                        requestUploadImg(mCurrentImg, imgPathList);
                    }
                }else{
                    dismissProgressDialog();
                }
                break;
            case TASK_UPLOAD_AUDIT:
                if(msg.getIsSuccess()){
                    ToastUtil.showCenter(this, "申请成功");
                    finish();
                }
                dismissProgressDialog();
                break;
        }
    }
    @OnClick({R.id.ivDiploma,R.id.ivIdCard,R.id.ivIdCardNav})
    public void selectImage(View view){
        mCurrentImg = (ImageView) view;
        startSelectImageActivity(1);
    }

    @OnClick(R.id.tv_right)
    public void uploadAudit(View view){
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(name)|| TextUtils.isEmpty(phone)){
            ToastUtil.showCenter(this,"请填写姓名和联系方式");
        } else if (ivDiploma.getTag()== null || ivIdCard.getTag()== null || ivIdCardNav.getTag()== null){
            ToastUtil.showCenter(this,"请上传图片");

        } else{//上传数据
            showProgressDialog();
            restUsage.postAudit(TASK_UPLOAD_AUDIT,id,name,phone,etJob.getText().toString(),
                    ivDiploma.getTag().toString(),ivIdCard.getTag().toString(),ivIdCardNav.getTag().toString());
        }
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

    /**
     * 选择图片
     */
    private void startSelectImageActivity(int maxNum){
        if(maxNum<=0){
            return ;
        }
        Intent intent1 = new Intent(PostAuditActivity.this, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 上传图片
     */
    private void requestUploadImg(ImageView target,List<String> pathList){
        ThirdSDKManager.getInstance().uploadImage(pathList, new ThirdSDKManager.UploadCallback(target) {

            @Override
            public void onSuccess(final Map<String, String> successMap, List<String> failureList, Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                L.v("上传成功=========> "+targetObj);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        if (successMap.values().isEmpty()){
                            ToastUtil.showCenter(PostAuditActivity.this, "详情图片上传失败");
                        } else {//上传成功
                            Set<String> keys = successMap.keySet();
                            for (String key:keys){//只获取第一个值
                                mCurrentImg.setImageBitmap(BitmapFactory.decodeFile(key));
                                mCurrentImg.setTag(successMap.get(key));
                                break;
                            }

                        }

                    }
                });

            }

            @Override
            public void onFailure(List<String> failureList, String errCode, String errMsg, Object targetObj) {
                super.onFailure(failureList, errCode, errMsg, targetObj);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        ToastUtil.showCenter(PostAuditActivity.this, "详情图片上传失败");
                    }
                });
            }

        });
    }

}
