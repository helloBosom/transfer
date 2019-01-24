package fun.peri.message;

import lombok.Getter;
import lombok.Setter;
/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class Info extends BaseMessage {
    String header;
    String idFrom;
    String idTo;
}
