package com.beyond.library.sharesdk;

import java.io.Serializable;

/**
 * Created by linjinfa on 2017/6/8.
 * email 331710168@qq.com
 */
public class WebViewShare implements Serializable {

    private String title = "";      // 分享的标题
    private String desc = "";       // 分享的内容
    private String link = "";       // 分享的链接
    private String imgUrl = "";     // icon的URL
    private String imagePath;
    private String newId;

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
