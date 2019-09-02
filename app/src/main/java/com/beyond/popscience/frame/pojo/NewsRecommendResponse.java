package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by zhouhuakang on 2017/9/15.
 */

public class NewsRecommendResponse {
    private List<NewsRecommendObj> newsList;

    public List<NewsRecommendObj> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsRecommendObj> newsList) {
        this.newsList = newsList;
    }
}
