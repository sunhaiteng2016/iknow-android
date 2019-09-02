package com.beyond.popscience.frame.net;

import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.frame.pojo.UserInfo;
import com.beyond.popscience.module.home.entity.Address;
import com.beyond.popscience.widget.AddressPopWindow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册
 *
 * Created by yao.cui on 2017/6/13.
 */

public class AccountRestUsage extends AppBaseRestUsageV2{

    private final String REGISTER = "/user/register";//注册

    private final String SEND_CODE = "/user/requestPIN";//获取验证码

    /**
     * 找回密码
     */
    private final String FIND_PWD = "/user/setPassword";
    /**
     * 获取个人信息
     */
    private final String GET_USER_MESSAGE = "/user/getUserMessage";
    /**
     * 个人信息修改
     */
    private final String SET_USER_MESSAGE = "/user/setUserMessage";

    /**
     * 重置密码
     */
    private final String RESET_PWD = "/user/resetPassword";

    /**
     * 乡镇json
     */
    private final String GET_AREA_JSON = "/user/getAreaJson";

    /**
     * 获取 乡镇
     */
    private final String GET_TOWN = "/address/getTown";
    /**
     * 获取 村
     */
    private final String GET_COUNTRY = "/address/getCountry";

    /**
     *
     */
    private final String GET_PRODUCTAREA_JSON = "/product/getProductArea";

    /**
     *获取订单列表
     */
    private final String orderList = "/order/orderList";
    /**
     * 提交注册信息
     * @param taskId
     * @param account
     * @param mobile
     * @param pwd
     * @param area
     * @param address
     * @param pin
     */
    public void register(int taskId, String account,String mobile,String pwd,String area,String address,String pin,String deviceId,String yqCode){

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("account", account);
        paramMap.put("password", pwd);
        paramMap.put("mobile",mobile);
        paramMap.put("area",AddressPopWindow.cunid+"");
        paramMap.put("address", AddressPopWindow.cunname);
        paramMap.put("pin",pin);
        paramMap.put("mac",deviceId);
        paramMap.put("yqCode",yqCode);
        post(REGISTER, paramMap, new NewCustomResponseHandler<String>(taskId){});
    }

    /**
     * 发送注册验证码
     * @param taskId
     * @param mobile
     */
    public void sendCoderegister(int taskId,String mobile){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobile", mobile);
        paramMap.put("pinflag", "1");
        post(SEND_CODE,paramMap,new NewCustomResponseHandler<String>(taskId){});
    }


    /**
     * 找回密码
     * @param taskId
     * @param account
     * @param newPwd
     * @param pin
     */
    public void findPwd(int taskId,String account,String newPwd,String pin){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("account", account);
        paramMap.put("newpassword", newPwd);
        paramMap.put("pin",pin);
        post(FIND_PWD, paramMap, new NewCustomResponseHandler<String>(taskId){});

    }

    /**
     * 发送验证码
     * @param taskId
     * @param mobile
     */
    public void sendCodePwd(int taskId,String mobile){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobile", mobile);
        paramMap.put("pinflag", "2");
        post(SEND_CODE,paramMap,new NewCustomResponseHandler<String>(taskId){});
    }

    public void getdingdanlist(int taskId,String buyUserId){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("buyUserId", buyUserId);
        post(orderList,paramMap,new NewCustomResponseHandler<String>(taskId){});
    }
    /**
     * 获取用户信息
     * @param taskId
     */
    public void getUserInfo(int taskId){
        get(GET_USER_MESSAGE, null, new NewCustomResponseHandler<UserInfo>(taskId){}.setCallSuperRefreshUI(false));
    }

    /**
     * 更新用户信息
     * @param taskId
     */
    public void updateUserInfo(int taskId, String paramKey, String paramValue, Object targetObj){
        Map<String, String> paramMap = new HashMap<>();
        if(paramKey.contains("nick")){
            paramKey = "nickname";
        }
        paramMap.put(paramKey, paramValue);
        post(SET_USER_MESSAGE, paramMap, new NewCustomResponseHandler<String>(taskId){}.setTargetObj(targetObj));
    }

    /**
     * 更新用户信息
     * @param taskId
     */
    public void updateUserInfo(int taskId, Map<String,String> params){
        post(SET_USER_MESSAGE, params, new NewCustomResponseHandler<Map<String,String>>(taskId){});
    }

    /**
     * 更新用户信息
     * @param taskId
     */
    public void resetPwd(int taskId, String oldPwd, String newPwd){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("password", oldPwd);
        paramMap.put("newPassword", newPwd);
        post(RESET_PWD, paramMap, new NewCustomResponseHandler<String>(taskId){});
    }

    /**
     * 乡镇json
     * @param taskId
     */
    public void getAreaJson(int taskId){
        get(GET_AREA_JSON, null, new NewCustomResponseHandler<List<Address>>(taskId){}.setCallSuperRefreshUI(false));
    }

    /**
     *
     * @param taskId
     */
    public void getProductAreaJson(int taskId){
        get(GET_PRODUCTAREA_JSON, null, new NewCustomResponseHandler<List<Address>>(taskId){}.setCallSuperRefreshUI(false));
    }

}
