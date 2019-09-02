package com.beyond.popscience.locationgoods.bean;

import java.io.Serializable;

public class SpecificationAttributeBean implements Serializable {

    public String title;
    public String content;
    public String hint;

    public SpecificationAttributeBean(String title,String hint, String content) {
        this.title = title;
        this.hint = hint;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SpecificationAttributeBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
