package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by danxiang.feng on 2017/10/15.
 */

public class JobResponse extends BaseList {

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
}
