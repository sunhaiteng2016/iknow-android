package com.beyond.popscience.frame.pojo;

/**
 * 轮播图
 * Created by yao.cui on 2017/6/14.
 */

public class Carousel extends BaseObject {

    public String newsId;
    private String pic;
    private String title;
    public String productId;
    public String uid;
    public String link;
    /**
     * 1：新闻 2：外链
     */
    public String type;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
