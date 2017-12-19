package com.logic.message;

/**
 * 远端已连接消息
 */
public class ConnectedMessage extends Message {
    String remoteInsideIp;
    int remoteInsidePort;
    String remoteOutsideIp;
    int remoteOutsidePort;
    String id;

    public String getRemoteInsideIp() {
        return remoteInsideIp;
    }

    public void setRemoteInsideIp(String remoteInsideIp) {
        this.remoteInsideIp = remoteInsideIp;
    }

    public int getRemoteInsidePort() {
        return remoteInsidePort;
    }

    public void setRemoteInsidePort(int remoteInsidePort) {
        this.remoteInsidePort = remoteInsidePort;
    }

    public String getRemoteOutsideIp() {
        return remoteOutsideIp;
    }

    public void setRemoteOutsideIp(String remoteOutsideIp) {
        this.remoteOutsideIp = remoteOutsideIp;
    }

    public int getRemoteOutsidePort() {
        return remoteOutsidePort;
    }

    public void setRemoteOutsidePort(int remoteOutsidePort) {
        this.remoteOutsidePort = remoteOutsidePort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
