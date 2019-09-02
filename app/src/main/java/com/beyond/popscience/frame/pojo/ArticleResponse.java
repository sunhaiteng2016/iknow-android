package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by linjinfa on 2017/7/20.
 * email 331710168@qq.com
 */
public class ArticleResponse extends BaseObject {

    private long totalcount;
    private long pagesize;
    private long currentpage;
    private long totalpage;
    private List<ArticleInfo> articleList;

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

    public List<ArticleInfo> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleInfo> articleList) {
        this.articleList = articleList;
    }
}
