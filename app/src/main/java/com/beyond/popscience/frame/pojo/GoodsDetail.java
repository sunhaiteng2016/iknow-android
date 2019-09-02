package com.beyond.popscience.frame.pojo;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商品详情
 * Created by yao.cui on 2017/6/29.
 */

public class GoodsDetail extends BaseObject {
    protected final String SEPERATE=",";

    public String productId;
    public String createTime;
    public String startTime;
    public String address;
    public String title;
    public String detailPics;//detailPics 最后一张图为商品详情图，前面几张为轮播图，
    public String userId;
    public String realName;
    public String mobile;
    public String avatar;
    public String price;
    public String introduce;
    public String coverPic;
    public String classfyId;
    public String classfyName;
    public String classifyName;  //技能、任务处的分类
    public String skillId;
    public String taskId;
    public Long commentCount;

    public String areaName ;




    /**
     * 	1：个人 2：企业
     */
    public String authorityType;


    public String lat;   //纬度 必须	9,6 小数点后6位
    public String lon;   //经度 必须	8,6 小数点后6位
    /**
     *
     */
    public List<GoodsDescImgObj> goods;
    /**
     * 1:技能 2:任务 3:出租出售 4求租求购 5商品
     */
    public String appGoodsType;

    public String[] getValues(){
        if (detailPics== null) detailPics="";
        return this.detailPics.split(SEPERATE);
    }

    /**
     * 获取详情图片
     * @return
     */
    public List<String> getDetailPicList(){
        if(TextUtils.isEmpty(detailPics)){
            return null;
        }
        String imgs[] = detailPics.split(SEPERATE);
        if(imgs!=null && imgs.length>0){
            return new ArrayList<String>(Arrays.asList(imgs));
        }
        return null;
    }

    /**
     * 获取最后一个
     * @return
     */
    public String getLast(){
        String[] values = getValues();
        int length = values.length;
        if (length == 0) return "";
        return values[length-1];
    }

    public int getAuthTypeInt(){
        if(authorityType ==null || "".equals(authorityType)){
            return 0;
        }
        try {
            return Integer.parseInt(authorityType);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "GoodsDetail{" +
                "SEPERATE='" + SEPERATE + '\'' +
                ", productId='" + productId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", address='" + address + '\'' +
                ", title='" + title + '\'' +
                ", detailPics='" + detailPics + '\'' +
                ", userId='" + userId + '\'' +
                ", realName='" + realName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatar='" + avatar + '\'' +
                ", price='" + price + '\'' +
                ", introduce='" + introduce + '\'' +
                ", coverPic='" + coverPic + '\'' +
                ", classfyId='" + classfyId + '\'' +
                ", classfyName='" + classfyName + '\'' +
                ", classifyName='" + classifyName + '\'' +
                ", skillId='" + skillId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", commentCount=" + commentCount +
                ", areaName='" + areaName + '\'' +
                ", authorityType='" + authorityType + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", goods=" + goods +
                ", appGoodsType='" + appGoodsType + '\'' +
                '}';
    }
}
