package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileConnectMessage extends Message {
    String id;
    String fileLocation;
}
