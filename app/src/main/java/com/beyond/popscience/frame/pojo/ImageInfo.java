package com.beyond.popscience.frame.pojo;

/**
 * Created by linjinfa on 2017/7/21.
 * email 331710168@qq.com
 */
public class ImageInfo extends BaseObject {

    private String imgUr;
    private int resId;

    public ImageInfo() {
    }

    public ImageInfo(int resId) {
        this.resId = resId;
    }

    public String getImgUr() {
        return imgUr;
    }

    public void setImgUr(String imgUr) {
        this.imgUr = imgUr;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
