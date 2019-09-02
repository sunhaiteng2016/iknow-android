package com.beyond.popscience.module.home.entity;

public class MyMenu {


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
