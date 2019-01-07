package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息基类
 */
@Getter
@Setter
public abstract class Message implements Serializable {
    Enum header;
}
