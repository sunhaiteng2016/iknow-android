package com.beyond.popscience.module.home.entity;

import java.util.List;

public class FriendsBeans {


    /**
     * code : 0
     * message : 查询成功
     * data : [{"userid":285176,"nickname":"ceshi","avatar":"http://www.appwzd.cn/micro_mart/ncss/logo.png","mobile":"13067781520","address":"丁岙村"}]
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
         * userid : 285176
         * nickname : ceshi
         * avatar : http://www.appwzd.cn/micro_mart/ncss/logo.png
         * mobile : 13067781520
         * address : 丁岙村
         */

        private int userid;
        private String nickname;
        private String avatar;
        private String mobile;
        private String address;

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
    }
}
