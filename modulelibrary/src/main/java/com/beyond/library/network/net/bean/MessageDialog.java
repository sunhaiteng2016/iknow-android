package com.beyond.library.network.net.bean;

import java.io.Serializable;

/**
 * Created by linjinfa on 2017/6/8.
 * email 331710168@qq.com
 */
public class MessageDialog implements Serializable {

    private String title = null;
    private String content = null;

    public MessageDialog() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
