package com.beyond.popscience.frame.pojo.point;

import com.beyond.popscience.frame.pojo.BaseObject;

public class StBean extends BaseObject{
    private String userid;
    private String signdate;
    private String score;
    private String keysta;
    private String days;
    private String type;
    private String sqlsigndate;
    private String ajxsigndate;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSqlsigndate() {
        return sqlsigndate;
    }

    public void setSqlsigndate(String sqlsigndate) {
        this.sqlsigndate = sqlsigndate;
    }

    public String getAjxsigndate() {
        return ajxsigndate;
    }

    public void setAjxsigndate(String ajxsigndate) {
        this.ajxsigndate = ajxsigndate;
    }
}
