package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NATInfo extends Message {
    String header;
    String ip;
    int port;
    String id;
}
