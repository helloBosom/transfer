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
    static final String METHOD_POST = "post";
    private String url;
    private String queryString;
    NameValuePair[] parameters;
    String resultType;

    public HttpRequest(String resultType) {
        this.resultType = resultType;
    }

}
