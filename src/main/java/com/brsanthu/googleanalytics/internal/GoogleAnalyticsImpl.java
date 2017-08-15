/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brsanthu.googleanalytics.internal;

import static com.brsanthu.googleanalytics.internal.GaUtils.isEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brsanthu.googleanalytics.GoogleAnalytics;
import com.brsanthu.googleanalytics.GoogleAnalyticsConfig;
import com.brsanthu.googleanalytics.GoogleAnalyticsExecutor;
import com.brsanthu.googleanalytics.GoogleAnalyticsStats;
import com.brsanthu.googleanalytics.httpclient.HttpBatchRequest;
import com.brsanthu.googleanalytics.httpclient.HttpClient;
import com.brsanthu.googleanalytics.httpclient.HttpRequest;
import com.brsanthu.googleanalytics.httpclient.HttpResponse;
import com.brsanthu.googleanalytics.request.DefaultRequest;
import com.brsanthu.googleanalytics.request.EventHit;
import com.brsanthu.googleanalytics.request.ExceptionHit;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsParameter;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;
import com.brsanthu.googleanalytics.request.ItemHit;
import com.brsanthu.googleanalytics.request.PageViewHit;
import com.brsanthu.googleanalytics.request.ScreenViewHit;
import com.brsanthu.googleanalytics.request.SocialHit;
import com.brsanthu.googleanalytics.request.TimingHit;
import com.brsanthu.googleanalytics.request.TransactionHit;

/**
 * This is the main class of this library that accepts the requests from clients and sends the events to Google
 * Analytics (GA).
 *
 * Clients needs to instantiate this object with {@link GoogleAnalyticsConfig} and {@link DefaultRequest}. Configuration
 * contains sensible defaults so one could just initialize using one of the convenience constructors.
 *
 * This object is ThreadSafe and it is intended that clients create one instance of this for each GA Tracker Id and
 * reuse each time an event needs to be posted.
 *
 * This object contains resources which needs to be shutdown/disposed. So {@link #close()} method is called to release
 * all resources. Once close method is called, this instance cannot be reused so create new instance if required.
 */
public class GoogleAnalyticsImpl implements GoogleAnalytics, GoogleAnalyticsExecutor {
    protected static final Logger logger = LoggerFactory.getLogger(GoogleAnalyticsImpl.class);

    protected final GoogleAnalyticsConfig config;
    protected final DefaultRequest defaultRequest;
    protected final HttpClient httpClient;
    protected final ExecutorService executor;
    protected GoogleAnalyticsStatsImpl stats = new GoogleAnalyticsStatsImpl();
    protected List<HttpRequest> currentBatch = new ArrayList<>();

    public GoogleAnalyticsImpl(GoogleAnalyticsConfig config, DefaultRequest defaultRequest, HttpClient httpClient, ExecutorService executor) {
        this.config = config;
        this.defaultRequest = defaultRequest;
        this.httpClient = httpClient;
        this.executor = executor;
    }

    @Override
    public GoogleAnalyticsConfig getConfig() {
        return config;
    }

    public DefaultRequest getDefaultRequest() {
        return defaultRequest;
    }

    @Override
    public Future<GoogleAnalyticsResponse> postAsync(GoogleAnalyticsRequest<?> request) {
        if (!config.isEnabled()) {
            return null;
        }

        return executor.submit(() -> post(request));
    }

    @Override
    public GoogleAnalyticsResponse post(GoogleAnalyticsRequest<?> gaReq) {
        GoogleAnalyticsResponse response = new GoogleAnalyticsResponse();
        if (!config.isEnabled()) {
            return response;
        }

        try {
            if (config.isBatchingEnabled()) {
                response = postBatch(gaReq);
            } else {
                response = postSingle(gaReq);
            }

        } catch (Exception e) {
            logger.warn("Exception while sending the Google Analytics tracker request " + gaReq, e);
        }

        return response;
    }

    protected GoogleAnalyticsResponse postBatch(GoogleAnalyticsRequest<?> gaReq) {
        GoogleAnalyticsResponse resp = new GoogleAnalyticsResponse();
        HttpRequest httpReq = createHttpRequest(gaReq);
        resp.setRequestParams(httpReq.getBodyParams());

        if (config.isGatherStats()) {
            gatherStats(gaReq);
        }

        synchronized (currentBatch) {
            currentBatch.add(httpReq);
            // If the batch size has reached the configured max,
            // then send the batch to google then clear the batch to start a new batch
            submitBatch(false);
        }

        return resp;
    }

    private void submitBatch(boolean force) {
        if (currentBatch.isEmpty()) {
            return;
        }

        if (force || currentBatch.size() >= config.getBatchSize()) {
            logger.debug("Submitting a batch of " + currentBatch.size() + " requests to GA");

            httpClient.postBatch(new HttpBatchRequest().setUrl(config.getBatchUrl()).setRequests(currentBatch));
            currentBatch.clear();
        }
    }

    protected GoogleAnalyticsResponse postSingle(GoogleAnalyticsRequest<?> gaReq) {

        HttpRequest httpReq = createHttpRequest(gaReq);
        HttpResponse httpResp = httpClient.post(httpReq);

        GoogleAnalyticsResponse response = new GoogleAnalyticsResponse();
        response.setStatusCode(httpResp.getStatusCode());
        response.setRequestParams(httpReq.getBodyParams());

        if (config.isGatherStats()) {
            gatherStats(gaReq);
        }

        return response;
    }

    private HttpRequest createHttpRequest(GoogleAnalyticsRequest<?> gaReq) {
        HttpRequest httpReq = new HttpRequest(config.getUrl());

        // Process the parameters
        processParameters(gaReq, httpReq);

        // Process custom dimensions
        processCustomDimensionParameters(gaReq, httpReq);

        // Process custom metrics
        processCustomMetricParameters(gaReq, httpReq);

        return httpReq;
    }

    protected void processParameters(GoogleAnalyticsRequest<?> request, HttpRequest req) {

        Map<GoogleAnalyticsParameter, String> requestParms = request.getParameters();
        Map<GoogleAnalyticsParameter, String> defaultParms = defaultRequest.getParameters();

        for (GoogleAnalyticsParameter parm : defaultParms.keySet()) {

            String value = requestParms.get(parm);
            String defaultValue = defaultParms.get(parm);

            if (isEmpty(value) && !isEmpty(defaultValue)) {
                requestParms.put(parm, defaultValue);
            }
        }

        for (GoogleAnalyticsParameter key : requestParms.keySet()) {
            req.addBodyParam(key.getParameterName(), requestParms.get(key));
        }
    }

    /**
     * Processes the custom dimensions and adds the values to list of parameters, which would be posted to GA.
     * 
     * @param request
     * @param postParms
     */
    protected void processCustomDimensionParameters(GoogleAnalyticsRequest<?> request, HttpRequest req) {
        Map<String, String> customDimParms = new HashMap<String, String>();
        for (String defaultCustomDimKey : defaultRequest.customDimensions().keySet()) {
            customDimParms.put(defaultCustomDimKey, defaultRequest.customDimensions().get(defaultCustomDimKey));
        }

        Map<String, String> requestCustomDims = request.customDimensions();
        for (String requestCustomDimKey : requestCustomDims.keySet()) {
            customDimParms.put(requestCustomDimKey, requestCustomDims.get(requestCustomDimKey));
        }

        for (String key : customDimParms.keySet()) {
            req.addBodyParam(key, customDimParms.get(key));
        }
    }

    /**
     * Processes the custom metrics and adds the values to list of parameters, which would be posted to GA.
     * 
     * @param request
     * @param postParms
     */
    protected void processCustomMetricParameters(GoogleAnalyticsRequest<?> request, HttpRequest req) {
        Map<String, String> customMetricParms = new HashMap<String, String>();
        for (String defaultCustomMetricKey : defaultRequest.custommMetrics().keySet()) {
            customMetricParms.put(defaultCustomMetricKey, defaultRequest.custommMetrics().get(defaultCustomMetricKey));
        }

        Map<String, String> requestCustomMetrics = request.custommMetrics();
        for (String requestCustomDimKey : requestCustomMetrics.keySet()) {
            customMetricParms.put(requestCustomDimKey, requestCustomMetrics.get(requestCustomDimKey));
        }

        for (String key : customMetricParms.keySet()) {
            req.addBodyParam(key, customMetricParms.get(key));
        }
    }

    protected void gatherStats(GoogleAnalyticsRequest<?> request) {
        String hitType = request.hitType();

        if (Constants.HIT_PAGEVIEW.equalsIgnoreCase(hitType)) {
            stats.pageViewHit();

        } else if (Constants.HIT_SCREENVIEW.equals(hitType)) {
            stats.screenViewHit();

        } else if (Constants.HIT_EVENT.equals(hitType)) {
            stats.eventHit();

        } else if (Constants.HIT_ITEM.equals(hitType)) {
            stats.itemHit();

        } else if (Constants.HIT_TXN.equals(hitType)) {
            stats.transactionHit();

        } else if (Constants.HIT_SOCIAL.equals(hitType)) {
            stats.socialHit();

        } else if (Constants.HIT_TIMING.equals(hitType)) {
            stats.timingHit();

        } else if (Constants.HIT_EXCEPTION.equals(hitType)) {
            stats.exceptionHit();
        }
    }

    @Override
    public void close() {
        try {
            executor.shutdown();
        } catch (Exception e) {
            // ignore
        }

        try {
            httpClient.close();
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public GoogleAnalyticsStats getStats() {
        return stats;
    }

    @Override
    public void resetStats() {
        stats = new GoogleAnalyticsStatsImpl();
    }

    @Override
    public EventHit event() {
        return (EventHit) new EventHit().setExecutor(this);
    }

    @Override
    public ExceptionHit exception() {
        return (ExceptionHit) new ExceptionHit().setExecutor(this);
    }

    @Override
    public ItemHit item() {
        return (ItemHit) new ItemHit().setExecutor(this);
    }

    @Override
    public PageViewHit pageView() {
        return (PageViewHit) new PageViewHit().setExecutor(this);
    }

    @Override
    public PageViewHit pageView(String url, String title) {
        return pageView().documentUrl(url).documentTitle(title);
    }

    @Override
    public PageViewHit pageView(String url, String title, String description) {
        return pageView(url, title).contentDescription(description);
    }

    @Override
    public ScreenViewHit screenView() {
        return (ScreenViewHit) new ScreenViewHit().setExecutor(this);
    }

    @Override
    public ScreenViewHit screenView(String appName, String screenName) {
        return screenView().applicationName(appName).screenName(screenName);
    }

    @Override
    public SocialHit social() {
        return (SocialHit) new SocialHit().setExecutor(this);
    }

    @Override
    public SocialHit social(String socialNetwork, String socialAction, String socialTarget) {
        return social().socialNetwork(socialNetwork).socialAction(socialAction).socialActionTarget(socialTarget);
    }

    @Override
    public TimingHit timing() {
        return (TimingHit) new TimingHit().setExecutor(this);
    }

    @Override
    public TransactionHit transaction() {
        return (TransactionHit) new TransactionHit().setExecutor(this);
    }

    @Override
    public void ifEnabled(Runnable runnable) {
        if (!config.isEnabled()) {
            return;
        }

        runnable.run();
    }

    @Override
    public void flush() {
        submitBatch(true);
    }

}
