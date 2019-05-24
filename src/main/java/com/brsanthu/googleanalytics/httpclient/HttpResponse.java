package com.brsanthu.googleanalytics.httpclient;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int statusCode;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public int getStatusCode() {
        return statusCode;
    }

    public HttpResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getBody() {
        return body;
    }

    public HttpResponse setBody(String body) {
        this.body = body;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpResponse setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

}
