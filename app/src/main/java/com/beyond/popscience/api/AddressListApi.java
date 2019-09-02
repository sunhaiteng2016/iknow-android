package com.beyond.popscience.api;


import android.util.Log;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.AddressListBean;
import com.beyond.popscience.frame.pojo.DataBean;
import com.beyond.popscience.frame.pojo.ServiceGoodsList;
import com.beyond.popscience.frame.util.UserInfoUtil;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址列表
 */

public class AddressListApi extends AppBaseRestUsageV2 {

    //获取地址列表
    public void getAddressList(int taskId,String userId){//ADDRESS_LIST
        Map<String,String> params = new HashMap<>();
        params.put("userId",UserInfoUtil.getInstance().getUserInfo().getUserId());
        Log.e("ADDRESS_List: ",params.toString());
        post(UrlConfig.ADDRESS_LIST,params, new NewCustomResponseHandler<List<DataBean>>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    //获取地址列表
    public void getAddressList2(int taskId,String userId){//ADDRESS_LIST
        String urlP = UrlConfig.ADDRESS_LIST + "/{0}";
//        Map<String,String> params = new HashMap<>();
//        params.put("userId",userId);
//        Log.e("ADDRESS_List: ",params.toString());
        String url = MessageFormat.format(urlP,userId);
        post(url,null,new NewCustomResponseHandler<AddressListBean>(taskId) {
        }.setCallSuperRefreshUI(true));
    }

    //添加地址
    public void addAddress(int taskId, String contactName,String contactPhone,String province,String city
    ,String area,String street,String addressDetail,String status){
      Map<String,String> params = new HashMap<>();
      params.put("userId", UserInfoUtil.getInstance().getUserInfo().getUserId());
      params.put("contactName",contactName);
      params.put("contactPhone",contactPhone);
      params.put("province",province);
      params.put("city",city);
      params.put("area",area);
      params.put("street",street);
      params.put("addressDetail",addressDetail);
      params.put("status",status);//1:默认地址 其他 常用地址
      Log.e("ADDRESS_ADD : ",params.toString());
     post(UrlConfig.ADDRESS_ADD,params, new NewCustomResponseHandler<String>(taskId) {
     }.setCallSuperRefreshUI(false));
    }

    //设置默认地址, 以及编辑地址
    public void updateAddress(int taskId,String addressId,String contactName,String contactPhone,String province,
                              String city,String area,String street,String addressDetail
                                ,String status){
        Map<String,String> params = new HashMap<>();
        params.put("addressId",addressId);//地址id
        params.put("userId", UserInfoUtil.getInstance().getUserInfo().getUserId());
        params.put("contactName",contactName);//联系人姓名
        params.put("contactPhone",contactPhone);//联系人电话
        params.put("province",province);//省
        params.put("city",city);//市
        params.put("area",area);//区
        params.put("street",street);//街道
        params.put("addressDetail",addressDetail);//详细地址
        params.put("status",status);//1:默认地址 2,3,以上常用地址
        post(UrlConfig.UPDATE_ADDRESS,params, new NewCustomResponseHandler<String>(taskId) {
        }.setCallSuperRefreshUI(true));

    }

    /**
     * addressId       地址id          不可空
     */
    public void dedeleteAddress(int taskId,String addressId ){
        Map<String,String> params = new HashMap<>();
        params.put("addressId",addressId);
        post(UrlConfig.DETELT_ADDRESS,params, new NewCustomResponseHandler<String>(taskId) {
        }.setCallSuperRefreshUI(true));
    }
}
