package com.beyond.popscience.frame.pojo;

import android.text.TextUtils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by danxiang.feng on 2017/10/13.
 */

public class BuildingDetail extends GoodsDetail {

    public String uid; //	房源ID
    public String buildingId;


    public String rentId;
    public String config;
    public String decorate;
    public String sellType;   //1：中介 2：房东
    public String size;
    public String houseType;

    public String startPrice;
    public String endPrice;
    public String startArea;
    public String endArea;
    public String locationRequire;  //位置要求
    public String hourseTypeRequire;  //户型要求
    public String trade;   //交易方式	1:求租 2:求购
    //public String authorityType;//认证方式

    @Override
    public String toString() {
        return "BuildingDetail{" +
                "uid='" + uid + '\'' +
                ", buildingId='" + buildingId + '\'' +
                ", rentId='" + rentId + '\'' +
                ", config='" + config + '\'' +
                ", decorate='" + decorate + '\'' +
                ", sellType='" + sellType + '\'' +
                ", size='" + size + '\'' +
                ", houseType='" + houseType + '\'' +
                ", startPrice='" + startPrice + '\'' +
                ", endPrice='" + endPrice + '\'' +
                ", startArea='" + startArea + '\'' +
                ", endArea='" + endArea + '\'' +
                ", locationRequire='" + locationRequire + '\'' +
                ", hourseTypeRequire='" + hourseTypeRequire + '\'' +
                ", trade='" + trade + '\'' +
                '}';
    }

    /**
     * 获取配置
     *
     * @return
     */
    public List<String> getConfigList() {
        if (TextUtils.isEmpty(config)) {
            return null;
        }
        String configs[] = config.split(SEPERATE);
        if (configs != null && configs.length > 0) {
            return new ArrayList<String>(Arrays.asList(configs));
        }
        return null;
    }

    public int getSellTypeInt() {
        if (sellType == null || "".equals(sellType)) {
            return 0;
        }
        try {
            return Integer.parseInt(sellType);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTradeInt() {
        if (trade == null || "".equals(trade)) {
            return 0;
        }
        try {
            return Integer.parseInt(trade);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
