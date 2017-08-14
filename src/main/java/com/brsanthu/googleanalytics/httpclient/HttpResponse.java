package com.brsanthu.googleanalytics.httpclient;

public class HttpResponse {

    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public HttpResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

}
