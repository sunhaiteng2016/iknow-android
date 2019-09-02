package com.beyond.library.network.enity;


import com.beyond.library.network.net.bean.BaseResponse;

import java.io.Serializable;

/**
 * @author linjinfa 331710168@qq.com
 * @date 2014-4-15
 */
public class MSG implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean isSuccess;
    /**
     * 是否网络错误
     */
    private Boolean isNetworkError;
    /**
     * 是否调用父类的 refreshUI 方法
     */
    private Boolean isCallSuperRefreshUI;
    private boolean isNull = false;
    private String msg;
    private String code;
    /**
     *
     */
    private String responseData;
    private Object obj;
    private Object targetObj;
    /**
     * code!=0时解析成的对象
     */
    private Object errorResponseObj;
    /**
     * app 2.0 response   by E.K
     */
    private BaseResponse baseResponse;
    /**
     * 是否来自 cache
     */
    private boolean isFromCache;

    public MSG() {
        super();
    }
    public MSG(Boolean isSuccess, String msg) {
        super();
        this.isSuccess = isSuccess;
        this.msg = msg;
    }
    public MSG(Boolean isSuccess, String msg, Object obj) {
        super();
        this.isSuccess = isSuccess;
        this.msg = msg;
        this.obj = obj;
    }
    public MSG(Boolean isSuccess, Object obj) {
        super();
        this.isSuccess = isSuccess;
        this.obj = obj;
    }
    public MSG(Boolean isSuccess, Object obj, boolean isNull) {
        super();
        this.isSuccess = isSuccess;
        this.obj = obj;
        this.isNull = isNull;
    }
    public MSG(Boolean isSuccess, Boolean isNetworkError, Boolean isCallSuperRefreshUI, String msg) {
        super();
        this.isSuccess = isSuccess;
        this.isNetworkError = isNetworkError;
        this.isCallSuperRefreshUI = isCallSuperRefreshUI;
        this.msg = msg;
    }
    public MSG(Boolean isSuccess, Boolean isNetworkError, Boolean isCallSuperRefreshUI, String msg, String code) {
        super();
        this.isSuccess = isSuccess;
        this.isNetworkError = isNetworkError;
        this.isCallSuperRefreshUI = isCallSuperRefreshUI;
        this.msg = msg;
        this.code = code;
    }
    public MSG(Boolean isSuccess, Boolean isNetworkError, String msg) {
        super();
        this.isSuccess = isSuccess;
        this.isNetworkError = isNetworkError;
        this.msg = msg;
    }
    public Boolean getIsSuccess() {
        return isSuccess;
    }
    public MSG setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
        return this;
    }
    public Boolean getIsNetworkError() {
        return isNetworkError;
    }
    public MSG setIsNetworkError(Boolean isNetworkError) {
        this.isNetworkError = isNetworkError;
        return this;
    }
    public String getMsg() {
        return msg;
    }
    public MSG setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public Object getObj() {
        return obj;
    }
    public MSG setObj(Object obj) {
        this.obj = obj;
        return this;
    }

    public boolean isNull() {
        return isNull;
    }

    public MSG setIsNull(boolean isNull) {
        this.isNull = isNull;
        return this;
    }

    public String getCode() {
        return code;
    }

    public MSG setCode(String code) {
        this.code = code;
        return this;
    }

    public Boolean getIsCallSuperRefreshUI() {
        return isCallSuperRefreshUI;
    }

    public MSG setIsCallSuperRefreshUI(Boolean isCallSuperRefreshUI) {
        this.isCallSuperRefreshUI = isCallSuperRefreshUI;
        return this;
    }

    public Object getTargetObj() {
        return targetObj;
    }

    public MSG setTargetObjSelf(Object targetObj){
        this.targetObj = targetObj;
        return this;
    }

    public String getResponseData() {
        return responseData;
    }

    public MSG setResponseData(String responseData) {
        this.responseData = responseData;
        return this;
    }

    public Object getErrorResponseObj() {
        return errorResponseObj;
    }

    public MSG setErrorResponseObj(Object errorResponseObj) {
        this.errorResponseObj = errorResponseObj;
        return this;
    }

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public MSG setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
        return this;
    }

    public boolean isFromCache() {
        return isFromCache;
    }

    public MSG setFromCache(boolean fromCache) {
        isFromCache = fromCache;
        return this;
    }

    @Override
    public String toString() {
        return "MSG [isSuccess=" + isSuccess + ", isFromCache= "+ isFromCache +" , msg=" + msg + ", obj=" + obj
                + "]";
    }
}
