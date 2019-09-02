package com.beyond.popscience.module.home.entity;

import java.io.Serializable;
import java.util.List;

public class GroupBean implements Serializable {


    /**
     * code : 0
     * message : 获取成功！
     * data : [{"groupid":"72079545270273","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":1,"createtime":1548138821000},{"groupid":"72079848308737","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":1,"createtime":1548139111000},{"groupid":"72080013983745","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":1,"createtime":1548139268000},{"groupid":"72081079336961","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":1,"createtime":1548140285000},{"groupid":"72081340432385","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":1,"createtime":1548140534000},{"groupid":"72081470455810","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":1,"createtime":1548140657000},{"groupid":"72081692753925","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":1,"createtime":1548140869000},{"groupid":"72082376425473","groupname":"孙崇泽的群组","createuser":151623,"headimg":null,"introduce":null,"count":1,"createtime":1548141522000}]
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
         * groupid : 72079545270273
         * groupname : 孙崇泽的群组
         * createuser : 151623
         * headimg : null
         * introduce : null
         * count : 1
         * createtime : 1548138821000
         */

        private String groupid;
        private String groupname;
        private int createuser;
        private Object headimg;
        private Object introduce;
        private int count;
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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
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
