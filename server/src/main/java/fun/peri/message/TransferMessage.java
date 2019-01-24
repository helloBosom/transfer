package fun.peri.message;

import lombok.Getter;
import lombok.Setter;
/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class TransferMessage extends BaseMessage {
    String idFrom;
    String idTo;
    String insideIp;
    int insidePort;
}
