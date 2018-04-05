package com.brsanthu.googleanalytics.httpclient;

public class HttpResponse {

    private int statusCode;
    private String originalResponse = null;

    public int getStatusCode() {
        return statusCode;
    }

    public HttpResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getOriginalResponse() {
        return originalResponse;
    }

    public HttpResponse setOriginalResponse(String originalResponse) {
        this.originalResponse = originalResponse;
        return this;
    }

}
