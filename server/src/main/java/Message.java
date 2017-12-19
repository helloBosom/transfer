import java.io.Serializable;

public abstract class Message implements Serializable {
    String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
