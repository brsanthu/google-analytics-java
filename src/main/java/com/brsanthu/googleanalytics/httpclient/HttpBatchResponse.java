package com.brsanthu.googleanalytics.httpclient;

public class HttpBatchResponse {
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public HttpBatchResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
