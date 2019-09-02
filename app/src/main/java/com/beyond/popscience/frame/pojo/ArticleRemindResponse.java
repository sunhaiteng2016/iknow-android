package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by linjinfa on 2017/7/22.
 * email 331710168@qq.com
 */
public class ArticleRemindResponse extends BaseObject {

    private List<ArticleRemind> RemindList;

    public List<ArticleRemind> getRemindList() {
        return RemindList;
    }

    public void setRemindList(List<ArticleRemind> remindList) {
        RemindList = remindList;
    }
}
