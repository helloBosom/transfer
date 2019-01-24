package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息基类
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public abstract class BaseMessage implements Serializable {
    protected Enum header;
}
