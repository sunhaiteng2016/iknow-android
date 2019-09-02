package com.beyond.popscience.frame.pojo.point;

import com.beyond.popscience.frame.pojo.BaseObject;

public class SignCheckBean extends BaseObject{
    private String userid;
    private String signdate;
    private String ajxsigndate;
    private String sqlsigndate;//通过和上面的字段判断是否相等来判断今天是否能签到,相等则不可以签到,不相等则可以签到
    private String score;
    private String keysta;//连续签到天数
    private String days;//总共连续签到天数:days*7+当周连续签到天数

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSigndate() {
        return signdate;
    }

    public void setSigndate(String signdate) {
        this.signdate = signdate;
    }

    public String getAjxsigndate() {
        return ajxsigndate;
    }

    public void setAjxsigndate(String ajxsigndate) {
        this.ajxsigndate = ajxsigndate;
    }

    public String getSqlsigndate() {
        return sqlsigndate;
    }

    public void setSqlsigndate(String sqlsigndate) {
        this.sqlsigndate = sqlsigndate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getKeysta() {
        return keysta;
    }

    public void setKeysta(String keysta) {
        this.keysta = keysta;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
