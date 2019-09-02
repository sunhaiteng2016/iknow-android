package com.beyond.popscience.frame.pojo;

/**
 * Created by danxiang.feng on 2017/10/13.
 */

public class BuildingRequest extends BaseObject {

    private String square;  //面积排序	0：小的在前 1:大的在前
    private String price;   //价格排序	0：小的在前 1:大的在前
    private String trade;    //更多按钮	1:出租 2：出售 没有则默认所有
    private String classify;  //房源类型	格式：一级，二级
    private String query;  //搜索词	默认无
    private String areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
