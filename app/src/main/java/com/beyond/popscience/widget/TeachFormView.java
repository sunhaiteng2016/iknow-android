package com.beyond.popscience.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.util.L;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.TeachItem;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;
import com.beyond.popscience.module.social.PublishTeachActivity;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.VideoFile;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yao.cui on 2017/7/18.
 */

public class TeachFormView extends LinearLayout implements View.OnClickListener{

    private Context context;
    private ImageView ivVideo;
    private ImageView ivCover;
    private EditText etDes;
    private ImageView mCurrentImg;

    private ImageView ivDelVideo;
    private ImageView ivDelCover;
    private ImageView ivPlay;

    private boolean isCover = true;

    private FormListener listener;
    private TeachItem data;

    private CustomHandler handler;

    private String thumbPath;

    public TeachFormView(Context context) {
        super(context);
        init(context);
    }

    public TeachFormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TeachFormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public TeachFormView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.social_teach_form,this,true);
        etDes = (EditText) findViewById(R.id.etDes);
        ivVideo = (ImageView) findViewById(R.id.ivVideo);
        ivCover = (ImageView) findViewById(R.id.ivCover);
        ivDelCover = (ImageView) findViewById(R.id.ivDelCover);
        ivDelVideo = (ImageView) findViewById(R.id.ivDelVideo);
        ivPlay = (ImageView) findViewById(R.id.ivPlay);

        ivVideo.setOnClickListener(this);
        ivCover.setOnClickListener(this);
        ivDelVideo.setOnClickListener(this);
        ivDelCover.setOnClickListener(this);
        handler = new CustomHandler(context);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivCover:
                if (listener!= null){
                    listener.clickCover(this);
                }
                break;
            case R.id.ivVideo:
                if (listener != null){
                    listener.clickVideo(this);
                }
                break;
            case R.id.ivDelCover:
                ivCover.setTag(null);
                ivCover.setImageBitmap(null);
                ivDelCover.setVisibility(GONE);
                break;
            case R.id.ivDelVideo:
                ivVideo.setTag(null);
                ivVideo.setImageBitmap(null);
                ivVideo.setVisibility(GONE);
                ivPlay.setVisibility(GONE);
                this.thumbPath = "";
                break;
        }
    }

    /**
     * 设置 video
     * @param thumbPath
     * @param videoPath
     */
    public void setVideo(String thumbPath,String videoPath){
        isCover = false;
        mCurrentImg = ivVideo;
        this.thumbPath = thumbPath;
        requestUpload(ivVideo, Arrays.asList(videoPath));

        if (listener != null){
            listener.uploadStart();
        }
    }

    /**
     * 设置封面
     * @param imgPath
     */
    public void setCover(String imgPath){
        mCurrentImg = ivCover;

        isCover = true;
        requestUpload(ivCover,Arrays.asList(imgPath));
        if (listener != null){
            listener.uploadStart();
        }
    }

    public void setOnClickListener(FormListener listener){
        this.listener = listener;
    }

    /**
     * 上传图片/视频
     */
    private void requestUpload(ImageView target,List<String> pathList){
        ThirdSDKManager.getInstance().uploadImage(pathList, new ThirdSDKManager.UploadCallback(target) {

            @Override
            public void onSuccess(final Map<String, String> successMap, List<String> failureList, Object targetObj) {
                super.onSuccess(successMap, failureList, targetObj);
                Message msg = Message.obtain();

                if (successMap.values().isEmpty()){
                    msg.what = 1;
                } else {//上传成功
                    Set<String> keys = successMap.keySet();
                    for (String key:keys){//只获取第一个值
                        msg.what = 2;
                        Bundle data = new Bundle();
                        data.putString("httpPath",successMap.get(key));
                        data.putString("localPath",key);
                        msg.setData(data);

                        break;
                    }

                }

                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(List<String> failureList, final String errCode, final String errMsg, Object targetObj) {
                super.onFailure(failureList, errCode, errMsg, targetObj);
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }

        });
    }

    /**
     * 设置hint
     * @param hint
     */
    public void setHint(String hint){
        etDes.setHint(hint);
    }


    /**
     * 封面 视频点击事件
     */
    public interface FormListener{
        /**
         * 点击上传封面
         */
        void clickCover(TeachFormView target);

        /**
         * 点击上传视频
         */
        void clickVideo(TeachFormView target);

        /**
         * 上传开始
         */
        void uploadStart();

        /**
         * 上传结束
         */
        void uploadFinish();


    }

    /**
     * 验证内容 必填 或者全都不填
     * @return
     */
    public String verifyData(){
        String result = "";
        if (TextUtils.isEmpty(etDes.getText())&& ivCover.getTag()==null && ivVideo.getTag()==null){
            result = "";
        } else if (!TextUtils.isEmpty(etDes.getText())&& ivCover != null && ivVideo != null){
            result = "";
        } else {
            result = "请填写每组完整视频信息";
        }
        return result;
    }

    /**
     * 获取数据
     * @return
     */
    public TeachItem getData(){
        if (ivCover.getTag() == null || ivVideo.getTag() == null || TextUtils.isEmpty(etDes.getText())){
            return null;
        } else {
            if (data == null){
                data = new TeachItem();
            }

            data.setCoverPic(ivCover.getTag()==null?"":ivCover.getTag().toString());
            data.setVedioUrl(ivVideo.getTag()==null?"":ivVideo.getTag().toString());
            data.setDescription(etDes.getText().toString());
            return data;
        }

    }

    class CustomHandler extends Handler{
        private WeakReference<Context> contextWeakReference;

        public CustomHandler(Context context){
            contextWeakReference = new WeakReference<Context>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (contextWeakReference==null|| contextWeakReference.get()==null) return;

            if (listener != null){
                listener.uploadFinish();
            }
            if (msg.what == 1){
                ToastUtil.showCenter(contextWeakReference.get(), "上传失败");
            } else if (msg.what == 2){
                ToastUtil.showCenter(contextWeakReference.get(), "上传成功");
                Bundle data = msg.getData();
                if (data!= null){
                    if (isCover){
                        mCurrentImg.setImageBitmap(BitmapFactory.decodeFile(data.get("localPath").toString()));
                        ivDelCover.setVisibility(VISIBLE);
                    } else {
                        ivDelVideo.setVisibility(VISIBLE);
                        ivVideo.setImageBitmap(BitmapFactory.decodeFile(thumbPath));
                        ivPlay.setVisibility(VISIBLE);
                    }
                    mCurrentImg.setTag(data.get("httpPath").toString());
                }

            }
        }
    }


}
