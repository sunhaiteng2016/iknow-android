package com.beyond.popscience.frame.pojo;

/**
 * Created by linjinfa on 2017/6/13.
 * email 331710168@qq.com
 */
public class NewsDetailContent extends BaseObject {

    private String contentIndex;
    /**
     * 0:图片1:文字 2:视频
     */
    private String contentType;
    private String contentNews;

    public String getContentIndex() {
        return contentIndex;
    }

    public void setContentIndex(String contentIndex) {
        this.contentIndex = contentIndex;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentNews() {
        return contentNews;
    }

    public void setContentNews(String contentNews) {
        this.contentNews = contentNews;
    }

    /**
     * 是否文字
     * @return
     */
    public boolean isTxt(){
        return "1".equals(contentType);
    }

}
