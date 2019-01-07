package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 第二次握手消息
 */
@Getter
@Setter
public class SecondHandshakeMessage extends Message {
    String id;
}
