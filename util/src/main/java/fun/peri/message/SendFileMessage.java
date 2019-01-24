package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 可以发送文件消息
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class SendFileMessage extends BaseMessage {
    private String id;
}
