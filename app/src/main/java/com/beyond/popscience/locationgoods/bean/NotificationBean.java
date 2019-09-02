package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

public class NotificationBean extends BaseResponse {


    /**
     * id : 100013
     * userId : 285436
     * title : 社团公告：路桥电大是一个积极、阳光、充满正能量的集体，位于路北小学正对面，区政府西边，是为方便路桥区域求学的朋友而设立的，办学20多年以来，为地方政府培养一大批用得上、下得去、留得住的复合型高等专门人才，2013年升级为台州电大路桥学院，直属管理，除招生、管理、教学放在在路桥外，学费、师资等都和本部一样，成为路桥人民家门口的高等学府，极大地方便路桥学员就近提升，免除了奔波，使得学习工作两不误。路桥电大不仅在教学管理上科学、规范，更在校园文化建设上精彩纷呈，学生联合会活动更是特色，不仅“学知识 拿文凭 交朋
     * type : 3
     * typeId : 10
     * detail : 10
     * isRead : 0
     * createTime : 2019-05-24 09:41:49
     */

    private int id;
    private int userId;
    private String title;
    private int type;
    private String typeId;
    private String detail;
    private int isRead;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
