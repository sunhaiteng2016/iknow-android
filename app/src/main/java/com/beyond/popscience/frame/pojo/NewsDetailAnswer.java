package com.beyond.popscience.frame.pojo;

/**
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class NewsDetailAnswer extends BaseObject {

    private String rightAnswer;
    private String qid;
    private String match;

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }
}
