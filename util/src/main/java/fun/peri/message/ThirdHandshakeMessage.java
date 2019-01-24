package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 第三次握手消息
 * @author hellobosom@gmail.com
 */
@Setter
@Getter
public class ThirdHandshakeMessage extends BaseMessage {
    private String id;
}
