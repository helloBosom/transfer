package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 * 远端已连接消息
 */
@Setter
@Getter
public class ConnectedMessage extends BaseMessage {
    private String remoteInsideIp;
    private Integer remoteInsidePort;
    private String remoteOutsideIp;
    private Integer remoteOutsidePort;
    private String id;
}
