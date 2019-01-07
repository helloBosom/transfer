package fun.peri.message;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class FileMessage extends Message {
    private static final long serialVersionUID = 1L;
    private File file;
    private String file_md5;
    private int starPos;
    private byte[] bytes;
    private int endPos;
}
