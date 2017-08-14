package com.brsanthu.googleanalytics.httpclient;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String url;
    private Map<String, String> bodyParams = new HashMap<>();

    public HttpRequest(String url) {
        this.url = url;
    }

    public HttpRequest post() {
        method = "POST";
        return this;
    }

    public HttpRequest addBodyParam(String key, String value) {
        bodyParams.put(key, value);
        return this;
    }

    public Map<String, String> getBodyParams() {
        return bodyParams;
    }
}
