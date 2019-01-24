package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 * 连接消息
 */
@Getter
@Setter
public class ConnectMessage extends BaseMessage {
    private String id;
    private String remoteIp;
    private Integer remotePort;
    private String localIp;
    private Integer localPort;
}
