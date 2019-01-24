package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class NATInfo extends BaseMessage {
    String header;
    String ip;
    int port;
    String id;
}
