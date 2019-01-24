package fun.peri.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class InetProperties {
    private String localIp;
    int localPort;
    String serverIp;
    int serverPort;
    String remoteIp;
    int remotePort;
}
