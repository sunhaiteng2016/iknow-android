package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class NewsDetailAnswerObj extends BaseObject {

    private List<NewsDetailAnswer> resultList;
    private String score;

    public List<NewsDetailAnswer> getResultList() {
        return resultList;
    }

    public void setResultList(List<NewsDetailAnswer> resultList) {
        this.resultList = resultList;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    /**
     *
     * @return
     */
    public float getScoreFloat() {
        try {
            return Float.parseFloat(score);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
