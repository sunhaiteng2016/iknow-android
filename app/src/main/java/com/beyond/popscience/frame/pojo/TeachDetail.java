package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by yao.cui on 2017/7/20.
 */

public class TeachDetail extends BaseObject {
    private String title;
    private String publishTime;
    private List<TeachItem> contentList;
    private String coverPic;
    private String vedioUrl;
    private String description;

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

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getVedioUrl() {
        return vedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        this.vedioUrl = vedioUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
