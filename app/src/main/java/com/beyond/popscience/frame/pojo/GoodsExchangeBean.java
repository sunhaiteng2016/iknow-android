package com.beyond.popscience.frame.pojo;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class GoodsExchangeBean extends BaseObject {
    private String pic;
    private String title;
    private String greanB;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGreanB() {
        return greanB;
    }

    public void setGreanB(String greanB) {
        this.greanB = greanB;
    }

    @Override
    public String toString() {
        return "GoodsExchangeBean{" +
                "pic='" + pic + '\'' +
                ", title='" + title + '\'' +
                ", greanB='" + greanB + '\'' +
                '}';
    }
}
