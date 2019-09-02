package com.beyond.popscience.frame.pojo.point;

import com.beyond.popscience.frame.pojo.BaseObject;

public class StuBean extends BaseObject{
    private String userid;
    private String score;
    private String createtime;
    private String updatetime;
    private String town;
    private String village;
    private String mobile;
    private String showname;
    private String balance;
    private String isVisitScore;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getIsVisitScore() {
        return isVisitScore;
    }

    public void setIsVisitScore(String isVisitScore) {
        this.isVisitScore = isVisitScore;
    }
}
