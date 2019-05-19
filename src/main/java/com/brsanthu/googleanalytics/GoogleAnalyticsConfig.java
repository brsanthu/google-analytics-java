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
package com.brsanthu.googleanalytics;

import com.brsanthu.googleanalytics.discovery.AwtRequestParameterDiscoverer;
import com.brsanthu.googleanalytics.discovery.DefaultRequestParameterDiscoverer;
import com.brsanthu.googleanalytics.discovery.RequestParameterDiscoverer;
import com.brsanthu.googleanalytics.internal.GoogleAnalyticsImpl;
import com.brsanthu.googleanalytics.internal.GoogleAnalyticsStatsImpl;

/**
 * Properties that can be configured in this library. These would include any properties that are required to process
 * the tracking request or enhance the tracking request (but not specified in measurement protocol like User agent).
 * <p>
 * Most of the properties are initialization level and request level. If a property is a initialization level property,
 * it should be set at the time of GoogleAnalytics object initialization. If a property is a request level property, it
 * can be set any time and it will be effective.
 * <p>
 * All properties of this config object supports method chaining. So for example, you could do,
 * <code>new GoogleAnalyticsConfig().setMaxThreads(2).setThreadNameFormat("name");</code>
 *
 * @author Santhosh Kumar
 */
public class GoogleAnalyticsConfig {
    private String threadNameFormat = "googleanalyticsjava-thread-{0}";
    private boolean enabled = true;
    private int minThreads = 0;
    private int maxThreads = 5;
    private int threadTimeoutSecs = 300;
    private int threadQueueSize = 1000;
    private int maxHttpConnectionsPerRoute = 10;
    private boolean useHttps = true;
    private boolean validate = true;
    private boolean batchingEnabled = false;
    private int batchSize = 20;
    private String httpUrl = "http://www.google-analytics.com/collect";
    private String httpsUrl = "https://www.google-analytics.com/collect";
    private String batchUrl = "https://www.google-analytics.com/batch";
    private String userAgent = null;
    private String proxyHost = null;
    private int proxyPort = 80;
    private String proxyUserName = null;
    private String proxyPassword = null;
    private boolean discoverRequestParameters = true;
    private boolean gatherStats = false;
    private RequestParameterDiscoverer requestParameterDiscoverer = new DefaultRequestParameterDiscoverer();
    private GoogleAnalyticsExceptionHandler exceptionHandler;

    public RequestParameterDiscoverer getRequestParameterDiscoverer() {
        return requestParameterDiscoverer;
    }

    /**
     * Sets the appropriate request parameter discoverer. Default is {@link DefaultRequestParameterDiscoverer} but can
     * be changed to {@link AwtRequestParameterDiscoverer} if you want to use Toolkit to derive the screen resolution
     * etc.
     *
     * Please make sure you also enable the discovery using {@link #setDiscoverRequestParameters(boolean)}
     *
     * @param requestParameterDiscoverer can be null and is so, parameters will not be discovered.
     * @return
     */
    public GoogleAnalyticsConfig setRequestParameterDiscoverer(RequestParameterDiscoverer requestParameterDiscoverer) {
        this.requestParameterDiscoverer = requestParameterDiscoverer;
        return this;
    }

    public boolean isGatherStats() {
        return gatherStats;
    }

    /**
     * If set to true, {@link GoogleAnalyticsImpl} will collect the basic stats about successful event postings for
     * various hit types and keeps a copy of {@link GoogleAnalyticsStatsImpl}, which can be retrieved using
     * {@link GoogleAnalyticsImpl#getStats()}
     *
     * @param gatherStats
     * @return
     */
    public GoogleAnalyticsConfig setGatherStats(boolean gatherStats) {
        this.gatherStats = gatherStats;
        return this;
    }

    /**
     * Sets the thread name format that should be while creating the threads.
     * <p>
     * Default is "googleanalytics-thread-{0}" where {0} is the thread counter. If you specify a custom format, make
     * sure {0} is there somewhere otherwise all threads will be nameed same and can be an issue for troubleshooting.
     *
     * @param threadNameFormat non-null string for thread name.
     */
    public GoogleAnalyticsConfig setThreadNameFormat(String threadNameFormat) {
        this.threadNameFormat = threadNameFormat;
        return this;
    }

    public String getThreadNameFormat() {
        return threadNameFormat;
    }

    /**
     * Deprecated since 1.0.6
     *
     * @deprecated Use {@link #setDiscoverRequestParameters(boolean)} instead
     */
    @Deprecated
    public GoogleAnalyticsConfig setDeriveSystemParameters(boolean deriveSystemProperties) {
        return setDiscoverRequestParameters(deriveSystemProperties);
    }

    /**
     * If true, derives the system properties (User Language, Region, Country, Screen Size, Color Depth, and File
     * encoding) and adds to the default request.
     *
     * <p>
     * This is <strong>initialization</strong> level configuration (must be set while creating GoogleAnalytics object).
     * </p>
     */
    public GoogleAnalyticsConfig setDiscoverRequestParameters(boolean discoverSystemParameters) {
        discoverRequestParameters = discoverSystemParameters;
        return this;
    }

    public boolean isDiscoverRequestParameters() {
        return discoverRequestParameters;
    }

    /**
     * Sets the user name which should be used to authenticate to the proxy server. This is applicable only if
     * {@link #setProxyHost(String)} is not empty.
     *
     * <p>
     * This is <strong>initialization</strong> level configuration (must be set while creating GoogleAnalytics object).
     * </p>
     */
    public GoogleAnalyticsConfig setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
        return this;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * Sets the password which should be used to authenticate to the proxy server. This is applicable only if
     * {@link #setProxyHost(String)} and {@link #setProxyUserName(String)} is not empty.
     *
     * <p>
     * This is <strong>initialization</strong> level configuration (must be set while creating GoogleAnalytics object).
     * </p>
     */
    public GoogleAnalyticsConfig setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * Sets the host name of the proxy server, to connect to Google analytics.
     *
     * <p>
     * This is <strong>initialization</strong> level configuration (must be set while creating GoogleAnalytics object).
     * </p>
     */
    public GoogleAnalyticsConfig setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets the host name of the proxy server, to connect to Google analytics.
     *
     * <p>
     * This is <strong>initialization</strong> level configuration (must be set while creating GoogleAnalytics object).
     * </p>
     */
    public GoogleAnalyticsConfig setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the user agent string that should be sent while making the http request. Default is Apache Http Client's
     * user agent, which looks something similar to this. <code>Apache-HttpClient/release (java 1.5)</code>
     *
     * <p>
     * This is <strong>initialization</strong> level configuration (must be set while creating GoogleAnalytics object).
     * </p>
     */
    public GoogleAnalyticsConfig setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enables or disables the GoogleAnalytics posting. If disabled, library will continue to accept the send/post
     * requests but silently skips sending the event and returns successful response. Default is <code>false</code>.
     *
     * <p>
     * This is <strong>request</strong> level configuration (can be changed any time).
     * </p>
     */
    public GoogleAnalyticsConfig setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Maximum threads to use to process the asynchronous event posting and Http client connection pooling. Default is
     *
     * <p>
     * This is <strong>initialization</strong> level configuration (must be set while creating GoogleAnalytics object).
     * </p>
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    public GoogleAnalyticsConfig setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }

    public int getMinThreads() {
        return minThreads;
    }

    public GoogleAnalyticsConfig setMinThreads(int minThreads) {
        this.minThreads = minThreads;
        return this;
    }

    public boolean isUseHttps() {
        return useHttps;
    }

    /**
     * Instructs to use https url to send the events. Default is true.
     *
     * <p>
     * This is <strong>request</strong> level configuration (can be changed any time).
     * </p>
     */
    public GoogleAnalyticsConfig setUseHttps(boolean useHttps) {
        this.useHttps = useHttps;
        return this;
    }

    public boolean isValidate() {
        return validate;
    }

    /**
     * If set, validates the request before sending to Google Analytics. If any errors found, GoogleAnalyticsException
     * will be thrown with details. Default is false. Note that, if you are sending the event in async mode, then
     * request is always validated and logged to log file as warnings irrespective of this flag.
     *
     * <p>
     * This is <strong>request</strong> level configuration (can be changed any time).
     * </p>
     */
    public GoogleAnalyticsConfig setValidate(boolean validate) {
        this.validate = validate;
        return this;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    /**
     * URL to use when posting the event in http mode. This url is Google Analytics service url and usually not updated
     * by the clients.
     *
     * <p>
     * Default value is <code>http://www.google-analytics.com/collect</code>
     * </p>
     *
     * <p>
     * This is <strong>request</strong> level configuration (can be changed any time).
     * </p>
     */
    public GoogleAnalyticsConfig setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
        return this;
    }

    public String getHttpsUrl() {
        return httpsUrl;
    }

    /**
     * URL to use when posting the event in https mode. This url is Google Analytics service url and usually not updated
     * by the clients.
     * <p>
     * Default value is <code>https://www.google-analytics.com/collect</code>
     *
     * <p>
     * This is <strong>request</strong> level configuration (can be changed any time).
     * </p>
     */
    public GoogleAnalyticsConfig setHttpsUrl(String httpsUrl) {
        this.httpsUrl = httpsUrl;
        return this;
    }

    public String getUrl() {
        return useHttps ? httpsUrl : httpUrl;
    }

    public int getMaxHttpConnectionsPerRoute() {
        return maxHttpConnectionsPerRoute;
    }

    public GoogleAnalyticsConfig setMaxHttpConnectionsPerRoute(int maxHttpConnectionsPerRoute) {
        this.maxHttpConnectionsPerRoute = maxHttpConnectionsPerRoute;
        return this;
    }

    @Override
    public String toString() {
        return "GoogleAnalyticsConfig [threadNameFormat=" + threadNameFormat + ", enabled=" + enabled + ", minThreads=" + minThreads + ", maxThreads="
                + maxThreads + ", threadTimeoutSecs=" + threadTimeoutSecs + ", threadQueueSize=" + threadQueueSize + ", maxHttpConnectionsPerRoute="
                + maxHttpConnectionsPerRoute + ", useHttps=" + useHttps + ", validate=" + validate + ", httpUrl=" + httpUrl + ", httpsUrl=" + httpsUrl
                + ", userAgent=" + userAgent + ", proxyHost=" + proxyHost + ", proxyPort=" + proxyPort + ", proxyUserName=" + proxyUserName
                + ", proxyPassword=" + mask(proxyPassword) + ", discoverRequestParameters=" + discoverRequestParameters + ", gatherStats="
                + gatherStats + ", requestParameterDiscoverer=" + requestParameterDiscoverer + "]";
    }

    public static String mask(String value) {
        return value == null ? null : "********";
    }

    public int getThreadQueueSize() {
        return threadQueueSize;
    }

    public GoogleAnalyticsConfig setThreadQueueSize(int threadQueueSize) {
        this.threadQueueSize = threadQueueSize;
        return this;
    }

    public int getThreadTimeoutSecs() {
        return threadTimeoutSecs;
    }

    public GoogleAnalyticsConfig setThreadTimeoutSecs(int threadTimeoutSecs) {
        this.threadTimeoutSecs = threadTimeoutSecs;
        return this;
    }

    public String getBatchUrl() {
        return batchUrl;
    }

    public GoogleAnalyticsConfig setBatchUrl(String batchUrl) {
        this.batchUrl = batchUrl;
        return this;
    }

    public boolean isBatchingEnabled() {
        return batchingEnabled;
    }

    public GoogleAnalyticsConfig setBatchingEnabled(boolean batchingEnabled) {
        this.batchingEnabled = batchingEnabled;
        return this;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public GoogleAnalyticsConfig setBatchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public GoogleAnalyticsExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * Set an exception handler which will implement the behavior in case of any exceptions. If not set, default
     * behavior is to log a warning message.
     */
    public GoogleAnalyticsConfig setExceptionHandler(GoogleAnalyticsExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

}
