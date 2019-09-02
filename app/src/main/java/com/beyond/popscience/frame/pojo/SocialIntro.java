package com.beyond.popscience.frame.pojo;

/**
 * 社团简介
 *
 * Created by yao.cui on 2017/7/19.
 */

public class SocialIntro extends BaseObject {
    private String name;
    private String display;//顶部展示图
    private String introduce;//社团介绍
    private int remindNum;//	消息数(评论&回复)
    private int adminUser;//	1：是 0：否

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getRemindNum() {
        return remindNum;
    }

    public void setRemindNum(int remindNum) {
        this.remindNum = remindNum;
    }

    public int getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(int adminUser) {
        this.adminUser = adminUser;
    }

    /**
     * 判断是否是管理员
     * @return
     */
    public boolean isAdmin(){
        return 1 == this.adminUser;
    }
}
