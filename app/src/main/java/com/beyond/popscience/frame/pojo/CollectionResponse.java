package com.beyond.popscience.frame.pojo;

import com.beyond.popscience.module.home.entity.News;

import java.util.List;

/**
 * Created by linjinfa on 2017/6/14.
 * email 331710168@qq.com
 */
public class CollectionResponse extends BaseObject {

    private long totalcount;
    private long pagesize;
    private long currentpage;
    private long totalpage;
    private List<News> collectionList;

    public long getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(long totalcount) {
        this.totalcount = totalcount;
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

    public long getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(long totalpage) {
        this.totalpage = totalpage;
    }

    public List<News> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<News> collectionList) {
        this.collectionList = collectionList;
    }
}
