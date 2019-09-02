package com.beyond.popscience.frame.pojo;

/**
 * Created by danxiang.feng on 2017/10/11.
 */

public class SkillRequest extends BaseObject {
    //技能列表排序搜索
    private String all;   //综合	0 :价格低的在前 1：价格高的在前
    private String distance; //距离排序	0：近的在前 1:远的在前
    private String classify;  //类别	类别名称 没有则取全部 格式：大类，小类
    private String type;   //筛选	1:个人认证 2：企业认证 没有则取全部
    private String query;  //搜索词	默认无

    private String lat;   //纬度 必须	9,6 小数点后6位
    private String lon;   //经度 必须	8,6 小数点后6位

    private String areaName ;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
