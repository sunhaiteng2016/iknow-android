package com.beyond.library.network.net.bean;



import java.io.Serializable;

/**
 * Created by East.K on 2016/12/15.
 *
 * @ClassName: BaseResponse
 * @Description: 前端返回结构
 */

public class BaseResponse<T> implements Serializable {
    /********************************************************
     * 返回码
     * <p/>
     * 0 表示成功,此时取 data 中的数据;
     * 非 0 表示失败,此时取 message 中的数据;
     ********************************************************/
    private long code = 0;

    /********************************************************
     * 错误消息
     * <p/>
     * 提示(自动消失)
     * 对话框(带有按钮)
     ********************************************************/
    private String message = null;

    private String msg=null;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /********************************************************
     * 返回的业务出参
     ********************************************************/
    private T data = null;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
