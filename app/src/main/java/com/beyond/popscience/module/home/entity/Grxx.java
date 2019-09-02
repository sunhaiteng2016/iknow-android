package com.beyond.popscience.module.home.entity;

public class Grxx {


    /**
     * code : 0
     * message : 获取成功！
     * data : {"userid":151623,"showname":"孙崇泽","score":138,"hisScore":138,"md5Pay":"false","mobile":"13561173094","town":1,"village":10001,"balance":0,"isVisitScore":null,"createtime":1541829533000,"updatetime":1541829572000}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userid : 151623
         * showname : 孙崇泽
         * score : 138.0
         * hisScore : 138.0
         * md5Pay : false
         * mobile : 13561173094
         * town : 1
         * village : 10001
         * balance : 0.0
         * isVisitScore : null
         * createtime : 1541829533000
         * updatetime : 1541829572000
         */

        private int userid;
        private String showname;
        private double score;
        private double hisScore;
        private String md5Pay;
        private String mobile;
        private int town;
        private int village;
        private double balance;
        private Object isVisitScore;
        private long createtime;
        private long updatetime;

        private int isShop;

        public int getIsShop() {
            return isShop;
        }

        public void setIsShop(int isShop) {
            this.isShop = isShop;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getShowname() {
            return showname;
        }

        public void setShowname(String showname) {
            this.showname = showname;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public double getHisScore() {
            return hisScore;
        }

        public void setHisScore(double hisScore) {
            this.hisScore = hisScore;
        }

        public String getMd5Pay() {
            return md5Pay;
        }

        public void setMd5Pay(String md5Pay) {
            this.md5Pay = md5Pay;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getTown() {
            return town;
        }

        public void setTown(int town) {
            this.town = town;
        }

        public int getVillage() {
            return village;
        }

        public void setVillage(int village) {
            this.village = village;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public Object getIsVisitScore() {
            return isVisitScore;
        }

        public void setIsVisitScore(Object isVisitScore) {
            this.isVisitScore = isVisitScore;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public long getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(long updatetime) {
            this.updatetime = updatetime;
        }
    }
}
