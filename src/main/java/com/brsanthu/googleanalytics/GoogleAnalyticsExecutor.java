package com.brsanthu.googleanalytics;

import java.util.concurrent.Future;

import com.brsanthu.googleanalytics.request.GoogleAnalyticsRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;

public interface GoogleAnalyticsExecutor {
    GoogleAnalyticsResponse post(GoogleAnalyticsRequest<?> request);

    Future<GoogleAnalyticsResponse> postAsync(GoogleAnalyticsRequest<?> request);
}
