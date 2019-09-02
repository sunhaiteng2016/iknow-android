package com.beyond.popscience.module.home.entity;

import java.util.List;

public class CSDS {

    /**
     * code : 0
     * message : 查询成功
     * data : [{"userid":151623,"nickname":"孙崇泽","avatar":"http://kpnew.appwzd.cn/header/2c1ecf3a3a96455faf865634dcee4290.jpg","mobile":"13561173094","address":""}]
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
         * userid : 151623
         * nickname : 孙崇泽
         * avatar : http://kpnew.appwzd.cn/header/2c1ecf3a3a96455faf865634dcee4290.jpg
         * mobile : 13561173094
         * address :
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
