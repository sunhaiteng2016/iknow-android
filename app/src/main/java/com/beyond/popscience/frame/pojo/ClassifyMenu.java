package com.beyond.popscience.frame.pojo;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class ClassifyMenu extends BaseObject {

    private String menuUid;   //二级分类id
    private String menuName;  //二级分类名

    public String getMenuUid() {
        return menuUid;
    }

    public void setMenuUid(String menuUid) {
        this.menuUid = menuUid;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
