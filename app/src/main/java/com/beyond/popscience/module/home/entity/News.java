package com.beyond.popscience.module.home.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/6/9.
 */

public class News implements Serializable{


    /**
     * 乡镇新闻
     */
    public static final int TYPE_TOWN_NEWS = 1;
    /**
     * 乡镇公告
     */
    public static final int TYPE_TOWN_ANNOUNCEMENT = 2;

    /**
     * homepage news
     */
    public static final int TYPE_HOME_NEWS = 3;

    public static final String SEPERATE = ",";//value 分隔符

    public int newsStyle;
    public String pics;
    public String title;
    public String newsId;
    public String publishTime;
    public String view;
    public String auchor;
    public String stick;

    public String link;
    /**
     * 	1:新闻 2：公告
     */
    public int searchType;
    /**
     * 0：默认新闻   1：乡镇新闻  2:乡镇公告
     */
    public int appNewsType = 0;

    public int type;

    /**
     * 是否乡镇新闻
     * @return
     */
    public boolean isTownNews(){
        return appNewsType == TYPE_TOWN_NEWS;
    }

    /**
     * 是否默认新闻
     * @return
     */
    public boolean isDefaultNews(){
        return appNewsType == 0;
    }

    /**
     * 获取pic url
     * @return
     */
    public String[] getPicArray(){
        if (pics== null) pics="";
        return this.pics.split(SEPERATE);
    }

    /**
     * 获取第一个pic url
     * @return
     */
    public String getFirstPic(){
        String[] values = getPicArray();
        return values==null||values.length<=0?"":values[0];
    }

    /**
     * 是否置顶
     * @return
     */
    public boolean isStick(){
        return "1".equals(stick);
    }

}
