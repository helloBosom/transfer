package fun.peri;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * fun.peri.HttpProtocolHandler 是模拟HTTP请求的核心类，最主要的是execute这个执行HTTP请求的方法
 */

public class HttpProtocolHandler {

    private static final String DEFAULT_CHARSET = "utf-8";
    /**
     * 连接超时时间，由bean factory设置，缺省为8秒钟
     */
    private int defaultConnectionTimeout = 8000;
    /**
     * 回应超时时间, 由bean factory设置，缺省为60秒钟
     */
    private int defaultSoTimeout = 2 * 60000;
    /**
     * 闲置连接超时时间, 由bean factory设置，缺省为60秒钟
     */
    private int defaultIdleConnTimeout = 60000;
    private int defaultMaxConnPerHost = 30;
    private int defaultMaxTotalConn = 80;
    /**
     * 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）:1秒
     */
    private static final long defaultHttpConnectionManagerTimeout = 3 * 1000;
    /**
     * HTTP连接管理器，该连接管理器必须是线程安全的.
     */
    private HttpConnectionManager connectionManager;
    private static HttpProtocolHandler httpProtocolHandler = new HttpProtocolHandler();

    /**
     * 工厂方法
     *
     * @return
     */
    public static HttpProtocolHandler getInstance() {
        return httpProtocolHandler;
    }

    /**
     * 私有的构造方法
     */
    private HttpProtocolHandler() {
// 创建一个线程安全的HTTP连接池
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);
        connectionManager.getParams().setMaxTotalConnections(defaultMaxTotalConn);
        IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
        ict.addConnectionManager(connectionManager);
        ict.setConnectionTimeout(defaultIdleConnTimeout);
        ict.start();
    }

    /**
     * 执行Http请求
     *
     * @param request
     * @return
     */
    public HttpResponse execute(HttpRequest request) {
        HttpClient httpclient = new HttpClient(connectionManager);
        // 设置连接超时
        int connectionTimeout = defaultConnectionTimeout;
        if (request.getConnectionTimeout() > 0) {
            connectionTimeout = request.getConnectionTimeout();
        }
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
        // 设置回应超时
        int soTimeout = defaultSoTimeout;
        if (request.getTimeout() > 0) {
            soTimeout = request.getTimeout();
        }
        httpclient.getHttpConnectionManager().getParams()
                .setSoTimeout(soTimeout);
        // 设置等待ConnectionManager释放connection的时间
        httpclient.getParams().setConnectionManagerTimeout(
                defaultHttpConnectionManagerTimeout);
        String charset = request.getCharset();
        charset = charset == null ? DEFAULT_CHARSET : charset;
        HttpMethod method = null;
        if (request.getMethod().equals(HttpRequest.METHOD_GET)) {
            method = new GetMethod(request.getUrl());
            method.getParams().setCredentialCharset(charset);
            // parseNotifyConfig会保证使用GET方法时，request一定使用QueryString
            method.setQueryString(request.getQueryString());
        } else {
            method = new PostMethod(request.getUrl());
            ((PostMethod) method).addParameters(request.getParameters());
            method.addRequestHeader("Content-Type",
                    "application/x-www-form-urlencoded; text/html; charset=" + charset);
        }
        // 设置Http Header中的User-Agent属性
        method.addRequestHeader("User-Agent", "Mozilla/4.0");
        HttpResponse response = new HttpResponse();
        try {
            httpclient.executeMethod(method);
            if (request.getResultType().equals("String")) {
                response.setResult(method.getResponseBodyAsString());
            } else if (request.getResultType().equals("bytes")) {
                response.setByteResult(method.getResponseBody());
            }
            response.setResponseHeaders(method.getResponseHeaders());
        } catch (UnknownHostException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        } catch (Exception ex) {
            return null;
        } finally {
            method.releaseConnection();
        }
        return response;
    }

}
