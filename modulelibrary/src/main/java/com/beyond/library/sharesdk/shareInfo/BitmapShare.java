package com.beyond.library.sharesdk.shareInfo;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;

/**
 * Created by chris on 15/10/5.
 */
public class BitmapShare implements ICustomShareFields {

    public static final String NAME = BitmapShare.class.getSimpleName();
    private String bitmapPath = null;
    private MutiShareFields mutiFields = null;

    @Override
    public void setSelf(String platformName, BaseShareFields fields) {
        setShareInfo(platformName, fields);
    }

    @Override
    public HashMap<String, BaseShareFields> getMutiMap() {
        return mutiFields.getMutiMap();
    }

    public BitmapShare(List<Platform> platforms, Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmapPath = saveBitmapSdCard(bitmap);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        mutiFields = new MutiShareFields(this);
        mutiFields.initData(platforms);
    }

    /**
     * @param bitmap
     * @return
     */
    private String saveBitmapSdCard(Bitmap bitmap) {
        String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(sdCard, System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public void setShareInfo(String key, BaseShareFields fields) {
        fields.setImagePath(bitmapPath);
    }

}
