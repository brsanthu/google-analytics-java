package com.brsanthu.googleanalytics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.brsanthu.googleanalytics.discovery.DefaultRequestParameterDiscoverer;
import com.brsanthu.googleanalytics.discovery.RequestParameterDiscoverer;
import com.brsanthu.googleanalytics.httpclient.ApacheHttpClientImpl;
import com.brsanthu.googleanalytics.httpclient.HttpClient;
import com.brsanthu.googleanalytics.internal.GaUtils;
import com.brsanthu.googleanalytics.internal.GoogleAnalyticsImpl;
import com.brsanthu.googleanalytics.internal.GoogleAnalyticsThreadFactory;
import com.brsanthu.googleanalytics.request.DefaultRequest;

public class GoogleAnalyticsBuilder {
    private GoogleAnalyticsConfig config = new GoogleAnalyticsConfig();
    private DefaultRequest defaultRequest = new DefaultRequest();
    private HttpClient httpClient;
    private ExecutorService executor;
    private GoogleAnalyticsExecutor googleAnalyticsExecutor;

    public GoogleAnalyticsBuilder withGoogleAnalyticsExecutor(GoogleAnalyticsExecutor googleAnalyticsExecutor) {
        this.googleAnalyticsExecutor = googleAnalyticsExecutor;
        return this;
    }

    public GoogleAnalyticsBuilder withConfig(GoogleAnalyticsConfig config) {
        this.config = GaUtils.firstNotNull(config, new GoogleAnalyticsConfig());
        return this;
    }

    public GoogleAnalyticsBuilder withTrackingId(String trackingId) {
        defaultRequest.trackingId(trackingId);
        return this;
    }

    public GoogleAnalyticsBuilder withAppName(String value) {
        defaultRequest.applicationName(value);
        return this;
    }

    public GoogleAnalyticsBuilder withAppVersion(String value) {
        defaultRequest.applicationVersion(value);
        return this;
    }

    public GoogleAnalyticsBuilder withDefaultRequest(DefaultRequest defaultRequest) {
        this.defaultRequest = GaUtils.firstNotNull(defaultRequest, new DefaultRequest());
        return this;
    }

    public GoogleAnalyticsBuilder withExecutor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public GoogleAnalyticsBuilder withHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public GoogleAnalytics build() {
        if (config.isDiscoverRequestParameters()) {
            RequestParameterDiscoverer discoverer = GaUtils.firstNotNull(config.getRequestParameterDiscoverer(),
                    DefaultRequestParameterDiscoverer.INSTANCE);

            discoverer.discoverParameters(config, defaultRequest);
        }

        return new GoogleAnalyticsImpl(config, defaultRequest, createHttpClient(), createExecutor(), googleAnalyticsExecutor);
    }

    protected HttpClient createHttpClient() {
        if (httpClient != null) {
            return httpClient;
        }

        return new ApacheHttpClientImpl(config);
    }

    protected ExecutorService createExecutor() {
        if (executor != null) {
            return executor;
        }

        return new ThreadPoolExecutor(config.getMinThreads(), config.getMaxThreads(), config.getThreadTimeoutSecs(), TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(config.getThreadQueueSize()), createThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    protected ThreadFactory createThreadFactory() {
        return new GoogleAnalyticsThreadFactory(config.getThreadNameFormat());
    }
}
