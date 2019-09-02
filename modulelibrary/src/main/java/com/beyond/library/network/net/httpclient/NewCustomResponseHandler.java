package com.beyond.library.network.net.httpclient;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.network.task.IUIController;
import com.beyond.library.network.task.TaskManager;
import com.beyond.library.util.L;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class NewCustomResponseHandler<T> extends CustomResponseHandler implements ParameterizedType {

    /**
     * 任务Id
     */
    private int taskId;
    /**
     * 控制器标识符
     */
    private String identification;
    /**
     * 是否调用父类的 refreshUI 方法
     */
    private boolean isCallSuperRefreshUI = true;
    private boolean isCallSuperMessageDialog = true;
    private Object targetObj;


    /**
     * **************************************************************************************************************************************************************************************
     * <p>
     * Constructor
     * <p>
     * **************************************************************************************************************************************************************************************
     */
    public NewCustomResponseHandler() {

    }

    public NewCustomResponseHandler(int taskId) {
        this.taskId = taskId;
    }

    public NewCustomResponseHandler(int taskId, String identification) {
        this.taskId = taskId;
        this.identification = identification;
    }

    /**
     * 转 boolean
     *
     * @param booleanValue
     * @return
     */
    private boolean parseBoolean(String booleanValue) {
        try {
            return Boolean.parseBoolean(booleanValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onSuccess(int httpStatusCode, Map<String, List<String>> headerMap, String responseStr) {
        super.onSuccess(httpStatusCode, headerMap, responseStr);
        BaseResponse responseObj = null;
        try {
            responseObj = new Gson().fromJson(responseStr, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isFromCache = false;
        List<String> headerList = headerMap.get("isFromCache");
        if (headerList != null && headerList.size() != 0) {
            String isFromCacheStr = headerList.get(0);
            isFromCache = parseBoolean(isFromCacheStr);
            if (isFromCache) {
                if (responseObj == null) {
                    responseObj = new BaseResponse();
                }
            }
        }
        if (responseObj == null) {
            handlerMsg(new MSG(false, false, isCallSuperRefreshUI, null).setTargetObjSelf(targetObj).setFromCache(isFromCache));
            return;
        }
        long code = responseObj.getCode();
        if (code == 0) {
            handlerMsg(new MSG(true, responseObj.getData()).setMsg(responseObj.getMessage() == null ? responseObj.getMsg() : responseObj.getMessage()).setBaseResponse(responseObj).setTargetObjSelf(targetObj).setCode(String.valueOf(code)).setObj(responseObj.getData()).setIsCallSuperRefreshUI(isCallSuperRefreshUI).setFromCache(isFromCache));
        } else {
            handlerMsg(new MSG(false, false, isCallSuperRefreshUI, responseObj.getMessage() == null ? responseObj.getMsg() : responseObj.getMessage()).setBaseResponse(responseObj).setTargetObjSelf(targetObj).setCode(String.valueOf(code)).setObj(responseObj.getData()).setFromCache(isFromCache));
        }
    }

    @Override
    public void onFailure(int statusCode, Map<String, List<String>> headerMap, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headerMap, responseString, throwable);
        if (responseString != null)
            L.v("onFailure (" + identification + ") taskId:" + taskId + " string====> code:" + statusCode + " lenght:" + responseString.length());
        MSG errorMsg = handlerError(statusCode, responseString, null, throwable);
        errorMsg.setIsCallSuperRefreshUI(isCallSuperRefreshUI);
        errorMsg.setTargetObjSelf(targetObj);
        handlerMsg(errorMsg);
    }

    @Override
    public Type[] getActualTypeArguments() {
        Class clz = this.getClass();
        Type superclass = clz.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("没传泛型,请带上{}...");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments();
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public Type getRawType() {
        return BaseResponse.class;
    }

    /**
     * 返回消息到控制器
     *
     * @param msg
     */
    protected void handlerMsg(MSG msg) {
        if (identification == null) {
            return;
        }
        IUIController uiController = TaskManager.getInstance().getUIController(identification);
        if (uiController != null) {
            uiController.refreshUI(taskId, msg);
        }
    }

    /**
     * **************************************************************************************************************************************************************************************
     * <p>
     * set && get
     * <p>
     * **************************************************************************************************************************************************************************************
     */
    public int getTaskId() {
        return taskId;
    }

    public NewCustomResponseHandler setTaskId(int taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getIdentification() {
        return identification;
    }

    public NewCustomResponseHandler setIdentification(String identification) {
        this.identification = identification;
        return this;
    }

    public boolean isCallSuperRefreshUI() {
        return isCallSuperRefreshUI;
    }

    public NewCustomResponseHandler setCallSuperRefreshUI(boolean callSuperRefreshUI) {
        isCallSuperRefreshUI = callSuperRefreshUI;
        return this;
    }

    public Object getTargetObj() {
        return targetObj;
    }

    public NewCustomResponseHandler setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
        return this;
    }
}
