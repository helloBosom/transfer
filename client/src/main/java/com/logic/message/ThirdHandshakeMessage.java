package com.logic.message;

/**
 * 第三次握手消息
 */
public class ThirdHandshakeMessage extends Message {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
