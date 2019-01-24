package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 */
@Setter
@Getter
public class FileConnectMessage extends BaseMessage {
    private String id;
    private String fileLocation;
}
