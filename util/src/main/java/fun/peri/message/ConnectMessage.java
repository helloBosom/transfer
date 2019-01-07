package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 连接消息
 */
@Getter
@Setter
public class ConnectMessage extends Message {
    private String id;
    private String remoteIp;
    private int remotePort;
    private String localIp;
    private int localPort;
}
