package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * 教学详情
 * Created by yao.cui on 2017/7/17.
 */

public class NewsDetail extends BaseObject {
    private String title;
    private String publishTime;
    private List<TeachItem> contentList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public List<TeachItem> getContentList() {
        return contentList;
    }

    public void setContentList(List<TeachItem> contentList) {
        this.contentList = contentList;
    }
}
