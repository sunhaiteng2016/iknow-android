package com.beyond.popscience.frame.pojo.point;

import com.beyond.popscience.frame.pojo.BaseObject;

public class IndexGoods extends BaseObject{
    private String id;
    private String commodityname;
    private float freight;
    private float money;
    private float integral;
    private String displaythepicture;
    private String userid;
    private String details;
    private String title;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommodityname() {
        return commodityname;
    }

    public void setCommodityname(String commodityname) {
        this.commodityname = commodityname;
    }

    public float getFreight() {
        return freight;
    }

    public void setFreight(float freight) {
        this.freight = freight;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getIntegral() {
        return integral;
    }

    public void setIntegral(float integral) {
        this.integral = integral;
    }

    public String getDisplaythepicture() {
        return displaythepicture;
    }

    public void setDisplaythepicture(String displaythepicture) {
        this.displaythepicture = displaythepicture;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
