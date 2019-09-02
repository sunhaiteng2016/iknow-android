package com.beyond.popscience.frame.pojo;

/**
 * Created by danxiang.feng on 2017/10/15.
 */

public class JobRequest extends BaseObject {

    private String area;    //地点	二级乡镇ID
    private String position;   //职位	格式：一级，二级
    private String salary; //工资类型	1：1K以下 2：1K到3K 3：3K到5K 4:5K-7K 5:7K-10K 6: 10K以上 7:面议
    private String query;   //搜索词
    private String education; //学历
    private String areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
}
