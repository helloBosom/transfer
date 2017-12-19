package com.logic.message;

/**
 * 第四次握手，用以关闭连接
 */
public class ForthHandshakeMessage extends Message {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
