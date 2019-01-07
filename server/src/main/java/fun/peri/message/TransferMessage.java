package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferMessage extends Message {
    String idFrom;
    String idTo;
    String insideIp;
    int insidePort;
}
