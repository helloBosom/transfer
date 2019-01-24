package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 第二次握手消息
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class SecondHandshakeMessage extends BaseMessage {
    private String id;
}
