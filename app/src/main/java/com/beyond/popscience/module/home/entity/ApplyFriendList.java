package com.beyond.popscience.module.home.entity;

import java.util.List;

public class ApplyFriendList  {


    /**
     * code : 0
     * message : 获取成功
     * data : [{"id":5,"userid":151951,"touserid":151623,"message":"我是:可","status":1,"createtime":1548159453000,"type":null,"nickName":"秋山","tonickName":"孙崇泽","headImg":"http://kpnew.appwzd.cn/header/b5b7ad82390b4d4c9defe7958fa6292f.jpg","toheadImg":"http://kpnew.appwzd.cn/header/2c1ecf3a3a96455faf865634dcee4290.jpg"},{"id":4,"userid":1,"touserid":151623,"message":"我是孙海腾","status":3,"createtime":1548068429000,"type":null,"nickName":"Ray","tonickName":"孙崇泽","headImg":"http://kp.appwzd.cn/header/3de1b0a5d72d45b7a3e14ad4b5949945.jpeg","toheadImg":"http://kpnew.appwzd.cn/header/2c1ecf3a3a96455faf865634dcee4290.jpg"}]
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
         * id : 5
         * userid : 151951
         * touserid : 151623
         * message : 我是:可
         * status : 1
         * createtime : 1548159453000
         * type : null
         * nickName : 秋山
         * tonickName : 孙崇泽
         * headImg : http://kpnew.appwzd.cn/header/b5b7ad82390b4d4c9defe7958fa6292f.jpg
         * toheadImg : http://kpnew.appwzd.cn/header/2c1ecf3a3a96455faf865634dcee4290.jpg
         */

        private int id;
        private int userid;
        private int touserid;
        private String message;
        private int status;
        private long createtime;
        private Object type;
        private String nickName;
        private String tonickName;
        private String headImg;
        private String toheadImg;

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

        public int getTouserid() {
            return touserid;
        }

        public void setTouserid(int touserid) {
            this.touserid = touserid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getTonickName() {
            return tonickName;
        }

        public void setTonickName(String tonickName) {
            this.tonickName = tonickName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getToheadImg() {
            return toheadImg;
        }

        public void setToheadImg(String toheadImg) {
            this.toheadImg = toheadImg;
        }
    }
}
