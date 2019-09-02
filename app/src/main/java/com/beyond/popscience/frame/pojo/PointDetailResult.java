package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class PointDetailResult extends BaseObject {
    private String stexchang;

    private int totalPage;
    private int currentPage;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    private List<Details> list;

    private User user;

    public String getStexchang() {
        return stexchang;
    }

    public void setStexchang(String stexchang) {
        this.stexchang = stexchang;
    }

    public List<Details> getList() {
        return list;
    }

    public void setList(List<Details> list) {
        this.list = list;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class Details {
        private String userid;
        private String showname;
        private String score;
        private String createtime;
        private String town;
        private String village;
        private String message;
        private String rest;
        private String mobile;
        private String type;

        @Override
        public String toString() {
            return "Details{" +
                    "userid='" + userid + '\'' +
                    ", showname='" + showname + '\'' +
                    ", score='" + score + '\'' +
                    ", createtime='" + createtime + '\'' +
                    ", town='" + town + '\'' +
                    ", village='" + village + '\'' +
                    ", message='" + message + '\'' +
                    ", rest='" + rest + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getShowname() {
            return showname;
        }

        public void setShowname(String showname) {
            this.showname = showname;
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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRest() {
            return rest;
        }

        public void setRest(String rest) {
            this.rest = rest;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class User {
        private String userid;
        private String score;
        private String createtime;
        private String updatetime;
        private String town;
        private String village;
        private String mobile;
        private String showname;
        private String balance;

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

        @Override
        public String toString() {
            return "User{" +
                    "userid='" + userid + '\'' +
                    ", score='" + score + '\'' +
                    ", createtime='" + createtime + '\'' +
                    ", updatetime='" + updatetime + '\'' +
                    ", town='" + town + '\'' +
                    ", village='" + village + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", showname='" + showname + '\'' +
                    ", balance='" + balance + '\'' +
                    '}';
        }
    }
}
