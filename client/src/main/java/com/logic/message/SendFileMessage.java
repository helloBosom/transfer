package com.logic.message;

/**
 * 可以发送文件消息
 */
public class SendFileMessage extends Message {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
