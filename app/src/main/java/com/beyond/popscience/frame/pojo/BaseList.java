package com.beyond.popscience.frame.pojo;

/**
 * 列表
 * Created by lenovo on 2017/6/29.
 */

public class BaseList extends BaseObject {
    protected int currentpage;
    protected int pagesize;
    protected int totalpage;
    protected int totalcount;

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }
}
