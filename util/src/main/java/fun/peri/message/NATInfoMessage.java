package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NATInfoMessage extends Message {
    String outsideIp;
    int outsidePort;
    String insideIp;
    int insidePort;
    String id;
}
