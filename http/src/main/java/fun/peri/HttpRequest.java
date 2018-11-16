package fun.peri;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.httpclient.NameValuePair;

@Setter
@Getter
class HttpRequest {

    private int connectionTimeout;

    private int timeout;

    private String charset;

    private String method;

    static final String METHOD_GET = "get";

    private String url;

    private String queryString;

    NameValuePair[] Parameters;

    String resultType;

    public HttpRequest(String resultType) {
        this.resultType = resultType;
    }

}
