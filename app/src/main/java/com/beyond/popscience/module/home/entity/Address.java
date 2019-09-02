package com.beyond.popscience.module.home.entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by yao.cui on 2017/6/10.
 */

public class Address implements Serializable {

    private int id;
    private String name;
    private String pic;
    private Address parentAddress;
    private List<Address> child;

    public Address getParentAddress() {
        return parentAddress;
    }

    public void setParentAddress(Address parentAddress) {
        this.parentAddress = parentAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Address> getChild() {
        return child;
    }

    public void setChild(List<Address> child) {
        this.child = child;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * 拼装Address
     * @param parentAddress
     * @param childAddress
     * @return
     */
    public static Address assembledAddress(Address parentAddress, Address childAddress){
        Address address = null;
        if(parentAddress!=null){
            address = new Address();

            if(childAddress!=null){
                address.setId(childAddress.getId());
                address.setName(childAddress.getName());
                address.setPic(childAddress.getPic());
            }

            Address parentAddressTmp = new Address();
            parentAddressTmp.setId(parentAddress.getId());
            parentAddressTmp.setName(parentAddress.getName());
            parentAddressTmp.setPic(parentAddress.getPic());
            address.setParentAddress(parentAddressTmp);
        }
        return address;
    }
}
