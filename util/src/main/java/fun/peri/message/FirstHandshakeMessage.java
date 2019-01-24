package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 第一次握手消息
 *
 * @author hellobosom@gmail.com
 */
@Setter
@Getter
public class FirstHandshakeMessage extends BaseMessage {
    private String id;
}
