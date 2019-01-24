package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class ConnectMessage extends BaseMessage {
    private String id;
    private String remoteIp;
    private Integer remotePort;
    private String localIp;
    private Integer localPort;

    @Override
    public String toString() {
        return "ConnectMessage{" +
                "id='" + id + '\'' +
                ", remoteIp='" + remoteIp + '\'' +
                ", remotePort=" + remotePort +
                ", localIp='" + localIp + '\'' +
                ", localPort=" + localPort +
                ", header='" + header + '\'' +
                '}';
    }
}
