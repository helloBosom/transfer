package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class ConnectedMessage extends BaseMessage {
    private String remoteInsideIp;
    private Integer remoteInsidePort;
}
