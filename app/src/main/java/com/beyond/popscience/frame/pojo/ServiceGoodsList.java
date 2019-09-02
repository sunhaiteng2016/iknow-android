package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * 商品列表
 * Created by yao.cui on 2017/6/29.
 */

public class ServiceGoodsList extends BaseList {
    private List<ServiceGoodsItem> productList;
    private List<ServiceGoodsItem> skillList;
    private List<ServiceGoodsItem> taskList;
    private List<BuildingDetail> buildingList;
    private List<BuildingDetail> rentList;
    private List<JobDetail>  jobList;
    private List<JobDetail>  jobApplyList;

    public List<JobDetail> getJobList() {
        return jobList;
    }

    public void setJobList(List<JobDetail> jobList) {
        this.jobList = jobList;
    }

    public List<JobDetail> getJobApplyList() {
        return jobApplyList;
    }

    public void setJobApplyList(List<JobDetail> jobApplyList) {
        this.jobApplyList = jobApplyList;
    }

    public List<BuildingDetail> getRentList() {
        return rentList;
    }

    public void setRentList(List<BuildingDetail> rentList) {
        this.rentList = rentList;
    }

    public List<BuildingDetail> getBuildingList() {
        return buildingList;
    }

    public void setBuildingList(List<BuildingDetail> buildingList) {
        this.buildingList = buildingList;
    }

    public List<ServiceGoodsItem> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<ServiceGoodsItem> taskList) {
        this.taskList = taskList;
    }

    public List<ServiceGoodsItem> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<ServiceGoodsItem> skillList) {
        this.skillList = skillList;
    }

    public List<ServiceGoodsItem> getProductList() {
        return productList;
    }

    public void setProductList(List<ServiceGoodsItem> productList) {
        this.productList = productList;
    }
}
