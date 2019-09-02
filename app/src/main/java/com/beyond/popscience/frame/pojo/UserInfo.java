package com.beyond.popscience.frame.pojo;

import android.text.TextUtils;

/**
 * 登录用户信息
 * Created by linjinfa on 2017/6/13.
 * email 331710168@qq.com
 */
public class UserInfo extends BaseObject {

    private String token;
    private String userId;
    /**
     * 	1:男 0:女
     */
    private String sex;
    private String nickName;
    private String avatar;
    private String birthday;
    private String area;
    private String address;
    private String career;
    private String education;
    private String Mobile;
    private String villageId;
    private String villageName;
    private String score;
    private String detailedArea;
    private  String areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDetailedArea() {
        return detailedArea;
    }

    public void setDetailedArea(String detailedArea) {
        this.detailedArea = detailedArea;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickname) {
        this.nickName = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getArea() {
        return villageId;
    }

    public void setArea(String area) {
        this.villageId = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }

    /**
     * 是否男
     * @return
     */
    public boolean isMan(){
        return "1".equals(sex);
    }

    /**
     * 获取生日 年
     * @return
     */
    public String getBirthdayYear(){
        if(TextUtils.isEmpty(birthday)){
            return birthday;
        }
        String birthdays[] = birthday.split(",");
        if(birthdays!=null && birthday.length()>0){
            return birthdays[0];
        }
        return null;
    }

    /**
     * 获取生日 月
     * @return
     */
    public String getBirthdayMonth(){
        if(TextUtils.isEmpty(birthday)){
            return birthday;
        }
        String birthdays[] = birthday.split(",");
        if(birthdays!=null && birthday.length()>1){
            return birthdays[1];
        }
        return null;
    }

    /**
     * 获取显示的 出生年月
     * @return
     */
    public String getDisplayBirthday(){
        if(!TextUtils.isEmpty(birthday)){
            String year = getBirthdayYear();
            String month = getBirthdayMonth();
            if(!TextUtils.isEmpty(year) && !TextUtils.isEmpty(month)){
                return year+"年"+month+"月";
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", sex='" + sex + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday='" + birthday + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", career='" + career + '\'' +
                ", education='" + education + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", villageId='" + villageId + '\'' +
                ", villageName='" + villageName + '\'' +
                ", score='" + score + '\'' +
                ", detailedArea='" + detailedArea + '\'' +
                ", areaName='" + areaName + '\'' +
                '}';
    }
}
