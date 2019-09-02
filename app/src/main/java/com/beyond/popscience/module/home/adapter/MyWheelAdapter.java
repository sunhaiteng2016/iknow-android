package com.beyond.popscience.module.home.adapter;

import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.widget.wheelview.adapter.WheelAdapter;

import java.util.List;

/**
 * Created by yao.cui on 2017/6/10.
 */

public class MyWheelAdapter implements WheelAdapter {
    private List<Address> mAddress;

    public MyWheelAdapter(List<Address> address){
        this.mAddress = address;
    }

    @Override
    public int getMaximumLength() {
        return 20;
    }

    @Override
    public int getItemsCount() {
        if (mAddress== null) return 0;
        return mAddress.size();
    }

    @Override
    public String getItem(int index) {
        Address address = getItemObject(index);
        if (address!= null){
            return address.getName();
        }
        return "";
    }

    /**
     *
     * @param index
     * @return
     */
    public Address getItemObject(int index){
        if(mAddress!=null && index>=0 && index<mAddress.size()){
            return mAddress.get(index);
        }
        return null;
    }


}
