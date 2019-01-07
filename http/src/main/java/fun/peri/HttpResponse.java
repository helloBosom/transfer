package fun.peri;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.httpclient.Header;

@Getter
@Setter
public class HttpResponse {

    private String result;
    private Header[] responseHeaders;
    byte[] ByteResult;
    String stringResult;

}
