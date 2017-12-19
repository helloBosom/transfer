package com.logic.message;

public class NATInfoMessage extends Message {
    String outsideIp;
    int outsidePort;
    String insideIp;
    int insidePort;
    String id;

    public String getOutsideIp() {
        return outsideIp;
    }

    public void setOutsideIp(String outsideIp) {
        this.outsideIp = outsideIp;
    }

    public int getOutsidePort() {
        return outsidePort;
    }

    public void setOutsidePort(int outsidePort) {
        this.outsidePort = outsidePort;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
