package com.beyond.popscience.locationgoods.utils;

import com.beyond.popscience.locationgoods.bean.SpecificationAttributeBean;

import java.util.ArrayList;
import java.util.List;

public class ReleaseDateUtils {
    public static List<SpecificationAttributeBean> GPspecificationAttributeBeansList = new ArrayList<>();
    public static List<SpecificationAttributeBean> SCspecificationAttributeBeansList = new ArrayList<>();
    public static List<SpecificationAttributeBean> ShuiCspecificationAttributeBeansList = new ArrayList<>();
    public static List<SpecificationAttributeBean> RQDCspecificationAttributeBeansList = new ArrayList<>();
    public static List<SpecificationAttributeBean> LYspecificationAttributeBeansList = new ArrayList<>();
    public static List<SpecificationAttributeBean> CurspecificationAttributeBeansList = new ArrayList<>();
    public static List<SpecificationAttributeBean> ShoesAndClothingList = new ArrayList<>();

    public ReleaseDateUtils() {
        initDates();
    }
    private void initDates() {
        //果品
        GPspecificationAttributeBeansList.add(new SpecificationAttributeBean("果  径:", "xxxmm、xxx个/500克等", ""));
        GPspecificationAttributeBeansList.add(new SpecificationAttributeBean("包  装:", "如纸箱，泡沫等", ""));
        GPspecificationAttributeBeansList.add(new SpecificationAttributeBean("价  格:", "商品价格（只限数字）", ""));
        GPspecificationAttributeBeansList.add(new SpecificationAttributeBean("拼团价:", "选填，不填代表没有拼团价", ""));
        GPspecificationAttributeBeansList.add(new SpecificationAttributeBean("价格单位:", "即价格单位重量，如xxxg、xxxkg/箱", ""));
        GPspecificationAttributeBeansList.add(new SpecificationAttributeBean("库  存:", "多少个", ""));

        //蔬菜
        SCspecificationAttributeBeansList.add(new SpecificationAttributeBean("大  小:", "xxxmm、xxx个/500克等", ""));
        SCspecificationAttributeBeansList.add(new SpecificationAttributeBean("挑  选:", "精挑/粗挑", ""));
        SCspecificationAttributeBeansList.add(new SpecificationAttributeBean("清  洗:", "已清洗/未清洗", ""));
        SCspecificationAttributeBeansList.add(new SpecificationAttributeBean("包  装:", "如纸箱，泡沫等", ""));
        SCspecificationAttributeBeansList.add(new SpecificationAttributeBean("价  格:", "商品价格（只限数字）", ""));
        SCspecificationAttributeBeansList.add(new SpecificationAttributeBean("拼团价:", "选填，不填代表没有拼团价", ""));
        SCspecificationAttributeBeansList.add(new SpecificationAttributeBean("价格单位:", "即价格单位重量，如xxxg、xxxkg/箱", ""));
        SCspecificationAttributeBeansList.add(new SpecificationAttributeBean("库  存:", "多少个", ""));
        //水产
        ShuiCspecificationAttributeBeansList.add(new SpecificationAttributeBean("大  小:", "xxxmm、xxx个/500克等", ""));
        ShuiCspecificationAttributeBeansList.add(new SpecificationAttributeBean("包  装:", "如纸箱，泡沫等", ""));
        ShuiCspecificationAttributeBeansList.add(new SpecificationAttributeBean("价  格:", "商品价格（只限数字）", ""));
        ShuiCspecificationAttributeBeansList.add(new SpecificationAttributeBean("拼团价:", "选填，不填代表没有拼团价", ""));
        ShuiCspecificationAttributeBeansList.add(new SpecificationAttributeBean("价格单位：", "即价格单位重量，如xxxg、xxxkg/箱", ""));
        ShuiCspecificationAttributeBeansList.add(new SpecificationAttributeBean("库  存:", "多少个", ""));

        //肉禽蛋
        RQDCspecificationAttributeBeansList.add(new SpecificationAttributeBean("大  小:", "xxxmm、xxx个/500克等", ""));
        RQDCspecificationAttributeBeansList.add(new SpecificationAttributeBean("包  装:", "如纸箱，泡沫等", ""));
        RQDCspecificationAttributeBeansList.add(new SpecificationAttributeBean("价  格:", "商品价格（只限数字）", ""));
        RQDCspecificationAttributeBeansList.add(new SpecificationAttributeBean("拼团价:", "选填，不填代表没有拼团价", ""));
        RQDCspecificationAttributeBeansList.add(new SpecificationAttributeBean("价格单位:", "即价格单位重量，如xxxg、xxxkg/箱", ""));
        RQDCspecificationAttributeBeansList.add(new SpecificationAttributeBean("库  存:", "多少个", ""));

        //粮油
        LYspecificationAttributeBeansList.add(new SpecificationAttributeBean("包  装:", "如纸箱，泡沫等", ""));
        LYspecificationAttributeBeansList.add(new SpecificationAttributeBean("价  格:", "商品价格（只限数字）", ""));
        LYspecificationAttributeBeansList.add(new SpecificationAttributeBean("拼团价:", "选填，不填代表没有拼团价", ""));
        LYspecificationAttributeBeansList.add(new SpecificationAttributeBean("价格单位:", "即价格单位重量，如xxxg、xxxkg/箱", ""));
        LYspecificationAttributeBeansList.add(new SpecificationAttributeBean("库  存:", "多少个", ""));

        //鞋服
        ShoesAndClothingList.add(new SpecificationAttributeBean("尺  寸:", "如xx，xxl", ""));
        ShoesAndClothingList.add(new SpecificationAttributeBean("颜  色:", "如白色...", ""));
        ShoesAndClothingList.add(new SpecificationAttributeBean("价  格:", "多少钱", ""));
        ShoesAndClothingList.add(new SpecificationAttributeBean("库  存:", "多少个", ""));
    }


}
