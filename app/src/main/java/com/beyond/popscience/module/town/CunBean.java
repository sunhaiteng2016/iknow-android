package com.beyond.popscience.module.town;

import java.util.List;

/**
 * Created by sht on 2018/6/15.
 */

public class CunBean  {

    /**
     * code : 0
     * message : 查询成功
     * data : [{"child":[{"id":260001,"name":"东城1镇 - 1村","parent":26,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/ddcfa40690e6493eab39b1f2dbade9cd.jpg","leaves":2},{"id":260002,"name":"东城1镇 - 2村","parent":26,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f664014fd8cc40d48d312449b8957712.jpg","leaves":2}],"id":26,"name":"东城1镇"},{"child":[{"id":270001,"name":"东城2镇 - 1村","parent":27,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/6b08e407dc754ce68961fdfc015e1468.jpg","leaves":2}],"id":27,"name":"东城2镇"}]
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
         * child : [{"id":260001,"name":"东城1镇 - 1村","parent":26,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/ddcfa40690e6493eab39b1f2dbade9cd.jpg","leaves":2},{"id":260002,"name":"东城1镇 - 2村","parent":26,"pic":"http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/f664014fd8cc40d48d312449b8957712.jpg","leaves":2}]
         * id : 26
         * name : 东城1镇
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
             * id : 260001
             * name : 东城1镇 - 1村
             * parent : 26
             * pic : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/ddcfa40690e6493eab39b1f2dbade9cd.jpg
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
