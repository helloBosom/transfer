public class ConnectedMessage extends Message {
    String remoteInsideIp;
    int remoteInsidePort;

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
}
