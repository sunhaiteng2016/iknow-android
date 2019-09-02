package com.beyond.popscience.module.home.entity;

import java.util.List;

/**
 * Created by sht on 2018/6/12.
 */

public class XiangAddresss {

    /**
     * code : 0
     * message : 获取成功
     * data : [{"id":1,"name":"福应街道","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/12/0b5ce1874e0f4de3b8adf3dc2557e817.jpg","leaves":1},{"id":2,"name":"南峰街道","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/2b121f9b4ca545eaa581c1a4c773892a.jpg","leaves":1},{"id":3,"name":"安洲街道","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/fc0b1f8a80d941918df68fcc2eac5306.jpg","leaves":1},{"id":4,"name":"安岭乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":1},{"id":5,"name":"溪港乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":1},{"id":6,"name":"湫山乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":1},{"id":7,"name":"横溪镇","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/134a4fd6193346d2879a97eaf2f36d78.jpg","leaves":1},{"id":8,"name":"埠头镇","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/0baa43c27f9545fda87514f4be64e2a9.jpg","leaves":1},{"id":9,"name":"皤滩乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/e78d37c10621447ea061d6e1a20c2566.jpg","leaves":1},{"id":10,"name":"淡竹乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/b1f0a7847b324a2482e853713de17256.jpg","leaves":1},{"id":11,"name":"白塔镇","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/2ce3f12d7d944a2ab8b374cfdf230caa.jpg","leaves":1},{"id":12,"name":"田市镇","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/ae5b5eeb6fa14351995cd3ca4cbed622.jpg","leaves":1},{"id":13,"name":"官路镇","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/13/7aeee69071bb468b9bc325baab22e009.jpg","leaves":1},{"id":14,"name":"上张乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":1},{"id":15,"name":"步路乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/14/c7139269411b4a33bdd2ab23dae2d266.jpg","leaves":1},{"id":16,"name":"广度乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":1},{"id":17,"name":"下各镇","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/14/67b3e4492a154345b62807b1ad943210.jpg","leaves":1},{"id":18,"name":"大战乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":1},{"id":19,"name":"双庙乡","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":1},{"id":20,"name":"朱溪镇","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/14/8cc361c21aaf48f4b8211e1d666abc59.jpg","leaves":1},{"id":21,"name":"其他镇","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/14/8cc361c21aaf48f4b8211e1d666abc59.jpg","leaves":1},{"id":22,"name":"县机关企事业","parent":331024,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/14/67b3e4492a154345b62807b1ad943210.jpg","leaves":1}]
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
         * id : 1
         * name : 福应街道
         * parent : 331024
         * pic : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/12/0b5ce1874e0f4de3b8adf3dc2557e817.jpg
         * leaves : 1
         */

        private int id;
        private String name;
        private int parent;
        private String pic;
        private int leaves;
        private int flag;

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
