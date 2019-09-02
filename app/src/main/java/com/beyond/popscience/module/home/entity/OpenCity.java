package com.beyond.popscience.module.home.entity;

import java.util.List;

/**
 * Created by sht on 2018/6/17.
 */

public class OpenCity {

    /**
     * code : 0
     * message : 获取成功
     * data : [{"child":[{"id":110102,"areaName":"西城区","areaParentId":110100,"isopen":1},{"id":110106,"areaName":"丰台区","areaParentId":110100,"isopen":1}],"id":110100,"areaName":"北京市"},{"child":[{"id":331024,"areaName":"仙居县","areaParentId":331000,"isopen":1}],"id":331000,"areaName":"台州市"}]
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
         * child : [{"id":110102,"areaName":"西城区","areaParentId":110100,"isopen":1},{"id":110106,"areaName":"丰台区","areaParentId":110100,"isopen":1}]
         * id : 110100
         * areaName : 北京市
         */

        private int id;
        private String areaName;
        private List<ChildBean> child;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean {
            /**
             * id : 110102
             * areaName : 西城区
             * areaParentId : 110100
             * isopen : 1
             */

            private int id;
            private String areaName;
            private int areaParentId;
            private int isopen;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public int getAreaParentId() {
                return areaParentId;
            }

            public void setAreaParentId(int areaParentId) {
                this.areaParentId = areaParentId;
            }

            public int getIsopen() {
                return isopen;
            }

            public void setIsopen(int isopen) {
                this.isopen = isopen;
            }
        }
    }
}
