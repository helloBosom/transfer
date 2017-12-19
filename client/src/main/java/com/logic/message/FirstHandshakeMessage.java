package com.logic.message;

/**
 * 第一次握手消息
 */
public class FirstHandshakeMessage extends Message {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
