package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Info extends Message {
    String header;
    String idFrom;
    String idTo;
}
