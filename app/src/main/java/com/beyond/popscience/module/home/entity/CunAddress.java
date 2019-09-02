package com.beyond.popscience.module.home.entity;

import java.util.List;

/**
 * Created by sht on 2018/6/12.
 */

public class CunAddress {

    /**
     * code : 0
     * message : 获取成功
     * data : [{"id":90001,"name":"金董坑","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90002,"name":"朱姆溪","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90003,"name":"板桥","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90004,"name":"皤滩","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90005,"name":"韦羌","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90006,"name":"吕前","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90007,"name":"山下","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90008,"name":"万竹口","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90009,"name":"汤坎头","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90010,"name":"万竹王","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":90011,"name":"石柱山","parent":9,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2}]
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
         * id : 90001
         * name : 金董坑
         * parent : 9
         * pic : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg
         * leaves : 2
         */

        private int id;
        private String name;
        private int parent;
        private String pic;
        private int leaves;
        public int flag;
        public int getFlag() {
            return flag;
        }
        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getLeaves() {
            return leaves;
        }

        public void setLeaves(int leaves) {
            this.leaves = leaves;
        }
    }
}
