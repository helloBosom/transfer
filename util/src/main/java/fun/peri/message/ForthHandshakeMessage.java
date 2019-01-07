package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 第四次握手，用以关闭连接
 */
@Getter
@Setter
public class ForthHandshakeMessage extends Message {
    String id;
}
