package com.beyond.popscience.frame.pojo;

/**
 * Created by yao.cui on 2017/7/22.
 */

public class PushMsg extends BaseObject {
    private String title;
    private String auchor;
    private String publishTime;
    private int type;//1:新闻 2：乡镇新闻 3：乡镇公告
    private String typeId;//对应新闻ID/乡镇新闻ID/公告ID

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuchor() {
        return auchor;
    }

    public void setAuchor(String auchor) {
        this.auchor = auchor;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * 判断是否是首页新闻
     * @return
     */
    public boolean isNewsHomePage(){
        return this.type == 1;
    }

    /**
     * 判断是否是乡镇新闻
     * @return
     */
    public boolean isNewsTown(){
        return this.type == 2;
    }

    /**
     * 判断是否是乡镇公告
     * @return
     */
    public boolean isNoticeTown(){
        return  this.type == 3;
    }

    /**
     * 添加好友
     * @return
     */
    public boolean isFriends(){
        return  this.type == 4;
    }
}
