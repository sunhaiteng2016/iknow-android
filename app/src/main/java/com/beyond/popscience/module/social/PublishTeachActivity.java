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
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.PublishTeachResult;
import com.beyond.popscience.frame.pojo.TeachItem;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.beyond.popscience.widget.TeachFormView;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.VideoFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.vincent.filepicker.activity.VideoPickActivity.IS_NEED_CAMERA;

/**
 * 社团：发布教学
 *
 * Created by yao.cui on 2017/7/18.
 */

public class PublishTeachActivity extends BaseActivity implements TeachFormView.FormListener{
    private final int HANDL_PHOTO_TASK_ID = 1211;//压缩图片
    private static final int TASK_PUBLISH_TEACH = 1411;//发布教学
    private static final String KEY_ID = "id";

    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.tv_right)
    protected TextView tvConfirm;
    @BindView(R.id.etTitle)
    protected EditText etTitle;
    @BindView(R.id.form1)
    protected TeachFormView form1;
    @BindView(R.id.form2)
    protected TeachFormView form2;
    @BindView(R.id.form3)
    protected TeachFormView form3;

    private TeachFormView currentForm;
    @Request
    private SocialRestUsage restUsage;

    private String communityId;

    public static void startActivity(Context context,String id){
        Intent intent = new Intent(context,PublishTeachActivity.class);
        intent.putExtra(KEY_ID,id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_publish_teach;
    }

    @Override
    public void initUI() {
        super.initUI();
        communityId = getIntent().getStringExtra(KEY_ID);

        tvTitle.setText("发布教学");
        tvConfirm.setText("确定");
        tvConfirm.setVisibility(View.VISIBLE);
        form1.setOnClickListener(this);
        form2.setOnClickListener(this);
        form3.setOnClickListener(this);

        form2.setHint("在此输入第二段视频描述...");
        form3.setHint("在此输入第三段视频描述...");
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        if (taskId== HANDL_PHOTO_TASK_ID && msg.getIsSuccess()){
            List<String> imgPathList = (List<String>) msg.getObj();
            currentForm.setCover(imgPathList.get(0));
        } else if (taskId == TASK_PUBLISH_TEACH && msg.getIsSuccess()){

            PublishTeachResult result = (PublishTeachResult) msg.getObj();
            if (result!= null){
                ToastUtil.showCenter(this,"发布成功");
                TeachDetailActivity.startActivity(this,result.getTeachId());
                finish();
            }
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
            case Constant.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    ArrayList<VideoFile> videoList = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                    if(videoList!=null && videoList.size()!=0){
                        VideoFile file = videoList.get(0);
                        //上传视频问文件
                        currentForm.setVideo(file.getThumbnail(),file.getPath());
                    }

                }
                break;
        }
    }

    @OnClick(R.id.tv_right)
    public void confirm(View view){
        if (!TextUtils.isEmpty(form1.verifyData()) || !TextUtils.isEmpty(form1.verifyData())||
                !TextUtils.isEmpty(form1.verifyData())){
            ToastUtil.showCenter(this,"请填写完整信息");
        }

        List<TeachItem> data = new ArrayList<>();
        if (form1.getData() == null && form2.getData() == null&& form3.getData()==null){
            ToastUtil.showCenter(this,"请填写教学内容");
            return;
        }
        if (form1.getData() != null){
            data.add(form1.getData());
        }

        if (form2.getData() != null){
            data.add(form2.getData());
        }
        if (form3.getData() != null){
            data.add(form3.getData());
        }
        restUsage.publishTeach(TASK_PUBLISH_TEACH,communityId,etTitle.getText().toString(),
                data);
    }
    /**
     * 选择图片
     */
    private void selectImg(int maxNum){
        if(maxNum<=0){
            return ;
        }
        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 选择视频
     * @param maxNum
     */
    private void selectVedio(int maxNum){
        if (maxNum<=0) return;

        Intent intent2 = new Intent(this, VideoPickActivity.class);
        intent2.putExtra(IS_NEED_CAMERA, false);
        intent2.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);
    }


    @Override
    public void clickCover(TeachFormView target) {
        currentForm = target;
        selectImg(1);
    }

    @Override
    public void clickVideo(TeachFormView target) {
        currentForm = target;
        selectVedio(1);
    }

    @Override
    public void uploadStart() {
        showProgressDialog("正在上传");
    }

    @Override
    public void uploadFinish() {
        dismissProgressDialog();
    }
}
