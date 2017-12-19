package com.logic.message;

/**
 * 第二次握手消息
 */
public class SecondHandshakeMessage extends Message {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
