package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 远端已连接消息
 */
@Setter
@Getter
public class ConnectedMessage extends Message {
    private String remoteInsideIp;
    private int remoteInsidePort;
    private String remoteOutsideIp;
    private int remoteOutsidePort;
    private String id;
}
