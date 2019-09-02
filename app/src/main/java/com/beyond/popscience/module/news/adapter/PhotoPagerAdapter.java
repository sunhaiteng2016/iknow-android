package com.beyond.popscience.module.news.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;

import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.frame.base.CustomPagerAdapter;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.frame.util.FileUtil;
import com.beyond.popscience.frame.view.photoview.PhotoView;
import com.beyond.popscience.frame.view.photoview.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by linjinfa 331710168@qq.com on 2015/1/29.
 */
public class PhotoPagerAdapter extends CustomPagerAdapter<String> {

    private String currImgUrl;

    private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which==0){   //保存到相册
                ImageLoader.getInstance().loadImage(currImgUrl,new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {}
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        saveToalbum(null);
                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        saveToalbum(loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            }else{
                dialog.dismiss();
            }
        }
    };

    public PhotoPagerAdapter(Context context) {
        super(context);
    }

    /**
     * 保存到相册
     */
    private void saveToalbum(Bitmap bitmap){
        if(bitmap==null){
            ToastUtil.showCenter(context, "保存到相册失败");
            return ;
        }
        try {
            String filePath = saveBitmapToCamera(bitmap);
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, System.currentTimeMillis()+".jpg", "");
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(filePath));
            intent.setData(uri);
            context.sendBroadcast(intent);
            ToastUtil.showCenter(context, "保存到相册成功");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showCenter(context, "保存到相册失败");
        }
    }

    /**
     * 保存图片到系统相册
     * @param bitmap
     * @return
     */
    private static String saveBitmapToCamera(Bitmap bitmap) {
        final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String SYSTEM_CAMERA_IMG = SDCARD+"/DCIM/Camera";
        File file_will = new File(FileUtil.getDirectory(SYSTEM_CAMERA_IMG), System.currentTimeMillis() + ".jpg");
        if(!file_will.exists()){
            try {
                file_will.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file_will);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error){
            error.printStackTrace();
        }
        return file_will.getAbsolutePath();
    }


    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = viewMap.get(position);
        if(view==null){
            view = new PhotoView(context);
            PhotoView photoView = (PhotoView) view;
            final String imgUrl = dataList.get(position);
            imageLoader.displayImage(imgUrl, photoView, getDisplayImageOptions());
            viewMap.put(position, photoView);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    currImgUrl = imgUrl;
                    D.show(context, "", new String[]{"保存到相册", "取消"}, onClickListener);
                    return false;
                }
            });
            ((PhotoView) view).setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if(context!=null && context instanceof ViewTapClick){
                        ((ViewTapClick)context).itemTapClick();
                    }
                }
            });
        }
        container.addView(view);
        return view;
    }

    public interface ViewTapClick{
        public void itemTapClick();
    }
}
