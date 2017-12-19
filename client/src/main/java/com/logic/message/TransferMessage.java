package com.logic.message;

/**
 * 请求协助打洞消息
 */
public class TransferMessage extends Message {

    String idFrom;
    String idTo;
    String insideIp;
    int insidePort;

    public String getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public String getInsideIp() {
        return insideIp;
    }

    public void setInsideIp(String insideIp) {
        this.insideIp = insideIp;
    }

    public int getInsidePort() {
        return insidePort;
    }

    public void setInsidePort(int insidePort) {
        this.insidePort = insidePort;
    }
}
