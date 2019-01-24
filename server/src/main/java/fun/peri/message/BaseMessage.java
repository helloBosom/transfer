package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public abstract class BaseMessage implements Serializable {
    String header;

    @Override
    public String toString() {
        return "BaseMessage{" +
                "header='" + header + '\'' +
                '}';
    }
}
