package com.beyond.popscience.module.home.entity;

import java.util.List;

public class FriendsBean {


    /**
     * code : 0
     * message : 获取成功
     * data : [{"userid":1,"nickname":"Ray","avatar":"http://kp.appwzd.cn/header/3de1b0a5d72d45b7a3e14ad4b5949945.jpeg","mobile":"123","address":"县机关企事业 县体育局","type":1},{"userid":12,"nickname":"格格公","avatar":"http://kp.appwzd.cn/header/6fac681502dc41dd9bb77e7a982fad94.jpeg","mobile":"15726833092","address":"福应街道 月塘社区","type":1},{"userid":13,"nickname":"俞姜屹","avatar":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a5fd19ad2eee4e0d816f7b688adadc24.jpg","mobile":"13819661713","address":"","type":1},{"userid":15,"nickname":"徐美玉","avatar":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a5fd19ad2eee4e0d816f7b688adadc24.jpg","mobile":"18391099180","address":"","type":1},{"userid":16,"nickname":"张珈铭","avatar":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a5fd19ad2eee4e0d816f7b688adadc24.jpg","mobile":"13682583019","address":"","type":1},{"userid":17,"nickname":"陈炳光","avatar":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a5fd19ad2eee4e0d816f7b688adadc24.jpg","mobile":"13806862653","address":"","type":1},{"userid":18,"nickname":"尹珠莲","avatar":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a5fd19ad2eee4e0d816f7b688adadc24.jpg","mobile":"13586581612","address":"","type":1},{"userid":19,"nickname":"jl5","avatar":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a5fd19ad2eee4e0d816f7b688adadc24.jpg","mobile":"13606766181","address":"","type":1},{"userid":21,"nickname":"景星望月","avatar":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a5fd19ad2eee4e0d816f7b688adadc24.jpg","mobile":"13967612618","address":"","type":1},{"userid":22,"nickname":"沈志林","avatar":"http://kp.appwzd.cn/header/1335ace18ca84df88004ea2d8658ada7.jpeg","mobile":"13968569010","address":"","type":1}]
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
         * userid : 1
         * nickname : Ray
         * avatar : http://kp.appwzd.cn/header/3de1b0a5d72d45b7a3e14ad4b5949945.jpeg
         * mobile : 123
         * address : 县机关企事业 县体育局
         * type : 1
         */

        private int userid;
        private String nickname;
        private String avatar;
        private String mobile;
        private String address;
        private int type;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
