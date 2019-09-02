package com.beyond.library.network.net.bean;

import java.io.Serializable;

/**
 * Created by linjinfa on 2017/6/8.
 * email 331710168@qq.com
 */
public class Message implements Serializable {

    private String toast = null;
    private MessageDialog dialog = null;

    public Message() {
    }

    public String getToast() {
        return this.toast;
    }

    public void setToast(String toast) {
        this.toast = toast;
    }

    public MessageDialog getDialog() {
        return this.dialog;
    }

    public void setDialog(MessageDialog dialog) {
        this.dialog = dialog;
    }

}
