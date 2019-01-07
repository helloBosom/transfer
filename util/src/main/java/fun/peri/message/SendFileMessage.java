package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 可以发送文件消息
 */
@Getter
@Setter
public class SendFileMessage extends Message {
    String id;
}
