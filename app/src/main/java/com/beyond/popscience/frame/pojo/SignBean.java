package com.beyond.popscience.frame.pojo;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class SignBean extends BaseObject{
    private String weekday;
    private boolean isSelect;
    private boolean isSigned;

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    @Override
    public String toString() {
        return "SignBean{" +
                "weekday='" + weekday + '\'' +
                ", isSelect=" + isSelect +
                ", isSigned=" + isSigned +
                '}';
    }
}
