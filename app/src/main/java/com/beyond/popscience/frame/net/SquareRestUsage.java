package com.beyond.popscience.frame.net;

import android.text.TextUtils;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.AuthResult;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.frame.pojo.ClassifyResponse;
import com.beyond.popscience.frame.pojo.SkillRequest;
import com.beyond.popscience.frame.pojo.SkillResponse;
import com.beyond.popscience.module.home.WelcomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by danxiang.feng on 2017/10/11.
 */

public class SquareRestUsage extends AppBaseRestUsageV2 {

    private final String GET_CAROUSEL = "/skill/getCarousel?type=%s";//轮播图
    private final String GET_CAROUSELS = "/skill/getCarousel";//轮播图
    private final String GET_SKILL_LIST = "/skill/getSkillList/%s";  //技能列表

    private final String GET_TASK_LIST = "/skill/getTaskList/%s";  //任务列表(包括搜索)
    private final String GET_SKILL_CLASSIFY = "/skill/getClassify";   //分类列表

    private final String PUBLISH_SKILL = "/skill/release";  //发布技能

    private final String PUBLISH_TASK = "/skill/taskRelease";  //发布任务

    private final String EDIT_SKILL = "/skill/editSkill/%s";  //修改技能

    private final String EDIT_TASK = "/skill/editTask/%s";   //修改任务

    private final String GET_AUTH_STATUS = "/skill/getAuthStatus"; //获取认证状态
    /**
     * 技能详情
     */
    private final String GET_SKILL_DETAIL = "/skill/getskillDetail/%s";
    /**
     * 任务详情
     */
    private final String GET_TASK_DETAIL = "/skill/gettaskDetail/%s";
    /**
     * 个人认证
     */
    private final String PERSONAL_AUTH = "/skill/auth/personal";
    /**
     * 企业认证
     */
    private final String COMPANY_AUTH = "/skill/auth/company";

    /**
     * 轮播
     *
     * @param taskId
     * @param type
     */
    public void getSkillCarousel(int taskId, String type) {
        String url = String.format(GET_CAROUSEL, type);
        getCache(url, null, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    /**
     * 轮播
     *
     * @param taskId
     * @param type
     */
    public void getSkillCarouselTwo(int taskId, String type) {
        String url = String.format(GET_CAROUSELS, type);
        Map<String,String> map  = new HashMap<>();
        map.put("areaName", WelcomeActivity.seletedAdress);
        post(url, map, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    /**
     * 技能列表
     *
     * @param taskId
     * @param page
     * @param skillRequest
     */
    public void getSkillList(int taskId, int page, SkillRequest skillRequest) {
        String url = String.format(GET_SKILL_LIST, page);
        post(url, skillRequest, new NewCustomResponseHandler<SkillResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 任务列表
     *
     * @param taskId
     * @param page
     * @param skillRequest
     */
    public void getTaskList(int taskId, int page, SkillRequest skillRequest) {
        String url = String.format(GET_TASK_LIST, page);
        post(url, skillRequest, new NewCustomResponseHandler<SkillResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 分类列表
     *
     * @param taskId
     */
    public void getSkillClassify(int taskId) {
        get(GET_SKILL_CLASSIFY, null, new NewCustomResponseHandler<ClassifyResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 发布技能
     *
     * @param taskId
     * @param request
     */
    public void publishSkill(int taskId, String skillId, GoodsDetail request) {
        if (TextUtils.isEmpty(skillId)) {  //发布
            post(PUBLISH_SKILL, request, new NewCustomResponseHandler<GoodsDetail>(taskId) {
            }.setCallSuperRefreshUI(true));
        } else {
            String url = String.format(EDIT_SKILL, skillId);
            post(url, request, new NewCustomResponseHandler<String>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }

    /**
     * 发布任务
     *
     * @param taskId
     * @param request
     */
    public void publishTask(int taskId, String task_ID, GoodsDetail request) {
        if (TextUtils.isEmpty(task_ID)) {
            post(PUBLISH_TASK, request, new NewCustomResponseHandler<GoodsDetail>(taskId) {
            }.setCallSuperRefreshUI(true));
        } else {
            String url = String.format(EDIT_TASK, task_ID);
            post(url, request, new NewCustomResponseHandler<String>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }

    /**
     * 获取认证状态
     *
     * @param taskId
     */
    public void getAuthStatus(int taskId) {
        get(GET_AUTH_STATUS, null, new NewCustomResponseHandler<AuthResult>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 技能详情
     *
     * @param taskId
     */
    public void getSkillDetail(int taskId, String skillId) {
        String url = String.format(GET_SKILL_DETAIL, skillId != null ? skillId : "");
        get(url, null, new NewCustomResponseHandler<GoodsDetail>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 任务详情
     *
     * @param taskId
     */
    public void getTaskDetail(int taskId, String task_ID) {
        String url = String.format(GET_TASK_DETAIL, task_ID != null ? task_ID : "");
        get(url, null, new NewCustomResponseHandler<GoodsDetail>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 个人认证
     */
    public void authPersonal(int taskId, HashMap<String, String> paramMap) {
        post(PERSONAL_AUTH, paramMap, new NewCustomResponseHandler<String>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 企业认证
     */
    public void authCompany(int taskId, HashMap<String, String> paramMap) {
        post(COMPANY_AUTH, paramMap, new NewCustomResponseHandler<String>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

}
