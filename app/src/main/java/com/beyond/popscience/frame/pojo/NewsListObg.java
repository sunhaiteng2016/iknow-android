package com.beyond.popscience.frame.pojo;

import com.beyond.popscience.module.home.entity.News;

import java.util.List;

/**
 * 新闻列表
 * Created by yao.cui on 2017/6/15.
 */

public class NewsListObg extends BaseObject{

    private int totalcount;
    private int pagesize;
    private List<News> newsList;
    private int currentpage;
    private int totalpage;

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }
}
