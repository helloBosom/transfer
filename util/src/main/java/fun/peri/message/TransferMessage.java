package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 请求协助打洞消息
 *
 * @author hellobosom@gmail.com
 */
@Setter
@Getter
public class TransferMessage extends BaseMessage {
    private String idFrom;
    private String idTo;
    private String insideIp;
    private Integer insidePort;
}
