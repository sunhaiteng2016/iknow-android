package com.beyond.popscience.module.home.entity;

public class Groups {


    /**
     * code : 0
     * message : 创建成功
     * data : {"groupid":"72081470455810","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":null,"createtime":1548140657201}
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
         * groupid : 72081470455810
         * groupname : 孙崇泽的群组
         * createuser : 151623
         * headimg : null
         * introduce : null
         * count : null
         * createtime : 1548140657201
         */

        private String groupid;
        private String groupname;
        private int createuser;
        private Object headimg;
        private Object introduce;
        private Object count;
        private long createtime;

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public int getCreateuser() {
            return createuser;
        }

        public void setCreateuser(int createuser) {
            this.createuser = createuser;
        }

        public Object getHeadimg() {
            return headimg;
        }

        public void setHeadimg(Object headimg) {
            this.headimg = headimg;
        }

        public Object getIntroduce() {
            return introduce;
        }

        public void setIntroduce(Object introduce) {
            this.introduce = introduce;
        }

        public Object getCount() {
            return count;
        }

        public void setCount(Object count) {
            this.count = count;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }
    }
}
