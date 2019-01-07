package fun.peri.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InetProperties {
    String localIp;
    int localPort;
    String serverIp;
    int serverPort;
    String remoteIp;
    int remotePort;
}
