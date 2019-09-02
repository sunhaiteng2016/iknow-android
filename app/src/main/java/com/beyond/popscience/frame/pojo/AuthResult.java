package com.beyond.popscience.frame.pojo;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class AuthResult extends BaseObject {

    private String company;  //企业认证	0：未认证 1:已认证 2:待审核
    private String people;   //个人认证	0：未认证 1:已认证 2:待审核

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }
}
