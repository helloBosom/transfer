package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * @author hellobosom@gmail.com
 */
@Getter
@Setter
public class FileMessage extends BaseMessage {
    private static final Long serialVersionUID = 1L;
    private File file;
    private String fileMD5;
    private Integer starPos;
    private byte[] bytes ;
    private Integer endPos;
}
