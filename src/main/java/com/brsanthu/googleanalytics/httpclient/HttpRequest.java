package com.brsanthu.googleanalytics.httpclient;

import java.util.HashMap;
import java.util.Map;

import com.brsanthu.googleanalytics.request.GoogleAnalyticsRequest;

public class HttpRequest {
    private String contentType;
    private String method;
    private String url;
    private Map<String, String> bodyParams = new HashMap<>();
    private GoogleAnalyticsRequest<?> googleAnalyticsRequest;

    public HttpRequest(String url) {
        setUrl(url);
    }

    public HttpRequest post() {
        setMethod("POST");
        return this;
    }

    public HttpRequest addBodyParam(String key, String value) {
        bodyParams.put(key, value);
        return this;
    }

    public Map<String, String> getBodyParams() {
        return bodyParams;
    }

    public String getContentType() {
        return contentType;
    }

    public HttpRequest setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public GoogleAnalyticsRequest<?> getGoogleAnalyticsRequest() {
        return googleAnalyticsRequest;
    }

    public HttpRequest setGoogleAnalyticsRequest(GoogleAnalyticsRequest<?> googleAnalyticsRequest) {
        this.googleAnalyticsRequest = googleAnalyticsRequest;
        return this;
    }
}
