package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class IndexResult extends BaseObject {
    private List<St> st;
    private String town;
    private Stu stu;

    public List<St> getSt() {
        return st;
    }

    public void setSt(List<St> st) {
        this.st = st;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Stu getStu() {
        return stu;
    }

    public void setStu(Stu stu) {
        this.stu = stu;
    }

    public class St {
        private String userid;
        private String signdate;
        private String score;
        private String keysta;
        private String days;
        private String type;
        private String sqlsigndate;
        private String ajxsigndate;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getSigndate() {
            return signdate;
        }

        public void setSigndate(String signdate) {
            this.signdate = signdate;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getKeysta() {
            return keysta;
        }

        public void setKeysta(String keysta) {
            this.keysta = keysta;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSqlsigndate() {
            return sqlsigndate;
        }

        public void setSqlsigndate(String sqlsigndate) {
            this.sqlsigndate = sqlsigndate;
        }

        public String getAjxsigndate() {
            return ajxsigndate;
        }

        public void setAjxsigndate(String ajxsigndate) {
            this.ajxsigndate = ajxsigndate;
        }
    }

    public class Stu {
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

        @Override
        public String toString() {
            return "Stu{" +
                    "userid='" + userid + '\'' +
                    ", score='" + score + '\'' +
                    ", createtime='" + createtime + '\'' +
                    ", updatetime='" + updatetime + '\'' +
                    ", town='" + town + '\'' +
                    ", village='" + village + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", showname='" + showname + '\'' +
                    ", balance='" + balance + '\'' +
                    ", isVisitScore='" + isVisitScore + '\'' +
                    '}';
        }
    }
}
