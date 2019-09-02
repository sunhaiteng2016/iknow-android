package com.beyond.popscience.module.home.entity;

public class UserDate {


    /**
     * code : 0
     * message : 获取成功
     * data : {"address":"丁岙村","headImg":"http://www.appwzd.cn/micro_mart/ncss/logo.png","sex":1,"nickname":"ceshi","mobile":"13067781520","remakname":"","type":2,"userid":285176}
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
         * address : 丁岙村
         * headImg : http://www.appwzd.cn/micro_mart/ncss/logo.png
         * sex : 1
         * nickname : ceshi
         * mobile : 13067781520
         * remakname :
         * type : 2
         * userid : 285176
         */

        private String address;
        private String headImg;
        private int sex;
        private String nickname;
        private String mobile;
        private String remakname;
        private int type;
        private int userid;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRemakname() {
            return remakname;
        }

        public void setRemakname(String remakname) {
            this.remakname = remakname;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }
    }
}
