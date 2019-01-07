package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 请求协助打洞消息
 */
@Setter
@Getter
public class TransferMessage extends Message {
    String idFrom;
    String idTo;
    String insideIp;
    int insidePort;
}
