package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectedMessage extends Message {
    String remoteInsideIp;
    int remoteInsidePort;
}
