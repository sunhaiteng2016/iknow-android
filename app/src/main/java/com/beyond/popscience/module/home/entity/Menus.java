package com.beyond.popscience.module.home.entity;

import java.util.List;

public class Menus {


    private List<UserAllTabBean> userAllTab;
    private List<ResultListBean> resultList;

    public List<UserAllTabBean> getUserAllTab() {
        return userAllTab;
    }

    public void setUserAllTab(List<UserAllTabBean> userAllTab) {
        this.userAllTab = userAllTab;
    }

    public List<ResultListBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultListBean> resultList) {
        this.resultList = resultList;
    }

    public static class UserAllTabBean {
        /**
         * id : 1
         * tabname : 本地产
         * tabpic : www.pic.cpm/aaa.jpg
         * category : 1
         * type : 1
         * taburl :
         * classify : 1
         * isdefault : null
         * createtime : null
         */

        private int id;
        private String tabname;
        private String tabpic;
        private int category;
        private int type;
        private String taburl;
        private int classify;
        private Object isdefault;
        private Object createtime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTabname() {
            return tabname;
        }

        public void setTabname(String tabname) {
            this.tabname = tabname;
        }

        public String getTabpic() {
            return tabpic;
        }

        public void setTabpic(String tabpic) {
            this.tabpic = tabpic;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTaburl() {
            return taburl;
        }

        public void setTaburl(String taburl) {
            this.taburl = taburl;
        }

        public int getClassify() {
            return classify;
        }

        public void setClassify(int classify) {
            this.classify = classify;
        }

        public Object getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(Object isdefault) {
            this.isdefault = isdefault;
        }

        public Object getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Object createtime) {
            this.createtime = createtime;
        }
    }

    public static class ResultListBean {
        /**
         * tabList : [{"id":2,"tabname":"实体店","tabpic":"www.pic.cpm/bbb.jpg","category":1,"type":1,"taburl":"","classify":2,"isdefault":null,"createtime":null},{"id":3,"tabname":"社团","tabpic":"www.pic.cpm/ccc.jpg","category":1,"type":1,"taburl":"","classify":3,"isdefault":null,"createtime":null},{"id":4,"tabname":"乡镇","tabpic":"www.pic.cpm/ccc.jpg","category":1,"type":1,"taburl":"","classify":0,"isdefault":null,"createtime":null},{"id":5,"tabname":"房产","tabpic":"www.pic.cpm/ccc.jpg","category":1,"type":1,"taburl":"","classify":0,"isdefault":null,"createtime":null},{"id":6,"tabname":"招聘","tabpic":"www.pic.cpm/ccc.jpg","category":1,"type":1,"taburl":"","classify":0,"isdefault":null,"createtime":null},{"id":7,"tabname":"技能","tabpic":"www.pic.cpm/ccc.jpg","category":1,"type":1,"taburl":"","classify":0,"isdefault":null,"createtime":null},{"id":8,"tabname":"积分","tabpic":"www.pic.cpm/ccc.jpg","category":1,"type":1,"taburl":"","classify":0,"isdefault":null,"createtime":null}]
         * cateId : 1
         * name : 为您推荐
         */

        private int cateId;
        private String name;
        private List<TabListBean> tabList;

        public int getCateId() {
            return cateId;
        }

        public void setCateId(int cateId) {
            this.cateId = cateId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<TabListBean> getTabList() {
            return tabList;
        }

        public void setTabList(List<TabListBean> tabList) {
            this.tabList = tabList;
        }

        public static class TabListBean {
            /**
             * id : 2
             * tabname : 实体店
             * tabpic : www.pic.cpm/bbb.jpg
             * category : 1
             * type : 1
             * taburl :
             * classify : 2
             * isdefault : null
             * createtime : null
             */

            private int id;
            private String tabname;
            private String tabpic;
            private int category;
            private int type;
            private String taburl;
            private int classify;
            private Object isdefault;
            private Object createtime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTabname() {
                return tabname;
            }

            public void setTabname(String tabname) {
                this.tabname = tabname;
            }

            public String getTabpic() {
                return tabpic;
            }

            public void setTabpic(String tabpic) {
                this.tabpic = tabpic;
            }

            public int getCategory() {
                return category;
            }

            public void setCategory(int category) {
                this.category = category;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTaburl() {
                return taburl;
            }

            public void setTaburl(String taburl) {
                this.taburl = taburl;
            }

            public int getClassify() {
                return classify;
            }

            public void setClassify(int classify) {
                this.classify = classify;
            }

            public Object getIsdefault() {
                return isdefault;
            }

            public void setIsdefault(Object isdefault) {
                this.isdefault = isdefault;
            }

            public Object getCreatetime() {
                return createtime;
            }

            public void setCreatetime(Object createtime) {
                this.createtime = createtime;
            }
        }
    }
}
