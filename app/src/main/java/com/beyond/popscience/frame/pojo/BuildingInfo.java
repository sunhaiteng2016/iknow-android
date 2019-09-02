package com.beyond.popscience.frame.pojo;

/**
 * Created by danxiang.feng on 2017/10/13.
 */

public class BuildingInfo extends ServiceGoodsItem {

    private String houseType;   //户型	例：2厅2室1卫 -> 2,2,2
    private String size;   //面积
    private String trade;   //交易方式	1：出租 2：出售

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }
}
