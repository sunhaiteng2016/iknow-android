package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * 圈子-- 公告列表
 *
 * Created by yao.cui on 2017/7/17.
 */

public class NoticeListObj extends BaseList {
    private List<NoticeDetail> noticeList;

    public List<NoticeDetail> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeDetail> noticeList) {
        this.noticeList = noticeList;
    }
}
