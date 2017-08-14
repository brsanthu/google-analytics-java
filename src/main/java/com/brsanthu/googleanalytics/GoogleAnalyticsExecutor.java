package com.brsanthu.googleanalytics;

import java.util.concurrent.Future;
import java.util.function.Supplier;

import com.brsanthu.googleanalytics.request.GoogleAnalyticsRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;

public interface GoogleAnalyticsExecutor {
	GoogleAnalyticsResponse post(GoogleAnalyticsRequest<?> request);

	GoogleAnalyticsResponse post(Supplier<GoogleAnalyticsRequest<?>> requestProvider);

	Future<GoogleAnalyticsResponse> postAsync(Supplier<GoogleAnalyticsRequest<?>> requestProvider);

	Future<GoogleAnalyticsResponse> postAsync(GoogleAnalyticsRequest<?> request);
}
