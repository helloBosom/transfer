package fun.peri;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HttpClientUtils {

    private static HttpClient httpClient;
    private static final String GET = "get";
    private static final String POST = "post";

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * 返回 HttpClient 单个实例
     */
    public static HttpClient getInstance() {
        if (httpClient == null) {
            httpClient = new HttpClient();
            //RFC_2109是支持较普遍的一个，还有其他cookie协议
            httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        }
        return httpClient;
    }

    public static String sendHttpMethod(String url, JSONObject params, String method) {
        String returnValue = null;
        HttpClient client = HttpClientUtils.getInstance();
        if (params != null) {
            url += params.toString();
        }
        if (Objects.equals(GET, method)) {
            GetMethod methodGet = getMethod(url);
            int status;
            try {
                //指定传送字符集为UTF-8格式
                client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
                status = client.executeMethod(methodGet);
                if (status == 200) {
                    returnValue = methodGet.getResponseBodyAsString();
                }
            } catch (Exception e) {
                System.out.println(url + "调用失败......");
            }
        } else if (Objects.equals(POST, method)) {
            List<NameValuePair> params_ = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                params_.add(new NameValuePair(key, params.get(key).toString()));
            }
            try {
                returnValue = sendPostInfo(params_, url);
                System.out.println(returnValue);
            } catch (Exception e) {
                System.out.println(url + "调用失败......");
            }
        }
        return returnValue;
    }

    /**
     * 使用GET方法，如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
     */
    public static GetMethod getMethod(String url) {
        GetMethod getMethod = new GetMethod(url);
        //设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        return getMethod;
    }

    public static List<NameValuePair> buildRequestPara(List<NameValuePair> sArray) {
        // 除去数组中的空值
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (NameValuePair nameValuePair : sArray) {
            String value = nameValuePair.getValue();
            if (value == null || value.equals("")
                    || nameValuePair.getName().equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.add(new NameValuePair(nameValuePair.getName(), value));
        }
        return result;
    }

    public static String sendPostInfo(List<NameValuePair> sParaTemp, String gateway) throws Exception {
        // 待请求参数数组
        List<NameValuePair> sPara = buildRequestPara(sParaTemp);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest("bytes");
        // 设置编码集
        request.setCharset("utf-8");
        request.setParameters(generateNameValuePair(sPara));
        request.setUrl(gateway);
        HttpResponse response = httpProtocolHandler.execute(request);
        if (response == null) {
            return null;
        }
        String strResult = response.getStringResult();
        return strResult;
    }

    private static NameValuePair[] generateNameValuePair(List<NameValuePair> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (NameValuePair nvp : properties) {
            nameValuePair[i++] = new NameValuePair(nvp.getName(), nvp.getValue());
        }
        return nameValuePair;
    }

}

