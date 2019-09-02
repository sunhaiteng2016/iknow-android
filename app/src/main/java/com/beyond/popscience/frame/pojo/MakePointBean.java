package com.beyond.popscience.frame.pojo;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class MakePointBean extends BaseObject{
    private String title;
    private String description;
    private String point;
    private String doSomething;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getDoSomething() {
        return doSomething;
    }

    public void setDoSomething(String doSomething) {
        this.doSomething = doSomething;
    }

    @Override
    public String toString() {
        return "MakePointBean{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", point='" + point + '\'' +
                ", doSomething='" + doSomething + '\'' +
                '}';
    }
}
