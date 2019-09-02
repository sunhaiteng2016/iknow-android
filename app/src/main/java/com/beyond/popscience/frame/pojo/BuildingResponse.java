package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by danxiang.feng on 2017/10/13.
 */

public class BuildingResponse extends BaseList {

    private List<BuildingDetail> buildingList;
    private List<BuildingDetail> rentList;

    public List<BuildingDetail> getBuildingList() {
        return buildingList;
    }

    public void setBuildingList(List<BuildingDetail> buildingList) {
        this.buildingList = buildingList;
    }

    public List<BuildingDetail> getRentList() {
        return rentList;
    }

    public void setRentList(List<BuildingDetail> rentList) {
        this.rentList = rentList;
    }
}
