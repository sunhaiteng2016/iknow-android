package com.beyond.popscience.frame.pojo;

/**
 * Created by linjinfa on 2017/6/22.
 * email 331710168@qq.com
 */
public class SocialInfo extends BaseObject {

    private String name;
    private String logo;
    private String communityId;
    private int state;//	0:未加入 1:申请中 2：已加入
    private int type;//	1:正规社团 2：普通社团
    private String updateNum;
    private boolean isCheck=false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    /**
     * 0：社团圈子   1：乡镇圈子
     */
    private int appType = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateNum() {
        return updateNum;
    }

    public void setUpdateNum(String updateNum) {
        this.updateNum = updateNum;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    /**
     * 是否乡镇圈子
     * @return
     */
    public boolean isTown(){
        return appType == 1;
    }

    /**
     *
     * @return
     */
    public long getUpdateNumLong(){
        try {
            return Long.parseLong(updateNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断是否正在审核
     * @return
     */
    public boolean isInAudit(){
        return 1== this.getState();
    }

    /**
     * 判断是否为普通社团
     * @return
     */
    public boolean isNormal(){
        return this.type == 2;
    }

}
