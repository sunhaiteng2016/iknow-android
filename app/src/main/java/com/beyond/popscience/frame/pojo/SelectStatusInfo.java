package com.beyond.popscience.frame.pojo;

/**
 * Created by danxiang.feng on 2017/10/14.
 */

public class SelectStatusInfo extends BaseObject {

    private String title;
    private String value;
    private boolean isSelected;

    public SelectStatusInfo(){}

    public SelectStatusInfo(String title,String value, boolean isSelected){
        this.title = title;
        this.value = value;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
