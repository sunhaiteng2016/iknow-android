package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by danxiang.feng on 2017/10/11.
 */

public class SkillResponse extends BaseList {

    private List<ServiceGoodsItem> skillList;
    private List<ServiceGoodsItem> taskList;

    public List<ServiceGoodsItem> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<ServiceGoodsItem> skillList) {
        this.skillList = skillList;
    }

    public List<ServiceGoodsItem> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<ServiceGoodsItem> taskList) {
        this.taskList = taskList;
    }


    @Override
    public String toString() {
        return "SkillResponse{" +
                "skillList=" + skillList +
                ", taskList=" + taskList +
                '}';
    }
}
