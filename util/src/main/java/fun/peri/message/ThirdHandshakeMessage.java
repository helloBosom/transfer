package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 第三次握手消息
 */
@Setter
@Getter
public class ThirdHandshakeMessage extends Message {
    String id;
}
