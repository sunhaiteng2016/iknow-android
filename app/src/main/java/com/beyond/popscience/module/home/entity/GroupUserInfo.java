package com.beyond.popscience.module.home.entity;

public class GroupUserInfo {


    /**
     * code : 0
     * message : 获取成功！
     * data : {"id":null,"userid":151623,"groupid":"72079545270273","groupname":"孙崇泽的群组","darao":2,"zhiding":2,"status":null,"createtime":null,"createUser":151623,"isMe":null}
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
         * id : null
         * userid : 151623
         * groupid : 72079545270273
         * groupname : 孙崇泽的群组
         * darao : 2
         * zhiding : 2
         * status : null
         * createtime : null
         * createUser : 151623
         * isMe : null
         */

        private Object id;
        private int userid;
        private String groupid;
        private String groupname;
        private int darao;
        private int zhiding;
        private Object status;
        private Object createtime;
        private int createUser;
        private Object isMe;
        private int flag;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

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

        public int getDarao() {
            return darao;
        }

        public void setDarao(int darao) {
            this.darao = darao;
        }

        public int getZhiding() {
            return zhiding;
        }

        public void setZhiding(int zhiding) {
            this.zhiding = zhiding;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Object createtime) {
            this.createtime = createtime;
        }

        public int getCreateUser() {
            return createUser;
        }

        public void setCreateUser(int createUser) {
            this.createUser = createUser;
        }

        public Object getIsMe() {
            return isMe;
        }

        public void setIsMe(Object isMe) {
            this.isMe = isMe;
        }
    }
}
