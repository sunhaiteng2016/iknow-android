package com.beyond.popscience.module.mservice.task;

import android.graphics.Bitmap;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.task.ITask;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理拍照图片Task
 * Created by linjinfa 331710168@qq.com on 2015/1/30.
 */
public class HandlPhotoTask extends ITask {

    /**
     *
     */
    private List<String> imgPathList;
    /**
     * 最大上传大小
     */
    private long maxSize = 150*1024;
    /**
     * 是否强制压缩 一半
     */
    private boolean isForceCompreHalf = true;

    public HandlPhotoTask(int taskId, List<String> imgPathList) {
        super(taskId);
        this.imgPathList = imgPathList;
    }

    public HandlPhotoTask(int taskId, List<String> imgPathList, long maxSize) {
        super(taskId);
        this.imgPathList = imgPathList;
        this.maxSize = maxSize;
        this.isForceCompreHalf = false;
    }

    @Override
    public MSG doTask() {
        List<String> resultImgPath = new ArrayList<String>();
        for(String imgPath : imgPathList){
            File imgFile = new File(imgPath);
            if(!imgFile.exists()){  //不存在
                continue;
            }

            if(!ImageLoader.getInstance().isInited()){
                ImageLoaderUtil.init(context);
            }
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(false).considerExifParams(true).build();
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync("file://"+imgPath ,options);
            if(bitmap!=null){
                long imgSize = imgFile.length();
                String thumpImagePath;
                if(imgSize<=maxSize){   //如果原图大小 小于150kb 不做压缩
                    thumpImagePath = imgFile.getAbsolutePath();
                }else{
                    thumpImagePath = ImageUtils.compressBlurryThumpImageToSizeK(imgSize, isForceCompreHalf ? (imgSize / 2) : maxSize, bitmap);
                }
                bitmap.recycle();
                resultImgPath.add(thumpImagePath);
            }
        }
        return new MSG(true,resultImgPath);
    }

}
