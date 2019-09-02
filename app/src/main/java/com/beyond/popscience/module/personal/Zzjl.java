package com.beyond.popscience.module.personal;

import java.util.List;

public class Zzjl {


    /**
     * code : 0
     * message : 获取成功 ！
     * data : [{"id":10016,"userid":264310,"username":"测试007","userscore":2,"touserid":151951,"tousername":"秋山","touserscore":27408,"score":1,"remarks":"1","type":1,"createtime":1542092567000,"updatetime":null}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10016
         * userid : 264310
         * username : 测试007
         * userscore : 2
         * touserid : 151951
         * tousername : 秋山
         * touserscore : 27408
         * score : 1
         * remarks : 1
         * type : 1
         * createtime : 1542092567000
         * updatetime : null
         */

        private int id;
        private int userid;
        private String username;
        private int userscore;
        private int touserid;
        private String tousername;
        private int touserscore;
        private int score;
        private String remarks;
        private int type;
        private long createtime;
        private Object updatetime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUserscore() {
            return userscore;
        }

        public void setUserscore(int userscore) {
            this.userscore = userscore;
        }

        public int getTouserid() {
            return touserid;
        }

        public void setTouserid(int touserid) {
            this.touserid = touserid;
        }

        public String getTousername() {
            return tousername;
        }

        public void setTousername(String tousername) {
            this.tousername = tousername;
        }

        public int getTouserscore() {
            return touserscore;
        }

        public void setTouserscore(int touserscore) {
            this.touserscore = touserscore;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public Object getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(Object updatetime) {
            this.updatetime = updatetime;
        }
    }
}
