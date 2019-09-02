package com.beyond.popscience.widget.wheelview.adapter;


import com.beyond.popscience.widget.wheelview.WheelMenuInfo;

import java.util.List;

/**
 * Created by linjinfa 331710168@qq.com on 2015/9/7.
 */
public class WheelMenuAdapter implements WheelAdapter {

    private List<WheelMenuInfo> wheelMenuInfoList;

    public WheelMenuAdapter(List<WheelMenuInfo> wheelMenuInfoList) {
        this.wheelMenuInfoList = wheelMenuInfoList;
    }

    @Override
    public int getItemsCount() {
        if(wheelMenuInfoList!=null){
            return wheelMenuInfoList.size();
        }
        return 0;
    }

    @Override
    public String getItem(int index) {
        if(wheelMenuInfoList!=null && index>=0 && index<wheelMenuInfoList.size()){
            return wheelMenuInfoList.get(index).getName();
        }
        return null;
    }

    /**
     *
     * @param index
     * @return
     */
    public WheelMenuInfo getItemObject(int index){
        if(wheelMenuInfoList!=null && index>=0 && index<wheelMenuInfoList.size()){
            return wheelMenuInfoList.get(index);
        }
        return null;
    }

    @Override
    public int getMaximumLength() {
        return 5;
    }

    public List<WheelMenuInfo> getDataLisr() {
        return wheelMenuInfoList;
    }
}
