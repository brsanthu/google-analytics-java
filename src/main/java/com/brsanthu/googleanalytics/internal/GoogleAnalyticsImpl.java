/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.brsanthu.googleanalytics.internal;

import static com.brsanthu.googleanalytics.internal.GaUtils.isEmpty;
import static com.brsanthu.googleanalytics.request.GoogleAnalyticsParameter.QUEUE_TIME;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
        response.setGoogleAnalyticsRequest(gaReq);

        if (!config.isEnabled()) {
            return response;
        }

        try {
            if (config.isBatchingEnabled()) {
                response = postBatch(gaReq);
            } else {
                response = postSingle(gaReq);
            }

        } catch (Throwable e) {
            if (config.getExceptionHandler() != null) {
                config.getExceptionHandler().handle(e);
            } else {
                logger.warn("Exception while sending the Google Analytics tracker request " + gaReq, e);
            }
        }

        return response;
    }

    protected GoogleAnalyticsResponse postBatch(GoogleAnalyticsRequest<?> gaReq) {
        GoogleAnalyticsResponse resp = new GoogleAnalyticsResponse();
        resp.setGoogleAnalyticsRequest(gaReq);

        HttpRequest httpReq = createHttpRequest(gaReq);
        resp.setRequestParams(httpReq.getBodyParams());

        if (config.isGatherStats()) {
            gatherStats(gaReq);
        }

        synchronized (currentBatch) {
            currentBatch.add(httpReq);
        }

        // If the batch size has reached the configured max,
        // then send the batch to google then clear the batch to start a new batch
        submitBatch(false);

        return resp;
    }

    private void submitBatch(boolean force) {
        if (currentBatch.isEmpty()) {
            return;
        }

        if (isSubmitBatch(force)) {

            // Synchronized block is to ensure only one of the writers will actually write the batch.
            synchronized (currentBatch) {

                // If two threads pass the if condition and then one of them actually writes,
                // other will do the same since they were blocked sync block. this ensures that
                // others will not post it even if multiple threads were to wait at sync block at same time
                // https://en.wikipedia.org/wiki/Double-checked_locking
                if (isSubmitBatch(force)) {
                    processAutoQueueTime(currentBatch);

                    logger.debug("Submitting a batch of " + currentBatch.size() + " requests to GA");
                    httpClient.postBatch(new HttpBatchRequest().setUrl(config.getBatchUrl()).setRequests(currentBatch));
                    currentBatch.clear();
                }
            }
        }
    }

    protected HttpRequest processAutoQueueTime(HttpRequest request) {
        if (!config.isAutoQueueTimeEnabled()) {
            return request;
        }

        List<HttpRequest> requests = new ArrayList<>();
        requests.add(request);

        processAutoQueueTime(requests);

        return request;
    }

    protected void processAutoQueueTime(List<HttpRequest> requests) {
        if (!config.isAutoQueueTimeEnabled()) {
            return;
        }

        // If there is no queue time specified, then set the queue time to time since event occurred to current time
        // (time at which event being posted). This is helpful for batched requests as request may be sitting in queue
        // for a while and we need to calculate the time.
        for (HttpRequest req : requests) {
            if (req.getGoogleAnalyticsRequest() == null || req.getGoogleAnalyticsRequest().occurredAt() == null) {
                continue;
            }

            String qtParamName = QUEUE_TIME.getParameterName();

            Map<String, String> params = req.getBodyParams();

            int millis = (int) ChronoUnit.MILLIS.between(req.getGoogleAnalyticsRequest().occurredAt(), ZonedDateTime.now());
            int qtMillis = params.containsKey(qtParamName) ? millis + Integer.parseInt(params.get(qtParamName)) : millis;

            params.put(qtParamName, String.valueOf(qtMillis));

            req.getGoogleAnalyticsRequest().queueTime(qtMillis);
        }
    }

    private boolean isSubmitBatch(boolean force) {
        return force || currentBatch.size() >= config.getBatchSize();
    }

    protected GoogleAnalyticsResponse postSingle(GoogleAnalyticsRequest<?> gaReq) {

        HttpRequest httpReq = processAutoQueueTime(createHttpRequest(gaReq));
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

        httpReq.setGoogleAnalyticsRequest(gaReq);

        processParameters(gaReq, httpReq);

        processCustomDimensionParameters(gaReq, httpReq);

        processCustomMetricParameters(gaReq, httpReq);

        return httpReq;
    }

    protected void processParameters(GoogleAnalyticsRequest<?> gaReq, HttpRequest httpReq) {

        Map<GoogleAnalyticsParameter, String> requestParms = gaReq.getParameters();
        Map<GoogleAnalyticsParameter, String> defaultParms = defaultRequest.getParameters();

        for (GoogleAnalyticsParameter parm : defaultParms.keySet()) {

            String value = requestParms.get(parm);
            String defaultValue = defaultParms.get(parm);

            if (isEmpty(value) && !isEmpty(defaultValue)) {
                requestParms.put(parm, defaultValue);
            }
        }

        anonymizeUserIp(gaReq, httpReq);

        for (GoogleAnalyticsParameter key : requestParms.keySet()) {
            httpReq.addBodyParam(key.getParameterName(), requestParms.get(key));
        }
    }

    private void anonymizeUserIp(GoogleAnalyticsRequest<?> gaReq, HttpRequest httpReq) {
        if (config.isAnonymizeUserIp() && gaReq.userIp() != null) {
            try {
                InetAddress ip = InetAddress.getByName(gaReq.userIp());
                byte[] address = ip.getAddress();
                int anonymizedBytes = ip instanceof Inet6Address ? 10 : 1;

                for (int i = 0; i < anonymizedBytes; ++i) {
                    address[address.length - i - 1] = 0;
                }

                String anonymizedIp = InetAddress.getByAddress(address).getHostAddress();
                gaReq.userIp(anonymizedIp);
            } catch (Exception e) {
                logger.warn("Error anonymizing user ip", e);
            }
        }
    }

    /**
     * Processes the custom dimensions and adds the values to list of parameters, which would be posted to GA.
     *
     * @param request
     * @param postParms
     */
    protected void processCustomDimensionParameters(GoogleAnalyticsRequest<?> request, HttpRequest req) {
        Map<String, String> customDimParms = new HashMap<>();
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
        Map<String, String> customMetricParms = new HashMap<>();
        for (String defaultCustomMetricKey : defaultRequest.customMetrics().keySet()) {
            customMetricParms.put(defaultCustomMetricKey, defaultRequest.customMetrics().get(defaultCustomMetricKey));
        }

        Map<String, String> requestCustomMetrics = request.customMetrics();
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
        flush();

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
