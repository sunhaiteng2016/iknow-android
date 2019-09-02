package com.beyond.popscience.frame.net;

import android.text.TextUtils;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.BuildingDetail;
import com.beyond.popscience.frame.pojo.BuildingDetailtwo;
import com.beyond.popscience.frame.pojo.BuildingRequest;
import com.beyond.popscience.frame.pojo.BuildingResponse;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.ClassifyResponse;
import com.beyond.popscience.module.home.WelcomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by danxiang.feng on 2017/10/13.
 */

public class BuildingRestUsage extends AppBaseRestUsageV2 {

    private final String GET_CAROUSEL = "/building/getCarousel";  //出租出售 轮播图

    private final String GET_CLASSIFY = "/building/getClassify";   //获取房屋类别

    private final String GET_BUILDING_LIST = "/building/getBuildingList/%s";   //出租出售列表(含搜索)

    private final String GET_RENT_LIST = "/building/getRentList/%s";   //出求租求购列表(含搜索)

    private final String GET_BUILDING_DETAL = "/building/getBuildingDetail/%s";   //出租出售详情

    private final String GET_RENT_DETAIL = "/building/getRentDetail/%s";   //求租求购详情

    private final String BUILDING_RELEASE = "/building/release";   //出租出售发布

    private final String RENT_RELEASE = "/building/rentRelease";   //求租求购发布

    private final String EDIT_BUILDING = "/building/editBuilding/%s";   //修改出租出售

    private final String EDIT_RENT = "/building/editRent/%s";  //修改求租求售

    private final String GET_DECORATE = "/building/getDecorate";  //获取房屋装修

    private final String GET_CONFIG = "/building/getConfig";  //获取房屋配置

    /**
     * 轮播
     *
     * @param
     */
    public void getCarouselTwo(int taskId) {
        Map<String,String> map = new HashMap<>();
        map.put("address", WelcomeActivity.seletedAdress);
        post(GET_CAROUSEL, map, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    /**
     * 轮播
     *
     * @param taskId
     */
    public void getCarousel(int taskId) {
        getCache(GET_CAROUSEL, null, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    /**
     * 分类列表
     *
     * @param taskId
     */
    public void getClassify(int taskId) {
        get(GET_CLASSIFY, null, new NewCustomResponseHandler<ClassifyResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 出租出售列表(含搜索)
     *
     * @param taskId
     * @param page
     * @param request
     */
    public void getBuildingList(int taskId, int page, BuildingRequest request) {
        String url = String.format(GET_BUILDING_LIST, page);
        post(url, request, new NewCustomResponseHandler<BuildingResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 求租求购列表(含搜索)
     *
     * @param taskId
     * @param page
     * @param request
     */
    public void getRentList(int taskId, int page, BuildingRequest request) {
        String url = String.format(GET_RENT_LIST, page);
        post(url, request, new NewCustomResponseHandler<BuildingResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 出租出售详情
     *
     * @param taskId
     * @param buildingId
     */
    public void getBuildingDetail(int taskId, String buildingId) {
        String url = String.format(GET_BUILDING_DETAL, buildingId);
        get(url, null, new NewCustomResponseHandler<BuildingDetail>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 求租求购详情
     *
     * @param taskId
     * @param rentId
     */
    public void getRentDetail(int taskId, String rentId) {
        String url = String.format(GET_RENT_DETAIL, rentId);
        get(url, null, new NewCustomResponseHandler<BuildingDetail>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 出租出售发布/修改
     * @param taskId
     * @param request
     */
    public void publishBuilding(int taskId,String buildingId, BuildingDetail request){
        if(TextUtils.isEmpty(buildingId)){
            post(BUILDING_RELEASE, request, new NewCustomResponseHandler<BuildingDetail>(taskId) {
            }.setCallSuperRefreshUI(true));
        } else {
            String url = String.format(EDIT_BUILDING,buildingId);
            post(url, request, new NewCustomResponseHandler<String>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }
    /**
     * 出租出售发布/修改
     * @param taskId
     * @param request
     */
    public void publishRent(int taskId,String rentId, BuildingDetail request){
        if(TextUtils.isEmpty(rentId)){
            post(RENT_RELEASE, request, new NewCustomResponseHandler<BuildingDetail>(taskId) {
            }.setCallSuperRefreshUI(true));
        } else {
            String url = String.format(EDIT_RENT,rentId);
            post(url, request, new NewCustomResponseHandler<String>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }

    /**
     * 获取房屋装修
     */
    public void getDecorateClassify(int taskId){
        get(GET_DECORATE, null, new NewCustomResponseHandler<ClassifyResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取房屋配置
     * @param taskId
     */
    public void getConfigClassify(int taskId){
        get(GET_CONFIG, null, new NewCustomResponseHandler<ClassifyResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
}
