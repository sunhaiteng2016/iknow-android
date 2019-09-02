package com.beyond.popscience.frame.net;

import android.text.TextUtils;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.Carousel;
import com.beyond.popscience.frame.pojo.ClassifyResponse;
import com.beyond.popscience.frame.pojo.JobDetail;
import com.beyond.popscience.frame.pojo.JobRequest;
import com.beyond.popscience.frame.pojo.JobResponse;
import com.beyond.popscience.module.home.WelcomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by danxiang.feng on 2017/10/15.
 */

public class JobRestUsage extends AppBaseRestUsageV2 {

    private final String GET_CAROUSEL = "/job/getCarousel";//轮播图

    private final String GET_CLASSIFY = "/job/getClassify";//获取职业类别

    private final String GET_INDUSTRY = "/job/getIndustry";//获取行业

    private final String GET_JOB_PROVIDE_LIST = "/job/getJobList/%s";  //招聘列表(含搜索)

    private final String GET_JOB_APPLY_LIST = "/job/getApplyList/%s";  // 求职列表(含搜索)

    private final String JOB_PROVIDE_RELEASE = "/job/release";  //招聘发布

    private final String JOB_APPLY_RELEASE = "/job/applyRelease"; //求职发布

    private final String EDIT_JOB_PROVIDE = "/job/editJob/%s";  //修改招聘发布

    private final String EDIT_JOB_APPLY = "/job/editApplyJob/%s"; //修改求职发布

    private final String GET_JOB_PROVIDE_DETAIL = "/job/getJobDetail/%s";  // 招聘详情

    private final String GET_JOB_APPLY_DETAIL = "/job/getApplyDetail/%s"; //求职详情

    /**
     * 技能轮播
     *
     * @param taskId
     */
    public void getCarousel(int taskId) {
        getCache(GET_CAROUSEL, null, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 技能轮播
     *
     * @param taskId
     */
    public void getCarouselTwo(int taskId) {
        Map<String,String>  map  =new HashMap<>();
        map.put("address", WelcomeActivity.seletedAdress);
        post(GET_CAROUSEL, map, new NewCustomResponseHandler<ArrayList<Carousel>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
    /**
     * 获取职业类别
     * @param taskId
     */
    public void getClassify(int taskId){
        get(GET_CLASSIFY ,null, new NewCustomResponseHandler<ClassifyResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 获取行业
     * @param taskId
     */
    public void getIndustry(int taskId){
        get(GET_INDUSTRY, null ,new NewCustomResponseHandler<ClassifyResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 招聘列表(含搜索)
     * @param taskId
     * @param page
     * @param request
     */
    public void getJobProvideList(int taskId, int page, JobRequest request){
        String url = String.format(GET_JOB_PROVIDE_LIST, page);
        post(url, request, new NewCustomResponseHandler<JobResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 求职列表(含搜索)
     * @param taskId
     * @param page
     * @param request
     */
    public void getJobApplyList(int taskId,int page, JobRequest request){
        String url = String.format(GET_JOB_APPLY_LIST, page);
        post(url, request, new NewCustomResponseHandler<JobResponse>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 招聘详情
     * @param taskId
     * @param jobId
     */
    public void getJobProvideDetail(int taskId, String jobId){
        String url = String.format(GET_JOB_PROVIDE_DETAIL, jobId);
        get(url, null, new NewCustomResponseHandler<JobDetail>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 求职详情
     * @param taskId
     * @param applyId
     */
    public void getJobApplyDetail(int taskId, String applyId){
        String url = String.format(GET_JOB_APPLY_DETAIL, applyId);
        get(url, null, new NewCustomResponseHandler<JobDetail>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    /**
     * 招聘发布/编辑
     * @param taskId
     * @param jobId
     * @param request
     */
    public void publishJobProvide(int taskId, String jobId, JobDetail request){
        if(TextUtils.isEmpty(jobId)){
            post(JOB_PROVIDE_RELEASE, request, new NewCustomResponseHandler<JobDetail>(taskId) {
            }.setCallSuperRefreshUI(true));
        } else {
            String url = String.format(EDIT_JOB_PROVIDE, jobId);
            post(url, request, new NewCustomResponseHandler<String>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }


    /**
     * 求职发布/编辑
     * @param taskId
     * @param applyId
     * @param request
     */
    public void publishJobApply(int taskId, String applyId, JobDetail request){
        if(TextUtils.isEmpty(applyId)){
            post(JOB_APPLY_RELEASE, request, new NewCustomResponseHandler<JobDetail>(taskId) {
            }.setCallSuperRefreshUI(true));
        } else {
            String url = String.format(EDIT_JOB_APPLY, applyId);
            post(url, request, new NewCustomResponseHandler<String>(taskId) {
            }.setCallSuperRefreshUI(true));
        }
    }
}
