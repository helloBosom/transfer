package com.logic.message;

import java.io.Serializable;

/**
 * 消息基类
 */
public abstract class Message implements Serializable {
    String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
