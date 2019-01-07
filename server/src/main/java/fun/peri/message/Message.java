package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class Message implements Serializable {
    String header;
}
