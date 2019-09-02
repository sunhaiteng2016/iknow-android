package com.beyond.popscience.module.town.task;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.task.ITask;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.module.home.entity.Address;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索地址
 * Created by linjinfa on 2017/6/26.
 * email 331710168@qq.com
 */
public class SearchAddressTask extends ITask {

    private String keyword;
    private int areaType;

    public SearchAddressTask(int taskId, String keyword, int areaType) {
        super(taskId);
        this.keyword = keyword;
        this.areaType = areaType;
    }

    @Override
    public MSG doTask() {
        List<Address> searchResultList = new ArrayList<>();
        if (TextUtils.isEmpty(keyword)) {
            return new MSG(true, searchResultList);
        }
        List<Address> addressList = null;
        if (areaType == 1) {
            addressList = BeyondApplication.getInstance().getAreaAddressList();
        } else {
            addressList = BeyondApplication.getInstance().getCacheAddressList();
        }

        if (addressList == null) {
            addressList = new ArrayList<>();
        }

        for (Address parentAddress : addressList) {
            if (!TextUtils.isEmpty(parentAddress.getName())) {
                if (parentAddress.getName().toLowerCase().contains(keyword.toLowerCase())) {  //包含
                    if (parentAddress.getChild() != null) {
                        for (Address childAddress : parentAddress.getChild()) {

                            Address childAddressTmp = Address.assembledAddress(parentAddress, childAddress);
                            if (childAddressTmp != null) {
                                searchResultList.add(childAddressTmp);
                            }
                        }
                    }
                } else {
                    if (parentAddress.getChild() != null) {
                        for (Address childAddress : parentAddress.getChild()) {
                            if (!TextUtils.isEmpty(childAddress.getName()) && (parentAddress.getName().toLowerCase() + childAddress.getName().toLowerCase()).contains(keyword.toLowerCase())) {

                                Address childAddressTmp = new Address();
                                childAddressTmp.setId(childAddress.getId());
                                childAddressTmp.setName(childAddress.getName());

                                Address parentAddressTmp = new Address();
                                parentAddressTmp.setId(parentAddress.getId());
                                parentAddressTmp.setName(parentAddress.getName());
                                childAddressTmp.setParentAddress(parentAddressTmp);

                                searchResultList.add(childAddressTmp);
                            }
                        }
                    }
                }
            }
        }
        return new MSG(true, searchResultList);
    }

    /**
     * 从asset路径下读取对应文件转String输出
     *
     * @return
     */
    private String getJson(String fileName) {
        StringBuilder sb = new StringBuilder();
        AssetManager am = BeyondApplication.getInstance().getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

}
