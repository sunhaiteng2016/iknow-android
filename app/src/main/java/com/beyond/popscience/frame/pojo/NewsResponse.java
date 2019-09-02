package com.beyond.popscience.frame.pojo;

import com.beyond.popscience.module.home.entity.News;

import java.util.List;

/**
 * Created by linjinfa on 2017/6/13.
 * email 331710168@qq.com
 */
public class NewsResponse extends BaseObject {

    private long totalcount;
    private long totalpage;
    private long pagesize;
    private long currentpage;
    private List<News> newsList;

    public long getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(long totalcount) {
        this.totalcount = totalcount;
    }

    public long getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(long totalpage) {
        this.totalpage = totalpage;
    }

    public long getPagesize() {
        return pagesize;
    }

    public void setPagesize(long pagesize) {
        this.pagesize = pagesize;
    }

    public long getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(long currentpage) {
        this.currentpage = currentpage;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
