package com.brsanthu.googleanalytics.httpclient;

public interface HttpClient extends AutoCloseable {
    HttpResponse post(HttpRequest req);

    @Override
    void close();
}
