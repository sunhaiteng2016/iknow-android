package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.locationgoods.bean.RegisterNumBean;

import java.util.HashMap;

public class FromDataRestUsage extends AppBaseRestUsageV1 {
    private final String getStatusByArea = "/open/getStatusByArea";

    public void StatusByArea(int taskId, String areaname, String type) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("areaname", areaname);
        paramMap.put("type", type);
        post(getStatusByArea, paramMap, new NewCustomResponseHandler<String>(taskId) {
        });
    }

    private final String GET_REGISTER_NUMBER = "/user/yqDeatil";

    private final String yqGoods = "/user/yqGoods";
    private final String yqJob = "/user/yqJob";
    private final String yqBuild = "/user/yqBuild";
    private final String yqProduct = "/user/yqProduct";
    private final String yqSkill = "/user/yqSkill";


    public void getRegisterNumber(int taskId, HashMap<String, String> paramMap) {
        post(GET_REGISTER_NUMBER, paramMap, new NewCustomResponseHandler<RegisterNumBean>(taskId) {
        });
    }

    public void yqGoods(int taskId, HashMap<String, String> paramMap) {
        post(yqGoods, paramMap, new NewCustomResponseHandler<RegisterNumBean>(taskId) {
        });
    }

    public void yqJob(int taskId, HashMap<String, String> paramMap) {
        post(yqJob, paramMap, new NewCustomResponseHandler<RegisterNumBean>(taskId) {
        });
    }

    public void yqBuild(int taskId, HashMap<String, String> paramMap) {
        post(yqBuild, paramMap, new NewCustomResponseHandler<RegisterNumBean>(taskId) {
        });
    }

    public void yqProduct(int taskId, HashMap<String, String> paramMap) {
        post(yqProduct, paramMap, new NewCustomResponseHandler<RegisterNumBean>(taskId) {
        });
    }

    public void yqSkill(int taskId, HashMap<String, String> paramMap) {
        post(yqSkill, paramMap, new NewCustomResponseHandler<RegisterNumBean>(taskId) {
        });
    }

}
