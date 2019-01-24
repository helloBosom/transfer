package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class NATInfoMessage extends BaseMessage {
    private String outsideIp;
    private Integer outsidePort;
    private String insideIp;
    private Integer insidePort;
    private String id;
}
