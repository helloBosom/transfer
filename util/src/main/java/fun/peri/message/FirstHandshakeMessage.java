package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 第一次握手消息
 */
@Setter
@Getter
public class FirstHandshakeMessage extends Message {
    String id;
}
