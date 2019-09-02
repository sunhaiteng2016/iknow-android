package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class SuggestVo extends BaseObject {

    private String totalcount;
    private List<String> hotWords;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<String> getHotWords() {
        return hotWords;
    }

    public void setHotWords(List<String> hotWords) {
        this.hotWords = hotWords;
    }
}
