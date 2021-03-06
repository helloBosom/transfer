package fun.peri.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 */
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
