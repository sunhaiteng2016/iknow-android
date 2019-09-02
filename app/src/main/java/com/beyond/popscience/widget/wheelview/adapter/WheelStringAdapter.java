package com.beyond.popscience.widget.wheelview.adapter;


import java.util.List;

/**
 * Created by linjinfa 331710168@qq.com on 2015/9/7.
 */
public class WheelStringAdapter implements WheelAdapter {

    private List<String> stringList;

    public WheelStringAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public int getItemsCount() {
        if(stringList!=null){
            return stringList.size();
        }
        return 0;
    }

    @Override
    public String getItem(int index) {
        if(stringList!=null && index>=0 && index<stringList.size()){
            return stringList.get(index);
        }
        return null;
    }

    /**
     *
     * @param index
     * @return
     */
    public String getItemObject(int index){
        if(stringList!=null && index>=0 && index<stringList.size()){
            return stringList.get(index);
        }
        return null;
    }

    @Override
    public int getMaximumLength() {
        return 5;
    }

    public List<String> getDataList() {
        return stringList;
    }
}
