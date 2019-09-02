package com.beyond.popscience.module.home.entity;

import java.util.List;

/**
 * Created by sht on 2018/6/13.
 */

public class secondPage {

    /**
     * code : 0
     * message : 查询成功
     * data : [{"child":[{"id":230001,"name":"椒江1村","parent":23,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":230002,"name":"椒江2村","parent":23,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/fc4c65d529324891ae3e086eb5290d6d.jpg","leaves":2}],"id":23,"name":"椒江镇"},{"child":[{"id":240001,"name":"测试乡村1","parent":24,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":240002,"name":"测试乡村2","parent":24,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2}],"id":24,"name":"椒江三镇"},{"child":[],"id":25,"name":"椒江二镇"}]
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
         * child : [{"id":230001,"name":"椒江1村","parent":23,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg","leaves":2},{"id":230002,"name":"椒江2村","parent":23,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/fc4c65d529324891ae3e086eb5290d6d.jpg","leaves":2}]
         * id : 23
         * name : 椒江镇
         */

        private int id;
        private String name;
        private List<ChildBean> child;

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

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean {
            /**
             * id : 230001
             * name : 椒江1村
             * parent : 23
             * pic : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/old/2017/07/04/53d3a4f6a0e34886bbf7d800bcc02263.jpg
             * leaves : 2
             */

            private int id;
            private String name;
            private int parent;
            private String pic;
            private int leaves;

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
}
