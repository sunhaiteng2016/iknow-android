package com.beyond.popscience.module.social;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.pojo.ArticleInfo;
import com.beyond.popscience.frame.pojo.ImageInfo;
import com.beyond.popscience.frame.pojo.SocialInfo;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.module.mservice.task.HandlPhotoTask;
import com.beyond.popscience.module.social.adapter.ImageAdapter;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发布
 * Created by linjinfa on 2017/6/24.
 * email 331710168@qq.com
 */
public class PublishedActivity extends BaseActivity {

    /**
     * 发布
     */
    private final int REQUEST_PUBLISH_ARTICLE_TASK_ID = 1001;
    /**
     *
     */
    private final static String EXTRA_SOCIAL_INFO_KEY = "socialInfo";
    /**
     *
     */
    private final static String EXTRA_ARTICLE_INFO_KEY = "articleInfo";
    /**
     * 最多选择9张
     */
    private final int MAX_PHOTO_NUMBER = 9;

    /**
     * 处理图片
     */
    private final int HANDL_PHOTO_TASK_ID = 101;

    @BindView(R.id.tv_right)
    protected TextView rightTxtView;

    @BindView(R.id.leftTxtView)
    protected TextView leftTxtView;

    @BindView(R.id.ib_back)
    protected ImageButton backBtn;

    @BindView(R.id.contentEditTxt)
    protected EditText contentEditTxt;

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    /**
     *
     */
    private ImageAdapter imageAdapter;
    /**
     *
     */
    private SocialInfo socialInfo;
    /**
     *
     */
    private ArticleInfo articleInfo;
    /**
     * 上传成功的图片url
     */
    private List<String> uploadSuccessImgUrlList = new ArrayList<>();
    @Request
    private SocialRestUsage socialRestUsage;

    /**
     *
     */
    public static void startActivityForResult(Fragment fragment, int requestCode, SocialInfo socialInfo) {
        Intent intent = new Intent(fragment.getContext(), PublishedActivity.class);
        intent.putExtra(EXTRA_SOCIAL_INFO_KEY, socialInfo);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 修改帖子
     */
    public static void startActivityEditForResult(Activity activity, int requestCode, ArticleInfo articleInfo) {
        Intent intent = new Intent(activity, PublishedActivity.class);
        intent.putExtra(EXTRA_ARTICLE_INFO_KEY, articleInfo);
        activity.startActivityForResult(intent, requestCode);
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
    protected void onDestroy() {
        super.onDestroy();
        ThirdSDKManager.getInstance().stopUploadImage(PublishedActivity.this);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_published;
    }

    @Override
    public void initUI() {
        super.initUI();

        socialInfo = (SocialInfo) getIntent().getSerializableExtra(EXTRA_SOCIAL_INFO_KEY);
        articleInfo = (ArticleInfo) getIntent().getSerializableExtra(EXTRA_ARTICLE_INFO_KEY);
        if (socialInfo == null && articleInfo == null) {
            backNoAnim();
            return;
        }

        initRecyclerView();
        initEditView();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case HANDL_PHOTO_TASK_ID:   //处理图片
                if (msg.getIsSuccess()) {
                    List<String> imgPathList = (List<String>) msg.getObj();

                    addImg(imgPathList);
                }
                dismissProgressDialog();
                break;
            case REQUEST_PUBLISH_ARTICLE_TASK_ID:   //发布
                if (msg.getIsSuccess()) {
                    ToastUtil.showCenter(this, "发布成功");
                    ToastUtil.showCenter(this,"恭喜您, + 1 科普绿币!");
                    setResult(RESULT_OK);
                    finish();
                }
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 初始化编辑状态下的view
     */
    private void initEditView() {
        backBtn.setVisibility(View.GONE);
        leftTxtView.setVisibility(View.VISIBLE);
        leftTxtView.setText("取消");
        rightTxtView.setVisibility(View.VISIBLE);
        rightTxtView.setText("发布");

        if (articleInfo != null) {  //编辑
            contentEditTxt.setText(articleInfo.getContent());
            List<String> picList = articleInfo.getDetailPicList();
            addImg(picList);
        }
    }

    /**
     * 初始化 RecyclerView
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        imageAdapter = new ImageAdapter(this);
        recyclerView.setAdapter(imageAdapter);
    }

    /**
     * 添加产品图片
     */
    private void addImg(List<String> imgPathList) {
        if (imgPathList == null) {
            return;
        }

        for (String imgPath : imgPathList) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setImgUr(imgPath);
            imageAdapter.appendImage(imageInfo);
        }

        if (imageAdapter.getImageCount() >= MAX_PHOTO_NUMBER) {
            imageAdapter.delAddImgData();
        }

        imageAdapter.notifyDataSetChanged();
    }

    /**
     * 选择图片
     */
    public void startSelectImageActivity() {
        int maxNum = MAX_PHOTO_NUMBER - imageAdapter.getImageCount();
        if (maxNum <= 0) {
            return;
        }
        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra("IsNeedCamera", true);
        intent1.putExtra(Constant.MAX_NUMBER, maxNum);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 上传图片
     */
    private void requestUploadImg(List<String> pathList) {
        ThirdSDKManager.getInstance().uploadImage(this, pathList, new ThirdSDKManager.UploadCallback() {

            @Override
            public void onSuccess(Map<String, String> successMap, List<String> failureList, Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);

                uploadSuccessImgUrlList.clear();
                uploadSuccessImgUrlList.addAll(successMap.values());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestPublishArticle();
                    }
                });
            }

            @Override
            public void onFailure(List<String> failureList, String errCode, String errMsg, Object targetObj) {
                super.onFailure(failureList, errCode, errMsg, targetObj);
                uploadSuccessImgUrlList.clear();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        ToastUtil.showCenter(PublishedActivity.this, "图片上传失败");
                    }
                });
            }

        });
    }

    /**
     * 发布
     */
    private void requestPublishArticle() {
        List<String> imgUrlList = new ArrayList<>();
        imgUrlList.addAll(uploadSuccessImgUrlList);
        imgUrlList.addAll(imageAdapter.getImageUrlList());

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < imgUrlList.size(); i++) {
            sb.append(imgUrlList.get(i));
            if (i != imgUrlList.size() - 1) {
                sb.append(",");
            }
        }
        if (articleInfo != null) {  //修改
            socialRestUsage.editArticle(REQUEST_PUBLISH_ARTICLE_TASK_ID, articleInfo.getArticleId(), contentEditTxt.getText().toString(), sb.toString());
        } else {
            socialRestUsage.publishArticle(REQUEST_PUBLISH_ARTICLE_TASK_ID, socialInfo.getCommunityId(), contentEditTxt.getText().toString(), sb.toString());
        }
    }

    /**
     *
     */
    @OnClick(R.id.leftTxtView)
    @Override
    public void backClick(View view) {
        super.backClick(view);
    }

    /**
     * 发布
     *
     * @param view
     */
    @OnClick(R.id.tv_right)
    public void publishClick(View view) {
        List<String> imgPathList = imageAdapter.getImagePathList();
        if (TextUtils.isEmpty(contentEditTxt.getText().toString()) && imgPathList.size() <= 0) {
            ToastUtil.showCenter(this, "至少输入内容或上传一张图片");
            return;
        }

        showProgressDialog(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ThirdSDKManager.getInstance().stopUploadImage(PublishedActivity.this);
            }
        });
        if (imgPathList.size() <= 0) {
            requestPublishArticle();
        } else {
            requestUploadImg(imgPathList);
        }
    }

}
