package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by linjinfa on 2017/6/13.
 * email 331710168@qq.com
 */
public class ReplyResponse extends BaseObject {

    private long totalcount;
    private long totalpage;
    private long pagesize;
    private long currentpage;
    private List<Comment> replyList;

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

    public List<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Comment> replyList) {
        this.replyList = replyList;
    }
}
