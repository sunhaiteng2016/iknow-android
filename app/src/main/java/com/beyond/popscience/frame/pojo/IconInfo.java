package com.beyond.popscience.frame.pojo;

/**
 * Created by linjinfa on 2017/10/13.
 * email 331710168@qq.com
 */
public class IconInfo extends BaseObject {

    private String fieldName;
    private String iconUrl;
    private String name;

    public IconInfo() {
    }

    public IconInfo(String fieldName, String iconUrl, String name) {
        this.fieldName = fieldName;
        this.iconUrl = iconUrl;
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
