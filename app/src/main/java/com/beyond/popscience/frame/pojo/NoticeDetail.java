package com.beyond.popscience.frame.pojo;

/**
 * 圈子 公告详情
 *
 * Created by yao.cui on 2017/7/17.
 */

public class NoticeDetail extends BaseObject{

    /**
     * publishTime : 2019月05月30日
     * author : Starry
     * title : 爱自拍社团公告
     * myLike : 0
     * noticeId : 38
     * content : 请合理发言，友好交流。
     */

    private String publishTime;
    private String author;
    private String title;
    private String myLike;
    private String noticeId;
    private String content;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMyLike() {
        return myLike;
    }

    public void setMyLike(String myLike) {
        this.myLike = myLike;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
