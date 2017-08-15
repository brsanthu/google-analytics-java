package com.brsanthu.googleanalytics.httpclient;

import java.util.ArrayList;
import java.util.List;

public class HttpBatchRequest {
    private String url;
    private List<HttpRequest> requests = new ArrayList<>();

    public HttpBatchRequest addRequest(HttpRequest request) {
        requests.add(request);
        return this;
    }

    public List<HttpRequest> getRequests() {
        return requests;
    }

    public HttpBatchRequest setRequests(List<HttpRequest> requests) {
        this.requests = requests;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpBatchRequest setUrl(String url) {
        this.url = url;
        return this;
    }

}
